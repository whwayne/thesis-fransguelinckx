package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanEvent;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanTwoFingerEvent;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.util.math.Vector3D;

public class CFPhotoAlbum {

	private ArrayList<CFImage> images;
	private MTRectangle rectangle;
	private final int DIMENSION_X = 500;
	private final int DIMENSION_Y = 350;
	private int pageNumber = 0;
	private final Vector3D leftImagePosition = new Vector3D(DIMENSION_X / 4,
			DIMENSION_Y / 2);
	private final Vector3D rightImagePosition = new Vector3D(
			3 * DIMENSION_X / 4, DIMENSION_Y / 2);

	public CFPhotoAlbum(MTApplication application, CFImage initialImage,
			CFScene scene) {
		this.rectangle = new MTRectangle(application, DIMENSION_X, DIMENSION_Y);
		this.rectangle.setNoFill(true);
		this.images = new ArrayList<CFImage>();
		this.addImage(initialImage);
		this.loadImages();
		scene.getCanvas().addChild(rectangle);
		setUpGestures(application);
	}

	private MTRectangle getRectangle() {
		return this.rectangle;
	}

	protected void addImage(CFImage image) {
		// TODO
		if (!this.getImages().contains(image)) {
			this.getImages().add(image);
			// image.getImage().removeFromParent();
			image.getImage().setPickable(false);
		}
	}

	protected void removeImage(CFImage image) {
		// TODO
		if (this.getImages().contains(image)) {
			this.getImages().remove(image);
			image.getImage().removeFromParent();
			this.getRectangle().getParent().addChild(image.getImage());
			image.getImage().setPickable(true);
		}
	}

	private ArrayList<CFImage> getImages() {
		return this.images;
	}

	protected void pageUp() {
		unloadImages();
		pageNumber++;
		loadImages();
	}

	private void loadImages() {
		CFImage leftImage = null;
		CFImage rightImage = null;
		if (this.getImages().size() - 1 >= this.getPageNumber()) {
			leftImage = this.getImages().get(this.getPageNumber());
		}
		if (this.getImages().size() - 1 >= this.getPageNumber() + 1) {
			rightImage = this.getImages().get(this.getPageNumber() + 1);
		}
		if (leftImage != null) {
			this.getRectangle().addChild(leftImage.getImage());
			leftImage.getImage().setPositionRelativeToParent(leftImagePosition);
			this.resizeImage(leftImage);
		}
		if (rightImage != null) {
			this.getRectangle().addChild(rightImage.getImage());
			rightImage.getImage().setPositionRelativeToParent(
					rightImagePosition);
			this.resizeImage(rightImage);
		}
	}

	private void resizeImage(CFImage image) {
		float scaleFactorX = (this.DIMENSION_X / 2)
				/ image.getImage().getWidthXY(TransformSpace.GLOBAL);
		float scaleFactorY = this.DIMENSION_Y
				/ image.getImage().getHeightXY(TransformSpace.GLOBAL);
		float result = Math.min(scaleFactorX, scaleFactorY);
		image.scaleImage(result);
	}

	private void unloadImages() {
		CFImage leftImage = null;
		CFImage rightImage = null;
		if (this.getImages().size() - 1 >= this.getPageNumber()) {
			leftImage = this.getImages().get(this.getPageNumber());
		}
		if (this.getImages().size() - 1 >= this.getPageNumber() + 1) {
			rightImage = this.getImages().get(this.getPageNumber() + 1);
		}
		if (leftImage != null) {
			this.getRectangle().removeChild(leftImage.getImage());
		}
		if (rightImage != null) {
			this.getRectangle().removeChild(leftImage.getImage());
		}
	}

	protected void pagedown() {
		unloadImages();
		pageNumber--;
		loadImages();
		// TODO
	}

	protected int getPageNumber() {
		return this.pageNumber;
	}

	private void setUpGestures(MTApplication mtApplication) {
		this.getRectangle().unregisterAllInputProcessors();
		this.getRectangle().removeAllGestureEventListeners();

		this.getRectangle().registerInputProcessor(
				new DragProcessor(mtApplication));
		this.getRectangle().addGestureListener(DragProcessor.class,
				new DefaultDragAction());

		this.getRectangle().registerInputProcessor(
				new RotateProcessor(mtApplication));
		this.getRectangle().addGestureListener(RotateProcessor.class,
				new DefaultRotateAction());
		
		this.getRectangle().registerInputProcessor(new PanProcessorTwoFingers(mtApplication));
		this.getRectangle().addGestureListener(PanProcessorTwoFingers.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				PanTwoFingerEvent e = (PanTwoFingerEvent)ge;
				System.out.println("pan");
			return false;
			}
		});
	}
}
