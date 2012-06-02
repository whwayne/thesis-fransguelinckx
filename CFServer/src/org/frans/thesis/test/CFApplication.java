package org.frans.thesis.test;

import org.frans.thesis.PhotoApp.CFAutoRotator;
import org.frans.thesis.PhotoApp.CFAutoScaler;
import org.frans.thesis.PhotoApp.CFPhotoScene;
import org.frans.thesis.service.CFTabletopService;
import org.mt4j.MTApplication;

public class CFApplication extends MTApplication{                              

	private static CFPhotoScene scene;
	private static final long serialVersionUID = 7630065981396687470L;
	private static CFTabletopService service;
	
	@Override
	public void startUp() {
		setupScene();
		this.addScene(scene);
	}
	
	public void setupScene(){
		scene = new CFPhotoScene(this, "CFPhotoScene");
		scene.addComponentModifier(new CFAutoRotator());
		scene.addComponentModifier(new CFAutoScaler());
	}
	
	public static void main(String[] args) {
		service = new CFTabletopService();
		addTabletopServiceListeners();
		initialize();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		service.connect();
	}
	
	public static void addTabletopServiceListeners(){
		service.addTabletopServiceListener(scene);
	}
}
