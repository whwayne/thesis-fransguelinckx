package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;

import processing.core.PApplet;
import processing.core.PImage;

public class CFMobileDeviceProxy extends CFComponent {

	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "android.png";
	private MTApplication mtApplication;
	// private CFScene scene;
	private MTRectangle component;
	private String name;
	private MTColor white = new MTColor(255,255,255);

	public CFMobileDeviceProxy(MTApplication mtApplication, CFScene scene, String name) {
		// this.scene = scene;
		this.mtApplication = mtApplication;
		this.name = name;
		IFont fontArial = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
				25, 	//Font size
				white,  //Font fill color
				white);	//Font outline color
		//Create a textfield
		MTTextArea textField = new MTTextArea(mtApplication, fontArial); 
		
		textField.setNoStroke(true);
		textField.setNoFill(true);
		
		textField.setText(this.name);
		PImage pImage = getMTApplication().loadImage(imagePath);
		this.component = new MTRectangle(pImage, this.mtApplication);
		this.component.addChild(textField);
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
