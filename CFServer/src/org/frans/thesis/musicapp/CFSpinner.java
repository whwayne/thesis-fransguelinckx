package org.frans.thesis.musicapp;

import gifAnimation.Gif;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFScene;
import org.mt4j.MTApplication;
import org.mt4j.util.math.Vector3D;

public class CFSpinner extends CFComponent {

	private Gif animation;
	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "spinner.gif";
	private CFComponent parent;

	public CFSpinner(MTApplication mtApplication, CFScene scene,
			CFComponent parent) {
		super(mtApplication, scene);
		this.setHeightLocal(50);
		this.setWidthLocal(50);
		this.setNoStroke(true);
		this.parent = parent;
		animation = new Gif(mtApplication, imagePath);
		// this.component.setPositionGlobal(this.parent.getPosition());
		this.setTexture(animation);
		this.setVisible(false);
		// scene.addCFComponent(this);
		this.parent.addChild(this);
		this.setPositionRelativeToParent(new Vector3D(
				this.parent.getWidth() - 25, this.parent.getHeight() - 25));
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

	public void start() {
		animation.play();
		this.setVisible(true);
	}

	public void stop() {
		animation.stop();
		this.setVisible(false);
	}

}
