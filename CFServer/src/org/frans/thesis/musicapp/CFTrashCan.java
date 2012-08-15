package org.frans.thesis.musicapp;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFScene;
import org.mt4j.MTApplication;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;

import processing.core.PImage;

/**
 * A class that represents a trashcan.
 */
public class CFTrashCan extends CFComponent {

	/**
	 * The path the the image of a trashcan.
	 */
	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "trashcan.png";

	/**
	 * Public constructor for this class. Sets up the visual component and the
	 * gestures to which this component has to react.
	 * 
	 * @param scene
	 *            The scene to which this component belongs.
	 */
	public CFTrashCan(CFScene scene) {
		super(scene);
		PImage pImage = scene.getMTApplication().loadImage(imagePath);
		this.setTexture(pImage);
		this.scaleComponentToStackSize();

		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		this.registerInputProcessor(new DragProcessor(this.getCFScene()
				.getMTApplication()));
		this.addGestureListener(DragProcessor.class, new DefaultDragAction());
		this.registerInputProcessor(new RotateProcessor(this.getCFScene()
				.getMTApplication()));
		this.addGestureListener(RotateProcessor.class,
				new DefaultRotateAction());

		this.setNoStroke(true);
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
}
