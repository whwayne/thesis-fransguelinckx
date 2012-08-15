package org.frans.thesis.PhotoApp;

import java.util.ArrayList;
import java.util.HashMap;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFFile;
import org.frans.thesis.service.CFTabletopClientManager;
import org.frans.thesis.service.CFTabletopServiceListener;
import org.mt4j.MTApplication;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class CFPhotoScene extends CFScene implements CFTabletopServiceListener {

	private HashMap<String, CFMobileDeviceProxy> cfMobileDeviceProxies;
	private ArrayList<CFPhotoAlbum> photoalbums;

	public CFPhotoScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.photoalbums = new ArrayList<CFPhotoAlbum>();
		this.cfMobileDeviceProxies = new HashMap<String, CFMobileDeviceProxy>();
	}

	@Override
	public void addMobileDevice(String clientName,
			CFTabletopClientManager tabletopClientManager) {
		if (!this.getCfMobileDeviceProxies().keySet().contains(clientName)) {
			MTColor color = MTColor.randomColor();
			color.setAlpha(MTColor.ALPHA_HALF_TRANSPARENCY);
			CFMobileDeviceProxy proxy = new CFMobileDeviceProxy(clientName, this,
					tabletopClientManager, color);
			this.getCfMobileDeviceProxies().put(clientName, proxy);
			this.getCfComponents().add(proxy);
			proxy.setPositionGlobal(new Vector3D(this.getMTApplication()
					.getWidth() / 2, this.getMTApplication().getHeight() / 2));
			this.getCanvas().addChild(proxy);
		}
	}

	public void addPhotoalbum(CFPhotoAlbum cfPhotoAlbum) {
		this.photoalbums.add(cfPhotoAlbum);
	}

	@Override
	public void fileFinished(CFFile file, String name) {
		MTColor color = this.getCfMobileDeviceProxies().get(name).getColor();
		new CFImage(file, this, color);
	}

	private HashMap<String, CFMobileDeviceProxy> getCfMobileDeviceProxies() {
		return cfMobileDeviceProxies;
	}

	@Override
	protected ArrayList<CFComponent> getNearbyCFComponents(
			CFComponent component1) {
		ArrayList<CFComponent> result = new ArrayList<CFComponent>();
		for (CFComponent component2 : this.getCfMobileDeviceProxies().values()) {
			if (!component2.equals(component1)
					&& component1.getDistanceto(component2) < this.CRITICAL_STACK_DISTANCE) {
				result.add(component2);
			}
		}
		result.addAll(super.getNearbyCFComponents(component1));
		return result;
	}

	@Override
	protected boolean isCloseToCFComponent(CFComponent image1) {
		for (CFComponent component : this.getCfMobileDeviceProxies().values()) {
			if (!component.equals(image1)
					&& image1.getDistanceto(component) < this.CRITICAL_STACK_DISTANCE) {
				return true;
			}
		}
		return super.isCloseToCFComponent(image1);
	}

	protected void reloadAlbums() {
		for (CFPhotoAlbum album : this.photoalbums) {
			album.unloadImages();
			album.loadImages();
		}
	}

	@Override
	public void removeMobileDevice(String name) {
		if (this.getCfMobileDeviceProxies().containsKey(name)) {
			CFMobileDeviceProxy proxy = this.getCfMobileDeviceProxies().get(
					name);
			this.getCanvas().removeChild(proxy);
			this.getCfMobileDeviceProxies().remove(proxy);
		}
	}

	@Override
	public void setIdle(String name) {
		this.getCfMobileDeviceProxies().get(name).stopSpinner();
	}

}
