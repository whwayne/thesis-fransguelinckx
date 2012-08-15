package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFScene;
import org.mt4j.MTApplication;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;

import processing.core.PImage;

public class CFTrashCan extends CFComponent implements AutoRotatable,
		AutoScalable {

	private boolean autoRotate = true;
	private boolean autoScale = true;
	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "trashcan.png";

	public CFTrashCan(MTApplication mtApplication, CFScene scene) {
		super(mtApplication, scene);
		PImage pImage = mtApplication.loadImage(imagePath);
		// this.component = new MTRectangle(mtApplication, pImage);
		this.setTexture(pImage);
		this.scaleComponentToStackSize();

		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		this.registerInputProcessor(new DragProcessor(this.getCFScene()
				.getMTApplication()));
		this.addGestureListener(DragProcessor.class, new DefaultDragAction());
		this.registerInputProcessor(new RotateProcessor(this.getCFScene()
				.getMTApplication()));
		this.addGestureListener(RotateProcessor.class,
				new DefaultRotateAction());

		this.setNoStroke(true);
	}

	@Override
	public boolean autoRotateIsOn() {
		return this.autoRotate;
	}

	@Override
	public boolean autoScaleIsOn() {
		return this.autoScale;
	}

	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		if (component instanceof CFImage) {
			this.getCFScene().removeCFComponent(component);
		} else if (component instanceof CFPhotoAlbum) {
			this.getCFScene().removeCFComponent(component);
		}
	}

	@Override
	public void handleRotatedCFComponent(CFComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleScaledCFComponent(CFComponent component) {
		// TODO Auto-generated method stub

	}

	protected boolean isTrashCan() {
		return true;
	}

	@Override
	public void turnAutoRotateOff() {
		this.autoRotate = false;
	}

	@Override
	public void turnAutoRotateOn() {
		this.autoRotate = true;
	}

	@Override
	public void turnAutoScaleOff() {
		this.autoScale = false;
	}

	@Override
	public void turnAutoScaleOn() {
		this.autoScale = true;
	}

}
