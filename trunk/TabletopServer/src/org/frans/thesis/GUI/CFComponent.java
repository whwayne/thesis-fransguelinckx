package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.math.Vector3D;

public abstract class CFComponent {

	private static final float STANDARD_MEASURE = 150;
	private static final int X_LOW_TRESHHOLD = 480;
	private static final int X_HIGH_TRESHHOLD = 1440;
	private static final int Y_LOW_TRESHHOLD = 270;
	private static final int Y_HIGH_TRESHHOLD = 810;

	protected MTRectangle component;
	protected MTApplication mtApplication;

	public CFComponent(MTApplication mtApplication) {
		this.mtApplication = mtApplication;
		this.component = new MTRectangle(mtApplication, 10, 10);
		

		this.getMTComponent().unregisterAllInputProcessors();
		this.getMTComponent().removeAllGestureEventListeners();
		this.getMTComponent().registerInputProcessor(
				new DragProcessor(mtApplication));
		this.getMTComponent().addGestureListener(DragProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						DragEvent de = (DragEvent) ge;

						switch (de.getId()) {
						case MTGestureEvent.GESTURE_ENDED:
							break;
						case MTGestureEvent.GESTURE_UPDATED:
							Vector3D position = getMTComponent().getPosition(
									TransformSpace.GLOBAL);
							
							if(position.x < X_LOW_TRESHHOLD && position.y < Y_LOW_TRESHHOLD){
								System.out.println("zone 1");
							}else if(position.x > X_LOW_TRESHHOLD & position.x < X_HIGH_TRESHHOLD && position.y  < Y_LOW_TRESHHOLD){
								System.out.println("zone 2");
							}else if(position.x > X_HIGH_TRESHHOLD && position.y < Y_LOW_TRESHHOLD){
								System.out.println("zone 3");
							}else if(position.x > X_HIGH_TRESHHOLD && position.y > Y_LOW_TRESHHOLD && position.y < Y_HIGH_TRESHHOLD){
								System.out.println("zone 4");
							}else if(position.x > X_HIGH_TRESHHOLD && position.y > Y_HIGH_TRESHHOLD){
								System.out.println("zone 5");
							}else if(position.x > X_LOW_TRESHHOLD && position.x < X_HIGH_TRESHHOLD && position.y > Y_HIGH_TRESHHOLD){
								System.out.println("zone 6");
							}else if(position.x < X_LOW_TRESHHOLD && position.y > Y_HIGH_TRESHHOLD){
								System.out.println("zone 7");
							}else if(position.x < X_LOW_TRESHHOLD && position.y > Y_LOW_TRESHHOLD && position.y < Y_HIGH_TRESHHOLD){
								System.out.println("zone 8");
							}
							break;
						default:
							break;
						}
						return false;
					}
				});
	}

	public void testForBounds(MTGestureEvent ge) {
	}

	protected MTApplication getMTApplication() {
		return this.mtApplication;
	}

	protected float getDistanceto(CFComponent component) {
		float result = 0;
		float x, y;
		x = Math.abs(this.getMTComponent()
				.getPosition(TransformSpace.RELATIVE_TO_PARENT).getX()
				- component.getMTComponent()
						.getPosition(TransformSpace.RELATIVE_TO_PARENT).getX());
		y = Math.abs(this.getMTComponent()
				.getPosition(TransformSpace.RELATIVE_TO_PARENT).getY()
				- component.getMTComponent()
						.getPosition(TransformSpace.RELATIVE_TO_PARENT).getY());
		result = (float) Math.sqrt((x * x) + (y * y));
		return result;
	}

	protected float getHeight() {
		return this.getMTComponent().getHeightXY(TransformSpace.GLOBAL);
	}

	protected MTRectangle getMTComponent() {
		return this.component;
	}

	protected Vector3D getPosition() {
		return this.getMTComponent().getPosition(TransformSpace.GLOBAL);
	}

	protected float getWidth() {
		return this.getMTComponent().getWidthXY(TransformSpace.GLOBAL);
	}

	protected abstract boolean isStackable();

	protected boolean isPhotoAlbum() {
		return false;
	}

	protected void reposition(Vector3D position) {
		this.getMTComponent().setPositionGlobal(position);
	}

	protected void rotate(Vector3D rotationPoint, float degrees) {
		Vector3D point = new Vector3D(rotationPoint.x + this.getWidth() / 2,
				rotationPoint.y + this.getHeight() / 2);
		this.getMTComponent().rotateZ(point, degrees);
	}

	protected void rotateRandomlyForStack() {
		this.getMTComponent().rotateZ(
				new Vector3D(this.getHeight() / 2, this.getWidth() / 2, 0),
				(float) (Math.random() * 360), TransformSpace.LOCAL);
	}

	protected void scaleImageToStackSize() {
		float scalingHeightFactor = CFComponent.STANDARD_MEASURE
				/ this.getHeight();
		float scalingWidthFactor = CFComponent.STANDARD_MEASURE
				/ this.getWidth();
		if (scalingHeightFactor < scalingWidthFactor) {
			this.getMTComponent().scale(scalingWidthFactor, scalingWidthFactor,
					1, new Vector3D(0, 0, 0));
		} else {
			this.getMTComponent().scaleGlobal(scalingHeightFactor,
					scalingHeightFactor, 1,
					this.getMTComponent().getPosition(TransformSpace.GLOBAL));
		}
	}

	protected void scaleImage(float factor) {
		this.getMTComponent().scale(factor, factor, 1,
				this.getMTComponent().getPosition(TransformSpace.GLOBAL));
	}
}
