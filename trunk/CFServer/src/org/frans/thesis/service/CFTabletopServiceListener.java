package org.frans.thesis.service;


public interface CFTabletopServiceListener {
	
	public void addMobileDevice(String clientName, CFTabletopClientManager tabletopClientManager);
	
	public void fileFinished(CFFile file, String name);
	
	public void removeMobileDevice(String name);

}
