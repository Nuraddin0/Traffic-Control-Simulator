import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.Timeline;
import javafx.geometry.Orientation;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Car extends Pane {
	private double xLength = 30;
	private double yLength = 10;
	private boolean stopped;
	private int pathNo;
	
	private Rectangle collider = new Rectangle(10,12); //Checking for stop


	private TrafficLight stoppedTraficLight;

	private Rectangle rect = new Rectangle(xLength, yLength);
	public PathTransition pt = new PathTransition();

	public Car(Path path, double pathLength, int pathNo) {
		this.getChildren().addAll(rect,collider);
		collider.setTranslateX(25);
		collider.setTranslateY(-1);
		collider.setFill(Color.YELLOW);
		collider.setOpacity(0.8);
		pt.setNode(this);
		pt.setPath(path);
		this.pathNo = pathNo;
		pt.setDuration(Duration.millis(pathLength * 15));
		pt.setInterpolator(Interpolator.LINEAR);
		pt.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				System.out.println(rect.getLocalToSceneTransform().getTx());
				
				//stop at traffic lights
				if (!stopped) {
					double frontX;
					double frontY;
					double middleX;
					double middleY;
					if (getRotate() < 45) {
						frontX = getTranslateX() + xLength;
						frontY = getTranslateY() + yLength / 2;
						middleX = getTranslateX() + xLength / 2;
						middleY = getTranslateY() + yLength / 2;
					} else if (getRotate() < 135) {
						frontX = getTranslateX() + xLength / 2;
						frontY = getTranslateY() + yLength * 2;
						middleX = getTranslateX() + xLength / 2;
						middleY = getTranslateY() + yLength / 2;
					} else if (getRotate() < 225) {
						frontX = getTranslateX();
						frontY = getTranslateY() + yLength / 2;
						middleX = getTranslateX() + xLength / 2;
						middleY = getTranslateY() + yLength / 2;
					} else if (getRotate() < 315) {
						frontX = getTranslateX() + xLength / 2;
						frontY = getTranslateY() - yLength;
						middleX = getTranslateX() + xLength / 2;
						middleY = getTranslateY() - yLength / 2;
					} else {
						frontX = getTranslateX() + xLength;
						frontY = getTranslateY() + yLength / 2;
						middleX = getTranslateX() + xLength / 2;
						middleY = getTranslateY() + yLength / 2;
					}

					for (int i = 0; i < Main.lights.size(); i++) {
						if (Main.lights.get(i).isRed()) {
							double frontDistance = Math.sqrt(Math.pow(frontX - Main.lights.get(i).getCenterX(), 2)
									+ Math.pow(frontY - Main.lights.get(i).getCenterY(), 2));
							double middleDistance = Math.sqrt(Math.pow(middleX - Main.lights.get(i).getCenterX(), 2)
									+ Math.pow(middleY - Main.lights.get(i).getCenterY(), 2));
							if (frontDistance <= 7 && frontDistance < middleDistance) {
								stoppedTraficLight = Main.lights.get(i);
								stopped = true;
								pt.pause();
							}
						}
					}
				}
				if(stopped) {
					if(stoppedTraficLight.isRed() == false) {
						pt.play();
					}
				}
				
				
				// stop at back of stopped cars
				double x = collider.getLocalToSceneTransform().getTx();
				double y = collider.getLocalToSceneTransform().getTy();
				double a = 10;
				double b = 12;
				
				double p1x = x;
				double p1y = y;
				
				double p2x = x + Math.cos(Math.toRadians(getRotate()))*a;
				double p2y = y + Math.sin(Math.toRadians(getRotate()))*a;
				
				double p3x = x + Math.cos(Math.toRadians(getRotate()))*a-Math.sin(Math.toRadians(getRotate()))*b;
				double p3y = y + Math.sin(Math.toRadians(getRotate()))*a + Math.cos(Math.toRadians(getRotate()))*b;
				
				double p4x = x - Math.sin(Math.toRadians(getRotate()))*b;
				double p4y = y + Math.cos(Math.toRadians(getRotate()))*b;
				
				for(int i = 0; i < Main.cars.size(); i++) {
					if(pathNo == Main.cars.get(i).getPathNo()) {
						//a
					}
				}
				if(!stopped) {
					
				}
				
			}
		};
		timer.start();

		pt.setOnFinished(e -> {
			Main.finishedCars++;
			Main.a.getChildren().remove(this);
			Main.cars.remove(this);
		});
		pt.play();
	}

	public boolean isStopped() {
		return stopped;
	}
	
	public double getxLength() {
		return xLength;
	}

	public void setxLength(double xLength) {
		this.xLength = xLength;
	}

	public double getyLength() {
		return yLength;
	}

	public void setyLength(double yLength) {
		this.yLength = yLength;
	}

	public Rectangle getRect() {
		return rect;
	}

	public PathTransition getPt() {
		return pt;
	}

	public int getPathNo() {
		return pathNo;
	}
}
