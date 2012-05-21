package org.frans.thesis.GUI;

import java.util.ArrayList;
import java.util.HashMap;

import org.frans.thesis.service.CFFile;
import org.frans.thesis.service.CFTabletopClientManager;
import org.frans.thesis.service.CFTabletopServiceListener;
import org.mt4j.MTApplication;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class CFScene extends AbstractScene implements CFTabletopServiceListener {

	private ArrayList<CFComponent> cfComponents;
	private ArrayList<CFPhotoAlbum> photoalbums;
	private HashMap<String, CFMobileDeviceProxy> cfMobileDeviceProxies;
	private final float CRITICAL_STACK_DISTANCE = 100;

	public CFScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.cfComponents = new ArrayList<CFComponent>();
		this.photoalbums = new ArrayList<CFPhotoAlbum>();
		this.cfMobileDeviceProxies = new HashMap<String, CFMobileDeviceProxy>();
		new CFTrashCan(mtApplication, this);
		new CFTrashCan(mtApplication, this);
//		new CFCopier(mtApplication, this);
	}

	protected void addCFComponent(CFComponent component) {
		if (!this.getCfComponents().contains(component)) {
			this.getCanvas().addChild(component.getMTComponent());
			component.getMTComponent().setPositionGlobal(new Vector3D(this.getMTApplication().getWidth()/2, this.getMTApplication().getHeight()/2));
			this.getCfComponents().add(component);
		}
	}

	@Override
	public void addMobileDevice(String clientName, CFTabletopClientManager tabletopClientManager) {
		if (!this.getCfMobileDeviceProxies().keySet().contains(clientName)) {
			MTColor color = MTColor.randomColor();
			color.setAlpha(MTColor.ALPHA_HALF_TRANSPARENCY);
			CFMobileDeviceProxy proxy = new CFMobileDeviceProxy(getMTApplication(), clientName, this, tabletopClientManager, color);
			this.getCfMobileDeviceProxies().put(clientName, proxy);
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
	public void fileFinished(CFFile file, String name) {
		MTColor color = this.getCfMobileDeviceProxies().get(name).getColor();
		new CFImage(getMTApplication(), file, this, color);
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
		for (CFComponent component2 : this.getCfMobileDeviceProxies().values()) {
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
		for(CFComponent component : this.getCfMobileDeviceProxies().values()){
			if (!component.equals(image1)
					&& image1.getDistanceto(component) < this.CRITICAL_STACK_DISTANCE) {
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

	public void addPhotoalbum(CFPhotoAlbum cfPhotoAlbum) {
		this.photoalbums.add(cfPhotoAlbum);
	}
	
	protected void reloadAlbums(){
		for(CFPhotoAlbum album : this.photoalbums){
			album.unloadImages();
			album.loadImages();
		}
	}

	@Override
	public void setIdle(String name) {
		this.getCfMobileDeviceProxies().get(name).stopSpinner();
	}
}
