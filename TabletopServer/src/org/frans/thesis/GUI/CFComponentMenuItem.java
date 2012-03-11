package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class CFComponentMenuItem extends CFComponent {

	private static String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator;
	private MTRectangle image;

	private CFComponentMenuItemListener listener;

	public CFComponentMenuItem(String fileName,
			CFComponentMenuItemListener listener, MTApplication mtApplication) {
		this.listener = listener;
		PImage pImage = mtApplication.loadImage(imagePath + fileName);
		image = new MTRectangle(mtApplication, pImage);
		image.setWidthLocal(75);
		image.setHeightLocal(75);
//		image.setNoStroke(true);
		image.setVisible(false);

		this.getMTComponent().unregisterAllInputProcessors();
		this.getMTComponent().removeAllGestureEventListeners();

		this.getMTComponent().registerInputProcessor(
				new TapProcessor(mtApplication));
		this.getMTComponent().addGestureListener(TapProcessor.class,
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

	private MTRectangle getImage() {
		return image;
	}

	@Override
	protected MTRectangle getMTComponent() {
		return this.image;
	}

	@Override
	protected boolean isStackable() {
		return false;
	}

	protected void setPosition(Vector3D position) {
		this.getMTComponent().setPositionGlobal(position);
	}

	public void setVisible(boolean visible) {
		this.getImage().setVisible(visible);
	}
}
