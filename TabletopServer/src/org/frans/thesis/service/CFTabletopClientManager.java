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

	protected void fileFinished(File file, String name){
		this.getService().fileFinished(file, name);
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

	protected void receivePieceOfFile(String fileName, String clientName, byte[] buffer, boolean lastPiece) {
		this.getClients().get(clientName).receivePieceOfFile(fileName, buffer, lastPiece);
	}

	protected void setIdle(String name) {
		this.getClients().get(name).setStatus(CFTabletopClient.IDLE);
	}

}
