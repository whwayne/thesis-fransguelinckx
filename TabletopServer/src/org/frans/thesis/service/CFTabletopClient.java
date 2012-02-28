package org.frans.thesis.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class CFTabletopClient {

	public static final int IDLE = 0;
	public static final int REQUESTING_PHOTOS = 1;

	private final String workingDirectory = System.getProperty("user.dir");
	private String name;
	private File file;
	private FileOutputStream out;
	private boolean isReceivingFile = false;
	private CFTabletopClientManager manager;
	private int status;

	public CFTabletopClient(String name, CFTabletopClientManager manager) {
		this.name = name;
		this.manager = manager;
		this.status = IDLE;
	}

	protected void receivePieceOfFile(byte[] buffer, boolean lastPiece) {
		try {
			if (!isReceivingFile) {
				// nieuwe file maken en beginnen vullen
				this.isReceivingFile = true;
				this.startNewFile();
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

	private void finishFile() throws IOException {
		this.getOutputStream().flush();
		this.getOutputStream().close();
		this.getManager().fileFinished(this.getFile());
	}

	private File getFile() {
		return this.file;
	}

	private void startNewFile() throws FileNotFoundException {
		this.file = new File(this.workingDirectory
				+ Calendar.getInstance().getTimeInMillis() + ".jpg");
		this.out = new FileOutputStream(file);
	}

	private FileOutputStream getOutputStream() {
		return this.out;
	}

	private void appendBuffer(byte[] buffer) throws IOException {
		this.getOutputStream().write(buffer);
	}

	private CFTabletopClientManager getManager() {
		return this.manager;
	}

	protected int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
