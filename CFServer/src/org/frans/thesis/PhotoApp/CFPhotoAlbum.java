package org.frans.thesis.PhotoApp;

import java.util.ArrayList;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFComponentMenu;
import org.frans.thesis.GUI.CFComponentMenuItemListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

/**
 * A class to represent a photo album on the tabletop.
 */
public class CFPhotoAlbum extends CFComponent implements CFAutoRotatable,
		CFAutoScalable {

	/**
	 * Boolean to indicate whether the auto rotation is on or off.
	 */
	private boolean autoRotate = true;

	/**
	 * Boolean to indicate whether auto scale is on or off.
	 */
	private boolean autoScale = true;

	/**
	 * The default width of an album.
	 */
	private float DIMENSION_X = 500;

	/**
	 * The default height of an album.
	 */
	private float DIMENSION_Y = 350;

	/**
	 * An array of images in an album.
	 */
	private ArrayList<CFImage> images;

	/**
	 * The relative position of the left image inside the square of the photo album.
	 */
	private Vector3D leftImagePosition;

	/**
	 * The page number, 0 by default. Each page in an album shows two images.
	 */
	private int pageNumber = 0;

	/**
	 * The relative position of the right image inside the square of the photo album.
	 */
	private Vector3D rightImagePosition;

	/**
	 * Public constructor for an album. Sets up the visual representation and
	 * the buttons to page through the album.
	 * 
	 * @param scene
	 *            The scene to which this album belongs.
	 */
	public CFPhotoAlbum(CFPhotoScene scene) {
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

	/**
	 * Adds an image to this photo.
	 * 
	 * @param image
	 *            The image that has to be added.
	 */
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

	/**
	 * Creates the menu of an album to be able to page through it.
	 */
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

	/**
	 * Returns the list of images in this album.
	 */
	protected ArrayList<CFImage> getImages() {
		return this.images;
	}

	/**
	 * Returns the current page number.
	 * @return
	 * 0 for page 1
	 * 1 for page 2
	 * etc.
	 */
	protected int getPageNumber() {
		return this.pageNumber;
	}

	/**
	 * Handles images being dropped on this album by adding them to the album.
	 */
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

	/**
	 * Loads two images to be visible, according to the current page number.
	 */
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
			leftImage.setVisible(true);
			this.resizeImage(leftImage);
			leftImage.setPositionRelativeToParent(leftImagePosition);
		}
		if (rightImage != null) {
			rightImage.setVisible(true);
			this.resizeImage(rightImage);
			rightImage.setPositionRelativeToParent(rightImagePosition);
		}
	}

	/**
	 * Lowers the page number by one and takes care of showing the correct images according to the page number.
	 */
	protected void pageDown() {
		if (pageNumber > 0) {
			unloadImages();
			pageNumber--;
			loadImages();
		}
	}
	
	/**
	 * Increases the page number by one and takes care of showing the correct images according to the page number.
	 */
	protected void pageUp() {
		if (this.getImages().size() >= ((this.getPageNumber() + 1) * 2) + 1) {
			unloadImages();
			pageNumber++;
			loadImages();
		}
	}

	/**
	 * Resizes an image so it fits inside the album.
	 * @param image
	 * The image that has to be resized.
	 */
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

	/**
	 * Makes the images on the current page number invisible.
	 */
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
			leftImage.setVisible(false);
		}
		if (rightImage != null) {
			rightImage.setVisible(false);
		}
	}

}
