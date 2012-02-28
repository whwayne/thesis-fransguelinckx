package org.frans.thesis.service;

import java.io.File;

public interface TabletopServiceListener {
	
	public void addMobileDevice(String name, CFTabletopClient tabletopClient);
	
	public void removeMobileDevice(String name);
	
	public void fileFinished(File file);

}
