package org.frans.thesis.test;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFComponentMenu;
import org.frans.thesis.GUI.CFScene;
import org.mt4j.util.math.Vector3D;

public class CFComponentNotAbstract extends CFComponent {

	public CFComponentNotAbstract(CFScene scene) {
		super(scene);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRotatedCFComponent(CFComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleScaledCFComponent(CFComponent component) {
		// TODO Auto-generated method stub
		
	}
	
	protected CFScene getCFScene() {
		return super.getCFScene();
	}
	
	protected CFComponentMenu getComponentMenu() {
		return super.getComponentMenu();
	}
	
	protected void reposition(Vector3D position) {
		super.reposition(position);
	}

}
