package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFComponentModifier;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

public class CFAutoScaler extends CFComponentModifier {

	@Override
	public void handleMovedCFComponent(CFComponent component) {
		// TODO Auto-generated method stub
		Vector3D position = component.getPosition(
				TransformSpace.GLOBAL);

		if (component.autoRotateIsOn()) {
			if (position.x < X_LOW_TRESHHOLD
					&& position.y < Y_LOW_TRESHHOLD) {
				// System.out.println("zone 1");
				component.scaleComponentToStackSize();
			} else if (position.x > X_LOW_TRESHHOLD
					& position.x < X_HIGH_TRESHHOLD
					&& position.y < Y_LOW_TRESHHOLD) {
				// System.out.println("zone 2");
				component.scaleComponentToStackSize();
			} else if (position.x > X_HIGH_TRESHHOLD
					&& position.y < Y_LOW_TRESHHOLD) {
				// System.out.println("zone 3");
				component.scaleComponentToStackSize();
			} else if (position.x > X_HIGH_TRESHHOLD
					&& position.y > Y_LOW_TRESHHOLD
					&& position.y < Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 4");
				component.scaleComponentToStackSize();
			} else if (position.x > X_HIGH_TRESHHOLD
					&& position.y > Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 5");
				component.scaleComponentToStackSize();
			} else if (position.x > X_LOW_TRESHHOLD
					&& position.x < X_HIGH_TRESHHOLD
					&& position.y > Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 6");
				component.rotateTo(0);
				component.scaleComponentToStackSize();
			} else if (position.x < X_LOW_TRESHHOLD
					&& position.y > Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 7");
				component.scaleComponentToStackSize();
			} else if (position.x < X_LOW_TRESHHOLD
					&& position.y > Y_LOW_TRESHHOLD
					&& position.y < Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 8");
				component.scaleComponentToStackSize();
			} else if (component.autoScaleIsOn()) {
				component.autoScale();
			}
		}
		
	}

}
