package org.frans.thesis.GUI;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;

public abstract class CFComponent {
	
	private static final float STANDARD_MEASURE = 100;

	protected abstract MTRectangle getComponent();
	
	protected abstract boolean isStackable();

	protected float getDistanceto(CFComponent image){
		float result = 0;
		float x, y;
		x = Math.abs(this.getComponent().getPosition(TransformSpace.RELATIVE_TO_PARENT).getX() - image.getComponent().getPosition(TransformSpace.RELATIVE_TO_PARENT).getX());
		y = Math.abs(this.getComponent().getPosition(TransformSpace.RELATIVE_TO_PARENT).getY() - image.getComponent().getPosition(TransformSpace.RELATIVE_TO_PARENT).getY());
		result = (float) Math.sqrt((x*x) + (y*y));
		return result;
	}
	
	protected void scaleImageToStackSize() {
		float scalingHeightFactor = CFComponent.STANDARD_MEASURE / this.getHeight();
		float scalingWidthFactor = CFComponent.STANDARD_MEASURE / this.getWidth();
		if(scalingHeightFactor < scalingWidthFactor){
			this.getComponent().scale(scalingWidthFactor, scalingWidthFactor, 1, new Vector3D(0, 0, 0));
		}else{
			this.getComponent().scale(scalingHeightFactor, scalingHeightFactor, 1, new Vector3D(0, 0, 0));
		}
	}

	protected float getHeight() {
		return this.getComponent().getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
	}

	protected float getWidth() {
		return this.getComponent().getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
	}

	protected Vector3D getPosition() {
		return this.getComponent().getPosition(TransformSpace.RELATIVE_TO_PARENT);
	}
	
	protected void rotateRandomlyForStack(){
		this.getComponent().rotateZ(new Vector3D(this.getHeight()/2, this.getWidth()/2, 0), (float) (Math.random() * 360), TransformSpace.LOCAL);
	}
	
	protected void reposition(Vector3D position){
		this.getComponent().setPositionGlobal(position);
	}
}
