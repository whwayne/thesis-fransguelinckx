package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFFile;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class CFImage extends CFComponent {

	private MTColor color;
	private CFFile image;

	public CFImage(MTApplication mtApplication, CFFile image, CFScene scene,
			MTColor color) {
		super(mtApplication, scene);
		this.color = color;
		this.image = image;
		PImage pImage = getMTApplication().loadImage(image.getFile().getPath());
		this.component.setTexture(pImage);
		this.scaleImageToStackSize();

		setUpGestures(mtApplication);

		this.getMTComponent().setStrokeColor(this.getColor());
		this.getMTComponent().setStrokeWeight(5);
		this.getCFScene().addCFComponent(getCFImage());
		autoScale();
	}

	protected CFFile getFile() {
		return this.image;
	}

	private void setUpGestures(MTApplication mtApplication) {
		this.getMTComponent().registerInputProcessor(new TapAndHoldProcessor(mtApplication, 1500));
		this.getMTComponent().addGestureListener(TapAndHoldProcessor.class,new TapAndHoldVisualizer(mtApplication, this.getCFScene().getCanvas()));
		this.getMTComponent().addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent th = (TapAndHoldEvent) ge;
						switch (th.getId()) {
						case TapAndHoldEvent.GESTURE_ENDED:
							if (th.isHoldComplete()) {
								createNewPhotoalbum();
							}
							break;
						default:
							break;
						}
						return false;
					}
				});
	}

	private void createNewPhotoalbum() {
		CFPhotoAlbum album = new CFPhotoAlbum(getMTApplication(), getCFImage(),(CFPhotoScene) getCFScene());
		album.addImage(getCFImage());

	}

	private MTColor getColor() {
		return this.color;
	}

	private CFImage getCFImage() {
		return this;
	}

	protected MTRectangle getImage() {
		return this.component;
	}

	@Override
	public MTRectangle getMTComponent() {
		return this.component;
	}

	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		if(component instanceof CFImage){
			this.scaleImageToStackSize();
			CFImage image = (CFImage) component;
			image.scaleImageToStackSize();
			Vector3D position = this.getPosition();
			image.reposition(position);	
		}
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
