package org.frans.thesis.GUI;

import org.frans.thesis.service.CFTabletopClient;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class CFMobileDeviceProxy extends CFComponent {

	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "android.png";
	private CFComponentMenu menu;
	private MTApplication mtApplication;
	private String name;

	private CFScene scene;
	private CFTabletopClient tabletopClient;

	public CFMobileDeviceProxy(MTApplication mtApplication, String name,
			CFScene scene, CFTabletopClient tabletopClient) {
		this.scene = scene;
		this.mtApplication = mtApplication;
		this.name = name;
		this.tabletopClient = tabletopClient;
		// IFont fontArial = FontManager.getInstance().createFont(mtApplication,
		// FontManager.DEFAULT_FONT, 40, // Font size
		// white, // Font fill color
		// white); // Font outline color
		setUpComponent(mtApplication);
		this.scaleImageToStackSize();

		setUpGestures(mtApplication);

		this.getMTComponent().setNoStroke(true);
		this.getMTComponent().setStrokeColor(new MTColor(255, 0, 0));
		createMenu();
	}

	private void createMenu() {
		this.menu = new CFComponentMenu(this, mtApplication);
		this.menu.addMenuItem("photos.png", new CFComponentMenuItemListener() {

			@Override
			public void processEvent() {
				downloadPhotos();
			}
		});
		this.menu.addMenuItem("calendar.png",
				new CFComponentMenuItemListener() {

					@Override
					public void processEvent() {
						downloadCalendar();
					}
				});
		this.menu.addMenuItem("pdf.png", new CFComponentMenuItemListener() {

			@Override
			public void processEvent() {
				downloadPdf();
			}
		});
		this.menu.repositionMenuItems();
	}

	private void downloadCalendar() {
		System.out.println("Download calendar");
	}

	private void downloadPdf() {
		System.out.println("Download pdf");
	}

	private void downloadPhotos() {
		this.getTabletopClient().setStatus(CFTabletopClient.REQUESTING_PHOTOS);
	}

	private CFComponentMenu getMenu() {
		return this.menu;
	}

	private PApplet getMTApplication() {
		return this.mtApplication;
	}

	private CFScene getScene() {
		return this.scene;
	}

	private CFTabletopClient getTabletopClient() {
		return this.tabletopClient;
	}

	@Override
	public boolean isStackable() {
		return false;
	}

	private void setUpComponent(MTApplication mtApplication) {
		MTTextArea textField = new MTTextArea(mtApplication, FontManager
				.getInstance().createFont(mtApplication, "SansSerif", 30,
						new MTColor(255, 255, 255)));
		// Create a textfield
		textField.setNoStroke(true);
		textField.setNoFill(true);
		textField.setText(this.name);
		textField.setPickable(false);

		PImage pImage = getMTApplication().loadImage(imagePath);
		MTRectangle mtImage = new MTRectangle(getMTApplication(), pImage);
		mtImage.setNoStroke(true);
		mtImage.setPickable(false);

		float height = textField.getHeightXY(TransformSpace.GLOBAL);
		height += mtImage.getHeightXY(TransformSpace.GLOBAL);
		float width = Math.max(textField.getWidthXY(TransformSpace.GLOBAL),
				mtImage.getWidthXY(TransformSpace.GLOBAL));
		this.component = new MTRectangle(getMTApplication(), width, height);

		this.component.addChild(textField);
		this.component.addChild(mtImage);
		mtImage.translate(new Vector3D(0, textField
				.getHeightXY(TransformSpace.GLOBAL), 0));
		this.component.setNoFill(true);
//		this.component.setNoStroke(true);
	}

	private void setUpGestures(MTApplication mtApplication) {
		this.getMTComponent().unregisterAllInputProcessors();
		this.getMTComponent().removeAllGestureEventListeners();

		this.getMTComponent().registerInputProcessor(
				new DragProcessor(mtApplication));
		this.getMTComponent().addGestureListener(DragProcessor.class,
				new DefaultDragAction());

		this.getMTComponent().registerInputProcessor(
				new RotateProcessor(mtApplication));
		this.getMTComponent().addGestureListener(RotateProcessor.class,
				new DefaultRotateAction());

		this.getMTComponent().registerInputProcessor(
				new TapProcessor(mtApplication));
		this.getMTComponent().addGestureListener(TapProcessor.class,
				new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent me) {
						TapEvent tapEvent = (TapEvent) me;
						if (tapEvent.isTapped()) {
							showMenu();
						}
						return false;
					}
				});
	}

	private void showMenu() {
		if (this.getMenu().isVisible()) {
			this.getMenu().setVisible(false);
		} else {
			this.getMenu().setVisible(true);
		}
	}
}