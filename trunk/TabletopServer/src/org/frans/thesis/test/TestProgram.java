package org.frans.thesis.test;

import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFTabletopService;
import org.mt4j.MTApplication;

public class TestProgram extends MTApplication{                              

	private static CFScene scene;
	private static CFTabletopService service;
	
	public static void main(String[] args) {
		initialize();
		service = new CFTabletopService();
		service.addTabletopServiceListener(scene);
	}
	
	@Override
	public void startUp() {
		scene = new CFScene(this, "CFScene");
		this.addScene(scene);
	}
	
	public CFTabletopService getService(){
		return TestProgram.service;
	}
}
