package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.gestureAction.DefaultScaleAction;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;

import processing.core.PImage;

public class CFImage extends CFComponent implements IGestureEventListener {

//	private static String imagePath = "org" + MTApplication.separator + "frans"
//			+ MTApplication.separator + "thesis" + MTApplication.separator
//			+ "GUI" + MTApplication.separator + "data"
//			+ MTApplication.separator;
	private MTApplication mtApplication;
	private CFScene scene;
	private MTColor color;

	public CFImage(MTApplication mtApplication, String imageName, CFScene scene, MTColor color) {
		this.color = color;
		this.mtApplication = mtApplication;
		this.scene = scene;
		PImage pImage = getMTApplication().loadImage(imageName);
		this.component = new MTRectangle(this.mtApplication, pImage);
		this.scaleImageToStackSize();

		setUpGestures(mtApplication);

		this.getMTComponent().setStrokeColor(this.getColor());
		this.getCFScene().addCFComponent(getCFImage());
	}

	private void setUpGestures(MTApplication mtApplication) {
		this.getMTComponent().unregisterAllInputProcessors();
		this.getMTComponent().removeAllGestureEventListeners();

		this.getMTComponent().registerInputProcessor(
				new DragProcessor(mtApplication));
		this.getMTComponent().addGestureListener(DragProcessor.class, this);

		this.getMTComponent().registerInputProcessor(
				new ScaleProcessor(mtApplication));
		this.getMTComponent().addGestureListener(ScaleProcessor.class,
				new DefaultScaleAction());

		this.getMTComponent().registerInputProcessor(
				new RotateProcessor(mtApplication));
		this.getMTComponent().addGestureListener(RotateProcessor.class,
				new DefaultRotateAction());
		
		this.getMTComponent().registerInputProcessor(new TapAndHoldProcessor(mtApplication, 1500));
		this.getMTComponent().addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(mtApplication, this.getCFScene().getCanvas()));
		this.getMTComponent().addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapAndHoldEvent th = (TapAndHoldEvent) ge;
				switch (th.getId()) {
				case TapAndHoldEvent.GESTURE_ENDED:
					if(th.isHoldComplete()){
						new CFPhotoAlbum(getMTApplication(), getCFImage(), getCFScene());
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
	}
	
	private MTColor getColor(){
		return this.color;
	}
	
	private CFImage getCFImage(){
		return this;
	}

	private CFScene getCFScene() {
		return this.scene;
	}

	protected MTRectangle getImage() {
		return this.component;
	}

	private MTApplication getMTApplication() {
		return this.mtApplication;
	}

	@Override
	protected MTRectangle getMTComponent() {
		return this.component;
	}

	@Override
	public boolean isStackable() {
		return true;
	}

	@Override
	public boolean processGestureEvent(MTGestureEvent ge) {
		DragEvent de = (DragEvent) ge;
		de.getTarget().translateGlobal(de.getTranslationVect());
		
		switch (de.getId()) {
		case MTGestureEvent.GESTURE_ENDED:
			if (this.getCFScene().isCloseToCFComponent(this)) {
				if (this.getCFScene().getNearCFComponents(this).get(0)
						.isStackable()) {
					this.getCFScene().addToStack(this);
				}else{
					for(CFComponent component : this.getCFScene().getNearCFComponents(this)){
						if(component.isPhotoAlbum()){
							CFPhotoAlbum album = (CFPhotoAlbum) component;
							album.addImage(this);
							break;
						}
					}
				}
			}
			break;
		default:
			break;
		}
		return false;
	}
}
