package org.frans.thesis;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusMethod;

@BusInterface (name = "org.frans.thesis.TabltopInterface")
public interface TabletopInterface {
	
	@BusMethod
	public String ping(String message) throws BusException;

}
