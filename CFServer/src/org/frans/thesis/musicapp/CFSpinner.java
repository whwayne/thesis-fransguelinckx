package org.frans.thesis.musicapp;

import gifAnimation.Gif;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFScene;
import org.mt4j.MTApplication;
import org.mt4j.util.math.Vector3D;

/**
 * A class to show a simple spinner animation on the tabletop GUI.
 */
public class CFSpinner extends CFComponent {

	/**
	 * The object responsible for animating the spinner.
	 */
	private Gif animation;
	
	/**
	 * The path to the gif-file.
	 */
	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "spinner.gif";
	
	/**
	 * The parent component to which this spinner belongs.
	 */
	private CFComponent parent;

	/**
	 * Public constructor for this class, which sets up the visual
	 * representation.
	 * 
	 * @param scene
	 *            The scene to which this component belongs.
	 * @param parent
	 *            The component to which this spinner belongs.
	 */
	public CFSpinner(CFScene scene,
			CFComponent parent) {
		super(scene);
		this.setHeightLocal(50);
		this.setWidthLocal(50);
		this.setNoStroke(true);
		this.parent = parent;
		animation = new Gif(scene.getMTApplication(), imagePath);
		this.setTexture(animation);
		this.setVisible(false);
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

	/**
	 * Starts the spinner animation and makes it visible.
	 */
	public void start() {
		animation.play();
		this.setVisible(true);
	}

	/**
	 * Stops the spinner animation and makes is invisible.
	 */
	public void stop() {
		animation.stop();
		this.setVisible(false);
	}

}
