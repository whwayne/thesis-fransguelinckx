package org.frans.thesis.service;

import java.io.File;
import java.util.HashMap;

public class CFTabletopClientManager {
	
	private HashMap<String, CFTabletopClient> clients;
	private CFTabletopService service;
	
	public CFTabletopClientManager(CFTabletopService service){
		this.service = service;
		this.clients = new HashMap<String, CFTabletopClient>();
	}
	
	protected CFTabletopClient addTabletopClient(String name) {
		CFTabletopClient tabletopClient = new CFTabletopClient(name, this);
		this.getClients().put(name, tabletopClient);
		return tabletopClient;
	}

	protected void fileFinished(File file){
		this.getService().fileFinished(file);
	}
	
	private HashMap<String, CFTabletopClient> getClients(){
		return this.clients;
	}

	private CFTabletopService getService(){
		return this.service;
	}
	
	protected int getStatus(String name) {
		return this.getClients().get(name).getStatus();
	}
	
	protected void receivePieceOfFile(int id, byte[] buffer){
		
	}

	protected void receivePieceOfFile(String name, byte[] buffer, boolean lastPiece) {
		this.getClients().get(name).receivePieceOfFile(buffer, lastPiece);
	}

	protected void setIdle(String name) {
		this.getClients().get(name).setStatus(CFTabletopClient.IDLE);
	}

}
