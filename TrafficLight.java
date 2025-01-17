
import java.io.File;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class TrafficLight extends Pane {

	private boolean isRed = false;
	private Circle circle;
	private final double radius = 5;
	private double x1, y1, x2, y2;
	private double width, height;

	public TrafficLight(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		
		width = Math.abs(x1-x2);
		height =Math.abs(y1-y2);
		
		Line line = new Line(0, 0, x2 - x1, y2 - y1);
		line.setStroke(Color.BLACK);
		circle = new Circle((x2 - x1) / 2, (y2 - y1) / 2, radius);
		circle.setFill(Color.GREEN);
		circle.setCursor(Cursor.HAND);
		this.getChildren().addAll(line, circle);
		this.setTranslateX(x1);
		this.setTranslateY(y1);
		//this.setBackground(Background.fill(Color.YELLOW));
		
		String audioFile = "sounds/Blip_select 1.wav";
		Media media = new Media(new File(audioFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setVolume(0.2);
		
		circle.setOnMousePressed(e -> {
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			changeColor();
		});
	}

	private void changeColor() {
		isRed = !isRed;
		if (isRed) {
			circle.setFill(Color.RED);
		} else {
			circle.setFill(Color.GREEN);
		}
	}

	public boolean isRed() {
		return isRed;
	}

	public Circle getCircle() {
		return circle;
	}

	public double getRadius() {
		return radius;
	}

	public double getX1() {
		return x1;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public double getY1() {
		return y1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}

	public double getX2() {
		return x2;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public double getY2() {
		return y2;
	}

	public void setY2(double y2) {
		this.y2 = y2;
	}

	public double getLineWidth() {
		return width;
	}
	
	public double getLineHeight() {
		return height;
	}
	
	public double getCenterX() {
		return this.getTranslateX() + (x2 - x1) / 2;
	}
	public double getCenterY() {
		return this.getTranslateY() + (y2 - y1) / 2;
	}
}
