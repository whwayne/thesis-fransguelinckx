package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.frans.thesis.PhotoApp.CFTrashCan;
import org.mt4j.MTApplication;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.math.Vector3D;

public abstract class CFScene extends AbstractScene{

	private ArrayList<CFComponent> cfComponents;
	protected final float CRITICAL_STACK_DISTANCE = 100;
	private ArrayList<CFComponentModifier> componentModifiers;

	public CFScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.cfComponents = new ArrayList<CFComponent>();
		this.componentModifiers = new ArrayList<CFComponentModifier>();
		new CFTrashCan(mtApplication, this);
		new CFTrashCan(mtApplication, this);
	}
	
	public void addComponentModifier(CFComponentModifier modifier){
		this.componentModifiers.add(modifier);
	}

	public void addCFComponent(CFComponent component) {
		if (!this.getCfComponents().contains(component)) {
			this.getCanvas().addChild(component.getMTComponent());
			component.getMTComponent().setPositionGlobal(
					new Vector3D(this.getMTApplication().getWidth() / 2, this
							.getMTApplication().getHeight() / 2));
			this.getCfComponents().add(component);
		}
	}

	protected ArrayList<CFComponent> getCfComponents() {
		return cfComponents;
	}

	@Override
	public void init() {
	}

	protected boolean isCloseToCFComponent(CFComponent image1) {
		for (CFComponent image2 : this.getCfComponents()) {
			if (!image2.equals(image1)
					&& image1.getDistanceto(image2) < this.CRITICAL_STACK_DISTANCE) {
				return true;
			}
		}
		return false;
	}


	protected ArrayList<CFComponent> getNearCFComponents(CFComponent component1) {
		ArrayList<CFComponent> result = new ArrayList<CFComponent>();
		for (CFComponent component2 : this.getCfComponents()) {
			if (!component2.equals(component1)
					&& component1.getDistanceto(component2) < this.CRITICAL_STACK_DISTANCE) {
				result.add(component2);
			}
		}
		return result;
	}

	@Override
	public void shutDown() {
	}

	protected void cFComponentDropped(CFComponent component) {
			for (CFComponent otherComponent : this.getCfComponents()) {
				if (!component.equals(otherComponent) && component.getDistanceto(otherComponent) < this.CRITICAL_STACK_DISTANCE) {
					otherComponent.handleDroppedCFComponent(component);
				}
			}
	}

	protected void cFComponentRotated(CFComponent component) {
			for (CFComponent otherComponent : this.getCfComponents()) {
					otherComponent.handleRotatedCFComponent(component);
			}
	}

	protected void cFComponentScaled(CFComponent component) {
			for (CFComponent otherComponent : this.getCfComponents()) {
					otherComponent.handleScaledCFComponent(component);
			}
	}

	public void removeCFComponent(CFComponent component) {
//		this.getCfComponents().remove(component);
		this.getCanvas().removeChild(component.getMTComponent());
	}

	public void cfComponentMoved(CFComponent component) {
		// TODO Auto-generated method stub
		for(CFComponentModifier modifier : this.componentModifiers){
			modifier.handleMovedCFComponent(component);
		}
		
	}
}
