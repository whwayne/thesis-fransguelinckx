package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

/**
 * A class that represents a contextual menu on the tabletop.
 */
public class CFComponentMenu extends CFComponent {

	/**
	 * An list a all menu items in this menu.
	 */
	private ArrayList<CFComponentMenuItem> menuItems;

	/**
	 * The component to which this menu belongs.
	 */
	private CFComponent owner;

	/**
	 * The constructor for a contexual menu.
	 * 
	 * @param component
	 *            The owner-component of this menu.
	 * @param mtApplication
	 *            The application to which this menu belongs.
	 */
	public CFComponentMenu(CFComponent component, MTApplication mtApplication) {
		super(mtApplication, component.getCFScene());
		this.owner = component;
		this.menuItems = new ArrayList<CFComponentMenuItem>();
	}

	/**
	 * Adds an item to this menu.
	 * 
	 * @param fileName
	 *            The file name of the image that the menu item should display.
	 * @param listener
	 *            The listener that should be called when a user taps the item.
	 */
	public void addMenuItem(String fileName,
			CFComponentMenuItemListener listener) {
		CFComponentMenuItem menuItem = new CFComponentMenuItem(fileName,
				listener, this.getCFScene().getMTApplication(), this.getOwner()
						.getCFScene());
		this.menuItems.add(menuItem);
		this.getOwner().addChild(menuItem);
		menuItem.reposition(new Vector3D(getOwner().getPosition().x, getOwner()
				.getPosition().y));
	}

	/**
	 * Returns the list of menu items in this menu.
	 */
	private ArrayList<CFComponentMenuItem> getMenuItems() {
		return this.menuItems;
	}

	/**
	 * Returns the owner component of this menu.
	 */
	private CFComponent getOwner() {
		return owner;
	}

	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		// TODO Auto-generated method stub

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
	 * Positions the first two menu items to the left and right of the owner
	 * component. Other items in this menu remain in the same position.
	 * 
	 * @pre There should be at least two menu items present in this menu.
	 */
	public void positionMenuItemsLeftAndRight() {
		CFComponent owner = this.getOwner();
		Vector3D leftPosition = owner.getPosition(TransformSpace.GLOBAL);
		leftPosition.translate(new Vector3D(-owner.getWidth() / 2, 0));
		Vector3D rightPosition = owner.getPosition(TransformSpace.GLOBAL);
		rightPosition.translate(new Vector3D(owner.getWidth() / 2, 0));

		this.getMenuItems().get(0).reposition(leftPosition);
		this.getMenuItems().get(1).reposition(rightPosition);
	}

	/**
	 * Positions all menu items around the owner-component, 30 degrees apart.
	 */
	public void repositionMenuItemsInCircle() {
		int degrees = 0;
		for (CFComponentMenuItem item : this.getMenuItems()) {
			item.reposition(new Vector3D(getOwner().getPosition().x, getOwner()
					.getPosition().y - 150));
			item.rotate(this.getOwner().getPosition(), degrees);
			degrees += 30;
		}
	}

	/**
	 * Makes this menu (items included) visible or invisible, depending on the
	 * parameter.
	 * 
	 * @param visible
	 *            Indicates wheter this menu should be visible or not. True ->
	 *            visible and vice versa.
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(this.getMenuItems() != null){
		for (CFComponentMenuItem item : this.getMenuItems()) {
			item.setVisible(visible);
		}
		}
	}

}
