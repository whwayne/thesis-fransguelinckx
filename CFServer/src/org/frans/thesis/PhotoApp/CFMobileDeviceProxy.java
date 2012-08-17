package org.frans.thesis.PhotoApp;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFComponentMenu;
import org.frans.thesis.GUI.CFComponentMenuItemListener;
import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.musicapp.CFSpinner;
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

/**
 * This class takes care of the visual representation of a connected client and
 * a client-menu on the tabletop. It acts as some kind of proxy for the
 * connected client.
 */
public class CFMobileDeviceProxy extends CFComponent implements
		CFAutoRotatable, CFAutoScalable {

	/**
	 * A boolean to indicate whether the auto rotation functionality is on or off.
	 */
	private boolean autoRotate = true;
	
	/**
	 * A boolean to indicate whether the autoscale functionality is on or off.
	 */
	private boolean autoScale = true;
	
	
	/**
	 * The name of the client that is represented by this proxy.
	 */
	private String clientName;

	/**
	 * The color associated by this proxy.
	 */
	private MTColor color;

	/**
	 * The path to a facebook logo.
	 */
	private String fbImagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "facebook_logo.png";

	/**
	 * The path to an android logo.
	 */
	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "android.png";

	/**
	 * A spinner used to indicate when data is being transferred to the
	 * tabeltop.
	 */
	private CFSpinner spinner;
	
	/**
	 * The client manager that manages the client that is represented by this
	 * proxy.
	 */
	private CFTabletopClientManager tabletopClientManager;

	/**
	 * Public constructor for this class. It sets up the component, scales it to
	 * stack size, sets up the necessary gestures and creates the menu.
	 * 
	 * @param clientName
	 *            The name of the client represented by this proxy.
	 * @param scene
	 *            The scene to which this proxy belongs.
	 * @param tabletopClientManager
	 *            The clientmanager of the client represented by this proxy.
	 * @param color
	 *            The color associated with this proxy.
	 */
	public CFMobileDeviceProxy(String clientName, CFScene scene,
			CFTabletopClientManager tabletopClientManager, MTColor color) {
		super(scene);
		this.color = color;
		this.clientName = clientName;
		this.tabletopClientManager = tabletopClientManager;
		setUpComponent(scene.getMTApplication());
		this.scaleComponentToStackSize();
		setUpGestures(scene.getMTApplication());
		this.setStrokeColor(this.getColor());
		createMenu();
	}

	@Override
	public boolean autoRotateIsOn() {
		return this.autoRotate;
	}

	@Override
	public boolean autoScaleIsOn() {
		return this.autoScale;
	}

	/**
	 * Creates the menu of a proxy with three buttons.
	 */
	private void createMenu() {
		this.setComponentMenu(new CFComponentMenu(this));
		this.getComponentMenu().addMenuItem("photos.png",
				new CFComponentMenuItemListener() {

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
		this.getComponentMenu().addMenuItem("pdf.png",
				new CFComponentMenuItemListener() {

					@Override
					public void processEvent() {
						downloadPdf();
					}
				});
		this.getComponentMenu().repositionMenuItemsInCircle();
	}

	/**
	 * Dummy method that is called when a user taps the calendar icon.
	 */
	private void downloadCalendar() {
		System.out.println("Download calendar");
	}

	/**
	 * Dummy method that is called when a user taps the pdf icon.
	 */
	private void downloadPdf() {
		System.out.println("Download pdf");
	}

	/**
	 * Method that is called when a user taps the photos icon. It sets the status
	 * of the client to REQUESTING_MUSIC and starts the spinner.
	 */
	private void downloadPhotos() {
		this.getTabletopClientManager().setStatus(this.getClientName(),
				CFTabletopClient.REQUESTING_PHOTOS);
		this.startSpinner();
	}

	/**
	 * Returns the name of the client being represented by the proxy.
	 * 
	 * @return
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * Returns the color associated with this proxy.
	 */
	protected MTColor getColor() {
		return this.color;
	}

	/**
	 * Returns the client manager of the client that is being represented by
	 * this proxy.
	 */
	private CFTabletopClientManager getTabletopClientManager() {
		return this.tabletopClientManager;
	}

	/**
	 * This method is called when users drop an interactive component on the
	 * proxy. In this case it only reacts when an image or photoalbum is dropped on a proxy, to
	 * publish it on facebook.
	 */
	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		if (component instanceof CFImage) {
			CFImage image = (CFImage) component;
			this.publishImageOnFacebook(image.getFile());
			image.removeFromParent();
		} else if (component instanceof CFPhotoAlbum) {
			CFPhotoAlbum album = (CFPhotoAlbum) component;
			for (CFImage image : album.getImages()) {
				this.publishImageOnFacebook(image.getFile());
			}
			album.removeFromParent();
		}
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
	 * Published an image on facebook.
	 * 
	 * @param cfFile
	 *            The image that has to be published on fb.
	 */
	protected void publishImageOnFacebook(CFFile cfFile) {
		this.getTabletopClientManager().publishImageOnFacebook(
				this.getClientName(), cfFile);
	}

	/**
	 * Sets up the visual representation of the proxy.
	 * 
	 * @param mtApplication
	 *            The application to which this proxy belongs.
	 */
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
		MTRectangle mtImage = new MTRectangle(getCFScene().getMTApplication(),
				pImage);
		mtImage.setNoStroke(true);
		mtImage.setPickable(false);

		float height = textField.getHeightXY(TransformSpace.GLOBAL);
		height += mtImage.getHeightXY(TransformSpace.GLOBAL);
		float width = Math.max(textField.getWidthXY(TransformSpace.GLOBAL),
				mtImage.getWidthXY(TransformSpace.GLOBAL));
		// this.component = new MTRectangle(getMTApplication(), width, height);
		this.setHeightLocal(height);
		this.setWidthLocal(width);

		this.addChild(textField);
		this.addChild(mtImage);
		mtImage.translate(new Vector3D(0, textField
				.getHeightXY(TransformSpace.GLOBAL), 0));
		this.setFillColor(getColor());
		this.turnAutoScaleOff();

		PImage facebookImage = getCFScene().getMTApplication().loadImage(
				fbImagePath);
		MTRectangle facebookLogo = new MTRectangle(getCFScene()
				.getMTApplication(), facebookImage);
		facebookLogo.setNoStroke(true);
		facebookLogo.setPickable(false);
		this.addChild(facebookLogo);
		facebookLogo.setPositionRelativeToParent(new Vector3D(this
				.getWidthXY(TransformSpace.GLOBAL), this
				.getHeightXY(TransformSpace.GLOBAL)));
	}

	/**
	 * Sets up the gestures which this proxy has to handle.
	 * In this case a listener is added to handle taps so the menu is shown or hidden.
	 */
	private void setUpGestures(MTApplication mtApplication) {
		this.registerInputProcessor(new TapProcessor(getRenderer()));
		this.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent me) {
						TapEvent tapEvent = (TapEvent) me;
						if (tapEvent.isTapped()) {
							toggleVisibilityOfMenu();
						}
						return false;
					}
				});
	}

	/**
	 * Switches the visibility of the menu of this proxy.
	 */
	private void toggleVisibilityOfMenu() {
		if (this.getComponentMenu().isVisible()) {
			this.getComponentMenu().setVisible(false);
		} else {
			this.getComponentMenu().setVisible(true);
		}
	}

	/**
	 * Starts the spinner of this proxy.
	 */
	private void startSpinner() {
		this.spinner = new CFSpinner(this.getCFScene(), this);
		spinner.start();
	}

	/**
	 * Stops the spinner of this proxy.
	 */
	public void stopSpinner() {
		spinner.stop();
	}

	@Override
	public void turnAutoRotateOff() {
		this.autoRotate = false;
	}

	@Override
	public void turnAutoRotateOn() {
		this.autoRotate = true;
	}

	@Override
	public void turnAutoScaleOff() {
		this.autoScale = false;
	}

	@Override
	public void turnAutoScaleOn() {
		this.autoScale = true;
	}
}
