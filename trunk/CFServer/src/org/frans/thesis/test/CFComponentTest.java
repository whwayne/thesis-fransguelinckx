package org.frans.thesis.test;

import org.frans.thesis.GUI.CFScene;
import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.test.AbstractWindowTestcase;
import org.mt4j.test.testUtil.TestRunnable;

public class CFComponentTest extends AbstractWindowTestcase {

	private CFComponentNotAbstract parent;
	private MTApplication app;
	private CFScene scene;
	
	public void testComponentAddRemove(){
		runTest(new TestRunnable() {
			@Override
			public void runMTTestCode() {
				assertEquals(parent.getCFScene(), scene);
				assertEquals(null, parent.getComponentMenu());
				assertEquals(100.0, parent.getHeight());
				assertEquals(100.0, parent.getWidth());
			}
		});
	}

	@Override
	public void inStartUp(MTApplication app) {
		this.app = app;
		//Add a scene to the mt application
		this.scene = new CFSceneNotAbstract(app, "Dummy Scene");
		app.addScene(scene);
		
		//Set up components
		parent = new CFComponentNotAbstract(scene);
		getCanvas().addChild(parent);
	}
	
	public MTCanvas getCanvas(){
		return this.scene.getCanvas();
	}

}
