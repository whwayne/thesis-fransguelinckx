package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponentModifiable;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

public interface CFAutoRotatable extends CFComponentModifiable {

	public boolean autoRotateIsOn();

	public Vector3D getPosition(TransformSpace global);

	public void rotateTo(int i);

	public void scaleComponentToStackSize();

	public void turnAutoRotateOff();

	public void turnAutoRotateOn();

}
