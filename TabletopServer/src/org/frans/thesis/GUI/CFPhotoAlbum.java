package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.util.math.Vector3D;

public class CFPhotoAlbum extends CFComponent {

	private ArrayList<CFImage> images;
	private MTApplication mtApplication;
	private CFComponentMenu menu;
	private final float DIMENSION_X = 500;
	private final float DIMENSION_Y = 350;
	private int pageNumber = 0;
	private final Vector3D leftImagePosition;
	private final Vector3D rightImagePosition;

	public CFPhotoAlbum(MTApplication application, CFImage initialImage,
			CFScene scene) {
		super(application);
		this.mtApplication = application;
//		this.component = new MTRectangle(application, DIMENSION_X, DIMENSION_Y);
		this.component.setHeightLocal(DIMENSION_Y);
		this.component.setWidthLocal(DIMENSION_X);
		this.component.setNoFill(true);
		this.images = new ArrayList<CFImage>();
		scene.addCFComponent(this);
		setUpGestures(application);
		this.createMenu();
		leftImagePosition = new Vector3D(DIMENSION_X / 4, DIMENSION_Y / 2);
		rightImagePosition = new Vector3D((3 * DIMENSION_X) / 4, DIMENSION_Y / 2);
		this.addImage(initialImage);
	}

	protected void addImage(CFImage image) {
		if (!this.getImages().contains(image)) {
			this.getImages().add(image);
			image.getImage().removeFromParent();
			image.getImage().setPickable(false);
			loadImages();
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

	private void loadImages() {
		CFImage leftImage = null;
		CFImage rightImage = null;
		if (this.getImages().size() - 1 >= this.getPageNumber()*2) {
			leftImage = this.getImages().get(this.getPageNumber()*2);
		}
		if (this.getImages().size() - 1 >= (this.getPageNumber()*2) + 1) {
			rightImage = this.getImages().get((this.getPageNumber()*2) + 1);
		}
		if (leftImage != null) {
			this.getMTComponent().addChild(leftImage.getImage());
			this.resizeImage(leftImage);
			leftImage.getImage().setPositionRelativeToParent(leftImagePosition);
		}
		if (rightImage != null) {
			this.getMTComponent().addChild(rightImage.getImage());
			this.resizeImage(rightImage);
			rightImage.getImage().setPositionRelativeToParent(rightImagePosition);
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
		if (this.getImages().size() - 1 >= this.getPageNumber()*2) {
			leftImage = this.getImages().get(this.getPageNumber()*2);
		}
		if (this.getImages().size() - 1 >= (this.getPageNumber()*2) + 1) {
			rightImage = this.getImages().get((this.getPageNumber()*2) + 1);
		}
		if (leftImage != null) {
			this.getMTComponent().removeChild(leftImage.getImage());
		}
		if (rightImage != null) {
			this.getMTComponent().removeChild(rightImage.getImage());
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
//		this.getMTComponent().unregisterAllInputProcessors();
//		this.getMTComponent().removeAllGestureEventListeners();

//		this.getMTComponent().registerInputProcessor(
//				new DragProcessor(mtApplication));
		this.getMTComponent().addGestureListener(DragProcessor.class,
				new DefaultDragAction());

		this.getMTComponent().registerInputProcessor(
				new RotateProcessor(mtApplication));
		this.getMTComponent().addGestureListener(RotateProcessor.class,
				new DefaultRotateAction());
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
	
	protected boolean isPhotoAlbum(){
		return true;
	}
}
