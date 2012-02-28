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

import java.io.File;
import java.util.ArrayList;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusListener;
import org.alljoyn.bus.BusObject;
import org.alljoyn.bus.Mutable;
import org.alljoyn.bus.SessionOpts;
import org.alljoyn.bus.SessionPortListener;
import org.alljoyn.bus.Status;

public class CFTabletopService implements CFTabletopServiceInterface, BusObject {

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
	private CFTabletopClientManager clientManager;
	private static boolean sessionEstablished = false;
	// private static int sessionId;
	static {
		System.loadLibrary("alljoyn_java");
	}

	private ArrayList<TabletopServiceListener> listeners;

	public CFTabletopService() {
		this.clientManager = new CFTabletopClientManager(this);
		this.listeners = new ArrayList<TabletopServiceListener>();
	}

	public void addTabletopServiceListener(TabletopServiceListener listener) {
		if (!this.getListeners().contains(listener)) {
			this.getListeners().add(listener);
		}
	}

	@Override
	public boolean attach(String name) throws BusException {
		CFTabletopClient tabletopClient = this.getClientManager().addTabletopClient(name);
		for (TabletopServiceListener listener : this.getListeners()) {
			listener.addMobileDevice(name, tabletopClient);
		}
		return true;
	}

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
		for (TabletopServiceListener listener : this.getListeners()) {
			listener.removeMobileDevice(name);
		}
		return true;
	}

	private ArrayList<TabletopServiceListener> getListeners() {
		return listeners;
	}
	@Override
	public String ping(String str) {
		System.out.println("Ping: " + str);
		return str;
	}
	
	private CFTabletopClientManager getClientManager(){
		return this.clientManager;
	}
	
	@Override
	public boolean receivePieceOfFile(String name, byte[] buffer, boolean lastPiece)
			throws BusException {
		this.getClientManager().receivePieceOfFile(name, buffer, lastPiece);
		return true;
	}
	
	protected void removeTabletopServiceListener(TabletopServiceListener listener) {
		if (this.getListeners().contains(listener)) {
			this.getListeners().remove(listener);
		}
	}
	
	protected void fileFinished(File file){
		for(TabletopServiceListener listener : this.getListeners()){
			listener.fileFinished(file);
		}
	}

	@Override
	public int getStatus(String name) throws BusException {
		return this.getClientManager().getStatus(name);
	}

	@Override
	public boolean setIdle(String name) throws BusException {
		this.getClientManager().setIdle(name);
		return false;
	}
}