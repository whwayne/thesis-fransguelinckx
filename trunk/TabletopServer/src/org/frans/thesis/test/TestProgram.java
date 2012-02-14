package org.frans.thesis.test;

import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFTabletopService;
import org.mt4j.MTApplication;

public class TestProgram extends MTApplication {

	private static final long serialVersionUID = -7674275591986637141L;
	private CFTabletopService tabletopService;

	public static void main(String[] args) {
		new TestProgram();
	}
	
	public TestProgram(){
		this.tabletopService = new CFTabletopService();
		this.tabletopService.connect();
		initialize();
	}

	@Override
	public void startUp() {
		CFScene scene = new CFScene(this, "CFScene");
		this.addScene(scene);
	}

}
