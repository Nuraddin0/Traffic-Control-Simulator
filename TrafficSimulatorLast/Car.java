
import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.Timeline;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Car extends Pane {
	public static int crashCounter;
	private double xLength = 14;
	private double yLength = 7;
	private boolean stopped;
	private int pathNo;

	private boolean firstControll; // it deletes de car if there is another car at spawn point
	private int firstControllCounter;

	private Rectangle collider = new Rectangle(5, 7); // Checking for stop

	private TrafficLight stoppedTraficLight; // The light that caused it to stop
	private Car stoppedCar; // The vehicle that caused it to stop

	private Rectangle rect = new Rectangle(xLength, yLength); // main rectangle ( car body ) 
	public PathTransition pt = new PathTransition();

	private Car thisCar; // We found such a solution because animation is detected when we call the tool as "this" in animation.

	public Car(Path path, double pathLength, int pathNo) {
		String audioFile = "sounds/car-crash-collision.mp3";
		Media media = new Media(new File(audioFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setVolume(0.3);
		
		thisCar = this;
		this.getChildren().addAll(rect,collider);
		rect.setArcHeight(2);
		rect.setArcWidth(2);
		rect.setOpacity(0);
		collider.setTranslateX(15);
		collider.setOpacity(0);
		
		//Getting a random car color
		switch((int)(Math.random()*8)) {
		case 0:
			rect.setFill(Color.DARKGREEN);
			break;
		case 1:
			rect.setFill(Color.BLUE);
			break;
		case 2:
			rect.setFill(Color.DARKRED);
			break;
		case 3:
			rect.setFill(Color.PURPLE);
			break;
		case 4:
			rect.setFill(Color.LIGHTSKYBLUE);
			break;
		case 5:
			rect.setFill(Color.MAGENTA);
			break;
		case 6:
			rect.setFill(Color.CYAN);
			break;
		case 7:
			rect.setFill(Color.DARKORANGE);
			break;
		}
		
		
		pt.setNode(this); // Assign Path transaction object as vehicle
		pt.setPath(path);
		
		this.pathNo = pathNo;
		pt.setDuration(Duration.millis(pathLength * 15));
		pt.setInterpolator(Interpolator.LINEAR);
		pt.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
		
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (!stopped) { // aracın ışıktan kaynaklı durma olayı
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
					if(stoppedTraficLight != null) { // ışık artık yeşilse devam et
						if(stoppedTraficLight.isRed() == false) {
							stoppedTraficLight = null;
							stopped = false;
							pt.play();
						}
					}

				}
				
				// stop at back of stopped cars
				double x = collider.getLocalToSceneTransform().getTx();
				double y = collider.getLocalToSceneTransform().getTy();
				double a = 10;
				double b = 12;
				
				int p1x = (int)x;
				int p1y = (int)y;
				
				int p2x = (int)(x + Math.cos(Math.toRadians(getRotate()))*a);
				int p2y = (int)(y + Math.sin(Math.toRadians(getRotate()))*a);
				
				int p3x = (int)(x + Math.cos(Math.toRadians(getRotate()))*a-Math.sin(Math.toRadians(getRotate()))*b);
				int p3y = (int)(y + Math.sin(Math.toRadians(getRotate()))*a + Math.cos(Math.toRadians(getRotate()))*b);
				
				int p4x = (int)(x - Math.sin(Math.toRadians(getRotate()))*b);
				int p4y = (int)(y + Math.cos(Math.toRadians(getRotate()))*b);
				
				for(int i = 0; i < Main.cars.size(); i++) {
					if(Main.cars.get(i) != thisCar /* && pathNo == Main.cars.get(i).getPathNo()*/) {
						
						if(Math.min(p1x, Math.min(p2x, Math.min(p3x, p4x))) - 3 < Main.cars.get(i).getRect().getLocalToSceneTransform().getTx()
								 && Math.max(p1x, Math.max(p2x, Math.max(p3x, p4x))) + 3 > Main.cars.get(i).getRect().getLocalToSceneTransform().getTx()
								 && Math.min(p1y, Math.min(p2y, Math.min(p3y, p4y))) - 3 < Main.cars.get(i).getRect().getLocalToSceneTransform().getTy()
								 && Math.max(p1y, Math.max(p2y, Math.max(p3y, p4y))) + 3 > Main.cars.get(i).getRect().getLocalToSceneTransform().getTy()) {					
							if(Main.cars.get(i).stopped) {
								stoppedCar = Main.cars.get(i);
								stopped = true;
								pt.pause();
								if(!firstControll) {// trafik sıkışıksa araç spawn etme
									Main.cars.remove(thisCar);
									Main.a.getChildren().remove(thisCar);
								}
								break; // When a vehicle that is not on its own path is spawned, exit the for directly and do not control other vehicles so that it does not move.
							}
						}
						else if(stopped){
							if(stoppedTraficLight == null) {
								stopped = false;
								pt.play();
							}
						}
					}

				}
			  
				for(int i = 0 ; i < Main.cars.size(); i++) {
					Car dd  = Main.cars.get(i);
					if(dd!= thisCar && Main.cars.contains(thisCar)) { // If our vehicle is not equal to itself at the moment and has been involved in an accident before and is not deleted from the array list and still continues to exist							
						boolean intersects = thisCar.localToScene(thisCar.getRect().getBoundsInLocal()).intersects(dd.localToScene(dd.getRect().getBoundsInLocal()));
						// If the location of our vehicle's rectangle's local area relative to the scene intersects with the location of the local area of ​​the other vehicle's rectangle relative to the scene
					  	if(intersects && firstControll && dd.firstControll) { // If there is an intersection and it is not due to vehicle congestion
							mediaPlayer.seek(Duration.seconds(0));
							mediaPlayer.play();
							
					  		System.out.println("çarpışma oldu ");
						     	crashCounter++; // increase the number of accidents
						     	Main.crashText.setText(String.format("Crashes: %d/%d", Car.crashCounter,Main.meta.getAllowedAccident())); // update crashed text
						     	Main.controllLose(Main.meta, Main.currentLevelName, Main.animation,Main.primaryStage); // check losing condition
					         	thisCar.pt.stop(); // animation stop
					         	dd.pt.stop(); 
				             		Main.a.getChildren().remove(thisCar); // remove the cars from pane
					         	Main.a.getChildren().remove( dd);
						     	Main.cars.remove( dd);
						     	Main.cars.remove(thisCar);
					
					    }
					}
				}
				
				if(!firstControll) { // trafik sıkışıksa araç spawn etme
					firstControllCounter++;
					if(firstControllCounter >= 10) {
						firstControll = true;
					}
				}
				if(!firstControll) { // trafik sıkışıksa araç spawn etme
					if(firstControllCounter >= 3) {
						rect.setOpacity(1);
					}
				}
			}
		};
		timer.start();

		pt.setOnFinished(e -> {
			Main.finishedCars++;
			Main.scoreText.setText(String.format("Score: %d/%d", Main.finishedCars,Main.meta.getWinCondition()));
			Main.controlWin(Main.meta, Main.currentLevelName, Main.animation,Main.primaryStage);
			Main.a.getChildren().remove(this);
			Main.cars.remove(this);
		});
		pt.play();// araç dümdüz gitsin
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
