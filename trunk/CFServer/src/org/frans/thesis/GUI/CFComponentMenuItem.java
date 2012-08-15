package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;

import processing.core.PImage;

/**
 * This class represents a menu item that belongs to a CFComponentMenu.
 */
public class CFComponentMenuItem extends CFComponent {

	/**
	 * The path to the folder containing the images of menu items.
	 * /org/frans/thesis/GUI/data
	 */
	private static String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator;

	/**
	 * The listener that should be called when a user taps a menu item.
	 */
	private CFComponentMenuItemListener listener;

	/**
	 * The public constructor for a menu item of 75px by 75px. By default a menu
	 * item is invisible.
	 * 
	 * @param fileName
	 *            The filename of the image that should be shown as menu item.
	 * @param listener
	 *            The listener that should be noticed when a user taps the item.
	 * @param scene
	 *            The scene to which this item belongs.
	 */
	public CFComponentMenuItem(String fileName,
			CFComponentMenuItemListener listener,
			CFScene scene) {
		super(scene);
		this.listener = listener;
		PImage pImage = scene.getMTApplication().loadImage(imagePath + fileName);
		this.setTexture(pImage);
		this.setWidthXYGlobal(75);
		this.setHeightXYGlobal(75);
		this.setVisible(false);
		this.setNoStroke(true);
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		this.registerInputProcessor(new TapProcessor(scene.getMTApplication()));
		this.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							CFComponentMenuItem.this.listener.processEvent();
						}
						return false;
					}
				});
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
