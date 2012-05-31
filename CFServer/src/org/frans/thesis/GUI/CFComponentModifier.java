package org.frans.thesis.GUI;

public abstract class CFComponentModifier {
	protected static final int X_LOW_TRESHHOLD = 480;
	protected static final int X_HIGH_TRESHHOLD = 1440;
	protected static final int Y_LOW_TRESHHOLD = 270;
	protected static final int Y_HIGH_TRESHHOLD = 810;
	
	public abstract void handleMovedCFComponent(CFComponent component);

}
