package org.frans.thesis.GUI;

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
import org.mt4j.util.math.Vector3D;

/**
 * This abstract class represents a visual, interactive component on the
 * tabletop and inherits from MTComponent (part of the MT4j-framework)
 */
public abstract class CFComponent extends MTRectangle implements
		CFComponentModifiable {

	/**
	 * Represents the maximum size of the side of a component when it is reduced
	 * to stack size.
	 */
	protected static final float STACK_SIZE = 150;

	/**
	 * Represents the size of the x-side of a component when it is autoscaled to
	 * large size.
	 */
	protected static final int X_WIDTH = 500;

	/**
	 * The angle of the component.
	 */
	private int angle = 0;

	/**
	 * A contextual menu that belongs to this component.
	 */
	private CFComponentMenu menu;

	/**
	 * The scene to which this component belongs.
	 */
	private CFScene scene;

	/**
	 * Public constructor for this abstract component. Sets two arguments and
	 * calls setUpGestures().
	 * 
	 * @param mtApplication
	 *            The application to which this component belongs.
	 * @param scene
	 *            The scene to which this component belongs.
	 */
	public CFComponent(CFScene scene) {
		super(scene.getMTApplication(), 100, 100);
		this.scene = scene;
		setUpGestures();
	}

	/**
	 * Autoscales this component so its fits into a square with length X_WIDTH
	 * as side.
	 */
	public void autoScale() {
		float scalefactor = X_WIDTH / this.getWidth();
		this.scaleImage(scalefactor);
	}

	/**
	 * Returns the current angle of this component.
	 */
	protected int getAngle() {
		return this.angle;
	}

	/**
	 * Returns this component
	 */
	protected CFComponent getCFComponent() {
		return this;
	}

	/**
	 * Returns the scene to which this component belongs.
	 */
	protected CFScene getCFScene() {
		return this.scene;
	}

	/**
	 * Returns the contextual menu that belongs to this components.
	 * 
	 * @return The contextual menu that belongs to this component. Return null
	 *         if this component has no menu.
	 */
	protected CFComponentMenu getComponentMenu() {
		return this.menu;
	}

	/**
	 * Calculates the distance between teh centers of this component and a given
	 * component
	 * 
	 * @param component
	 *            The component to which the distance has to be calculated
	 * @return The distance in pixels.
	 */
	public float getDistanceto(CFComponent component) {
		float result = 0;
		float x, y;
		x = Math.abs(this.getPosition(TransformSpace.RELATIVE_TO_PARENT).getX()
				- component.getPosition(TransformSpace.RELATIVE_TO_PARENT)
						.getX());
		y = Math.abs(this.getPosition(TransformSpace.RELATIVE_TO_PARENT).getY()
				- component.getPosition(TransformSpace.RELATIVE_TO_PARENT)
						.getY());
		result = (float) Math.sqrt((x * x) + (y * y));
		return result;
	}

	/**
	 * Returns the current height of this component.
	 */
	public float getHeight() {
		return this.getHeightXY(TransformSpace.GLOBAL);
	}

	/**
	 * Returns the current position of this component.
	 */
	protected Vector3D getPosition() {
		return this.getPosition(TransformSpace.GLOBAL);
	}

	/**
	 * Returns the current width of this component.
	 */
	public float getWidth() {
		return this.getWidthXY(TransformSpace.GLOBAL);
	}

	/**
	 * An abstract method that gets called automatically when other components
	 * are dragged near this component.
	 * 
	 * @param component
	 *            The component that was dragged near this component.
	 */
	public abstract void handleDroppedCFComponent(CFComponent component);

	/**
	 * An abstract method that gets called automatically when other components
	 * are rotated.
	 * 
	 * @param component
	 *            The component that was rotated.
	 */
	public abstract void handleRotatedCFComponent(CFComponent component);

	/**
	 * An abstract method that gets called automatically when other components
	 * are scaled.
	 * 
	 * @param component
	 *            The component that was scaled.
	 */
	public abstract void handleScaledCFComponent(CFComponent component);

	/**
	 * Repositions this component.
	 * 
	 * @param position
	 *            The position to which this component has to be moved.
	 */
	protected void reposition(Vector3D position) {
		this.setPositionGlobal(position);
	}

	/**
	 * Rotates this component.
	 * 
	 * @param rotationPoint
	 *            The point around which this component has to be rotated.
	 * @param degrees
	 *            The number of degrees this component has to be rotated.
	 */
	protected void rotate(Vector3D rotationPoint, int degrees) {
		this.angle += degrees;
		this.angle = this.angle % 360;
		Vector3D point = new Vector3D(rotationPoint.x + this.getWidth() / 2,
				rotationPoint.y + this.getHeight() / 2);
		this.rotateZ(point, degrees);
	}

	/**
	 * Rotates this component to a given angle.
	 */
	public void rotateTo(int angle) {
		int result = (angle - this.angle) % 360;
		this.angle = angle;
		this.rotateZ(this.getCenterPointGlobal(), result);
	}

	/**
	 * Scales this component so it fits into a square with STACK_SIZE as side.
	 */
	public void scaleComponentToStackSize() {
		float scalingHeightFactor = CFComponent.STACK_SIZE / this.getHeight();
		float scalingWidthFactor = CFComponent.STACK_SIZE / this.getWidth();
		if (scalingHeightFactor < scalingWidthFactor) {
			this.scale(scalingWidthFactor, scalingWidthFactor, 1,
					this.getPosition(TransformSpace.GLOBAL));
		} else {
			this.scale(scalingHeightFactor, scalingHeightFactor, 1,
					this.getPosition(TransformSpace.GLOBAL));
		}
	}

	/**
	 * Scales this component with a given factor.
	 */
	public void scaleImage(float factor) {
		this.scale(factor, factor, 1, this.getPosition(TransformSpace.GLOBAL));
	}

	/**
	 * Sets a contextual menu for this component.
	 */
	protected void setComponentMenu(CFComponentMenu menu) {
		this.menu = menu;
	}

	/**
	 * Sets up some gesture listeners so this interactive component reacts to
	 * user gestures, more precisely dragging, scaling and rotating.
	 */
	private void setUpGestures() {
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();

		this.registerInputProcessor(new DragProcessor(this.getRenderer()));
		this.addGestureListener(DragProcessor.class, new DefaultDragAction());

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

		this.registerInputProcessor(new ScaleProcessor(this.getRenderer()));
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

		this.registerInputProcessor(new RotateProcessor(this.getRenderer()));
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
	}
}
