package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponentModifiable;
import org.frans.thesis.GUI.CFComponentModifier;
import org.mt4j.util.math.Vector3D;

/**
 * A class that extends CFComponentModifier, so it is notified autmoatically when a interactive component on the tabletop has been moved by a user.
 */
public class CFAutoScaler extends CFComponentModifier {

	/**
	 * The only method in this class is inherited from CFComponentModifier.
	 * When an interactive component crosses certain boundaries, it is automatically scaled to stack size if its near the edge of the tabletop and vice versa.
	 */
	@Override
	public void handleMovedCFComponent(CFComponentModifiable component) {
		// TODO Auto-generated method stub
		CFAutoScalable scalable = (CFAutoScalable) component;
		Vector3D position = scalable.getPosition();

		if (scalable.autoScaleIsOn()) {
			if (position.x < X_LOW_TRESHHOLD && position.y < Y_LOW_TRESHHOLD) {
				// System.out.println("zone 1");
				scalable.scaleComponentToStackSize();
			} else if (position.x > X_LOW_TRESHHOLD
					& position.x < X_HIGH_TRESHHOLD
					&& position.y < Y_LOW_TRESHHOLD) {
				// System.out.println("zone 2");
				scalable.scaleComponentToStackSize();
			} else if (position.x > X_HIGH_TRESHHOLD
					&& position.y < Y_LOW_TRESHHOLD) {
				// System.out.println("zone 3");
				scalable.scaleComponentToStackSize();
			} else if (position.x > X_HIGH_TRESHHOLD
					&& position.y > Y_LOW_TRESHHOLD
					&& position.y < Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 4");
				scalable.scaleComponentToStackSize();
			} else if (position.x > X_HIGH_TRESHHOLD
					&& position.y > Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 5");
				scalable.scaleComponentToStackSize();
			} else if (position.x > X_LOW_TRESHHOLD
					&& position.x < X_HIGH_TRESHHOLD
					&& position.y > Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 6");
				scalable.scaleComponentToStackSize();
			} else if (position.x < X_LOW_TRESHHOLD
					&& position.y > Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 7");
				scalable.scaleComponentToStackSize();
			} else if (position.x < X_LOW_TRESHHOLD
					&& position.y > Y_LOW_TRESHHOLD
					&& position.y < Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 8");
				scalable.scaleComponentToStackSize();
			} else if (scalable.autoScaleIsOn()) {
				scalable.scaleComponentToLargeSize();
			}
		}

	}

}
