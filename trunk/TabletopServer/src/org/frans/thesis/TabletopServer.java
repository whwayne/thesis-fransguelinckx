package org.frans.thesis;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusObject;
import org.alljoyn.bus.Status;

public class TabletopServer implements TabletopInterface, BusObject {

	private static BusAttachment mBus;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TabletopServer cc = new TabletopServer();
			while (true) {
				Thread.currentThread().sleep(10000);
			}
		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}

	public TabletopServer() {
		mBus = new BusAttachment(getClass().getName());
		TabletopServer server = new TabletopServer();
		Status status = mBus.registerBusObject(server, "/tabletopserver");
		if (Status.OK != status) {
			System.out.println("BusAttachment.registerBusObject() failed: "
					+ status);
			System.exit(0);
			return;
		}

		status = mBus.connect();
		if (Status.OK != status) {
			System.out.println("BusAttachment.connect() failed: " + status);
			System.exit(0);
			return;
		}
		int flags = 0; // no request name flags
		status = mBus.requestName("org.frans.thesis.tabletop", flags);
		if (status != Status.OK) {
			System.out.println("BusAttachment.requestName failed: " + status);
			System.exit(0);
			return;
		}

	}

	@Override
	public String ping(String message) throws BusException {
		return message;
	}

}
