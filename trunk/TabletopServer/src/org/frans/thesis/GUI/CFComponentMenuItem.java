package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;

import processing.core.PImage;

public class CFComponentMenuItem extends CFComponent{
	
	private CFComponentMenuItemListener listener;
	private MTRectangle image;
	private MTRectangle getImage() {
		return image;
	}

	private static String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator;
	
	public CFComponentMenuItem(String fileName, CFComponentMenuItemListener listener, MTApplication mtApplication){
		this.listener = listener;
		PImage pImage = mtApplication.loadImage(imagePath + fileName);
		image = new MTRectangle(pImage, mtApplication);
		image.setVisible(false);
	}
	
	protected void processEvent(){
		this.listener.processEvent();
	}

	public void setVisible(boolean visible) {
		this.getImage().setVisible(visible);
	}

	@Override
	protected MTRectangle getMTComponent() {
		return this.image;
	}

	@Override
	protected boolean isStackable() {
		return false;
	}
}
