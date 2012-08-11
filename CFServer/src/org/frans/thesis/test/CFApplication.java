package org.frans.thesis.test;

import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFTabletopService;
import org.frans.thesis.service.CFTabletopServiceListener;
import org.mt4j.MTApplication;

public abstract class CFApplication extends MTApplication {

	protected CFScene scene;
	private static final long serialVersionUID = 7630065981396687470L;
	protected static CFTabletopService service = new CFTabletopService();

	@Override
	public synchronized void startUp() {
			setupScene();
			this.addScene(scene);
//		service.addTabletopServiceListener((CFTabletopServiceListener) scene);
	}

	protected abstract void setupScene();
}
