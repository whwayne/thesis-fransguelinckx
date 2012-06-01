package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFScene;
import org.mt4j.MTApplication;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;

import processing.core.PImage;

public class CFTrashCan extends CFComponent{
	
	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "trashcan.png";

	public CFTrashCan(MTApplication mtApplication, CFScene scene) {
		super(mtApplication, scene);
		PImage pImage = mtApplication.loadImage(imagePath);
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

	protected boolean isTrashCan() {
		return true;
	}

	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		if(component instanceof CFImage){
			this.getCFScene().removeCFComponent(component);
		}else if(component instanceof CFPhotoAlbum){
			this.getCFScene().removeCFComponent(component);
		}
	}

}
