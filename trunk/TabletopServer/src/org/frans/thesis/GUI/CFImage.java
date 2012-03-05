package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
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
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class CFImage extends CFComponent implements IGestureEventListener {

	private static String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator;

	private MTApplication mtApplication;

	private CFScene scene;

	public CFImage(MTApplication mtApplication, String imageName, CFScene scene) {
		this.mtApplication = mtApplication;
		this.scene = scene;
		PImage pImage = getMTApplication().loadImage(imageName);
		this.component = new MTRectangle(this.mtApplication, pImage);
		this.scaleImageToStackSize();

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
				case TapAndHoldEvent.GESTURE_STARTED:
					break;
				case TapAndHoldEvent.GESTURE_UPDATED:
					break;
				case TapAndHoldEvent.GESTURE_ENDED:
					//TODO
					if(th.isHoldComplete()){
						System.out.println("tap and hold");
					}
					break;
				default:
					break;
				}
				return false;
			}
		});

		this.getMTComponent().setNoStroke(true);
		this.getMTComponent().setDrawSmooth(true);
		this.component.setPositionGlobal(new Vector3D(mtApplication.getWidth()/2, mtApplication.getHeight()/2));
	}

	private CFScene getCFScene() {
		return this.scene;
	}

	protected MTImage getImage() {
		return (MTImage) this.component;
	}

	private PApplet getMTApplication() {
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
		de.getTargetComponent().translateGlobal(de.getTranslationVect()); // Moves
																			// the
																			// component
		switch (de.getId()) {
		case MTGestureEvent.GESTURE_ENDED:
			if (this.getCFScene().isCloseToCFComponent(this)) {
				if (this.getCFScene().getNearCFComponents(this).get(0)
						.isStackable()) {
					this.getCFScene().addToStack(this);
				}
			}
			break;
		default:
			break;
		}
		return false;
	}
}
