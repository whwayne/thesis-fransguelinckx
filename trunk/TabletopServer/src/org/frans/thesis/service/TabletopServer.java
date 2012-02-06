package org.frans.thesis.service;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusObject;

public class TabletopServer implements TabletopInterface, BusObject {

	@Override
	public String ping(String message) throws BusException {
		return message;
	}
}
