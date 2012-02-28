package org.frans.thesis.GUI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.frans.thesis.service.TabletopServiceListener;
import org.mt4j.MTApplication;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.math.Vector3D;

public class CFScene extends AbstractScene implements TabletopServiceListener {

	private ArrayList<CFComponent> cfComponents;
	private HashMap<String, CFMobileDeviceProxy> cfMobileDeviceProxies;
	private final float CRITICAL_STACK_DISTANCE = 100;

	public CFScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.cfComponents = new ArrayList<CFComponent>();
		this.cfMobileDeviceProxies = new HashMap<String, CFMobileDeviceProxy>();
		
//		CFMobileDeviceProxy proxy = new CFMobileDeviceProxy(getMTApplication(), name, this);
//		this.getCfMobileDeviceProxies().put(name, proxy);
//		this.getCanvas().addChild(proxy.getMTComponent());
	}

	protected void addCFImage(CFImage image) {
		if (!this.getCfComponents().contains(image)) {
			this.getCanvas().addChild(image.getMTComponent());
			this.getCfComponents().add(image);
		}
	}

	protected void addToStack(CFComponent component1) {
		ArrayList<CFComponent> nearCFComponents = this
				.getNearCFComponents(component1);
		component1.scaleImageToStackSize();
		Vector3D position = component1.getPosition();
		for (CFComponent component2 : nearCFComponents) {
			if (component2.isStackable()) {
				component2.scaleImageToStackSize();
				component2.reposition(position);
			}
		}
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
	public void shutDown() {
	}

	@Override
	public void addMobileDevice(String name) {
		if (!this.getCfMobileDeviceProxies().keySet().contains(name)) {
			CFMobileDeviceProxy proxy = new CFMobileDeviceProxy(getMTApplication(), name, this);
			this.getCfMobileDeviceProxies().put(name, proxy);
			this.getCanvas().addChild(proxy.getMTComponent());
		}
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
	public void fileFinished(File file) {
		CFImage image = new CFImage(getMTApplication(), file.getName(), this);
		this.addCFImage(image);
	}
}
