package org.frans.thesis.GUI;

import java.util.ArrayList;
import java.util.HashMap;

import org.frans.thesis.service.CFFile;
import org.frans.thesis.service.CFTabletopClientManager;
import org.frans.thesis.service.CFTabletopServiceListener;
import org.mt4j.MTApplication;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class CFPhotoScene extends CFScene implements CFTabletopServiceListener {

	private ArrayList<CFPhotoAlbum> photoalbums;
	private HashMap<String, CFMobileDeviceProxy> cfMobileDeviceProxies;

	public CFPhotoScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.photoalbums = new ArrayList<CFPhotoAlbum>();
		this.cfMobileDeviceProxies = new HashMap<String, CFMobileDeviceProxy>();
	}

	public void addMobileDevice(String clientName,
			CFTabletopClientManager tabletopClientManager) {
		if (!this.getCfMobileDeviceProxies().keySet().contains(clientName)) {
			MTColor color = MTColor.randomColor();
			color.setAlpha(MTColor.ALPHA_HALF_TRANSPARENCY);
			CFMobileDeviceProxy proxy = new CFMobileDeviceProxy(
					getMTApplication(), clientName, this,
					tabletopClientManager, color);
			this.getCfMobileDeviceProxies().put(clientName, proxy);
			this.getCfComponents().add(proxy);
			proxy.getMTComponent().setPositionGlobal(
					new Vector3D(this.getMTApplication().getWidth() / 2, this
							.getMTApplication().getHeight() / 2));
			this.getCanvas().addChild(proxy.getMTComponent());
		}
	}

	private HashMap<String, CFMobileDeviceProxy> getCfMobileDeviceProxies() {
		return cfMobileDeviceProxies;
	}

	public void fileFinished(CFFile file, String name) {
		MTColor color = this.getCfMobileDeviceProxies().get(name).getColor();
		new CFImage(getMTApplication(), file, this, color);
	}

	public void removeMobileDevice(String name) {
		if (this.getCfMobileDeviceProxies().containsKey(name)) {
			CFMobileDeviceProxy proxy = this.getCfMobileDeviceProxies().get(
					name);
			this.getCanvas().removeChild(proxy.getMTComponent());
			this.getCfMobileDeviceProxies().remove(proxy);
		}
	}

	public void addPhotoalbum(CFPhotoAlbum cfPhotoAlbum) {
		this.photoalbums.add(cfPhotoAlbum);
	}

	protected void reloadAlbums() {
		for (CFPhotoAlbum album : this.photoalbums) {
			album.unloadImages();
			album.loadImages();
		}
	}

	public void setIdle(String name) {
		this.getCfMobileDeviceProxies().get(name).stopSpinner();
	}

	protected boolean isCloseToCFComponent(CFComponent image1) {
		for (CFComponent component : this.getCfMobileDeviceProxies().values()) {
			if (!component.equals(image1)
					&& image1.getDistanceto(component) < this.CRITICAL_STACK_DISTANCE) {
				return true;
			}
		}
		return super.isCloseToCFComponent(image1);
	}

	protected ArrayList<CFComponent> getNearCFComponents(CFComponent component1) {
		ArrayList<CFComponent> result = new ArrayList<CFComponent>();
		for (CFComponent component2 : this.getCfMobileDeviceProxies().values()) {
			if (!component2.equals(component1)
					&& component1.getDistanceto(component2) < this.CRITICAL_STACK_DISTANCE) {
				result.add(component2);
			}
		}
		result.addAll(super.getNearCFComponents(component1));
		return result;
	}

}
