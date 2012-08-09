package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateEvent;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

/**
 * This abstract class represents a visual component on the tabletop and inherits from
 * MTComponent (part of the MT4j-framework)
 */
public abstract class CFComponent extends MTRectangle{

	/**
	 * Represents the maximum size of the side of a component when it is reduced to stack size.
	 */
	protected static final float STACK_SIZE = 150;
	
	/**
	 * Represents the size of the x-side of a component when it is autoscaled to large size.
	 */
	protected static final int X_WIDTH = 960;

	/**
	 * Represents the size of the y-side of a component when it is autoscaled to large size.
	 */
	protected static final int Y_HEIGHT = 540;

//	protected MTRectangle component;
//	protected MTApplication mtApplication;
	private int angle = 0;
	private CFScene scene;
	private boolean autoRotate = true;
	private boolean autoScale = true;
	private CFComponentMenu menu;

	public CFComponent(MTApplication mtApplication, CFScene scene) {
		super(mtApplication, 10, 10);
		this.scene = scene;
//		this.mtApplication = mtApplication;
//		this.component = new MTRectangle(mtApplication, 10, 10);
		setUpGestures(mtApplication);
	}

	private void setUpGestures(MTApplication mtApplication) {
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();

		this.registerInputProcessor(
				new DragProcessor(mtApplication));
		this.addGestureListener(DragProcessor.class,
				new DefaultDragAction());
		
		this.addGestureListener(DragProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						DragEvent de = (DragEvent) ge;
						switch (de.getId()) {
						case MTGestureEvent.GESTURE_UPDATED:
							getCFScene().cfComponentMoved(getCFComponent());
							break;
						case MTGestureEvent.GESTURE_ENDED:
							getCFScene().cFComponentDropped(getCFComponent());
							break;
						default:
							break;
						}
						return false;
					}
				});

		this.registerInputProcessor(
				new ScaleProcessor(mtApplication));
		this.addGestureListener(ScaleProcessor.class,
				new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						ScaleEvent se = (ScaleEvent) ge;
						scale(se.getScaleFactorX(), se.getScaleFactorY(),
								se.getScaleFactorZ(), se.getScalingPoint());
						return false;
					}
				});
		this.addGestureListener(ScaleProcessor.class,
				new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						turnAutoScaleOff();
						return false;
					}
				});

		this.registerInputProcessor(
				new RotateProcessor(mtApplication));
		this.addGestureListener(RotateProcessor.class,
				new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						RotateEvent re = (RotateEvent) ge;
						rotate(re.getRotationPoint(),
								(int) re.getRotationDegrees());
						return false;
					}
				});
		this.addGestureListener(RotateProcessor.class,
				new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						turnAutoRotateOff();
						return false;
					}
				});

		this.registerInputProcessor(
				new TapProcessor(mtApplication, 25, true, 350));
		this.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isDoubleTap()) {
							turnAutoRotateOn();
							turnAutoScaleOn();
						}
						return false;
					}
				});
	}

	protected CFComponent getCFComponent() {
		return this;
	}

	public abstract void handleDroppedCFComponent(CFComponent component);
	public abstract void handleScaledCFComponent(CFComponent component);
	public abstract void handleRotatedCFComponent(CFComponent component);

//	private double getDistanceToCenter() {
//		double result = 0;
//		float x = Math.abs(this.getMTComponent()
//				.getPosition(TransformSpace.GLOBAL).getX()
//				- (this.getMTApplication().getWidth() / 2));
//		float y = Math.abs(this.getMTComponent()
//				.getPosition(TransformSpace.GLOBAL).getY()
//				- (this.getMTApplication().getHeight() / 2));
//		result = Math.sqrt((x * x) + (y * y));
//		return result;
//	}

	public void autoScale() {
		// double distance = getDistanceToCenter();
		float scaleX = X_WIDTH / this.getWidth();
		float scaleY = Y_HEIGHT / this.getHeight();
		float scalefactor = Math.min(scaleX, scaleY);
		this.scaleImage(scalefactor);
	}

	public boolean autoRotateIsOn() {
		return this.autoRotate;
	}

	public boolean autoScaleIsOn() {
		return this.autoScale;
	}

	private void turnAutoRotateOff() {
		this.autoRotate = false;
	}

	protected void turnAutoScaleOff() {
		this.autoScale = false;
	}

	private void turnAutoRotateOn() {
		this.autoRotate = true;
	}

	private void turnAutoScaleOn() {
		this.autoScale = true;
	}

//	protected MTApplication getMTApplication() {
//		return this.getMTApplication();
//	}

	public float getDistanceto(CFComponent component) {
		float result = 0;
		float x, y;
		x = Math.abs(this.getPosition(TransformSpace.RELATIVE_TO_PARENT).getX()
				- component.getPosition(TransformSpace.RELATIVE_TO_PARENT).getX());
		y = Math.abs(this.getPosition(TransformSpace.RELATIVE_TO_PARENT).getY()
				- component.getPosition(TransformSpace.RELATIVE_TO_PARENT).getY());
		result = (float) Math.sqrt((x * x) + (y * y));
		return result;
	}

	public float getHeight() {
		return this.getHeightXY(TransformSpace.GLOBAL);
	}

//	public MTRectangle getMTComponent() {
//		return this.component;
//	}

	protected Vector3D getPosition() {
		return this.getPosition(TransformSpace.GLOBAL);
	}

	public float getWidth() {
		return this.getWidthXY(TransformSpace.GLOBAL);
	}

	protected void reposition(Vector3D position) {
		this.setPositionGlobal(position);
	}

	protected void rotate(Vector3D rotationPoint, int degrees) {
		this.angle += degrees;
		this.angle = this.angle % 360;

		Vector3D point = new Vector3D(rotationPoint.x + this.getWidth() / 2,
				rotationPoint.y + this.getHeight() / 2);
		this.rotateZ(point, degrees);
	}

	public void scale(float x, float y, float z, Vector3D scalingPoint) {
		this.scale(x, y, z, scalingPoint);
	}

	public void rotateTo(int angle) {
		int result = (angle - this.angle) % 360;
		this.angle = angle;
		this.rotateZ(
				this.getCenterPointGlobal(), result);
	}

	protected void rotateRandomlyForStack() {
		this.rotateZ(
				new Vector3D(this.getHeight() / 2, this.getWidth() / 2, 0),
				(float) (Math.random() * 360), TransformSpace.LOCAL);
	}

	public void scaleImageToStackSize() {
		float scalingHeightFactor = CFComponent.STACK_SIZE
				/ this.getHeight();
		float scalingWidthFactor = CFComponent.STACK_SIZE
				/ this.getWidth();
		if (scalingHeightFactor < scalingWidthFactor) {
			this.scale(scalingWidthFactor, scalingWidthFactor, 1, this.getPosition(TransformSpace.GLOBAL));
		} else {
			this.scale(scalingHeightFactor, scalingHeightFactor, 1, this.getPosition(TransformSpace.GLOBAL));
		}
	}

	public void scaleImage(float factor) {
		this.scale(factor, factor, 1,
				this.getPosition(TransformSpace.GLOBAL));
		// this.getMTComponent().scale(factor, factor, 1,
		// this.getMTComponent().getPosition(TransformSpace.GLOBAL));
	}

	protected int getAngle() {
		return this.angle;
	}

	protected CFScene getCFScene() {
		return this.scene;
	}
	
	protected CFComponentMenu getComponentMenu(){
		return this.menu;
	}
	
	protected void setComponentMenu(CFComponentMenu menu){
		this.menu = menu;
	}
}
