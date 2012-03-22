package org.frans.thesis.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class CFTabletopClient {

	public static final int IDLE = 0;
	public static final int REQUESTING_PHOTOS = 1;
	public static final int PUBLISHING_PHOTOS = 2;

	private CFFile file;
	private boolean isReceivingFile = false;
	private CFTabletopClientManager manager;
	private String name;
	private FileOutputStream out;
	private int status;
	private final String workingDirectory = System.getProperty("user.dir");
	private LinkedList<String> imageCue;

	public CFTabletopClient(String name, CFTabletopClientManager manager) {
		this.name = name;
		this.manager = manager;
		this.imageCue = new LinkedList<String>();
		this.status = IDLE;
	}

	private void appendBuffer(byte[] buffer) throws IOException {
		this.getOutputStream().write(buffer);
	}

	private void finishFile() throws IOException {
		this.getOutputStream().flush();
		this.getOutputStream().close();
		this.getManager().fileFinished(this.getFile(), this.getName());
	}
	
	private String getName(){
		return this.name;
	}

	private CFFile getFile() {
		return this.file;
	}

	private CFTabletopClientManager getManager() {
		return this.manager;
	}

	private FileOutputStream getOutputStream() {
		return this.out;
	}

	protected int getStatus() {
		return this.status;
	}

	protected void receivePieceOfFile(String path, byte[] buffer, boolean lastPiece) {
		try {
			if (!isReceivingFile) {
				// nieuwe file maken en beginnen vullen
				this.isReceivingFile = true;
				this.startNewFile(path);
				this.appendBuffer(buffer);
			} else if (lastPiece) {
				// laatste stuk eraan plakken en afsluiten
				this.appendBuffer(buffer);
				this.finishFile();
				this.isReceivingFile = false;
			} else {
				// stuk eraan plakken
				this.appendBuffer(buffer);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStatus(int status) {
		this.status = status;
	}

	private void startNewFile(String path) throws FileNotFoundException {
		this.file = new CFFile(path,new File(this.workingDirectory
				+ Calendar.getInstance().getTimeInMillis() + ".jpg"));
		this.out = new FileOutputStream(file.getFile());
	}

	public void publishImageOnFacebook(CFFile cfFile) {
		this.getImageCue().add(cfFile.getPath());
		this.setStatus(PUBLISHING_PHOTOS);
	}

	public void publishImagesOnFacebook(ArrayList<String> files) {
		for(String file : files){
			this.getImageCue().add(file);
		}
		this.setStatus(PUBLISHING_PHOTOS);
	}
	
	private LinkedList<String> getImageCue(){
		return this.imageCue;
	}
	
	protected String popImageCue(){
		String result = this.getImageCue().poll();
		if(this.getImageCue().peek() == null){
			this.setStatus(IDLE);
		}
		return result;
	}

}
