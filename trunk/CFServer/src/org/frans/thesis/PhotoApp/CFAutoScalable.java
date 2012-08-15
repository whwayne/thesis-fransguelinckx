package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponentModifiable;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

public interface CFAutoScalable extends CFComponentModifiable {

	public void autoScale();

	public boolean autoScaleIsOn();

	public Vector3D getPosition(TransformSpace global);

	public void scaleComponentToStackSize();

	public void turnAutoScaleOff();

	public void turnAutoScaleOn();

}
