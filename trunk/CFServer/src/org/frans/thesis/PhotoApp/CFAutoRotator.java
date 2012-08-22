package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponentModifiable;
import org.frans.thesis.GUI.CFComponentModifier;
import org.mt4j.util.math.Vector3D;

/**
 * A class that extends CFComponentModifier, so it is notified autmoatically when a interactive component on the tabletop has been moved by a user.
 */
public class CFAutoRotator extends CFComponentModifier {

	/**
	 * The only method in this class is inherited from CFComponentModifier.
	 * When an interactive component crosses certain boundaries, it is automatically rotated towards the edge of the tabletop.
	 */
	@Override
	public void handleMovedCFComponent(CFComponentModifiable rotatable) {
		CFAutoRotatable component = (CFAutoRotatable) rotatable;

		Vector3D position = component.getPosition();

		if (component.autoRotateIsOn()) {
			if (position.x < X_LOW_TRESHHOLD && position.y < Y_LOW_TRESHHOLD) {
				// System.out.println("zone 1");
				component.rotateTo(135);
			} else if (position.x > X_LOW_TRESHHOLD
					& position.x < X_HIGH_TRESHHOLD
					&& position.y < Y_LOW_TRESHHOLD) {
				// System.out.println("zone 2");
				component.rotateTo(180);
			} else if (position.x > X_HIGH_TRESHHOLD
					&& position.y < Y_LOW_TRESHHOLD) {
				// System.out.println("zone 3");
				component.rotateTo(225);
			} else if (position.x > X_HIGH_TRESHHOLD
					&& position.y > Y_LOW_TRESHHOLD
					&& position.y < Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 4");
				component.rotateTo(270);
			} else if (position.x > X_HIGH_TRESHHOLD
					&& position.y > Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 5");
				component.rotateTo(315);
			} else if (position.x > X_LOW_TRESHHOLD
					&& position.x < X_HIGH_TRESHHOLD
					&& position.y > Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 6");
				component.rotateTo(0);
			} else if (position.x < X_LOW_TRESHHOLD
					&& position.y > Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 7");
				component.rotateTo(45);
			} else if (position.x < X_LOW_TRESHHOLD
					&& position.y > Y_LOW_TRESHHOLD
					&& position.y < Y_HIGH_TRESHHOLD) {
				// System.out.println("zone 8");
				component.rotateTo(90);
			}
		}
	}

}