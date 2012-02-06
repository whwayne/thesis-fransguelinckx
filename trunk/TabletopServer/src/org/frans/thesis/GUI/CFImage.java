package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;

import processing.core.PApplet;
import processing.core.PImage;

public class CFImage {
	
	private static String imagePath =  "org" + MTApplication.separator + "frans" + MTApplication.separator + "thesis" + MTApplication.separator + "GUI" + MTApplication.separator + "data" + MTApplication.separator;
	private MTApplication mtApplication;
	private MTRectangle image;
	
	public CFImage(MTApplication mtApplication, String imageName){
		this.mtApplication = mtApplication;
		PImage pImage= getMTApplication().loadImage(imagePath + imageName);
		this.image = new MTRectangle(pImage, this.mtApplication);
	}

	private PApplet getMTApplication() {
		return this.mtApplication;
	}
	
	protected MTRectangle getImage(){
		return this.image;
	}

}
