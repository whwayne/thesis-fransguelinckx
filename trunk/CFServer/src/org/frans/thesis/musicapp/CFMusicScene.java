package org.frans.thesis.musicapp;

import java.util.HashMap;

import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFFile;
import org.frans.thesis.service.CFTabletopClientManager;
import org.frans.thesis.service.CFTabletopServiceListener;
import org.mt4j.MTApplication;

/**
 * A music scene, inherits from CFScene and implements the CFTabletopServiceListener/
 */
public class CFMusicScene extends CFScene implements CFTabletopServiceListener {

	/**
	 * A hash map of the mobile device proxies in this scene, mapped to their client names.
	 */
	private HashMap<String, CFMobileDeviceProxy> cfMobileDeviceProxies = new HashMap<String, CFMobileDeviceProxy>();

	/**
	 * Public constructor for this class.
	 * @param mtApplication
	 * The application to which this scene belongs.
	 * @param name
	 * The name of this scene.
	 */
	public CFMusicScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
	}

	/**
	 * Called when a new mobile device connects to the tabletop.
	 * Adds an instance of CFMobileDeviceProxy to the scene.
	 */
	@Override
	public void mobileDeviceConnected(String clientName,
			CFTabletopClientManager tabletopClientManager) {
		if (!this.getCfMobileDeviceProxies().keySet().contains(clientName)) {
			CFMobileDeviceProxy proxy = new CFMobileDeviceProxy(this, tabletopClientManager.getClient(clientName));
			this.getCfMobileDeviceProxies().put(clientName, proxy);
//			this.getCfComponents().add(proxy);
//			proxy.setPositionGlobal(new Vector3D(this.getMTApplication()
//					.getWidth() / 2, this.getMTApplication().getHeight() / 2));
			this.addCFComponent(proxy);
		}
	}

	/**
	 * Called when a file is completely transferred to the tabletop.
	 * Adds an instance of CFSong to this scene.
	 */
	@Override
	public void fileTransferred(CFFile file, String name) {
		this.addCFComponent(new CFSong(this, file));
	}

	/**
	 * Returns the hash map of mobile device proxies.
	 */
	private HashMap<String, CFMobileDeviceProxy> getCfMobileDeviceProxies() {
		return cfMobileDeviceProxies;
	}

	/**
	 * Called when a mobile device disconnects from the tabletop.
	 * Removes the mobile device proxy from the scene.
	 */
	@Override
	public void mobileDeviceDisconnected(String name) {
		if (this.getCfMobileDeviceProxies().containsKey(name)) {
			CFMobileDeviceProxy proxy = this.getCfMobileDeviceProxies().get(
					name);
//			this.getCanvas().removeChild(proxy);
			this.getCfMobileDeviceProxies().remove(proxy);
			this.removeCFComponent(proxy);
		}
	}

	/**
	 * Called when the status of a client was set to IDLE.
	 * Stops the spinner of said client.
	 */
	@Override
	public void clientIsIdle(String name) {
		this.getCfMobileDeviceProxies().get(name).stopSpinner();
	}

}