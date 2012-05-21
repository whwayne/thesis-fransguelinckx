package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.math.Vector3D;

public class CFPhotoAlbum extends CFComponent implements IGestureEventListener {

	private ArrayList<CFImage> images;
	private MTApplication mtApplication;
	private CFComponentMenu menu;
	private float DIMENSION_X = 500;
	private float DIMENSION_Y = 350;
	private int pageNumber = 0;
	private Vector3D leftImagePosition;
	private Vector3D rightImagePosition;

	public CFPhotoAlbum(MTApplication application, CFImage initialImage,
			CFScene scene) {
		super(application, scene);
		this.getCFScene().addPhotoalbum(this);
		this.mtApplication = application;
		// this.component = new MTRectangle(application, DIMENSION_X,
		// DIMENSION_Y);
		this.component.setHeightLocal(DIMENSION_Y);
		this.component.setWidthLocal(DIMENSION_X);
		this.component.setNoFill(true);
		this.images = new ArrayList<CFImage>();
		scene.addCFComponent(this);
		setUpGestures(application);
		this.createMenu();
		leftImagePosition = new Vector3D(DIMENSION_X / 4, DIMENSION_Y / 2);
		rightImagePosition = new Vector3D((3 * DIMENSION_X) / 4,
				DIMENSION_Y / 2);
//		this.addImage(initialImage);
	}

	protected void addImage(CFImage image) {
		if (!this.getImages().contains(image)) {
			this.getImages().add(image);
			this.getMTComponent().addChild(image.getMTComponent());
			image.getImage().setVisible(false);
			image.rotateTo(0);
			image.getImage().setPickable(false);
		}
	}

	protected void removeImage(CFImage image) {
		if (this.getImages().contains(image)) {
			this.getImages().remove(image);
			image.getImage().removeFromParent();
			this.getMTComponent().getParent().addChild(image.getImage());
			image.getImage().setPickable(true);
			loadImages();
		}
	}

	private ArrayList<CFImage> getImages() {
		return this.images;
	}

	protected void pageUp() {
		if (this.getImages().size() >= ((this.getPageNumber() + 1) * 2) + 1) {
			unloadImages();
			pageNumber++;
			loadImages();
		}
	}

	protected void loadImages() {
		CFImage leftImage = null;
		CFImage rightImage = null;
		if (this.getImages().size() - 1 >= this.getPageNumber() * 2) {
			leftImage = this.getImages().get(this.getPageNumber() * 2);
		}
		if (this.getImages().size() - 1 >= (this.getPageNumber() * 2) + 1) {
			rightImage = this.getImages().get((this.getPageNumber() * 2) + 1);
		}
		if (leftImage != null) {
//			this.getMTComponent().addChild(leftImage.getImage());
			leftImage.getImage().setVisible(true);
			this.resizeImage(leftImage);
			// leftImage.rotateTo(this.getAngle());
			leftImage.getImage().setPositionRelativeToParent(leftImagePosition);
		}
		if (rightImage != null) {
//			this.getMTComponent().addChild(rightImage.getImage());
			rightImage.getImage().setVisible(true);
			this.resizeImage(rightImage);
			// rightImage.rotateTo(this.getAngle());
			rightImage.getImage().setPositionRelativeToParent(
					rightImagePosition);
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

	protected void unloadImages() {
		CFImage leftImage = null;
		CFImage rightImage = null;
		if (this.getImages().size() - 1 >= this.getPageNumber() * 2) {
			leftImage = this.getImages().get(this.getPageNumber() * 2);
		}
		if (this.getImages().size() - 1 >= (this.getPageNumber() * 2) + 1) {
			rightImage = this.getImages().get((this.getPageNumber() * 2) + 1);
		}
		if (leftImage != null) {
//			this.getMTComponent().removeChild(leftImage.getImage());
			leftImage.getImage().setVisible(false);
		}
		if (rightImage != null) {
//			this.getMTComponent().removeChild(rightImage.getImage());
			rightImage.getImage().setVisible(false);
		}
	}

	protected void pageDown() {
		if (pageNumber > 0) {
			unloadImages();
			pageNumber--;
			loadImages();
		}
	}

	protected int getPageNumber() {
		return this.pageNumber;
	}

	private void setUpGestures(MTApplication mtApplication) {
		// this.getMTComponent().unregisterAllInputProcessors();
		// this.getMTComponent().removeAllGestureEventListeners();

		// this.getMTComponent().registerInputProcessor(
		// new DragProcessor(mtApplication));
		this.getMTComponent().addGestureListener(DragProcessor.class, this);

//		this.getMTComponent().registerInputProcessor(
//				new RotateProcessor(mtApplication));
//		this.getMTComponent().addGestureListener(RotateProcessor.class,
//				new DefaultRotateAction());
	}

	@Override
	protected boolean isStackable() {
		return false;
	}

	private void createMenu() {
		this.menu = new CFComponentMenu(this, mtApplication);
		this.menu.addMenuItem("left_arrow.png",
				new CFComponentMenuItemListener() {

					@Override
					public void processEvent() {
						pageDown();
					}
				});
		this.menu.addMenuItem("right_arrow.png",
				new CFComponentMenuItemListener() {

					@Override
					public void processEvent() {
						pageUp();
					}
				});
		this.menu.positionMenuItemsLeftAndRight();
		this.menu.setVisible(true);
	}

	protected boolean isPhotoAlbum() {
		return true;
	}

	@Override
	public boolean processGestureEvent(MTGestureEvent ge) {
		DragEvent de = (DragEvent) ge;
//		de.getTarget().translateGlobal(de.getTranslationVect());
		switch (de.getId()) {
		case MTGestureEvent.GESTURE_ENDED:
			if (this.getCFScene().isCloseToCFComponent(this)) {
				for (CFComponent component : this.getCFScene()
						.getNearCFComponents(this)) {
					if (component.isMobileProxy()) {
						CFMobileDeviceProxy proxy = (CFMobileDeviceProxy) component;
						for (CFImage image : this.getImages()) {
							proxy.publishImageOnFacebook(image.getFile());
						}
						this.getMTComponent().removeFromParent();
					}
				}
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	protected void scale(float x, float y, float z, Vector3D scalingPoint){
		super.scale(x, y, z, scalingPoint);
		this.DIMENSION_X *= x;
		this.DIMENSION_Y *= y;
	}

}
