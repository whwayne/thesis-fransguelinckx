package org.frans.thesis.PhotoApp;

import org.frans.thesis.service.CFTabletopServiceListener;
import org.frans.thesis.test.CFApplication;

/**
 * The main class to start the application.
 */
public class CFPhotoApplication extends CFApplication {

	private static final long serialVersionUID = 1093780567228664551L;

	/**
	 * Usually two methods are called in the main method:
	 * initialize(): Sets up the GUI.
	 * service.connect(): Published the tabletop service in the local wireless network.
	 */
	public static void main(String[] args) {
		initialize();
		service.connect();
	}

	/**
	 * Contains initial scene setup.
	 */
	@Override
	public void setupScene() {
		scene = new CFPhotoScene(this, "CFPhotoScene");
		scene.addComponentModifier(new CFAutoRotator());
		scene.addComponentModifier(new CFAutoScaler());
		service.addTabletopServiceListener((CFTabletopServiceListener) scene);
	}

}
