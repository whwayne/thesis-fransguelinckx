package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.math.Vector3D;

public class CFScene extends AbstractScene {

	private ArrayList<CFImage> cfImages;
	private final float CRITICAL_STACK_DISTANCE = 100;

	public CFScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		cfImages = new ArrayList<CFImage>();

		this.addCFImage(new CFImage(getMTApplication(), "foto1.jpg", this));
		this.addCFImage(new CFImage(getMTApplication(), "foto2.jpg", this));
	}

	protected void addCFImage(CFImage image) {
		if(!this.getCfImages().contains(image)){
			this.getCanvas().addChild(image.getImage());	
			this.getCfImages().add(image);
		}
	}
	
	protected boolean isCloseToCFImage(CFImage image1){
		for(CFImage image2 : this.getCfImages()){
			if(!image2.equals(image1) && image1.getDistanceto(image2) < this.CRITICAL_STACK_DISTANCE){
				return true;
			}
		}
		return false;
	}

	private ArrayList<CFImage> getCfImages() {
		return cfImages;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}

	protected void addToStack(CFImage image1) {
		ArrayList<CFImage> nearCFImages = this.getNearCFImages(image1);
		image1.scaleImageToStackSize();
		Vector3D position = image1.getPosition();
		for(CFImage image2 : nearCFImages){
			image2.scaleImageToStackSize();
			image2.reposition(position);
//			image2.rotateRandomlyForStack();
		}
	}

	private ArrayList<CFImage> getNearCFImages(CFImage image1) {
		ArrayList<CFImage> result = new ArrayList<CFImage>();
		for(CFImage image2 : this.getCfImages()){
			if(!image2.equals(image1) && image1.getDistanceto(image2) < this.CRITICAL_STACK_DISTANCE){
				result.add(image2);
			}
		}
		return result;
	}

}
