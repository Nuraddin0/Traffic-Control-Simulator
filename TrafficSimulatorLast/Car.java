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
	public static int crashCounter;
	private double xLength = 30;
	private double yLength = 10;
	private boolean stopped;
	private int pathNo;
	
	private boolean firstControll; //it deletes de car if there is another car at spawn point
	private int firstControllCounter;
	
	private Rectangle collider = new Rectangle(10,12); //Checking for stop


	private TrafficLight stoppedTraficLight;
	private Car stoppedCar;
	

	private Rectangle rect = new Rectangle(xLength, yLength);
	public PathTransition pt = new PathTransition();
	
	private Car thisCar;

	public Car(Path path, double pathLength, int pathNo) {
		thisCar = this;
		this.getChildren().addAll(rect,collider);
		collider.setTranslateX(25);
		collider.setTranslateY(-1);
		collider.setFill(null);
		//collider.setOpacity(0.8);
		pt.setNode(this);
		pt.setPath(path);

		this.pathNo = pathNo;
		pt.setDuration(Duration.millis(pathLength * 15));
		pt.setInterpolator(Interpolator.LINEAR);
		pt.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
		
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
			//	System.out.println(rect.getLocalToSceneTransform().getTx());
				//stop at traffic lights
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
							//System.out.printf("p1x:%f - p1y:%f - p2x:%f - p2y:%f - p3x:%f - p3y:%f - p4x:%f - p4y:%f *-* cx:%f - cy:%f\n", p1x,p1y,p2x,p2y,p3x,p3y,p4x,p4y,Main.cars.get(i).getRect().getLocalToSceneTransform().getTx(),Main.cars.get(i).getRect().getLocalToSceneTransform().getTy());
							if(Main.cars.get(i).stopped) {
								stoppedCar = Main.cars.get(i);
								stopped = true;
								pt.pause();
								if(!firstControll) {// trafik sıkışıksa araç spawn etme
									Main.cars.remove(thisCar);
									Main.a.getChildren().remove(thisCar);
								}
								break; // kendi pathinde olmayan araç spawn edildiğinde hareket etmesin diye direkt olarak for'dan çık ve diğer araçları kontrol etme 
							}
						}
						else if(stopped){
							if(stoppedTraficLight == null) {
								System.out.println("A");
								stopped = false;
								pt.play();
							}
						}
					}

				}
			  
				for(int i = 0 ; i < Main.cars.size(); i++) {
					Car dd  = Main.cars.get(i);
					if(dd!= thisCar && Main.cars.contains(thisCar)) {							
					  boolean intersects = thisCar.localToScene(thisCar.getRect().getBoundsInLocal()).intersects(dd.localToScene(dd.getRect().getBoundsInLocal()));
					  if(intersects) {
						     crashCounter++;
					         thisCar.pt.stop();
					         dd.pt.stop();
				             Main.a.getChildren().remove(thisCar);
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
				
				
			}
		};
		timer.start();

		pt.setOnFinished(e -> {
			Main.finishedCars++;
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
