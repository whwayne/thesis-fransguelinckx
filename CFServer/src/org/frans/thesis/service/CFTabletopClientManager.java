package org.frans.thesis.service;

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

	protected void fileFinished(CFFile file, String name){
		this.getService().fileFinished(file, name);
	}
	
	private HashMap<String, CFTabletopClient> getClients(){
		return this.clients;
	}

	private CFTabletopService getService(){
		return this.service;
	}
	
	protected int getStatus(String clientName) {
		return this.getClients().get(clientName).getStatus();
	}

	protected void receivePieceOfFile(String path, String clientName, byte[] buffer, boolean lastPiece) {
		this.getClients().get(clientName).receivePieceOfFile(path, buffer, lastPiece);
	}

	protected void setIdle(String name) {
		this.getClients().get(name).setStatus(CFTabletopClient.IDLE);
	}

	protected String getFileToPublish(String clientName) {
		return this.getClients().get(clientName).popImageCue();
	}

	public void setStatus(String clientName, int status) {
		this.getClients().get(clientName).setStatus(status);
	}

	public void publishImageOnFacebook(String clientName, CFFile cfFile) {
		this.getClients().get(clientName).publishImageOnFacebook(cfFile);
	}

	public void sendFileToClient(String clientName, CFFile file) {
		this.getClients().get(clientName).sendMusicFile(file);
	}

	public byte[] getNextFileBuffer(String clientName) {
		return this.getClients().get(clientName).getNextFileBuffer();
	}

}
