package org.frans.thesis.test;

import org.frans.thesis.GUI.CFAutoRotator;
import org.frans.thesis.GUI.CFAutoScaler;
import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFTabletopService;
import org.mt4j.MTApplication;

public class TestProgram extends MTApplication{                              

	private static CFScene scene;
	private static final long serialVersionUID = 7630065981396687470L;
	private static CFTabletopService service;
	
	public static void main(String[] args) {
		initialize();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		service = new CFTabletopService();
		service.addTabletopServiceListener(scene);
		service.connect();
	}
	
	@Override
	public void startUp() {
		scene = new CFScene(this, "CFScene");
		scene.addComponentModifier(new CFAutoRotator());
		scene.addComponentModifier(new CFAutoScaler());
		this.addScene(scene);
	}
}
