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

/**
 * The only scene in this application, which is also a tabletop service
 * listener.
 */
public class CFPhotoScene extends CFScene implements CFTabletopServiceListener {

	/**
	 * A hash map of all mobile device proxies in the scene, mapped to their
	 * names.
	 */
	private HashMap<String, CFMobileDeviceProxy> cfMobileDeviceProxies;

	/**
	 * A list of all photo albums in the scene.
	 */
	private ArrayList<CFPhotoAlbum> photoalbums;

	/**
	 * Public constructor for a photo scene.
	 * 
	 * @param mtApplication
	 *            The application to which this scene belongs.
	 * @param name
	 *            The name of the scene.
	 */
	public CFPhotoScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.photoalbums = new ArrayList<CFPhotoAlbum>();
		this.cfMobileDeviceProxies = new HashMap<String, CFMobileDeviceProxy>();
	}

	/**
	 * Adds a new mobile proxy to the scene and assigns a random color to is.
	 * 
	 * @pre The name of the proxy should be unique, no other proxy in the scene
	 *      can have the same name.
	 */
	@Override
	public void mobileDeviceConnected(String clientName,
			CFTabletopClientManager tabletopClientManager) {
		if (!this.getCfMobileDeviceProxies().keySet().contains(clientName)) {
			MTColor color = MTColor.randomColor();
			color.setAlpha(MTColor.ALPHA_HALF_TRANSPARENCY);
			CFMobileDeviceProxy proxy = new CFMobileDeviceProxy(clientName,
					this, tabletopClientManager, color);
			this.getCfMobileDeviceProxies().put(clientName, proxy);
			this.getCfComponents().add(proxy);
			proxy.setPositionGlobal(new Vector3D(this.getMTApplication()
					.getWidth() / 2, this.getMTApplication().getHeight() / 2));
			this.getCanvas().addChild(proxy);
		}
	}

	/**
	 * Adds a photo album to the scene.
	 * @param cfPhotoAlbum
	 * The photo album that has to be added.
	 */
	public void addPhotoalbum(CFPhotoAlbum cfPhotoAlbum) {
		this.photoalbums.add(cfPhotoAlbum);
	}

	/**
	 * Adds a new image to the scene and assigns it the same color as the client that sent it.
	 */
	@Override
	public void fileTransferred(CFFile file, String name) {
		MTColor color = this.getCfMobileDeviceProxies().get(name).getColor();
		new CFImage(file, this, color);
	}

	/**
	 * Returns the hash map with the mobile device proxies.
	 */
	private HashMap<String, CFMobileDeviceProxy> getCfMobileDeviceProxies() {
		return cfMobileDeviceProxies;
	}

	/**
	 * Gets all nearby components of a given component. More precisely: all
	 * components that are closer than CRITICAL_STACK_DISTANCE.
	 * 
	 * @param component
	 *            The component from which all nearby components have to be
	 *            returned.
	 * @return An array list containing all nearby components.
	 */
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

	/**
	 * Checks whether a given cfcomponent is close to another component in this
	 * scene. More precisely, if their centers are closer than
	 * CRITICAL_STACK_DISTANCE.
	 * 
	 * @param otherComponent
	 *            The component to be compared to to all the other components in
	 *            this scene.
	 * @return True if there is at least one other component that is close
	 *         enough. False if there is no other component that is close
	 *         enough.
	 */
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

	/**
	 * Unloads and loads all photo albums.
	 */
	protected void reloadAlbums() {
		for (CFPhotoAlbum album : this.photoalbums) {
			album.unloadImages();
			album.loadImages();
		}
	}

	/**
	 * Removes a mobile device with given name from the scene.
	 */
	@Override
	public void mobileDeviceDisconnected(String name) {
		if (this.getCfMobileDeviceProxies().containsKey(name)) {
			CFMobileDeviceProxy proxy = this.getCfMobileDeviceProxies().get(
					name);
			this.getCanvas().removeChild(proxy);
			this.getCfMobileDeviceProxies().remove(proxy);
		}
	}

	/**
	 * Stops the spinner of a client with a given name.
	 */
	@Override
	public void clientIsIdle(String name) {
		this.getCfMobileDeviceProxies().get(name).stopSpinner();
	}

}
