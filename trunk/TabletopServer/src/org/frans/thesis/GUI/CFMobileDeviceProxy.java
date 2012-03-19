package org.frans.thesis.GUI;

import org.frans.thesis.service.CFFile;
import org.frans.thesis.service.CFTabletopClient;
import org.frans.thesis.service.CFTabletopClientManager;
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

import processing.core.PImage;

public class CFMobileDeviceProxy extends CFComponent{

	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "android.png";
	private CFComponentMenu menu;
	private String clientName;
	
	public String getClientName() {
		return clientName;
	}

	private MTColor color;
	private CFTabletopClientManager tabletopClientManager;

	public CFMobileDeviceProxy(MTApplication mtApplication, String clientName,
			CFScene scene, CFTabletopClientManager tabletopClientManager, MTColor color) {
		super(mtApplication);
		this.color = color;
		this.clientName = clientName;
		this.tabletopClientManager = tabletopClientManager;
		setUpComponent(mtApplication);
		this.scaleImageToStackSize();
		setUpGestures(mtApplication);
		this.getMTComponent().setStrokeColor(this.getColor());
		createMenu();
	}
	
	protected MTColor getColor(){
		return this.color;
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
		this.menu.repositionMenuItemsInCircle();
	}

	private void downloadCalendar() {
		System.out.println("Download calendar");
	}

	private void downloadPdf() {
		System.out.println("Download pdf");
	}

	private void downloadPhotos() {
		this.getTabletopClientManager().setStatus(this.getClientName(), CFTabletopClient.REQUESTING_PHOTOS);
	}

	private CFComponentMenu getMenu() {
		return this.menu;
	}

	private CFTabletopClientManager getTabletopClientManager() {
		return this.tabletopClientManager;
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
		textField.setText(this.clientName);
		textField.setPickable(false);

		PImage pImage = getMTApplication().loadImage(imagePath);
		MTRectangle mtImage = new MTRectangle(getMTApplication(), pImage);
		mtImage.setNoStroke(true);
		mtImage.setPickable(false);

		float height = textField.getHeightXY(TransformSpace.GLOBAL);
		height += mtImage.getHeightXY(TransformSpace.GLOBAL);
		float width = Math.max(textField.getWidthXY(TransformSpace.GLOBAL),
				mtImage.getWidthXY(TransformSpace.GLOBAL));
//		this.component = new MTRectangle(getMTApplication(), width, height);
		this.component.setHeightLocal(height);
		this.component.setWidthLocal(width);

		this.getMTComponent().addChild(textField);
		this.getMTComponent().addChild(mtImage);
		mtImage.translate(new Vector3D(0, textField
				.getHeightXY(TransformSpace.GLOBAL), 0));
		this.getMTComponent().setFillColor(getColor());
	}

	private void setUpGestures(MTApplication mtApplication) {
//		this.getMTComponent().unregisterAllInputProcessors();
//		this.getMTComponent().removeAllGestureEventListeners();

//		this.getMTComponent().registerInputProcessor(
//				new DragProcessor(mtApplication));
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
	
	protected String getName(){
		return this.clientName;
	}
	

	protected boolean isMobileProxy() {
		return true;
	}

	protected void publishImageOnFacebook(CFFile cfFile) {
		this.getTabletopClientManager().publishImageOnFacebook(this.getClientName(), cfFile);
	}
}
