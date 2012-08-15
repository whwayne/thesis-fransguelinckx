package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFFile;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

/**
 * CFImage represents an image on the tabletop that is usually transferred from a mobile device. 
 */
public class CFImage extends CFComponent implements CFAutoRotatable,
		CFAutoScalable {

	/**
	 * The autorotation functionality is on by default.
	 */
	private boolean autoRotate = true;
	
	/**
	 * The autoscale functionality is on by default.
	 */
	private boolean autoScale = true;
	
	/**
	 * The color of the edge around an image.
	 */
	private MTColor edgeColor;
	
	/**
	 * The image file.
	 */
	private CFFile image;

	/**
	 * Public constructor for CFImage. Sets the image file, scene and color. Also sets up the visual representation and extra gestures. By default the image is scaled to a large size.
	 * @param image
	 * The file containing the actual image.
	 * @param scene
	 * The scene to which this image belongs.
	 * @param color
	 * The color of the edge around the image.
	 */
	public CFImage(CFFile image, CFScene scene, MTColor color) {
		super(scene);
		this.edgeColor = color;
		this.image = image;
		PImage pImage = this.getCFScene().getMTApplication()
				.loadImage(image.getFile().getPath());
		this.setTexture(pImage);
		this.scaleComponentToStackSize();
		setUpGestures();
		this.setStrokeColor(this.getEdgeColor());
		this.setStrokeWeight(5);
		this.getCFScene().addCFComponent(this);
		scaleComponentToLargeSize();
	}

	@Override
	public boolean autoRotateIsOn() {
		return this.autoRotate;
	}

	@Override
	public boolean autoScaleIsOn() {
		return this.autoScale;
	}

	/**
	 * Creates a new photo album and adds this image to the album.
	 */
	private void createNewPhotoalbum() {
		CFPhotoAlbum album = new CFPhotoAlbum((CFPhotoScene) getCFScene());
		album.addImage(this);

	}

	/**
	 * Returns the color of the edge around the image.
	 */
	private MTColor getEdgeColor() {
		return this.edgeColor;
	}
	
	/**
	 * Returns the file containing the actual image.
	 */
	protected CFFile getFile() {
		return this.image;
	}

	/**
	 * If a user drops an image on top of another, they are both scale to stack size and repositioned to the same location, thus creating a stack.
	 */
	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		if (component instanceof CFImage) {
			this.scaleComponentToStackSize();
			CFImage image = (CFImage) component;
			image.scaleComponentToStackSize();
			Vector3D position = this.getPosition();
			image.reposition(position);
		}
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
	 * Sets up extra gestures for CFImage:
	 * When a user taps and holds an image, it creates a photo album.
	 * When a user scales an image, it turns off the autoscale functionality
	 * When a user rotates an image, it turns off the autorotation funtionality.
	 * When a user double taps an image, it turns the autorotation and autoscale funationalities back on.
	 */
	private void setUpGestures() {
		this.registerInputProcessor(new TapAndHoldProcessor(this.getCFScene().getMTApplication(), 1500));
		this.addGestureListener(TapAndHoldProcessor.class,
				new TapAndHoldVisualizer(this.getCFScene().getMTApplication(), this.getCFScene()
						.getCanvas()));
		this.addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent th = (TapAndHoldEvent) ge;
						switch (th.getId()) {
						case MTGestureEvent.GESTURE_ENDED:
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
		this.addGestureListener(ScaleProcessor.class,
				new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						turnAutoScaleOff();
						return false;
					}
				});
		this.addGestureListener(RotateProcessor.class,
				new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						turnAutoRotateOff();
						return false;
					}
				});

		this.registerInputProcessor(new TapProcessor(getCFScene().getMTApplication(), 25, true,
				350));
		this.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isDoubleTap()) {
							turnAutoRotateOn();
							turnAutoScaleOn();
						}
						return false;
					}
				});
	}

	@Override
	public void turnAutoRotateOff() {
		this.autoRotate = false;
	}

	@Override
	public void turnAutoRotateOn() {
		this.autoRotate = true;
	}

	@Override
	public void turnAutoScaleOff() {
		this.autoScale = false;
	}

	@Override
	public void turnAutoScaleOn() {
		this.autoScale = true;
	}
}
