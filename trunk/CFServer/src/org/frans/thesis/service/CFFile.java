package org.frans.thesis.service;

import java.io.File;

/**
 * This class represents a file coming from mobile devices. It holds te file
 * itself and the location of the file in the file system of the mobile device.
 */
public class CFFile {

	/**
	 * The actual file.
	 */
	private File file;

	/**
	 * The path of the file in the file system of the mobile device.
	 */
	private String path;

	/**
	 * A public constructor.
	 * 
	 * @param path
	 *            The location of the file in the file system of the mobile
	 *            device.
	 * @param file
	 *            The file itself
	 */
	public CFFile(String path, File file) {
		this.file = file;
		this.path = path;
	}

	/**
	 * Returns the actual file.
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * Returns the location of the file in the file system of the mobile device.
	 */
	protected String getPath() {
		return this.path;
	}

}
