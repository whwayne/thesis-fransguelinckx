package org.frans.thesis.GUI;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;

public abstract class CFComponent {

	private static final float STANDARD_MEASURE = 150;

	protected MTRectangle component;

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
		return this.getMTComponent().getHeightXY(
				TransformSpace.GLOBAL);
	}

	protected MTRectangle getMTComponent(){
		return this.component;
	}

	protected Vector3D getPosition() {
		return this.getMTComponent().getPosition(
				TransformSpace.GLOBAL);
	}

	protected float getWidth() {
		return this.getMTComponent().getWidthXY(
				TransformSpace.GLOBAL);
	}

	protected abstract boolean isStackable();

	protected void reposition(Vector3D position) {
		this.getMTComponent().setPositionGlobal(position);
	}
	
	protected void rotate(Vector3D rotationPoint, float degrees){
		Vector3D point = new Vector3D(rotationPoint.x + this.getWidth()/2, rotationPoint.y + this.getHeight()/2);
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
					scalingHeightFactor, 1, this.getMTComponent().getPosition(TransformSpace.GLOBAL));
		}
	}
}
