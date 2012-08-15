package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponentModifiable;
import org.mt4j.util.math.Vector3D;

/**
 * This interface allows for interactive components to be automatically scaled.
 * It extends CFComponentModifiable.
 */
public interface CFAutoScalable extends CFComponentModifiable {

	/**
	 * Scales an interactive component to large size.
	 */
	public void scaleComponentToLargeSize();

	/**
	 * Checks if the autoscale functionality of a component is turned on.
	 * @return
	 * True if it is on and vice versa.
	 */
	public boolean autoScaleIsOn();

	/**
	 * Returns the position of an interactive component.
	 */
	public Vector3D getPosition();

	/**
	 * Scales an interactive component to small (stack) size.
	 */
	public void scaleComponentToStackSize();

	/**
	 * Turns the autoscale functionality of a component off.
	 */
	public void turnAutoScaleOff();

	/**
	 * Turns the autoscale functionality of a component on.
	 */
	public void turnAutoScaleOn();

}
