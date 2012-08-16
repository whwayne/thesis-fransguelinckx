package org.frans.thesis.service;

/**
 * An interface to allow objects to be notified about important events.
 */
public interface CFTabletopServiceListener {

	/**
	 * Notifies this listener that a new mobile device has connected to the
	 * tabletop.
	 * 
	 * @param clientName
	 *            The name of the client that has connected to the tabletop.
	 * @param tabletopClientManager
	 *            The client manager that manages the new client.
	 */
	public void mobileDeviceConnected(String clientName,
			CFTabletopClientManager tabletopClientManager);

	/**
	 * Notifies tis listener that a file has been transferred to the tabletop.
	 * 
	 * @param file
	 *            The file in question.
	 * @param name
	 *            The name of the client that has sent the file.
	 */
	public void fileTransferred(CFFile file, String name);

	/**
	 * Notifies this listener about a client that has disconnected from the
	 * tabletop.
	 * 
	 * @param name
	 *            The name of the disconnected client.
	 */
	public void mobileDeviceDisconnected(String name);

	/**
	 * Notifies this listener about the status of a client that has been set to
	 * IDLE.
	 * 
	 * @param name
	 *            The name of the client in question.
	 */
	public void clientIsIdle(String name);

}
