package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponentModifiable;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

public interface AutoRotatable extends CFComponentModifiable{
	
	public boolean autoRotateIsOn();

	public void turnAutoRotateOff();

	public void turnAutoRotateOn();
	
	public void scaleComponentToStackSize();

	public Vector3D getPosition(TransformSpace global);

	public void rotateTo(int i);

}
