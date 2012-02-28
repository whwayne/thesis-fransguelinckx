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
	
	protected void receivePieceOfFile(int id, byte[] buffer){
		
	}

	protected void receivePieceOfFile(String name, byte[] buffer, boolean lastPiece) {
		this.getClients().get(name).receivePieceOfFile(buffer, lastPiece);
	}
	
	private HashMap<String, CFTabletopClient> getClients(){
		return this.clients;
	}

	protected void addTabletopClient(String name) {
		this.getClients().put(name, new CFTabletopClient(name,this));
	}
	
	private CFTabletopService getService(){
		return this.service;
	}
	
	protected void fileFinished(File file){
		this.getService().fileFinished(file);
	}

}
