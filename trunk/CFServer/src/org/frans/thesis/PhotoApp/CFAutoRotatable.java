package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponentModifiable;
import org.mt4j.util.math.Vector3D;

/**
 * This interface allows for interactive components to be automatically rotated.
 * It extends CFComponentModifiable.
 */
public interface CFAutoRotatable extends CFComponentModifiable {
	
	/**
	 * Checks if the component allows autorotation.
	 * @return
	 * True if the component allows it and vice versa.
	 */
	public boolean autoRotateIsOn();

	/**
	 * Returns the position of a component.
	 */
	public Vector3D getPosition();

	/**
	 * Rotates the component to a given angle.
	 * @param i
	 * The angle to rotate the component to.
	 */
	public void rotateTo(int i);

	/**
	 * Scales the component to a compact size.
	 */
	public void scaleComponentToStackSize();

	/**
	 * Turns off the autorotation feature of a component. 
	 */
	public void turnAutoRotateOff();

	/**
	 * Turns on the autorotation feature of a component.
	 */
	public void turnAutoRotateOn();
}
