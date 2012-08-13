package org.frans.thesis.GUI;

/**
 * An abstract class that represents a component that gets notified automatically when a user moves an interactive component on the tabletop.
 */
public abstract class CFComponentModifier {
	protected static final int X_LOW_TRESHHOLD = 480;
	protected static final int X_HIGH_TRESHHOLD = 1440;
	protected static final int Y_LOW_TRESHHOLD = 270;
	protected static final int Y_HIGH_TRESHHOLD = 810;
	
	/**
	 * An abstract method that gets called automatically when a user moves an interactive component on the tabletop.
	 * @pre
	 * The instance of CFComponentModifier should be registered with the scene. 
	 */
	public abstract void handleMovedCFComponent(CFComponentModifiable component);

}
