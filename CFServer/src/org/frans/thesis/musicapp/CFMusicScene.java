package org.frans.thesis.musicapp;

import java.util.HashMap;

import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFFile;
import org.frans.thesis.service.CFTabletopClientManager;
import org.frans.thesis.service.CFTabletopServiceListener;
import org.mt4j.MTApplication;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class CFMusicScene extends CFScene implements CFTabletopServiceListener {

	private HashMap<String, CFMobileDeviceProxy> cfMobileDeviceProxies;
	
	public CFMusicScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.cfMobileDeviceProxies = new HashMap<String, CFMobileDeviceProxy>();
		// TODO Auto-generated constructor stub
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
			proxy.setPositionGlobal(
					new Vector3D(this.getMTApplication().getWidth() / 2, this
							.getMTApplication().getHeight() / 2));
			this.getCanvas().addChild(proxy);
		}
	}

	private HashMap<String, CFMobileDeviceProxy> getCfMobileDeviceProxies() {
		return cfMobileDeviceProxies;
	}

	public void fileFinished(CFFile file, String name) {
		new CFSong(getMTApplication(), this, file);
	}

	public void removeMobileDevice(String name) {
		if (this.getCfMobileDeviceProxies().containsKey(name)) {
			CFMobileDeviceProxy proxy = this.getCfMobileDeviceProxies().get(
					name);
			this.getCanvas().removeChild(proxy);
			this.getCfMobileDeviceProxies().remove(proxy);
		}
	}

	public void setIdle(String name) {
		this.getCfMobileDeviceProxies().get(name).stopSpinner();
	}

}
