package org.frans.thesis.PhotoApp;

import java.util.ArrayList;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFComponentMenu;
import org.frans.thesis.GUI.CFComponentMenuItemListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

public class CFPhotoAlbum extends CFComponent implements CFAutoRotatable,
		CFAutoScalable {

	private boolean autoRotate = true;
	private boolean autoScale = true;
	private float DIMENSION_X = 500;
	private float DIMENSION_Y = 350;
	private ArrayList<CFImage> images;
	private Vector3D leftImagePosition;
	private int pageNumber = 0;
	private Vector3D rightImagePosition;

	public CFPhotoAlbum(CFImage initialImage,
			CFPhotoScene scene) {
		super(scene);
		((CFPhotoScene) this.getCFScene()).addPhotoalbum(this);
		this.setHeightLocal(DIMENSION_Y);
		this.setWidthLocal(DIMENSION_X);
		this.setNoFill(true);
		this.images = new ArrayList<CFImage>();
		scene.addCFComponent(this);
		this.createMenu();
		leftImagePosition = new Vector3D(DIMENSION_X / 4, DIMENSION_Y / 2);
		rightImagePosition = new Vector3D((3 * DIMENSION_X) / 4,
				DIMENSION_Y / 2);
	}

	protected void addImage(CFImage image) {
		if (!this.getImages().contains(image)) {
			this.getImages().add(image);
			this.addChild(image);
			image.setVisible(false);
			image.rotateTo(0);
			image.setPickable(false);
		}
	}

	@Override
	public boolean autoRotateIsOn() {
		return this.autoRotate;
	}

	@Override
	public boolean autoScaleIsOn() {
		return this.autoScale;
	}

	private void createMenu() {
		this.setComponentMenu(new CFComponentMenu(this));
		this.getComponentMenu().addMenuItem("left_arrow.png",
				new CFComponentMenuItemListener() {

					@Override
					public void processEvent() {
						pageDown();
					}
				});
		this.getComponentMenu().addMenuItem("right_arrow.png",
				new CFComponentMenuItemListener() {

					@Override
					public void processEvent() {
						pageUp();
					}
				});
		this.getComponentMenu().positionMenuItemsLeftAndRight();
		this.getComponentMenu().setVisible(true);
	}

	protected ArrayList<CFImage> getImages() {
		return this.images;
	}

	protected int getPageNumber() {
		return this.pageNumber;
	}

	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		if (component instanceof CFImage) {
			CFImage image = (CFImage) component;
			this.addImage(image);
			((CFPhotoScene) this.getCFScene()).reloadAlbums();
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

	protected boolean isPhotoAlbum() {
		return true;
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
			// this.getMTComponent().addChild(leftImage.getImage());
			leftImage.setVisible(true);
			this.resizeImage(leftImage);
			// leftImage.rotateTo(this.getAngle());
			leftImage.setPositionRelativeToParent(leftImagePosition);
		}
		if (rightImage != null) {
			// this.getMTComponent().addChild(rightImage.getImage());
			rightImage.setVisible(true);
			this.resizeImage(rightImage);
			// rightImage.rotateTo(this.getAngle());
			rightImage.setPositionRelativeToParent(rightImagePosition);
		}
	}

	protected void pageDown() {
		if (pageNumber > 0) {
			unloadImages();
			pageNumber--;
			loadImages();
		}
	}

	protected void pageUp() {
		if (this.getImages().size() >= ((this.getPageNumber() + 1) * 2) + 1) {
			unloadImages();
			pageNumber++;
			loadImages();
		}
	}

	protected void removeImage(CFImage image) {
		if (this.getImages().contains(image)) {
			this.getImages().remove(image);
			image.removeFromParent();
			this.getParent().addChild(image);
			image.setPickable(true);
			loadImages();
		}
	}

	private void resizeImage(CFImage image) {
		float scaleFactorX = (this.DIMENSION_X / 2)
				/ image.getWidthXY(TransformSpace.GLOBAL);
		float scaleFactorY = this.DIMENSION_Y
				/ image.getHeightXY(TransformSpace.GLOBAL);
		float result = Math.min(scaleFactorX, scaleFactorY);
		image.scaleImage(result);
	}

	@Override
	public void scale(float x, float y, float z, Vector3D scalingPoint) {
		super.scale(x, y, z, scalingPoint);
		this.DIMENSION_X *= x;
		this.DIMENSION_Y *= y;
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
			// this.getMTComponent().removeChild(leftImage.getImage());
			leftImage.setVisible(false);
		}
		if (rightImage != null) {
			// this.getMTComponent().removeChild(rightImage.getImage());
			rightImage.setVisible(false);
		}
	}

}
