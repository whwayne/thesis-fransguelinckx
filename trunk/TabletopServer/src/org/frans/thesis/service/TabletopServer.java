package org.frans.thesis.service;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusObject;
import org.alljoyn.bus.Status;

public class TabletopServer implements TabletopInterface, BusObject {
	
	static {
        System.loadLibrary("alljoyn_java");
    }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
	public String ping(String message) throws BusException {
		return message;
	}
}
