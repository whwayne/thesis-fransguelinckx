package org.frans.thesis.GUI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.frans.thesis.service.CFTabletopClient;
import org.frans.thesis.service.TabletopServiceListener;
import org.mt4j.MTApplication;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class CFScene extends AbstractScene implements TabletopServiceListener {

	private ArrayList<CFComponent> cfComponents;
	private HashMap<String, CFMobileDeviceProxy> cfMobileDeviceProxies;
	private final float CRITICAL_STACK_DISTANCE = 100;

	public CFScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.cfComponents = new ArrayList<CFComponent>();
		this.cfMobileDeviceProxies = new HashMap<String, CFMobileDeviceProxy>();
	}

	protected void addCFComponent(CFComponent component) {
		if (!this.getCfComponents().contains(component)) {
			this.getCanvas().addChild(component.getMTComponent());
			component.getMTComponent().setPositionGlobal(new Vector3D(this.getMTApplication().getWidth()/2, this.getMTApplication().getHeight()/2));
			this.getCfComponents().add(component);
		}
	}

	@Override
	public void addMobileDevice(String name, CFTabletopClient tabletopClient) {
		if (!this.getCfMobileDeviceProxies().keySet().contains(name)) {
			MTColor color = MTColor.randomColor();
			color.setAlpha(MTColor.ALPHA_HALF_TRANSPARENCY);
			CFMobileDeviceProxy proxy = new CFMobileDeviceProxy(getMTApplication(), name, this, tabletopClient, color);
			this.getCfMobileDeviceProxies().put(name, proxy);
			proxy.getMTComponent().setPositionGlobal(new Vector3D(this.getMTApplication().getWidth()/2, this.getMTApplication().getHeight()/2));
			this.getCanvas().addChild(proxy.getMTComponent());
		}
	}

	protected void addToStack(CFComponent component1) {
		ArrayList<CFComponent> nearCFComponents = this
				.getNearCFComponents(component1);
		component1.scaleImageToStackSize();
		CFComponent component2 = nearCFComponents.get(0);
		Vector3D position = component2.getPosition();
		if(component2.isStackable()){
				component2.scaleImageToStackSize();
				component1.reposition(position);
		}
	}

	@Override
	public void fileFinished(File file, String name) {
		MTColor color = this.getCfMobileDeviceProxies().get(name).getColor();
		new CFImage(getMTApplication(), file.getPath(), this, color);
	}

	private ArrayList<CFComponent> getCfComponents() {
		return cfComponents;
	}

	private HashMap<String, CFMobileDeviceProxy> getCfMobileDeviceProxies() {
		return cfMobileDeviceProxies;
	}

	protected ArrayList<CFComponent> getNearCFComponents(CFComponent component1) {
		ArrayList<CFComponent> result = new ArrayList<CFComponent>();
		for (CFComponent component2 : this.getCfComponents()) {
			if (!component2.equals(component1)
					&& component1.getDistanceto(component2) < this.CRITICAL_STACK_DISTANCE) {
				result.add(component2);
			}
		}
		return result;
	}

	@Override
	public void init() {
	}

	protected boolean isCloseToCFComponent(CFComponent image1) {
		for (CFComponent image2 : this.getCfComponents()) {
			if (!image2.equals(image1)
					&& image1.getDistanceto(image2) < this.CRITICAL_STACK_DISTANCE) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void removeMobileDevice(String name) {
		if(this.getCfMobileDeviceProxies().containsKey(name)){
			CFMobileDeviceProxy proxy = this.getCfMobileDeviceProxies().get(name);
			this.getCanvas().removeChild(proxy.getMTComponent());
			this.getCfMobileDeviceProxies().remove(proxy);
		}
	}

	@Override
	public void shutDown() {
	}
}