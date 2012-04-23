package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;

import processing.core.PImage;

public class CFCopier extends CFComponent{

	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "copier.png";
	
	public CFCopier(MTApplication mtApplication, CFScene scene) {
		super(mtApplication, scene);PImage pImage = mtApplication.loadImage(imagePath);
//		this.component = new MTRectangle(mtApplication, pImage);
		this.component.setTexture(pImage);
		this.scaleImageToStackSize();
		this.getCFScene().addCFComponent(this);

		this.getMTComponent().unregisterAllInputProcessors();
		this.getMTComponent().removeAllGestureEventListeners();
		this.getMTComponent().registerInputProcessor(new DragProcessor(getMTApplication()));
		this.getMTComponent().addGestureListener(DragProcessor.class, new DefaultDragAction());
		this.getMTComponent().registerInputProcessor(new RotateProcessor(getMTApplication()));
		this.getMTComponent().addGestureListener(RotateProcessor.class, new DefaultRotateAction());
		
		this.getMTComponent().setNoStroke(true);
	}

	@Override
	protected boolean isStackable() {
		return false;
	}
	
	protected boolean isCopier() {
		return true;
	}
}
