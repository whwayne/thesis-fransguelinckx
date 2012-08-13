package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponentModifiable;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

public interface AutoScalable extends CFComponentModifiable{
	
	public boolean autoScaleIsOn();

	public void turnAutoScaleOff();

	public void turnAutoScaleOn();

	public void scaleComponentToStackSize();

	public void autoScale();

	public Vector3D getPosition(TransformSpace global);
	
	

}
