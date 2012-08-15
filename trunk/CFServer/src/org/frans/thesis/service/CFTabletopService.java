/*
 * Copyright 2010-2011, Qualcomm Innovation Center, Inc.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.frans.thesis.service;

import java.util.Vector;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusListener;
import org.alljoyn.bus.BusObject;
import org.alljoyn.bus.Mutable;
import org.alljoyn.bus.SessionOpts;
import org.alljoyn.bus.SessionPortListener;
import org.alljoyn.bus.Status;

/**
 * A class that communicates directly with mobile devices. It implements the
 * CFTabletopServiceInterface, which contains methods that can be called by
 * connected clients.
 */
public class CFTabletopService implements CFTabletopServiceInterface, BusObject {

	/**
	 * AllJoyn code.
	 */
	private static class MyBusListener extends BusListener {
		@Override
		public void nameOwnerChanged(String busName, String previousOwner,
				String newOwner) {
			if ("org.alljoyn.bus.samples.simple".equals(busName)) {
				System.out.println("BusAttachement.nameOwnerChanged(" + busName
						+ ", " + previousOwner + ", " + newOwner);
			}
		}
	}

	private static final short CONTACT_PORT = 42;
	private static boolean sessionEstablished = false;

	static {
		System.loadLibrary("alljoyn_java");
	}

	/**
	 * The client manager of this service which manages all connected clients.
	 */
	private CFTabletopClientManager clientManager;

	/**
	 * A list of tabletop service listeners that get notified when a client
	 * connects or disconnects, when a file has completely been sent to the
	 * tabletop or when the status of a client was set to IDLE.
	 */
	private Vector<CFTabletopServiceListener> listeners;

	/**
	 * Public constructor.
	 */
	public CFTabletopService() {
		this.clientManager = new CFTabletopClientManager(this);
		this.listeners = new Vector<CFTabletopServiceListener>();
	}

	/**
	 * Adds a new service listener of it is not already in the list.
	 * 
	 * @param listener
	 *            The listener to be added.
	 */
	public void addTabletopServiceListener(CFTabletopServiceListener listener) {
		if (!this.getListeners().contains(listener)) {
			this.getListeners().add(listener);
		}
	}

	/**
	 * Adds a new client to this service. The client manager and all service
	 * listeners get notified.
	 */
	@Override
	public boolean attach(String clientName) throws BusException {
		this.getClientManager().addTabletopClient(clientName);
		for (CFTabletopServiceListener listener : this.getListeners()) {
			listener.addMobileDevice(clientName, this.getClientManager());
		}
		return true;
	}

	/**
	 * Publishes this service in the local wifi network so clients can discover
	 * it and connect with it.
	 */
	public void connect() {
		BusAttachment mBus;
		mBus = new BusAttachment("AppName", BusAttachment.RemoteMessage.Receive);
		Status status;

		status = mBus.registerBusObject(this, "/SimpleService");
		if (status != Status.OK) {
			System.exit(0);
			return;
		}
		System.out.println("BusAttachment.registerBusObject successful");

		BusListener listener = new MyBusListener();
		mBus.registerBusListener(listener);

		status = mBus.connect();
		if (status != Status.OK) {
			System.exit(0);
			return;
		}
		System.out.println("BusAttachment.connect successful on "
				+ System.getProperty("org.alljoyn.bus.samples.simple"));

		Mutable.ShortValue contactPort = new Mutable.ShortValue(CONTACT_PORT);
		SessionOpts sessionOpts = new SessionOpts();
		sessionOpts.traffic = SessionOpts.TRAFFIC_MESSAGES;
		sessionOpts.isMultipoint = false;
		sessionOpts.proximity = SessionOpts.PROXIMITY_ANY;
		sessionOpts.transports = SessionOpts.TRANSPORT_ANY;

		status = mBus.bindSessionPort(contactPort, sessionOpts,
				new SessionPortListener() {
					@Override
					public boolean acceptSessionJoiner(short sessionPort,
							String joiner, SessionOpts sessionOpts) {
						System.out
								.println("SessionPortListener.acceptSessionJoiner called");
						if (sessionPort == CONTACT_PORT) {
							return true;
						} else {
							return false;
						}
					}

					@Override
					public void sessionJoined(short sessionPort, int id,
							String joiner) {
						System.out.println(String
								.format("SessionPortListener.sessionJoined(%d, %d, %s)",
										sessionPort, id, joiner));
						// sessionId = id;
						sessionEstablished = true;
					}
				});
		if (status != Status.OK) {
			System.exit(0);
			return;
		}
		System.out.println("BusAttachment.bindSessionPort successful");

		int flags = 0; // do not use any request name flags
		status = mBus.requestName("org.alljoyn.bus.samples.simple", flags);
		if (status != Status.OK) {
			System.exit(0);
			return;
		}
		System.out
				.println("BusAttachment.request 'org.alljoyn.bus.samples.simple' successful");

		status = mBus.advertiseName("org.alljoyn.bus.samples.simple",
				SessionOpts.TRANSPORT_ANY);
		if (status != Status.OK) {
			System.out.println("Status = " + status);
			mBus.releaseName("org.alljoyn.bus.samples.simple");
			System.exit(0);
			return;
		}
		System.out
				.println("BusAttachment.advertiseName 'org.alljoyn.bus.samples.simple' successful");

		while (!sessionEstablished) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("Thead Exception caught");
				e.printStackTrace();
			}
		}
		System.out.println("BusAttachment session established");

		while (true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				System.out.println("Thead Exception caught");
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean detach(String name) throws BusException {
		for (CFTabletopServiceListener listener : this.getListeners()) {
			listener.removeMobileDevice(name);
		}
		return true;
	}

	/**
	 * Notifies this service that a file has been completely transferred to the
	 * tabletop.
	 * 
	 * @param file
	 *            The file that has been transferred.
	 * @param name
	 *            The name of the client that has sent the file.
	 */
	protected void fileFinished(CFFile file, String name) {
		for (CFTabletopServiceListener listener : this.getListeners()) {
			listener.fileFinished(file, name);
		}
	}

	/**
	 * Returns the client manager of this service.
	 */
	private CFTabletopClientManager getClientManager() {
		return this.clientManager;
	}

	@Override
	public String getFileToPublish(String clientName) {
		return this.getClientManager().getFileToPublish(clientName);
	}

	/**
	 * Returns the list of service listeners.
	 */
	private synchronized Vector<CFTabletopServiceListener> getListeners() {
		return listeners;
	}

	@Override
	public byte[] getNextOutgoingFileBuffer(String clientName) {
		return this.getClientManager().getNextOutgoingFileBuffer(clientName);
	}

	@Override
	public int getStatus(String name) throws BusException {
		return this.getClientManager().getStatus(name);
	}

	@Override
	public boolean receivePieceOfFile(String path, String clientName,
			byte[] buf, boolean lastPiece) throws BusException {
		this.getClientManager().receivePieceOfFile(path, clientName, buf,
				lastPiece);
		return true;
	}

	/**
	 * Removes a given service listener from this service
	 * 
	 * @param listener
	 *            The listener that has to be removed.
	 */
	protected void removeTabletopServiceListener(
			CFTabletopServiceListener listener) {
		if (this.getListeners().contains(listener)) {
			this.getListeners().remove(listener);
		}
	}

	@Override
	public boolean setIdle(String name) throws BusException {
		this.getClientManager().setIdle(name);
		for (CFTabletopServiceListener listener : this.getListeners()) {
			listener.setIdle(name);
		}
		return false;
	}
}