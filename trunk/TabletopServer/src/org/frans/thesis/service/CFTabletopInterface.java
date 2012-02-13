package org.frans.thesis.service;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusMethod;

@BusInterface(name = "org.frans.thesis.TabltopInterface")
public interface CFTabletopInterface {

	@BusMethod
	public String ping(String message) throws BusException;

}
