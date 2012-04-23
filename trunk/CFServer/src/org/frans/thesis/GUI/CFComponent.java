package org.frans.thesis.GUI;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateEvent;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

public abstract class CFComponent {

	private static final float STANDARD_MEASURE = 150;
	private static final int X_LOW_TRESHHOLD = 480;
	private static final int X_HIGH_TRESHHOLD = 1440;
	private static final int Y_LOW_TRESHHOLD = 270;
	private static final int Y_HIGH_TRESHHOLD = 810;
	private static final int X_WIDTH = 960;
	private static final int Y_HEIGHT = 540;

	protected MTRectangle component;
	protected MTApplication mtApplication;
	private int angle = 0;
	private CFScene scene;
	private boolean autoRotate = true;
	private boolean autoScale = true;

	public CFComponent(MTApplication mtApplication, CFScene scene) {
		this.scene = scene;
		this.mtApplication = mtApplication;
		this.component = new MTRectangle(mtApplication, 10, 10);
		setUpGestures(mtApplication);
	}

	private void setUpGestures(MTApplication mtApplication) {
		this.getMTComponent().unregisterAllInputProcessors();
		this.getMTComponent().removeAllGestureEventListeners();
		
		this.getMTComponent().registerInputProcessor(new DragProcessor(mtApplication));
		this.getMTComponent().addGestureListener(DragProcessor.class, new DefaultDragAction());
		this.getMTComponent().addGestureListener(DragProcessor.class,new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						DragEvent de = (DragEvent) ge;

						switch (de.getId()) {
						case MTGestureEvent.GESTURE_ENDED:
							break;
						case MTGestureEvent.GESTURE_UPDATED:

							Vector3D position = getMTComponent().getPosition(
									TransformSpace.GLOBAL);

							if (autoRotateIsOn()) {
								if (position.x < X_LOW_TRESHHOLD
										&& position.y < Y_LOW_TRESHHOLD) {
									// System.out.println("zone 1");
									rotateTo(135);
									scaleImageToStackSize();
								} else if (position.x > X_LOW_TRESHHOLD
										& position.x < X_HIGH_TRESHHOLD
										&& position.y < Y_LOW_TRESHHOLD) {
									// System.out.println("zone 2");
									rotateTo(180);
									scaleImageToStackSize();
								} else if (position.x > X_HIGH_TRESHHOLD
										&& position.y < Y_LOW_TRESHHOLD) {
									// System.out.println("zone 3");
									rotateTo(225);
									scaleImageToStackSize();
								} else if (position.x > X_HIGH_TRESHHOLD
										&& position.y > Y_LOW_TRESHHOLD
										&& position.y < Y_HIGH_TRESHHOLD) {
									// System.out.println("zone 4");
									rotateTo(270);
									scaleImageToStackSize();
								} else if (position.x > X_HIGH_TRESHHOLD
										&& position.y > Y_HIGH_TRESHHOLD) {
									// System.out.println("zone 5");
									rotateTo(315);
									scaleImageToStackSize();
								} else if (position.x > X_LOW_TRESHHOLD
										&& position.x < X_HIGH_TRESHHOLD
										&& position.y > Y_HIGH_TRESHHOLD) {
									// System.out.println("zone 6");
									rotateTo(0);
									scaleImageToStackSize();
								} else if (position.x < X_LOW_TRESHHOLD
										&& position.y > Y_HIGH_TRESHHOLD) {
									// System.out.println("zone 7");
									rotateTo(45);
									scaleImageToStackSize();
								} else if (position.x < X_LOW_TRESHHOLD
										&& position.y > Y_LOW_TRESHHOLD
										&& position.y < Y_HIGH_TRESHHOLD) {
									// System.out.println("zone 8");
									rotateTo(90);
									scaleImageToStackSize();
								}else if(autoScaleIsOn()){
									autoScale();
								}
							}

							break;
						default:
							break;

						}
						return false;
					}
				});
		this.getMTComponent().addGestureListener(DragProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				DragEvent de = (DragEvent) ge;
				switch (de.getId()) {
				case MTGestureEvent.GESTURE_ENDED:
					checkIfDroppedOnTrashCan();
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		this.getMTComponent().addGestureListener(DragProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				DragEvent de = (DragEvent) ge;
				switch (de.getId()) {
				case MTGestureEvent.GESTURE_ENDED:
					checkIfDroppedOnCopier();
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		this.getMTComponent().registerInputProcessor(new ScaleProcessor(mtApplication));
		this.getMTComponent().addGestureListener(ScaleProcessor.class,new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				ScaleEvent se = (ScaleEvent) ge;
				scale(se.getScaleFactorX(), se.getScaleFactorY(), se.getScaleFactorZ(), se.getScalingPoint());
				return false;
			}
		});
		this.getMTComponent().addGestureListener(ScaleProcessor.class,new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						turnAutoScaleOff();
						return false;
					}
				});

		this.getMTComponent().registerInputProcessor(new RotateProcessor(mtApplication));
		this.getMTComponent().addGestureListener(RotateProcessor.class,new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				RotateEvent re = (RotateEvent) ge;
				rotate(re.getRotationPoint(), (int) re.getRotationDegrees());
				return false;
			}
		});
		this.getMTComponent().addGestureListener(RotateProcessor.class,new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						turnAutoRotateOff();
						return false;
					}
				});

		this.getMTComponent().registerInputProcessor(new TapProcessor(mtApplication, 25, true, 350));
		this.getMTComponent().addGestureListener(TapProcessor.class,new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isDoubleTap()) {
							turnAutoRotateOn();
							turnAutoScaleOn();
						}
						return false;
					}
				});
	}
	
	private double getDistanceToCenter(){
		double result = 0;
		float x = Math.abs(this.getMTComponent().getPosition(TransformSpace.GLOBAL).getX() - (this.getMTApplication().getWidth()/2));
		float y = Math.abs(this.getMTComponent().getPosition(TransformSpace.GLOBAL).getY() - (this.getMTApplication().getHeight()/2));
		result = Math.sqrt((x*x)+(y*y));
		return result;
	}

	private void autoScale() {
//		double distance = getDistanceToCenter();
		float scaleX = X_WIDTH/this.getWidth();
		float scaleY = Y_HEIGHT/this.getHeight();
		float scalefactor = Math.min(scaleX, scaleY);
		this.scaleImage(scalefactor);
	}

	private boolean autoRotateIsOn() {
		return this.autoRotate;
	}

	private boolean autoScaleIsOn() {
		return this.autoScale;
	}

	private void turnAutoRotateOff() {
		this.autoRotate = false;
	}

	protected void turnAutoScaleOff() {
		this.autoScale = false;
	}

	private void turnAutoRotateOn() {
		this.autoRotate = true;
	}

	private void turnAutoScaleOn() {
		this.autoScale = true;
	}

	protected MTApplication getMTApplication() {
		return this.mtApplication;
	}

	protected float getDistanceto(CFComponent component) {
		float result = 0;
		float x, y;
		x = Math.abs(this.getMTComponent()
				.getPosition(TransformSpace.RELATIVE_TO_PARENT).getX()
				- component.getMTComponent()
						.getPosition(TransformSpace.RELATIVE_TO_PARENT).getX());
		y = Math.abs(this.getMTComponent()
				.getPosition(TransformSpace.RELATIVE_TO_PARENT).getY()
				- component.getMTComponent()
						.getPosition(TransformSpace.RELATIVE_TO_PARENT).getY());
		result = (float) Math.sqrt((x * x) + (y * y));
		return result;
	}

	protected float getHeight() {
		return this.getMTComponent().getHeightXY(TransformSpace.GLOBAL);
	}

	protected MTRectangle getMTComponent() {
		return this.component;
	}

	protected Vector3D getPosition() {
		return this.getMTComponent().getPosition(TransformSpace.GLOBAL);
	}

	protected float getWidth() {
		return this.getMTComponent().getWidthXY(TransformSpace.GLOBAL);
	}

	protected abstract boolean isStackable();

	protected boolean isPhotoAlbum() {
		return false;
	}

	protected boolean isTrashCan() {
		return false;
	}

	protected boolean isMobileProxy() {
		return false;
	}

	protected void reposition(Vector3D position) {
		this.getMTComponent().setPositionGlobal(position);
	}

	protected void rotate(Vector3D rotationPoint, int degrees) {
		this.angle += degrees;
		this.angle = this.angle % 360;

		Vector3D point = new Vector3D(rotationPoint.x + this.getWidth() / 2,
				rotationPoint.y + this.getHeight() / 2);
		this.getMTComponent().rotateZ(point, degrees);
	}
	
	protected void scale(float x, float y, float z, Vector3D scalingPoint){
		this.getMTComponent().scale(x, y, z, scalingPoint);
	}

	protected void rotateTo(int angle) {
		int result = (angle - this.angle) % 360;
		this.angle = angle;
		this.getMTComponent().rotateZ(
				this.getMTComponent().getCenterPointGlobal(), result);
	}

	protected void rotateRandomlyForStack() {
		this.getMTComponent().rotateZ(
				new Vector3D(this.getHeight() / 2, this.getWidth() / 2, 0),
				(float) (Math.random() * 360), TransformSpace.LOCAL);
	}

	protected void scaleImageToStackSize() {
		float scalingHeightFactor = CFComponent.STANDARD_MEASURE
				/ this.getHeight();
		float scalingWidthFactor = CFComponent.STANDARD_MEASURE
				/ this.getWidth();
		if (scalingHeightFactor < scalingWidthFactor) {
			this.scale(scalingWidthFactor, scalingWidthFactor,
					1, this.getMTComponent().getPosition(TransformSpace.GLOBAL));
		} else {
			this.scale(scalingHeightFactor,
					scalingHeightFactor, 1,
					this.getMTComponent().getPosition(TransformSpace.GLOBAL));
		}
	}

	protected void scaleImage(float factor) {
		this.scale(factor, factor, 1, this.getMTComponent().getPosition(TransformSpace.GLOBAL));
//		this.getMTComponent().scale(factor, factor, 1,
//				this.getMTComponent().getPosition(TransformSpace.GLOBAL));
	}

	protected int getAngle() {
		return this.angle;
	}

	protected CFScene getCFScene() {
		return this.scene;
	}
	
	private void checkIfDroppedOnTrashCan(){
		boolean hit = false;
		if (getCFScene().isCloseToCFComponent(this)) {
			for (CFComponent component : getCFScene().getNearCFComponents(this)) {
				if(component.isTrashCan()){
					hit = true;
				}
			}
		}
		if(hit){
			this.getMTComponent().removeFromParent();
		}
	}
	
	private void checkIfDroppedOnCopier(){
		boolean hit = false;
		if (getCFScene().isCloseToCFComponent(this)) {
			for (CFComponent component : getCFScene().getNearCFComponents(this)) {
				if(component.isCopier()){
					hit = true;
				}
			}
		}
		if(hit){
			try {
				this.getCFScene().addCFComponent((CFComponent)this.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected boolean isCopier() {
		return false;
	}
}
