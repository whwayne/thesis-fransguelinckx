package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;

import processing.core.PApplet;
import processing.core.PImage;

public class CFMobileDeviceProxy extends CFComponent{
	
	private String imagePath =  "org" + MTApplication.separator + "frans" + MTApplication.separator + "thesis" + MTApplication.separator + "GUI" + MTApplication.separator + "data" + MTApplication.separator + "android.png";
	private MTApplication mtApplication;
//	private CFScene scene;
	private MTRectangle component;
	
	public CFMobileDeviceProxy(MTApplication mtApplication, CFScene scene){
//		this.scene = scene;
		this.mtApplication = mtApplication;

		PImage pImage = getMTApplication().loadImage(imagePath);
		this.component = new MTRectangle(pImage, this.mtApplication);
		this.scaleImageToStackSize();
	}

	private PApplet getMTApplication() {
		return this.mtApplication;
	}

	@Override
	public MTRectangle getComponent() {
		return this.component;
	}

	@Override
	public boolean isStackable() {
		return false;
	}
}
