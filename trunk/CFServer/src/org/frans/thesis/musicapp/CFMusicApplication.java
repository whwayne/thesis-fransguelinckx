package org.frans.thesis.musicapp;

import org.frans.thesis.service.CFTabletopServiceListener;
import org.frans.thesis.test.CFApplication;

public class CFMusicApplication extends CFApplication {

	private static final long serialVersionUID = 1093780567228664551L;

	public static void main(String[] args) {
		initialize();
		service.connect();
	}

	@Override
	public void setupScene() {
		scene = new CFMusicScene(this, "CFPhotoScene");
		service.addTabletopServiceListener((CFTabletopServiceListener) scene);
	}

}
