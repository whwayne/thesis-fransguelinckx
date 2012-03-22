package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

public class CFComponentMenu {
	private CFComponent owner;
	private ArrayList<CFComponentMenuItem> menuItems;

	private MTApplication mtApplication;

	private boolean visible = false;

	public CFComponentMenu(CFComponent component, MTApplication mtApplication) {
		this.owner = component;
		this.mtApplication = mtApplication;
		this.menuItems = new ArrayList<CFComponentMenuItem>();
	}

	protected void addMenuItem(String fileName,
			CFComponentMenuItemListener listener) {
		CFComponentMenuItem menuItem = new CFComponentMenuItem(fileName,
				listener, getMtApplication(), this.getOwner().getCFScene());
		this.menuItems.add(menuItem);
		this.getOwner().getMTComponent()
				.addChild(menuItem.getMTComponent());
	}

	private CFComponent getOwner() {
		return owner;
	}

	private ArrayList<CFComponentMenuItem> getMenuItems() {
		return this.menuItems;
	}

	private MTApplication getMtApplication() {
		return mtApplication;
	}

	protected boolean isVisible() {
		return this.visible;
	}
	
	protected void positionMenuItemsLeftAndRight(){
		CFComponent owner = this.getOwner();
		Vector3D leftPosition = owner.getMTComponent().getPosition(TransformSpace.GLOBAL);
		leftPosition.translate(new Vector3D(-owner.getWidth()/2, 0));
		Vector3D rightPosition = owner.getMTComponent().getPosition(TransformSpace.GLOBAL);
		rightPosition.translate(new Vector3D(owner.getWidth()/2, 0));
		
		this.getMenuItems().get(0).setPosition(leftPosition);
		this.getMenuItems().get(1).setPosition(rightPosition);
	}

	protected void repositionMenuItemsInCircle() {
		int degrees = 0;
		for(CFComponentMenuItem item : this.getMenuItems()){
			item.setPosition(new Vector3D(getOwner().getPosition().x, getOwner().getPosition().y-120));
			item.rotate(this.getOwner().getPosition(), degrees);
			degrees += 30;
		}
	}

	protected void setVisible(boolean visible) {
		if (visible && !this.isVisible()) {
			this.visible = true;
			for (CFComponentMenuItem item : this.getMenuItems()) {
				item.setVisible(true);
			}
		} else if (!visible && this.isVisible()) {
			this.visible = false;
			for (CFComponentMenuItem item : this.getMenuItems()) {
				item.setVisible(false);
			}
		}
	}

}
