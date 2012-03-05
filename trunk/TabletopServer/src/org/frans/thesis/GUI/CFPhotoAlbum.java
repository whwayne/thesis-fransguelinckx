package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;

public class CFPhotoAlbum {
	
	private ArrayList<CFImage> images;
	private MTRectangle rectangle;
	private final int DIMENSION_X = 1000;
	private final int DIMENSION_Y = 700;
	
	public CFPhotoAlbum(MTApplication application, CFImage initialImage){
		this.rectangle = new MTRectangle(application, DIMENSION_X, DIMENSION_Y);
		this.images = new ArrayList<CFImage>();
		this.addImage(initialImage);
	}
	
	private MTRectangle getRectangle(){
		return this.rectangle;
	}
	
	protected void addImage(CFImage image){
		//TODO
		if(!this.getImages().contains(image)){
			this.getImages().add(image);
			image.getImage().removeFromParent();
			this.getRectangle().addChild(image.getImage());
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
	
	

}
