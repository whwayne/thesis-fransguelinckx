package org.frans.thesis.service;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusObject;

public class CFTabletopServer implements CFTabletopInterface, BusObject {

	@Override
	public String ping(String message) throws BusException {
		return message;
	}
}
