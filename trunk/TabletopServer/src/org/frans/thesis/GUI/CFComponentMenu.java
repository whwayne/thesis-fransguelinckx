package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.mt4j.MTApplication;

public class CFComponentMenu {
	private CFComponent component;
	private CFComponent getCFComponent() {
		return component;
	}

	private MTApplication getMtApplication() {
		return mtApplication;
	}

	private MTApplication mtApplication;
	private boolean visible = false;
	private ArrayList<CFComponentMenuItem> menuItems;

	public CFComponentMenu(CFComponent component, MTApplication mtApplication) {
		this.component = component;
		this.mtApplication = mtApplication;
		this.menuItems = new ArrayList<CFComponentMenuItem>();
	}

	protected void addMenuItem(String fileName,
			CFComponentMenuItemListener listener) {
		CFComponentMenuItem menuItem = new CFComponentMenuItem(fileName, listener, getMtApplication()); 
		this.menuItems.add(menuItem);
		this.getCFComponent().getMTComponent().addChild(menuItem.getMTComponent());
		this.repositionMenuItems();
	}

	private void repositionMenuItems() {
		// TODO Auto-generated method stub
		
	}

	private ArrayList<CFComponentMenuItem> getMenuItems() {
		return this.menuItems;
	}

	protected boolean isVisible() {
		return this.visible;
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
