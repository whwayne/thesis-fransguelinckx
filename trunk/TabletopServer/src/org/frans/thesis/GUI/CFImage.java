package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.gestureAction.DefaultScaleAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class CFImage implements IGestureEventListener{

	private static String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator;
	private static final float STANDARD_MEASURE = 100;
	private MTRectangle image;
	private MTApplication mtApplication;

	private CFScene scene;
	
	public CFImage(MTApplication mtApplication, String imageName, CFScene scene) {
		this.mtApplication = mtApplication;
		this.scene = scene;
		PImage pImage = getMTApplication().loadImage(imagePath + imageName);
		this.image = new MTRectangle(pImage, this.mtApplication);
		this.scaleImageToStackSize();
		
		this.getImage().unregisterAllInputProcessors();
		this.getImage().removeAllGestureEventListeners();
		
		this.getImage().registerInputProcessor(new DragProcessor(mtApplication));
		this.getImage().addGestureListener(DragProcessor.class, this);
		
		this.getImage().registerInputProcessor(new ScaleProcessor(mtApplication));
		this.getImage().addGestureListener(ScaleProcessor.class, new DefaultScaleAction());
		
		this.getImage().registerInputProcessor(new RotateProcessor(mtApplication));
		this.getImage().addGestureListener(RotateProcessor.class, new DefaultRotateAction());
	}

	private CFScene getCFScene(){
		return this.scene;
	}

	protected float getDistanceto(CFImage image){
		float result = 0;
		float x, y;
		x = Math.abs(this.getImage().getPosition(TransformSpace.RELATIVE_TO_PARENT).getX() - image.getImage().getPosition(TransformSpace.RELATIVE_TO_PARENT).getX());
		y = Math.abs(this.getImage().getPosition(TransformSpace.RELATIVE_TO_PARENT).getY() - image.getImage().getPosition(TransformSpace.RELATIVE_TO_PARENT).getY());
		result = (float) Math.sqrt((x*x) + (y*y));
		return result;
	}

	protected MTRectangle getImage() {
		return this.image;
	}

	private PApplet getMTApplication() {
		return this.mtApplication;
	}

	protected float getHeight() {
		return this.getImage().getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
	}

	protected float getWidth() {
		return this.getImage().getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
	}

	@Override
	public boolean processGestureEvent(MTGestureEvent ge) {
		DragEvent de = (DragEvent)ge;
		de.getTargetComponent().translateGlobal(de.getTranslationVect()); //Moves the component
		switch (de.getId()) {
		case MTGestureEvent.GESTURE_ENDED:
			if(this.getCFScene().isCloseToCFImage(this)){
				this.getCFScene().addToStack(this);
			}
			break;
		default:
			break;
		}		
		return false;
	}
	
	protected void scaleImageToStackSize() {
		float scalingHeightFactor = CFImage.STANDARD_MEASURE / this.getHeight();
		float scalingWidthFactor = CFImage.STANDARD_MEASURE / this.getWidth();
		if(scalingHeightFactor < scalingWidthFactor){
			this.getImage().scale(scalingWidthFactor, scalingWidthFactor, 1, new Vector3D(0, 0, 0));
		}else{
			this.getImage().scale(scalingHeightFactor, scalingHeightFactor, 1, new Vector3D(0, 0, 0));
		}
	}
	
	protected void rotateRandomlyForStack(){
		this.getImage().rotateZ(new Vector3D(this.getHeight()/2, this.getWidth()/2, 0), (float) (Math.random() * 360), TransformSpace.LOCAL);
	}
	
	protected void reposition(Vector3D position){
		this.getImage().setPositionGlobal(position);
	}

	protected Vector3D getPosition() {
		return this.getImage().getPosition(TransformSpace.RELATIVE_TO_PARENT);
	}

}
