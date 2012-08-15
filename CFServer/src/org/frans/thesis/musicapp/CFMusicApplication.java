package org.frans.thesis.musicapp;

import org.frans.thesis.service.CFTabletopServiceListener;
import org.frans.thesis.test.CFApplication;

public class CFMusicApplication extends CFApplication {

	private static final long serialVersionUID = 1093780567228664551L;

	public static void main(String[] args) {
	    logger.info("Initializing GUI");
		initialize();
		logger.info("Initializing GUI: OK");
		logger.info("Publishing tabletop service in wireless network");
		service.connect();
		logger.info("Publishing tabletop service in wireless network: OK");
	}

	@Override
	public void setupScene() {
		scene = new CFMusicScene(this, "CFPhotoScene");
		service.addTabletopServiceListener((CFTabletopServiceListener) scene);
	}

}
