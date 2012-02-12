package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.math.Vector3D;

public class CFScene extends AbstractScene {

	private ArrayList<CFComponent> cfComponents;
	private ArrayList<CFMobileDeviceProxy> cfMobileDeviceProxies;
	private ArrayList<CFMobileDeviceProxy> getCfMobileDeviceProxies() {
		return cfMobileDeviceProxies;
	}

	private final float CRITICAL_STACK_DISTANCE = 100;

	public CFScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.cfComponents = new ArrayList<CFComponent>();
		this.cfMobileDeviceProxies = new ArrayList<CFMobileDeviceProxy>();

		this.addCFImage(new CFImage(getMTApplication(), "foto1.jpg", this));
		this.addCFImage(new CFImage(getMTApplication(), "foto2.jpg", this));
		this.addCFMobileDeviceProxy(new CFMobileDeviceProxy(getMTApplication(), this));
	}

	protected void addCFImage(CFImage image) {
		if(!this.getCfComponents().contains(image)){
			this.getCanvas().addChild(image.getComponent());	
			this.getCfComponents().add(image);
		}
	}
	
	protected void addCFMobileDeviceProxy(CFMobileDeviceProxy proxy){
		if(!this.getCfMobileDeviceProxies().contains(proxy)){
			this.getCanvas().addChild(proxy.getComponent());	
			this.getCfMobileDeviceProxies().add(proxy);
		}
	}
	
	protected boolean isCloseToCFComponent(CFComponent image1){
		for(CFComponent image2 : this.getCfComponents()){
			if(!image2.equals(image1) && image1.getDistanceto(image2) < this.CRITICAL_STACK_DISTANCE){
				return true;
			}
		}
		return false;
	}

	private ArrayList<CFComponent> getCfComponents() {
		return cfComponents;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}

	protected void addToStack(CFComponent component1) {
		ArrayList<CFComponent> nearCFComponents = this.getNearCFComponents(component1);
		component1.scaleImageToStackSize();
		Vector3D position = component1.getPosition();
		for(CFComponent component2 : nearCFComponents){
			if(component2.isStackable()){
				component2.scaleImageToStackSize();
				component2.reposition(position);
//				image2.rotateRandomlyForStack();
			}
		}
	}

	protected ArrayList<CFComponent> getNearCFComponents(CFComponent component1) {
		ArrayList<CFComponent> result = new ArrayList<CFComponent>();
		for(CFComponent component2 : this.getCfComponents()){
			if(!component2.equals(component1) && component1.getDistanceto(component2) < this.CRITICAL_STACK_DISTANCE){
				result.add(component2);
			}
		}
		return result;
	}

}
