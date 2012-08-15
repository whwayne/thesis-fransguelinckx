package org.frans.thesis.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * A class to represent a connected mobile device on the tabletop. It keeps
 * track of the communication between the mobile device and the tabletop. Tasks
 * are delegates to this class by the CFTabletopClientManager.
 */
public class CFTabletopClient {

	/**
	 * Five integers representing the status of a client. 0 = idle, nothing has
	 * to happen 1 = the tabletop is requesting photos from the client. 2 = the
	 * tabletop has one or more photos ready to be published on facebook. 3 =
	 * the tabletop is requesting music from the client. 4 = the tabletop has a
	 * music file ready to be sent to the tabletop.
	 */
	public static final int IDLE = 0;
	public static final int PUBLISHING_PHOTOS = 2;
	public static final int REQUESTING_MUSIC = 3;
	public static final int REQUESTING_PHOTOS = 1;
	public static final int SENDING_MUSIC = 4;

	/**
	 * A place where an unfinished file is being stored when this client is in
	 * the process of receiving a file.
	 */
	private CFFile bufferFileIn;

	/**
	 * The file that is being sent to a mobile device.
	 */
	private CFFile bufferFileOut;

	/**
	 * A list of file that have to be sent to the mobile device.
	 */
	private LinkedList<CFFile> fileCue;

	/**
	 * A list of image paths that have to be sent to the mobile device for
	 * publication
	 */
	private LinkedList<String> imageCue;

	/**
	 * A file output stream for sending files.
	 */
	private FileInputStream in;

	/**
	 * A boolean to indicate wheter this client is in the process of receiving a
	 * file.
	 */
	private boolean isReceivingFile = false;

	/**
	 * The client manager to which this client belongs.
	 */
	private CFTabletopClientManager manager;

	/**
	 * The name of the client being represented by this instance.
	 */
	private String name;

	/**
	 * A file input stream for receiving files.
	 */
	private FileOutputStream out;

	/**
	 * And integer representing the status of this client.
	 */
	private int status;

	/**
	 * The path to the working directory of this java-application.
	 */
	private final String workingDirectory = System.getProperty("user.dir");

	/**
	 * The public constructor for this class. Sets the name and manager and
	 * initiates the status as IDLE.
	 * 
	 * @param name
	 *            The name of the mobile device that this instance represents.
	 * @param manager
	 *            The manager to which this client belongs.
	 */
	public CFTabletopClient(String name, CFTabletopClientManager manager) {
		this.name = name;
		this.manager = manager;
		this.imageCue = new LinkedList<String>();
		this.fileCue = new LinkedList<CFFile>();
		this.status = IDLE;
	}

	/**
	 * Writes the contents of an array of bytes into the output stream.
	 * 
	 * @param buffer
	 *            The array of bytes that has to be written.
	 * @throws IOException
	 */
	private void appendBuffer(byte[] buffer) throws IOException {
		this.getOutputStream().write(buffer);
	}

	/**
	 * Flushes and closes the output stream and notices the manager.
	 * 
	 * @throws IOException
	 */
	private void finishFile() throws IOException {
		this.getOutputStream().flush();
		this.getOutputStream().close();
		this.getManager().fileFinished(this.getBufferFileIn(), this.getName());
	}

	/**
	 * Returns bufferFileIn of this client.
	 */
	private CFFile getBufferFileIn() {
		return this.bufferFileIn;
	}

	/**
	 * Returns the list of images that are in line to be published on facebook.
	 */
	private LinkedList<String> getImageCue() {
		return this.imageCue;
	}

	/**
	 * Returns the manager of this client.
	 */
	private CFTabletopClientManager getManager() {
		return this.manager;
	}

	/**
	 * Returns the name of this client.
	 */
	private String getName() {
		return this.name;
	}

	/**
	 * A method that gets the next array of bytes that has to be sent to the
	 * mobile device of this client. If no files are in line to be sent to the
	 * mobile device, this method sends an array with length of 1.
	 */
	protected byte[] getNextOutgoingFileBuffer() {
		this.setStatus(IDLE);
		byte[] buf = null;
		int status = -1;
		try {
			if (bufferFileOut == null) {
				bufferFileOut = this.fileCue.poll();
				if (bufferFileOut != null) {
					in = new FileInputStream(bufferFileOut.getFile());
				}
			}
			buf = new byte[100000];
			status = in.read(buf);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (status == -1) {
			bufferFileOut = null;
			return new byte[1];
		} else {
			return buf;
		}
	}

	/**
	 * Returns the output stream of this client.
	 */
	private FileOutputStream getOutputStream() {
		return this.out;
	}

	/**
	 * Returns the current status of this client.
	 * 
	 * @return
	 */
	protected int getStatus() {
		return this.status;
	}

	/**
	 * Returns the next image that has to be published on facebook and removes
	 * it from the list. If it was the last image, the status of this client is
	 * set to IDLE.
	 */
	protected String popImageCue() {
		String result = this.getImageCue().poll();
		if (this.getImageCue().peek() == null) {
			this.setStatus(IDLE);
		}
		return result;
	}

	/**
	 * A method to publish an image on facebook.
	 * 
	 * @param cfFile
	 *            The file that has to be published on facebook.
	 */
	public void publishImageOnFacebook(CFFile cfFile) {
		this.getImageCue().add(cfFile.getPath());
		this.setStatus(PUBLISHING_PHOTOS);
	}

	/**
	 * A method to publish an number of images on facebook.
	 * 
	 * @param files
	 *            The images that have to be published.
	 */
	public void publishImagesOnFacebook(ArrayList<String> files) {
		for (String file : files) {
			this.getImageCue().add(file);
		}
		this.setStatus(PUBLISHING_PHOTOS);
	}

	/**
	 * Accepts an array of bytes and adds it to the rest of a file or starts a
	 * new file if necessary.
	 * 
	 * @param path
	 *            The path of the file in teh file system of the mobile device.
	 * @param buffer
	 *            The array of bytes.
	 * @param lastPiece
	 *            A boolean to indicate if this is the last piece of this file.
	 */
	protected void receivePieceOfFile(String path, byte[] buffer,
			boolean lastPiece) {
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

	/**
	 * A method to send a file to the mobile device of this client.
	 */
	public void sendFile(CFFile file) {
		this.fileCue.add(file);
		this.setStatus(SENDING_MUSIC);
	}

	/**
	 * Sets the status of this client to a given number.
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Opens e new file and attaches the file output stream.
	 * 
	 * @param path
	 *            The path of the file in the file system of the mobile device.
	 *            Used to derive the extension of the file.
	 * @throws FileNotFoundException
	 */
	private void startNewFile(String path) throws FileNotFoundException {
		String ext = path.substring(path.lastIndexOf(".") + 1, path.length())
				.toLowerCase();
		this.bufferFileIn = new CFFile(path, new File(this.workingDirectory
				+ Calendar.getInstance().getTimeInMillis() + "." + ext));
		this.out = new FileOutputStream(bufferFileIn.getFile());
	}
}
