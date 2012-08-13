package org.frans.thesis.PhotoApp;


import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFComponentMenu;
import org.frans.thesis.GUI.CFComponentMenuItemListener;
import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFFile;
import org.frans.thesis.service.CFTabletopClient;
import org.frans.thesis.service.CFTabletopClientManager;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class CFMobileDeviceProxy extends CFComponent implements AutoRotatable, AutoScalable{

	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "android.png";
	private String fbImagePath = "org" + MTApplication.separator + "frans"
					+ MTApplication.separator + "thesis" + MTApplication.separator
					+ "GUI" + MTApplication.separator + "data"
					+ MTApplication.separator + "facebook_logo.png";
	private String clientName;
	private CFSpinner spinner;
	private boolean autoScale = true;
	private boolean autoRotate = true;
	
	public String getClientName() {
		return clientName;
	}
	
	private void startSpinner(){
		this.spinner = new CFSpinner(this.getCFScene().getMTApplication(), this.getCFScene(), this);
		spinner.start();
	}

	private MTColor color;
	private CFTabletopClientManager tabletopClientManager;

	public CFMobileDeviceProxy(MTApplication mtApplication, String clientName,
			CFScene scene, CFTabletopClientManager tabletopClientManager, MTColor color) {
		super(mtApplication, scene);
		this.color = color;
		this.clientName = clientName;
		this.tabletopClientManager = tabletopClientManager;
		setUpComponent(mtApplication);
		this.scaleComponentToStackSize();
		setUpGestures(mtApplication);
		this.setStrokeColor(this.getColor());
		createMenu();
	}
	
	protected MTColor getColor(){
		return this.color;
	}

	private void createMenu() {
		this.setComponentMenu(new CFComponentMenu(this, this.getCFScene().getMTApplication()));
		this.getComponentMenu().addMenuItem("photos.png", new CFComponentMenuItemListener() {

			@Override
			public void processEvent() {
				downloadPhotos();
			}
		});
		this.getComponentMenu().addMenuItem("calendar.png",
				new CFComponentMenuItemListener() {

					@Override
					public void processEvent() {
						downloadCalendar();
					}
				});
		this.getComponentMenu().addMenuItem("pdf.png", new CFComponentMenuItemListener() {

			@Override
			public void processEvent() {
				downloadPdf();
			}
		});
		this.getComponentMenu().repositionMenuItemsInCircle();
	}

	private void downloadCalendar() {
		System.out.println("Download calendar");
	}

	private void downloadPdf() {
		System.out.println("Download pdf");
	}

	private void downloadPhotos() {
		this.getTabletopClientManager().setStatus(this.getClientName(), CFTabletopClient.REQUESTING_PHOTOS);
		this.startSpinner();
	}

	private CFTabletopClientManager getTabletopClientManager() {
		return this.tabletopClientManager;
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

		PImage pImage = getCFScene().getMTApplication().loadImage(imagePath);
		MTRectangle mtImage = new MTRectangle(getCFScene().getMTApplication(), pImage);
		mtImage.setNoStroke(true);
		mtImage.setPickable(false);

		float height = textField.getHeightXY(TransformSpace.GLOBAL);
		height += mtImage.getHeightXY(TransformSpace.GLOBAL);
		float width = Math.max(textField.getWidthXY(TransformSpace.GLOBAL),
				mtImage.getWidthXY(TransformSpace.GLOBAL));
//		this.component = new MTRectangle(getMTApplication(), width, height);
		this.setHeightLocal(height);
		this.setWidthLocal(width);

		this.addChild(textField);
		this.addChild(mtImage);
		mtImage.translate(new Vector3D(0, textField
				.getHeightXY(TransformSpace.GLOBAL), 0));
		this.setFillColor(getColor());
		this.turnAutoScaleOff();
		
		PImage facebookImage = getCFScene().getMTApplication().loadImage(fbImagePath);
		MTRectangle facebookLogo = new MTRectangle(getCFScene().getMTApplication(), facebookImage);
		facebookLogo.setNoStroke(true);
		facebookLogo.setPickable(false);
		this.addChild(facebookLogo);
		facebookLogo.setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.GLOBAL), this.getHeightXY(TransformSpace.GLOBAL)));
	}

	private void setUpGestures(MTApplication mtApplication) {
		this.addGestureListener(TapProcessor.class,
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
		if (this.getComponentMenu().isVisible()) {
			this.getComponentMenu().setVisible(false);
		} else {
			this.getComponentMenu().setVisible(true);
		}
	}
	

//	protected boolean isMobileProxy() {
//		return true;
//	}

	protected void publishImageOnFacebook(CFFile cfFile) {
		this.getTabletopClientManager().publishImageOnFacebook(this.getClientName(), cfFile);
	}

	public void stopSpinner() {
		spinner.stop();
	}

	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		if(component instanceof CFImage){
			CFImage image = (CFImage) component;
			this.publishImageOnFacebook(image.getFile());
			image.removeFromParent();
		}else if(component instanceof CFPhotoAlbum){
			CFPhotoAlbum album = (CFPhotoAlbum) component;
			for(CFImage image : album.getImages()){
				this.publishImageOnFacebook(image.getFile());
			}
			album.removeFromParent();
		}
	}

	@Override
	public void handleScaledCFComponent(CFComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRotatedCFComponent(CFComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean autoScaleIsOn() {
		return this.autoScale;
	}

	@Override
	public void turnAutoScaleOff() {
		this.autoScale = false;
	}

	@Override
	public void turnAutoScaleOn() {
		this.autoScale = true;
	}

	@Override
	public boolean autoRotateIsOn() {
		return this.autoRotate;
	}

	@Override
	public void turnAutoRotateOff() {
		this.autoRotate = false;
	}

	@Override
	public void turnAutoRotateOn() {
		this.autoRotate = true;
	}
}
