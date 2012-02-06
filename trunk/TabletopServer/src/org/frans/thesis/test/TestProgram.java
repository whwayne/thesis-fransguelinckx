package org.frans.thesis.test;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.Status;
import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.TabletopServer;
import org.mt4j.MTApplication;

public class TestProgram extends MTApplication{
	
	private static final long serialVersionUID = -7674275591986637141L;

	static {
		System.loadLibrary("alljoyn_java");
	}
	
	public static void main(String[] args) {
		initialize();
		connect();
	}

	private static void connect() {
		try {
            /* Create a bus connection */
			System.out.print("Connecting...   ");
            BusAttachment bus = new BusAttachment(TabletopServer.class.getName());
            
            /* connect to the bus */
            Status status = bus.connect();
            if (Status.OK != status) {
                System.out.println("BusAttachment.connect() failed with " + status.toString());
                return;
            }
            System.out.println("Done!");
            
            /* Register the service */
            System.out.print("Registering...   ");
            TabletopServer tabletopService = new TabletopServer();
            status = bus.registerBusObject(tabletopService, "/tabletopservice");
            if (Status.OK != status) {
                System.out.println("BusAttachment.registerBusObject() failed: " + status.toString());
                return;
            }

            /* Request a well-known name */
            int flag = 0;
            status = bus.requestName("org.frans.thesis.tabletop", flag);
            if (status != Status.OK) {
            	System.out.println("Failed to obtain a well-known bus name.");
            }
            System.out.println("Done!");

            /* Wait forever (or until control-c) */
            while (true) {
				Thread.sleep(10000);
            }
        } catch (InterruptedException ex) {
            System.out.println("Interrupted");
        }
	}

	@Override
	public void startUp() {
		this.addScene(new CFScene(this, "CFScene"));
	}

}
