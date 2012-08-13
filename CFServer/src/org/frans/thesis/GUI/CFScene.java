package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.frans.thesis.PhotoApp.CFTrashCan;
import org.mt4j.MTApplication;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.math.Vector3D;

/**
 * An abstract class that represents a scene on the tabletop. A scene usually
 * contains several interactive components.
 */
public abstract class CFScene extends AbstractScene {

	/**
	 * An list of all interactive components that this scene contains.
	 */
	private ArrayList<CFComponent> cfComponents;

	/**
	 * The minimum distance (in pixels) between the centers of two interactive
	 * components for them to be considered "on top of each other". When a user
	 * drops an interactive component on top of the other, the method
	 * handleDroppedComponent() gets called automatically.
	 */
	protected final float CRITICAL_STACK_DISTANCE = 100;

	/**
	 * A list of component modifiers that belong to this scene.
	 */
	private ArrayList<CFComponentModifier> componentModifiers;

	/**
	 * The public constructor for CFScene. Adds two trashcans to the scene by
	 * default.
	 * 
	 * @param mtApplication
	 *            The mtapplication to which this scene belongs.
	 * @param name
	 *            The name of this scene.
	 */
	public CFScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.cfComponents = new ArrayList<CFComponent>();
		this.componentModifiers = new ArrayList<CFComponentModifier>();
		this.addCFComponent(new CFTrashCan(mtApplication, this));
		this.addCFComponent(new CFTrashCan(mtApplication, this));
	}

	/**
	 * Adds a component modifier to this scene.
	 */
	public void addComponentModifier(CFComponentModifier modifier) {
		if (modifier != null
				&& !this.getComponentModifiers().contains(modifier)) {
			this.componentModifiers.add(modifier);
		}
	}

	/**
	 * Returns the list of component modifiers.
	 */
	private ArrayList<CFComponentModifier> getComponentModifiers() {
		return this.componentModifiers;
	}

	/**
	 * Adds an instance of CFComponent to the center of the scene.
	 * 
	 * @param component
	 *            The component to be added to the scene. It wont be added if
	 *            component == null or is already in the scene.
	 */
	public void addCFComponent(CFComponent component) {
		if (component != null && !this.getCfComponents().contains(component)) {
			this.getCanvas().addChild(component);
			component.setPositionGlobal(new Vector3D(this.getMTApplication()
					.getWidth() / 2, this.getMTApplication().getHeight() / 2));
			this.getCfComponents().add(component);
		}
	}

	/**
	 * Returns the list of CFComponents in this scene.
	 */
	protected ArrayList<CFComponent> getCfComponents() {
		return cfComponents;
	}

	@Override
	public void init() {
	}

	/**
	 * Checks whether a given cfcomponent is close to another component in this
	 * scene. More precisely, if their centers are closer than
	 * CRITICAL_STACK_DISTANCE.
	 * 
	 * @param otherComponent
	 *            The component to be compared to to all the other components in
	 *            this scene.
	 * @return True if there is at least one other component that is close
	 *         enough. False if there is no other component that is close
	 *         enough.
	 */
	protected boolean isCloseToCFComponent(CFComponent otherComponent) {
		if (otherComponent != null) {
			for (CFComponent component : this.getCfComponents()) {
				if (!component.equals(otherComponent)
						&& otherComponent.getDistanceto(component) < this.CRITICAL_STACK_DISTANCE) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Gets all nearby components of a given component. More precisely: all components that are closer than CRITICAL_STACK_DISTANCE.
	 * @param component
	 * The component from which all nearby components have to be returned.
	 * @return
	 * An array list containing all nearby components.
	 */
	protected ArrayList<CFComponent> getNearbyCFComponents(
			CFComponent component) {
		ArrayList<CFComponent> result = new ArrayList<CFComponent>();
		for (CFComponent component2 : this.getCfComponents()) {
			if (!component2.equals(component)
					&& component.getDistanceto(component2) < this.CRITICAL_STACK_DISTANCE) {
				result.add(component2);
			}
		}
		return result;
	}

	@Override
	public void shutDown() {
	}

	/**
	 * A method that is called when a user releases an interactive component. It looks up all nearby interactive components and notifies them of the newly dropped component.
	 * @param component
	 * The component that has been released by a user.
	 */
	protected void cFComponentDropped(CFComponent component) {
		for (CFComponent otherComponent : this.getCfComponents()) {
			if (!component.equals(otherComponent)
					&& component.getDistanceto(otherComponent) < this.CRITICAL_STACK_DISTANCE) {
				otherComponent.handleDroppedCFComponent(component);
			}
		}
	}

	/**
	 * A method that is called when a user rotated an interactive component. It notifies all components in this scene about the rotated component.
	 * @param component
	 * The component that was rotated.
	 */
	protected void cFComponentRotated(CFComponent component) {
		for (CFComponent otherComponent : this.getCfComponents()) {
			otherComponent.handleRotatedCFComponent(component);
		}
	}

	/**
	 * A method that is called when a user scaled an interactive component. It notifies all components in this scene about the scaled component.
	 * @param component
	 */
	protected void cFComponentScaled(CFComponent component) {
		for (CFComponent otherComponent : this.getCfComponents()) {
			otherComponent.handleScaledCFComponent(component);
		}
	}

	/**
	 * Removes an interactive component from this scene.
	 * @param component
	 * The component that has to be removed.
	 */
	public void removeCFComponent(CFComponent component) {
		// this.getCfComponents().remove(component);
		this.getCanvas().removeChild(component);
	}

	/**
	 * A method that is called when a user moved an interactive component. It notifies all components in this scene about the moved component.
	 * @param component
	 * The component that was moved.
	 */
	public void cfComponentMoved(CFComponentModifiable component) {
		// TODO Auto-generated method stub
		for (CFComponentModifier modifier : this.componentModifiers) {
			modifier.handleMovedCFComponent(component);
		}

	}
}
