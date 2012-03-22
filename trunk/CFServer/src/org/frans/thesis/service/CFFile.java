package org.frans.thesis.service;

import java.io.File;

public class CFFile {
	
	private File file;
	private String path;
	
	public CFFile(String path, File file){
		this.file = file;
		this.path = path;
	}
	
	protected String getPath(){
		return this.path;
	}
	
	public File getFile(){
		return this.file;
	}

}
