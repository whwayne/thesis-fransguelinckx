package org.frans.thesis.start;

import org.apache.log4j.Logger;
import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFTabletopService;
import org.mt4j.MTApplication;

public abstract class CFApplication extends MTApplication {

	private static final long serialVersionUID = 7630065981396687470L;
	protected static CFTabletopService service;
	protected CFScene scene;
	
	public void setScene(CFScene scene) {
		this.scene = scene;
	}

	public static Logger logger = Logger.getLogger(CFApplication.class);

	protected abstract void setupScene();

	@Override
	public synchronized void startUp() {
		setupScene();
		this.addScene(scene);
	}
}
