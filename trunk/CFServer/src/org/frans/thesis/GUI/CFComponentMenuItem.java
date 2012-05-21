package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
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
	private CFComponentMenuItemListener listener;
	
	public CFComponentMenuItem(String fileName,
			CFComponentMenuItemListener listener, MTApplication mtApplication, CFScene scene) {
		super(mtApplication, scene);
		this.listener = listener;
		PImage pImage = mtApplication.loadImage(imagePath + fileName);
//		this.component = new MTRectangle(mtApplication, pImage);
		this.component.setTexture(pImage);
		this.component.setWidthLocal(75);
		this.component.setHeightLocal(75);
		this.component.setVisible(false);
		this.component.setNoStroke(true);

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

	@Override
	protected boolean isStackable() {
		return false;
	}

	protected void setPosition(Vector3D position) {
		this.getMTComponent().setPositionGlobal(position);
	}

	public void setVisible(boolean visible) {
		this.getMTComponent().setVisible(visible);
	}
}
