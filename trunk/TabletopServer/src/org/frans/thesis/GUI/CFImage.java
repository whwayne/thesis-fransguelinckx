package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.gestureAction.DefaultScaleAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;

import processing.core.PApplet;
import processing.core.PImage;

public class CFImage extends CFComponent implements IGestureEventListener{

	private static String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator;
	private MTImage rectangle;
	private MTApplication mtApplication;

	private CFScene scene;
	
	public CFImage(MTApplication mtApplication, String imageName, CFScene scene) {
		this.mtApplication = mtApplication;
		this.scene = scene;
		PImage pImage = getMTApplication().loadImage(imagePath + imageName);
		this.rectangle = new MTImage(pImage, this.mtApplication);
		this.scaleImageToStackSize();
		
		this.getComponent().unregisterAllInputProcessors();
		this.getComponent().removeAllGestureEventListeners();
		
		this.getComponent().registerInputProcessor(new DragProcessor(mtApplication));
		this.getComponent().addGestureListener(DragProcessor.class, this);
		
		this.getComponent().registerInputProcessor(new ScaleProcessor(mtApplication));
		this.getComponent().addGestureListener(ScaleProcessor.class, new DefaultScaleAction());
		
		this.getComponent().registerInputProcessor(new RotateProcessor(mtApplication));
		this.getComponent().addGestureListener(RotateProcessor.class, new DefaultRotateAction());
	}

	private CFScene getCFScene(){
		return this.scene;
	}

	public MTImage getComponent() {
		return this.rectangle;
	}

	private PApplet getMTApplication() {
		return this.mtApplication;
	}

	@Override
	public boolean processGestureEvent(MTGestureEvent ge) {
		DragEvent de = (DragEvent)ge;
		de.getTargetComponent().translateGlobal(de.getTranslationVect()); //Moves the component
		switch (de.getId()) {
		case MTGestureEvent.GESTURE_ENDED:
			if(this.getCFScene().isCloseToCFComponent(this)){
				if(this.getCFScene().getNearCFComponents(this).get(0).isStackable()){
					this.getCFScene().addToStack(this);
				}
			}
			break;
		default:
			break;
		}		
		return false;
	}

	@Override
	public boolean isStackable() {
		return true;
	}
}
