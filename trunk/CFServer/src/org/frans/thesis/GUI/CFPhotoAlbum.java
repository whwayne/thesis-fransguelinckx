package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

public class CFPhotoAlbum extends CFComponent {

	private ArrayList<CFImage> images;
	private MTApplication mtApplication;
	private CFComponentMenu menu;
	private float DIMENSION_X = 500;
	private float DIMENSION_Y = 350;
	private int pageNumber = 0;
	private Vector3D leftImagePosition;
	private Vector3D rightImagePosition;

	public CFPhotoAlbum(MTApplication application, CFImage initialImage,
			CFPhotoScene scene) {
		super(application, scene);
		((CFPhotoScene) this.getCFScene()).addPhotoalbum(this);
		this.mtApplication = application;
		// this.component = new MTRectangle(application, DIMENSION_X,
		// DIMENSION_Y);
		this.component.setHeightLocal(DIMENSION_Y);
		this.component.setWidthLocal(DIMENSION_X);
		this.component.setNoFill(true);
		this.images = new ArrayList<CFImage>();
		scene.addCFComponent(this);
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

	protected ArrayList<CFImage> getImages() {
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
//			 leftImage.rotateTo(this.getAngle());
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
	
	protected void scale(float x, float y, float z, Vector3D scalingPoint){
		super.scale(x, y, z, scalingPoint);
		this.DIMENSION_X *= x;
		this.DIMENSION_Y *= y;
	}

	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		if(component instanceof CFImage){
			CFImage image = (CFImage) component;
			this.addImage(image);
			((CFPhotoScene) this.getCFScene()).reloadAlbums();
		}
	}

}
