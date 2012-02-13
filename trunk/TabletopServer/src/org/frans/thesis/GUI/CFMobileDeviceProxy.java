package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.gestureAction.DefaultScaleAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class CFMobileDeviceProxy extends CFComponent implements IGestureEventListener {

	private MTRectangle component;
	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "android.png";
	private CFComponentMenu menu;
	private MTApplication mtApplication;

	private String name;
	private CFScene scene;
	private MTColor white = new MTColor(255, 255, 255);
	
	public CFMobileDeviceProxy(MTApplication mtApplication, CFScene scene,
			String name) {
		this.scene = scene;
		this.mtApplication = mtApplication;
		this.name = name;
		setUpComponent(mtApplication);
		this.scaleImageToStackSize();
		
		this.getMTComponent().unregisterAllInputProcessors();
		this.getMTComponent().removeAllGestureEventListeners();
		
		this.getMTComponent().registerInputProcessor(new DragProcessor(mtApplication));
		this.getMTComponent().addGestureListener(DragProcessor.class, new DefaultScaleAction());
		
		this.getMTComponent().registerInputProcessor(new RotateProcessor(mtApplication));
		this.getMTComponent().addGestureListener(RotateProcessor.class, new DefaultRotateAction());
		
		this.getMTComponent().registerInputProcessor(new TapProcessor(mtApplication));
		this.getMTComponent().addGestureListener(TapProcessor.class, this);
		
		this.getMTComponent().setNoStroke(true);
		this.getMTComponent().setDrawSmooth(true);
		
		this.menu = new CFComponentMenu(this, mtApplication);
		this.menu.addMenuItem("photos.png", new CFComponentMenuItemListener() {
			
			@Override
			public void processEvent() {
				downloadPhotos();
			}
		});
	}

	private void downloadPhotos(){
		System.out.println("Download photos");
	}

	private CFScene getCFScene() {
		return scene;
	}

	@Override
	public MTRectangle getMTComponent() {
		return this.component;
	}

	private PApplet getMTApplication() {
		return this.mtApplication;
	}

	@Override
	public boolean isStackable() {
		return false;
	}
	
	@Override
	public boolean processGestureEvent(MTGestureEvent ge) {
		TapEvent de = (TapEvent)ge;
		switch (de.getId()) {
		case TapEvent.BUTTON_UP:
			this.showMenu();
			break;
		default:
			break;
		}		
		return false;
	}

	private void setUpComponent(MTApplication mtApplication) {
		IFont fontArial = FontManager.getInstance().createFont(mtApplication,
				"arial.ttf", 40, // Font size
				white, // Font fill color
				white); // Font outline color
		// Create a textfield
		MTTextArea textField = new MTTextArea(mtApplication, fontArial);
		textField.setNoStroke(true);
		textField.setNoFill(true);
		textField.setText(this.name);
		textField.setPickable(false);

		PImage pImage = getMTApplication().loadImage(imagePath);
		MTRectangle mtImage = new MTRectangle(pImage, this.mtApplication);
		mtImage.setNoStroke(true);
		mtImage.setPickable(false);

		float height = textField.getHeightXY(TransformSpace.GLOBAL);
		height += mtImage.getHeightXY(TransformSpace.GLOBAL);
		float width = Math.max(textField.getWidthXY(TransformSpace.GLOBAL),
				mtImage.getWidthXY(TransformSpace.GLOBAL));
		this.component = new MTRectangle(width, height, mtApplication);

		this.component.addChild(textField);
		this.component.addChild(mtImage);
		mtImage.translate(new Vector3D(0, textField
				.getHeightXY(TransformSpace.GLOBAL), 0));
		this.component.setNoFill(true);
		this.component.setNoStroke(true);
	}

	private void showMenu() {
		if(this.getMenu().isVisible()){
			this.getMenu().setVisible(false);
		}else{
			this.getMenu().setVisible(true);
		}
	}
	
	private CFComponentMenu getMenu(){
		return this.menu;
	}
}
