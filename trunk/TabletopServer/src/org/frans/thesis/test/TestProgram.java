package org.frans.thesis.test;

import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFTabletopService;
import org.mt4j.MTApplication;

public class TestProgram extends MTApplication {                              
    private static final long serialVersionUID = 5219108306171490664L;

	public static void main(String[] args) {
		initialize();
	}

	@Override
	public void startUp() {
		CFTabletopService service = new CFTabletopService();
		CFScene scene = new CFScene(this, "CFScene");
		this.addScene(scene);
		service.addTabletopServiceListener(scene);
	}

}
