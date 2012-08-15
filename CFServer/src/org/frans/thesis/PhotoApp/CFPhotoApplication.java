package org.frans.thesis.PhotoApp;

import org.frans.thesis.service.CFTabletopServiceListener;
import org.frans.thesis.test.CFApplication;

public class CFPhotoApplication extends CFApplication {

	private static final long serialVersionUID = 1093780567228664551L;

	public static void main(String[] args) {
		initialize();
		service.connect();
	}

	@Override
	public void setupScene() {
		scene = new CFPhotoScene(this, "CFPhotoScene");
		scene.addComponentModifier(new CFAutoRotator());
		scene.addComponentModifier(new CFAutoScaler());
		service.addTabletopServiceListener((CFTabletopServiceListener) scene);
	}

}
