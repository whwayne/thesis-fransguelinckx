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
		this.setTexture(pImage);
		this.setWidthLocal(75);
		this.setHeightLocal(75);
		this.setVisible(false);
		this.setNoStroke(true);

		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		this.registerInputProcessor(
				new TapProcessor(mtApplication));
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

	protected void setPosition(Vector3D position) {
		this.setPositionGlobal(position);
	}

	public void setVisible(boolean visible) {
		this.setVisible(visible);
	}

	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleScaledCFComponent(CFComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRotatedCFComponent(CFComponent component) {
		// TODO Auto-generated method stub
		
	}
}
