package org.frans.thesis.service;

import java.util.HashMap;

/**
 * A class that manages all connected clients by delegating tasks to them.
 */
public class CFTabletopClientManager {

	/**
	 * A hashmap of clients, mapped to their names.
	 */
	private HashMap<String, CFTabletopClient> clients;

	/**
	 * The tabletop service to which this manager belongs.
	 */
	private CFTabletopService service;

	/**
	 * A public constructor for this class. Sets the tabletop service.
	 * 
	 * @param service
	 *            The tabletop service to which this manager belongs.
	 */
	public CFTabletopClientManager(CFTabletopService service) {
		this.service = service;
		this.clients = new HashMap<String, CFTabletopClient>();
	}

	/**
	 * Adds a client to this manager.
	 * 
	 * @param name
	 *            The name of the client that has to be added.
	 * @return The instance of CFTabletopClient representing the new client.
	 */
	protected CFTabletopClient addTabletopClient(String name) {
		CFTabletopClient tabletopClient = new CFTabletopClient(name, this);
		this.getClients().put(name, tabletopClient);
		return tabletopClient;
	}

	/**
	 * Method to notify the manager a complete file has been sent to the
	 * tabletop. Usually called by an instance of CFTabletopClient.
	 * 
	 * @param file
	 *            The finished file.
	 * @param name
	 *            The name of the client that has received the file.
	 */
	protected void fileFinished(CFFile file, String name) {
		this.getService().fileFinished(file, name);
	}

	/**
	 * Returns the hash map of clients of this manager.
	 */
	private HashMap<String, CFTabletopClient> getClients() {
		return this.clients;
	}

	/**
	 * Gets the next file that has to be published on facebook.
	 * 
	 * @param clientName
	 *            The name of the client of which the next file has to be
	 *            returned.
	 * @return The next file that has to be published on facebook. Null if there
	 *         is no next file.
	 */
	protected String getFileToPublish(String clientName) {
		return this.getClients().get(clientName).popImageCue();
	}

	/**
	 * A method that gets the next array of bytes that has to be sent to the
	 * mobile device of this client. If no files are in line to be sent to the
	 * mobile device, this method sends an array with length of 1.
	 * 
	 * @param clientName
	 *            The name of the client of which the next buffer has to be
	 *            returned.
	 */
	public byte[] getNextOutgoingFileBuffer(String clientName) {
		return this.getClients().get(clientName).getNextOutgoingFileBuffer();
	}

	/**
	 * Returns the tabletop service.
	 */
	private CFTabletopService getService() {
		return this.service;
	}

	/**
	 * Returns the status of a client with a given name.
	 * 
	 * @param clientName
	 *            The name of the client of which the status has to be
	 *            returned..
	 * @return The status of the client with the given name.
	 */
	protected int getStatus(String clientName) {
		return this.getClients().get(clientName).getStatus();
	}

	/**
	 * Adds a given file to the queue of images of a certain client that have to
	 * be published on facebook.
	 * 
	 * @param clientName
	 *            The name of the client that has to publish the image on
	 *            facebook.
	 * @param cfFile
	 *            The image file that has to be published on facebook.
	 */
	public void publishImageOnFacebook(String clientName, CFFile cfFile) {
		this.getClients().get(clientName).publishImageOnFacebook(cfFile);
	}

	/**
	 * A method that handles an array of bytes that was received on the
	 * tabletop.
	 * 
	 * @param path
	 *            The path of the file in the file system of the mobile device.
	 * @param clientName
	 *            The name of the mobile device that has sent the array of
	 *            bytes.
	 * @param buffer
	 *            The array of bytes.
	 * @param lastPiece
	 *            A boolean to indicate whether this is the last array of bytes
	 *            of that particular file.
	 */
	protected void receivePieceOfFile(String path, String clientName,
			byte[] buffer, boolean lastPiece) {
		this.getClients().get(clientName)
				.receivePieceOfFile(path, buffer, lastPiece);
	}

	/**
	 * Sends a given file to a client with a given name.
	 * 
	 * @param clientName
	 *            The name of the client to which the file has to be sent.
	 * @param file
	 *            The file that has to be sent to the client.
	 */
	public void sendFileToClient(String clientName, CFFile file) {
		this.getClients().get(clientName).sendFile(file);
	}

	/**
	 * Sets the status of a client with a given name to IDLE.
	 * 
	 * @param name
	 *            The name of the said client.
	 */
	protected void setIdle(String name) {
		this.getClients().get(name).setStatus(CFTabletopClient.IDLE);
	}

	/**
	 * Sets the status of a client with a given name to a given integer.
	 * 
	 * @param clientName
	 *            The name of the client of which the status has to be set.
	 * @param status
	 *            The status that has to be set.
	 */
	public void setStatus(String clientName, int status) {
		this.getClients().get(clientName).setStatus(status);
	}

}
