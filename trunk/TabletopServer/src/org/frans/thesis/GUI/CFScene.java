package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;

public class CFScene extends AbstractScene{

	public CFScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));

		this.addChild(new CFImage(getMTApplication(), "foto1.jpg"));
		this.addChild(new CFImage(getMTApplication(), "foto2.jpg"));
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}
	
	protected void addChild(CFImage image){
		this.getCanvas().addChild(image.getImage());
	}

}
