package org.frans.thesis.PhotoApp;

import gifAnimation.Gif;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFScene;
import org.mt4j.MTApplication;
import org.mt4j.util.math.Vector3D;

public class CFSpinner extends CFComponent {
	
	private Gif animation;
	private CFComponent parent;
	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "spinner.gif";

	public CFSpinner(MTApplication mtApplication, CFScene scene, CFComponent parent) {
		super(mtApplication, scene);
		this.component.setHeightLocal(50);
		this.component.setWidthLocal(50);
		this.component.setNoStroke(true);
		this.parent = parent;
		animation = new Gif(mtApplication, imagePath);
//		this.component.setPositionGlobal(this.parent.getPosition());
		this.component.setTexture(animation);
		this.component.setVisible(false);
//		scene.addCFComponent(this);
		this.parent.getMTComponent().addChild(this.getMTComponent());
		this.getMTComponent().setPositionRelativeToParent(new Vector3D(this.parent.getWidth()-25, this.parent.getHeight()-25));
	}

	public void start() {
		animation.play();
		this.component.setVisible(true);
	}
	
	public void stop(){
		animation.stop();
		this.component.setVisible(false);
	}

	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		// TODO Auto-generated method stub
		
	}

}
