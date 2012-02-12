package org.frans.thesis.service;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFScene;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;

import processing.core.PApplet;
import processing.core.PImage;

public class CFMobileDeviceProxy extends CFComponent{
	
	private String svgPath =  "org" + MTApplication.separator + "frans" + MTApplication.separator + "thesis" + MTApplication.separator + "GUI" + MTApplication.separator + "data" + MTApplication.separator + "android.svg";
	private MTApplication mtApplication;
	private CFScene scene;
	private MTRectangle component;
	
	public CFMobileDeviceProxy(MTApplication mtApplication, CFScene scene){
		this.scene = scene;
		this.mtApplication = mtApplication;

		PImage pImage = getMTApplication().loadImage(svgPath);
		this.component = new MTRectangle(pImage, this.mtApplication);
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
