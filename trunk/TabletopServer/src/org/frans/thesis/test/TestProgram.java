package org.frans.thesis.test;

import org.frans.thesis.GUI.CFScene;
import org.mt4j.MTApplication;

public class TestProgram extends MTApplication {

	private static final long serialVersionUID = -7674275591986637141L;

//	static {
//		System.loadLibrary("alljoyn_java");
//	}

	public static void main(String[] args) {
		 initialize();
//		connect();
	}

//	private static void connect() {
//		CFTabletopServer tabletopService = new CFTabletopServer();
//
//		BusAttachment mBus;
//		mBus = new BusAttachment(CFTabletopServer.class.getName(),
//				BusAttachment.RemoteMessage.Receive);
//
//		Status status = mBus.registerBusObject(tabletopService,
//				"/tabletopservice");
//		System.out.println("Bus registered");
//		if (status != Status.OK) {
//			System.exit(0);
//			return;
//		}
//
//		status = mBus.connect();
//		if (status != Status.OK) {
//			System.exit(0);
//			return;
//		}
//		final short CONTACT_PORT = 42;
//
//		Mutable.ShortValue contactPort = new Mutable.ShortValue(CONTACT_PORT);
//
//		SessionOpts sessionOpts = new SessionOpts();
//		sessionOpts.traffic = SessionOpts.TRAFFIC_MESSAGES;
//		sessionOpts.isMultipoint = false;
//		sessionOpts.proximity = SessionOpts.PROXIMITY_ANY;
//		sessionOpts.transports = SessionOpts.TRANSPORT_ANY;
//
//		status = mBus.bindSessionPort(contactPort, sessionOpts,
//				new SessionPortListener() {
//					@Override
//					public boolean acceptSessionJoiner(short sessionPort,
//							String joiner, SessionOpts sessionOpts) {
//						if (sessionPort == CONTACT_PORT) {
//							return true;
//						} else {
//							return false;
//						}
//					}
//				});
//
//		int flags = 0; // do not use any request name flags
//		status = mBus.requestName("org.frans.thesis.tabletop", flags);
//		if (status != Status.OK) {
//			System.exit(0);
//			return;
//		}
//
//		/*
//		 * Important: the well-known name advertised should be identical to the
//		 * well-known name requested from the bus. Using a different name is a
//		 * logic error.
//		 */
//		status = mBus.advertiseName("org.frans.thesis.tabletop",
//				SessionOpts.TRANSPORT_ANY);
//		if (status != Status.OK) {
//			/*
//			 * If we are unable to advertise the name, release the well-known
//			 * name from the local bus.
//			 */
//			mBus.releaseName("org.frans.thesis.tabletop");
//			System.exit(0);
//			return;
//		}
//
//		if (status != Status.OK) {
//			System.exit(0);
//			return;
//		}
//
//		/* Wait forever (or until control-c) */
//		while (true) {
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}

	@Override
	public void startUp() {
		this.addScene(new CFScene(this, "CFScene"));
	}

}
