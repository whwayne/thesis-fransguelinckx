package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;

public class CFPhotoAlbum {
	
	private ArrayList<CFImage> images;
	private MTRectangle rectangle;
	private final int DIMENSION_X = 1000;
	private final int DIMENSION_Y = 700;
	private int pageNumber = -1;
	private final Vector3D leftImagePosition = new Vector3D(DIMENSION_X/4, DIMENSION_Y/2);
	private final Vector3D rightImagePosition = new Vector3D(3*DIMENSION_X/4, DIMENSION_Y/2);
	
	public CFPhotoAlbum(MTApplication application, CFImage initialImage){
		this.rectangle = new MTRectangle(application, DIMENSION_X, DIMENSION_Y);
		this.images = new ArrayList<CFImage>();
		this.addImage(initialImage);
		this.pageUp();
	}

	private MTRectangle getRectangle(){
		return this.rectangle;
	}
	
	protected void addImage(CFImage image){
		//TODO
		if(!this.getImages().contains(image)){
			this.getImages().add(image);
			image.getImage().removeFromParent();
		}
	}
	
	protected void removeImage(CFImage image){
		//TODO
		if(this.getImages().contains(image)){
			this.getImages().remove(image);
			image.getImage().removeFromParent();
			this.getRectangle().getParent().addChild(image.getImage());
		}
	}
	
	private ArrayList<CFImage> getImages(){
		return this.images;
	}
	
	protected void pageUp(){
		unloadImages();
		pageNumber++;
		loadImages();
	}

	private void loadImages() {
		CFImage leftImage;
		CFImage rightImage;
		leftImage = this.getImages().get(this.getPageNumber());
		rightImage = this.getImages().get(this.getPageNumber()+1);
		if(leftImage != null){
			this.getRectangle().addChild(leftImage.getImage());
			leftImage.getImage().setPositionRelativeToParent(leftImagePosition);
		}
		if(rightImage != null){
			this.getRectangle().addChild(rightImage.getImage());
			rightImage.getImage().setPositionRelativeToParent(rightImagePosition);
		}
	}

	private void unloadImages() {
		CFImage leftImage = this.getImages().get(this.getPageNumber());
		CFImage rightImage = this.getImages().get(this.getPageNumber()+1);
		if(leftImage != null){
			this.getRectangle().removeChild(leftImage.getImage());
		}
		if(rightImage != null){
			this.getRectangle().removeChild(leftImage.getImage());
		}
	}
	
	protected void pagedown(){
		pageNumber--;
		//TODO
	}
	
	protected int getPageNumber(){
		return this.pageNumber;
	}
}
