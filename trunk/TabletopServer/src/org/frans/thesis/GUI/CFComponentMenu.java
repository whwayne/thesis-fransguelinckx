package org.frans.thesis.GUI;

import java.util.ArrayList;

import org.mt4j.MTApplication;

public class CFComponentMenu {
	private CFComponent component;
	private ArrayList<CFComponentMenuItem> menuItems;

	private MTApplication mtApplication;

	private boolean visible = false;

	public CFComponentMenu(CFComponent component, MTApplication mtApplication) {
		this.component = component;
		this.mtApplication = mtApplication;
		this.menuItems = new ArrayList<CFComponentMenuItem>();
	}

	protected void addMenuItem(String fileName,
			CFComponentMenuItemListener listener) {
		CFComponentMenuItem menuItem = new CFComponentMenuItem(fileName,
				listener, getMtApplication());
		this.menuItems.add(menuItem);
		this.getCFComponent().getMTComponent()
				.addChild(menuItem.getMTComponent());
		this.repositionMenuItems();
	}

	private CFComponent getCFComponent() {
		return component;
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

	private void repositionMenuItems() {
		// TODO Auto-generated method stub

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
