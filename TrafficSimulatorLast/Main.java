
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

// 150122025	Sabri Yıldız
// 150123992	Nuraddin Abbasov
// 150122071	Mehmet Burak İşgören
public class Main extends Application {

	public static Path[] paths = new Path[0]; // Array that keeps the paths on the map and which we always update
	public static ArrayList<TrafficLight> lights = new ArrayList<>();
	public static ArrayList<Car> cars = new ArrayList<Car>();

	public static ArrayList<RoadTile> roadtiles = new ArrayList<>();
	public static ArrayList<Building> buildings = new ArrayList<>();

	public static double[] pathsLength;
	public static int finishedCars; // count of car which reached finished
	public static SuperPane a;

	public static Timeline animation;
	public static MetaData meta = null;
	public static String currentLevelName;
	public static Stage primaryStage;
	public static Label scoreText;
	public static Label crashText;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		String audioFile = "sounds/MainMusic.mp3";
		Media media = new Media(new File(audioFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);

		mediaPlayer.setVolume(0.5);

		mediaPlayer.play();
		mediaPlayer.setOnEndOfMedia(() -> {
			System.out.println("End of media reached");
			mediaPlayer.seek(Duration.ZERO);
		});

		primaryStage.setOnCloseRequest(event -> {
			mediaPlayer.stop();
		});

		openMainMenu(primaryStage);

		Image icon = new Image("images/icon_.png");

		primaryStage.setTitle("Traffic Control Simulator");
		primaryStage.getIcons().add(icon);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	public static void openMainMenu(Stage primaryStage) {
		
		Pane mainMenu = new Pane();
		ImageView bg = new ImageView(new Image("images/anaMenu.jpg")); // bg is background image
		bg.setFitHeight(mainMenu.getHeight());
		bg.setFitWidth(mainMenu.getWidth());

		ImageView header = new ImageView(new Image("images/tcs.png"));
		header.setX(100);
		
		ImageView startGame = new ImageView(new Image("images/Start-Game.png"));
		ImageView exitGame = new ImageView(new Image("images/Exit.png"));

		startGame.setFitHeight(45 * 1.5);
		startGame.setFitWidth(160 * 1.5);
		exitGame.setFitHeight(35 * 1.5);
		exitGame.setFitWidth(61 * 1.5);
		startGame.setX(400 - startGame.getFitWidth() / 2);
		startGame.setY(400 - startGame.getFitHeight() / 2);
		exitGame.setX(400 - exitGame.getFitWidth() / 2);
		exitGame.setY(startGame.getY() + startGame.getFitHeight() + 15);

		FadeTransition fadeIn1 = new FadeTransition(Duration.seconds(0.5), startGame);
		FadeTransition fadeIn2 = new FadeTransition(Duration.seconds(0.5), exitGame);
		FadeTransition fadeIn3 = new FadeTransition(Duration.seconds(0.5), header);

		fadeIn1.setFromValue(0.0);
		fadeIn1.setToValue(1.0);
		fadeIn2.setFromValue(0.0);
		fadeIn2.setToValue(1.0);
		fadeIn3.setFromValue(0.0);
		fadeIn3.setToValue(1.0);

		fadeIn1.play();
		fadeIn2.play();
		fadeIn3.play();

		mainMenu.getChildren().add(bg);
		mainMenu.getChildren().add(header);
		mainMenu.getChildren().add(startGame);
		mainMenu.getChildren().add(exitGame);

		Scene sceneMainMenu = new Scene(mainMenu, 800, 800);

		primaryStage.setScene(sceneMainMenu);

		startGame.setCursor(Cursor.HAND);
		exitGame.setCursor(Cursor.HAND);

		String audioFile = "sounds/button-click-sound.mp3";
		Media media = new Media(new File(audioFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setVolume(0.5);

		exitGame.setOnMouseClicked(e -> { // If the player wants to exit the main menu

			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();

			Rectangle rec = new Rectangle();
			rec.setArcWidth(25); // Roundness along the x-axis
			rec.setArcHeight(25);// Roundness along the y-axis
			rec.setFill(Color.DARKORANGE);
			rec.setWidth(300);
			rec.setHeight(100);
			rec.setX(250);
			rec.setY(350);

			Label text = new Label();
			text.setText("Are you really sure you want to exit?");
			text.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
			text.setLayoutX(275);
			text.setLayoutY(355);

			Button btYes = new Button("Yes");
			btYes.setStyle("-fx-background-color: darkorange; -fx-font-size: 15px");
			btYes.setLayoutX(330);
			btYes.setLayoutY(420);

			Button btNo = new Button("No");
			btNo.setStyle("-fx-background-color: darkorange; -fx-font-size: 15px");
			btNo.setLayoutX(450);
			btNo.setLayoutY(420);

			mainMenu.getChildren().addAll(rec, text, btYes, btNo);

			btYes.setOnMouseEntered(event -> { // when the mouse comes over the button
												// The button is run away so that the user cannot press it.
				Random rand = new Random();
				int randX = rand.nextInt(50, 750);
				int randY = rand.nextInt(50, 750); 
				btYes.setLayoutX(randX);
				btYes.setLayoutY(randY);

			});
			btYes.setOnMouseClicked(event -> {

				mediaPlayer.seek(Duration.seconds(0));
				mediaPlayer.play();

				System.exit(0);
			});
			btNo.setOnMouseClicked(event -> {

				mediaPlayer.seek(Duration.seconds(0));
				mediaPlayer.play();

				mainMenu.getChildren().removeAll(rec, text, btYes, btNo); // If the user does not want to quit the game
			});
		});

		startGame.setOnMouseClicked(e -> { // If the user wants to switch to the game

			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();

			FadeTransition fadeOut1 = new FadeTransition(Duration.seconds(0.5), startGame);
			FadeTransition fadeOut2 = new FadeTransition(Duration.seconds(0.5), exitGame);
			FadeTransition fadeOut3 = new FadeTransition(Duration.seconds(0.5), header);

			fadeOut1.setFromValue(1.0);
			fadeOut1.setToValue(0.0);
			fadeOut2.setFromValue(1.0);
			fadeOut2.setToValue(0.0);
			fadeOut3.setFromValue(1.0);
			fadeOut3.setToValue(0.0);

			fadeOut1.play();
			fadeOut2.play();
			fadeOut3.play();

			fadeOut3.setOnFinished(event -> {
				levelSelect(primaryStage); // Go to level selection menu
			});

		});
	}

	public static Scene openLevel(String levelName, Stage primaryStage) throws FileNotFoundException {
//This method opens and reads the appropriate txt file according to the level name parameter it receives, creates the necessary objects and displays them on the screen.
//This is the exact method by which the map is opened.
		ImageView back = new ImageView(new Image("images/back.png"));
		back.setCursor(Cursor.HAND);
		back.setX(0);
		back.setY(800 - 51);

		File file = new File(levelName);
		Scanner scan = new Scanner(file);

		double previousPathX = 0;
		double previousPathY = 0;

		while (scan.hasNext()) { // keep reading txt file until you see empty line
			String tempString = scan.nextLine();
			String[] words = tempString.split(" "); // Separate the words in the line you received according to the
													// spaces
			String className = words[0]; // first word is class name
			if (className.equals("Metadata")) {
				Double width = Double.parseDouble(words[1]);
				Double height = Double.parseDouble(words[2]);

				int numberOfXCell = Integer.parseInt(words[3]);
				int numberOfYCell = Integer.parseInt(words[4]);
				int numberOfPaths = Integer.parseInt(words[5]);
				paths = new Path[numberOfPaths]; // paths array that contains all paths in that level
				pathsLength = new double[numberOfPaths]; // this contains the length of each path
				int winCondition = Integer.parseInt(words[6]);
				int allowedAccident = Integer.parseInt(words[7]);

				meta = new MetaData(width, height, numberOfXCell, numberOfYCell, numberOfPaths, winCondition,
						allowedAccident);
			} else if (className.equals("RoadTile")) {
				int type = Integer.parseInt(words[1]);
				int rotate = Integer.parseInt(words[2]);
				int gridx = Integer.parseInt(words[3]);
				int gridy = Integer.parseInt(words[4]);

				RoadTile road = new RoadTile(type, rotate, gridx, gridy);
				roadtiles.add(road);
			} else if (className.equals("Building")) {
				int type = Integer.parseInt(words[1]);
				int rotation = Integer.parseInt(words[2]);
				int colorIndex = Integer.parseInt(words[3]);
				int gridx = Integer.parseInt(words[4]);
				int gridy = Integer.parseInt(words[5]);

				Building building = new Building(type, rotation, colorIndex, gridx, gridy);

				buildings.add(building);
			} else if (className.equals("TrafficLight")) {
				Double x1 = Double.parseDouble(words[1]);
				Double y1 = Double.parseDouble(words[2]);
				Double x2 = Double.parseDouble(words[3]);
				Double y2 = Double.parseDouble(words[4]);

				TrafficLight light = new TrafficLight(x1, y1, x2, y2);

				lights.add(light);
			} else if (className.equals("Path")) {
				int index = Integer.parseInt(words[1]);
				double pathX = Double.parseDouble(words[3]);
				double pathY = Double.parseDouble(words[4]);
				if (words[2].equals("MoveTo")) {
					paths[index] = new Path();
					paths[index].setVisible(false);
					paths[index].getElements().add(new MoveTo(pathX, pathY));
					previousPathX = pathX;
					previousPathY = pathY;
				}
				if (words[2].equals("LineTo")) {
					paths[index].getElements().add(new LineTo(pathX, pathY));
					pathsLength[index] += Math
							.sqrt(Math.pow(pathY - previousPathY, 2) + Math.pow(pathX - previousPathX, 2));
					// paths.Length[ 0 ] = length of the first path within the same map
					previousPathX = pathX;
					previousPathY = pathY;
				}

			}
		}

		a = new SuperPane(meta.getSceneWidth(), meta.getSceneHeight(), meta.getNumberOfXCell(),
				meta.getNumberOfYCell());

		scoreText = new Label();
		scoreText.setText(String.format("Score: %d/%d", finishedCars, meta.getWinCondition())); // Presents the number
																								// of finished cars to
																								// the user instantly
		scoreText.setTranslateX(10);
		scoreText.setTranslateY(10);
		scoreText.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
		a.getChildren().add(scoreText);

		crashText = new Label();
		crashText.setText(String.format("Crashes: %d/%d", Car.crashCounter, meta.getAllowedAccident()));// Presents the
																										// number of
																										// accidents to
																										// the user
																										// instantly
		crashText.setTranslateX(10);
		crashText.setTranslateY(25);
		crashText.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
		a.getChildren().add(crashText);
		// add all created objects to pane
		for (int i = 0; i < roadtiles.size(); i++) {
			roadtiles.get(i).draw(meta);
			a.add(roadtiles.get(i), roadtiles.get(i).getGridx(), roadtiles.get(i).getGridy());
		}
		for (int i = 0; i < buildings.size(); i++) {
			buildings.get(i).draw(meta);
			a.add(buildings.get(i), buildings.get(i).getGridX(), buildings.get(i).getGridY());
		}
		for (int i = 0; i < lights.size(); i++) {
			a.getChildren().add(lights.get(i));
		}
		for (int i = 0; i < paths.length; i++) {
			paths[i].setStroke(Color.RED);
			a.getChildren().add(paths[i]);
		}
		a.getChildren().add(back); // add back button

		Scene scene = new Scene(a, meta.getSceneWidth(), meta.getSceneHeight());
		int pathCount = paths.length;
		MetaData meta1 = meta;

		animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> { // Create a vehicle at 1 second intervals
																			// and if the created vehicle is not null,
																			// add the vehicle to the pane and add it to
																			// the arraylist.
			Car newCar = spawnCar(pathCount);
			if (newCar != null) {
				a.getChildren().add(newCar);
				cars.add(newCar);
			}
		}));

		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
		String audioFile = "sounds/button-click-sound.mp3";
		Media media = new Media(new File(audioFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setVolume(0.5);
		back.setOnMouseClicked(e -> { // If the user wants to go back to the level selection menu while on a level,
										// reset everything, stop the animation and redirect to the level selection
										// menu.
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			finishedCars = 0;
			Car.crashCounter = 0;
			animation.stop();
			finito(roadtiles, buildings);
			levelSelect(primaryStage);
		});
		scan.close();
		return scene;

	}

	public static Car spawnCar(int pathCount) {
		if (Math.random() < 1) { // Choose a random path from the level and create a car there.
			int randomPath = ((int) (Math.random() * pathCount));
			return new Car(paths[randomPath], pathsLength[randomPath], randomPath);
		}
		return null;
	}

	public static void levelSelect(Stage primaryStage) {

		Pane levelPane = new Pane();
		Scene levelMenu = new Scene(levelPane, 800, 800);

		ImageView bg = new ImageView(new Image("images/anaMenu.jpg")); // bg is background image
		bg.setFitHeight(levelMenu.getHeight());
		bg.setFitWidth(levelMenu.getWidth());

		ImageView header = new ImageView(new Image("images/Select-Level.png"));
		header.setX(200);
		header.setY(100);

		ImageView back = new ImageView(new Image("images/back.png"));

		back.setX(0);
		back.setY(800 - 51);

		ImageView level1 = new ImageView(new Image("images/Level-1.png"));
		ImageView level2 = new ImageView(new Image("images/Level-2.png"));
		ImageView level3 = new ImageView(new Image("images/Level-3.png"));
		ImageView level4 = new ImageView(new Image("images/Level-4.png"));
		ImageView level5 = new ImageView(new Image("images/Level-5.png"));

		level1.setX(50);
		level1.setY(300);

		level2.setX(300);
		level2.setY(300);

		level3.setX(550);
		level3.setY(300);

		level4.setX(175);
		level4.setY(500);

		level5.setX(425);
		level5.setY(500);

		Rectangle btLevel1 = new Rectangle(level1.getX() + 21, level1.getY() + 17, 158, 110);
		btLevel1.setFill(Color.TRANSPARENT);
		btLevel1.setRotate(5);

		Rectangle btLevel2 = new Rectangle(level2.getX() + 22, level2.getY() + 16, 156, 108);
		btLevel2.setFill(Color.TRANSPARENT);
		btLevel2.setRotate(5);

		Rectangle btLevel3 = new Rectangle(level3.getX() + 22, level3.getY() + 16, 156, 108);
		btLevel3.setFill(Color.TRANSPARENT);
		btLevel3.setRotate(5);

		Rectangle btLevel4 = new Rectangle(level4.getX() + 22, level4.getY() + 16, 156, 108);
		btLevel4.setFill(Color.TRANSPARENT);
		btLevel4.setRotate(5);

		Rectangle btLevel5 = new Rectangle(level5.getX() + 22, level5.getY() + 16, 156, 108);
		btLevel5.setFill(Color.TRANSPARENT);
		btLevel5.setRotate(5);

		back.setCursor(Cursor.HAND);
		level1.setCursor(Cursor.HAND);
		level2.setCursor(Cursor.HAND);
		level3.setCursor(Cursor.HAND);
		level4.setCursor(Cursor.HAND);
		level5.setCursor(Cursor.HAND);
		btLevel1.setCursor(Cursor.HAND);
		btLevel2.setCursor(Cursor.HAND);
		btLevel3.setCursor(Cursor.HAND);
		btLevel4.setCursor(Cursor.HAND);
		btLevel5.setCursor(Cursor.HAND);

		String audioFile = "sounds/button-click-sound.mp3";
		Media media = new Media(new File(audioFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setVolume(0.5);

		back.setOnMouseClicked(e -> { // If the user wants to return to the starting menu from the level menu

			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();

			FadeTransition fadeOut1 = new FadeTransition(Duration.seconds(0.5), back);
			FadeTransition fadeOut2 = new FadeTransition(Duration.seconds(0.5), header);
			FadeTransition fadeOut3 = new FadeTransition(Duration.seconds(0.5), level1);
			FadeTransition fadeOut4 = new FadeTransition(Duration.seconds(0.5), level2);
			FadeTransition fadeOut5 = new FadeTransition(Duration.seconds(0.5), level3);
			FadeTransition fadeOut6 = new FadeTransition(Duration.seconds(0.5), level4);
			FadeTransition fadeOut7 = new FadeTransition(Duration.seconds(0.5), level5);

			fadeOut1.setFromValue(1.0);
			fadeOut1.setToValue(0.0);
			fadeOut2.setFromValue(1.0);
			fadeOut2.setToValue(0.0);
			fadeOut3.setFromValue(1.0);
			fadeOut3.setToValue(0.0);
			fadeOut4.setFromValue(1.0);
			fadeOut4.setToValue(0.0);
			fadeOut5.setFromValue(1.0);
			fadeOut5.setToValue(0.0);
			fadeOut6.setFromValue(1.0);
			fadeOut6.setToValue(0.0);
			fadeOut7.setFromValue(1.0);
			fadeOut7.setToValue(0.0);

			fadeOut1.play();
			fadeOut2.play();
			fadeOut3.play();
			fadeOut4.play();
			fadeOut5.play();
			fadeOut6.play();
			fadeOut7.play();

			fadeOut7.setOnFinished(event -> {
				openMainMenu(primaryStage);
			});

		});
		// We will set the click states for both the button and the image.
		/*
		 * Even if there were no buttons, the transparent frame would not detect a press
		 * when pressed. Buttons have also been added to detect clicks when pressing the
		 * entire frame
		 */
		btLevel1.setOnMouseClicked(e -> { // If the user wants to play Level1
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();

			Scene level = null;
			try {
				level = openLevel("levels/level1.txt", primaryStage);
				currentLevelName = "levels/level1.txt";
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});

		btLevel2.setOnMouseClicked(e -> {// If the user wants to play Level2
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();

			Scene level = null;
			try {
				level = openLevel("levels/level2.txt", primaryStage);
				currentLevelName = "levels/level2.txt";
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});

		btLevel3.setOnMouseClicked(e -> {// If the user wants to play Level3
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();

			Scene level = null;
			try {
				level = openLevel("levels/level3.txt", primaryStage);
				currentLevelName = "levels/level3.txt";
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});

		btLevel4.setOnMouseClicked(e -> {// If the user wants to play Level4
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			Scene level = null;
			try {
				level = openLevel("levels/level4.txt", primaryStage);
				currentLevelName = "levels/level4.txt";
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});

		btLevel5.setOnMouseClicked(e -> {// If the user wants to play Level5
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();

			Scene level = null;
			try {
				level = openLevel("levels/level5.txt", primaryStage);
				currentLevelName = "levels/level5.txt";
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});

		level1.setOnMouseClicked(e -> { // If the user wants to play Level1
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			Scene level = null;
			try {
				level = openLevel("levels/level1.txt", primaryStage);
				currentLevelName = "levels/level1.txt";

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});

		level2.setOnMouseClicked(e -> {// If the user wants to play Level2
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();

			Scene level = null;
			try {
				level = openLevel("levels/level2.txt", primaryStage);
				currentLevelName = "levels/level2.txt";
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});

		level3.setOnMouseClicked(e -> {// If the user wants to play Level3
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();

			Scene level = null;
			try {
				level = openLevel("levels/level3.txt", primaryStage);
				currentLevelName = "levels/level3.txt";
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});

		level4.setOnMouseClicked(e -> {// If the user wants to play Level4
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();

			Scene level = null;
			try {
				level = openLevel("levels/level4.txt", primaryStage);
				currentLevelName = "levels/level4.txt";
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});

		level5.setOnMouseClicked(e -> {// If the user wants to play Level5
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();

			Scene level = null;
			try {
				level = openLevel("levels/level5.txt", primaryStage);
				currentLevelName = "levels/level5.txt";
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});

		FadeTransition fadeIn1 = new FadeTransition(Duration.seconds(0.5), back);
		FadeTransition fadeIn2 = new FadeTransition(Duration.seconds(0.5), header);
		FadeTransition fadeIn3 = new FadeTransition(Duration.seconds(0.5), level1);
		FadeTransition fadeIn4 = new FadeTransition(Duration.seconds(0.5), level2);
		FadeTransition fadeIn5 = new FadeTransition(Duration.seconds(0.5), level3);
		FadeTransition fadeIn6 = new FadeTransition(Duration.seconds(0.5), level4);
		FadeTransition fadeIn7 = new FadeTransition(Duration.seconds(0.5), level5);

		fadeIn1.setFromValue(0.0);
		fadeIn1.setToValue(1.0);
		fadeIn2.setFromValue(0.0);
		fadeIn2.setToValue(1.0);
		fadeIn3.setFromValue(0.0);
		fadeIn3.setToValue(1.0);
		fadeIn4.setFromValue(0.0);
		fadeIn4.setToValue(1.0);
		fadeIn5.setFromValue(0.0);
		fadeIn5.setToValue(1.0);
		fadeIn6.setFromValue(0.0);
		fadeIn6.setToValue(1.0);
		fadeIn7.setFromValue(0.0);
		fadeIn7.setToValue(1.0);

		fadeIn1.play();
		fadeIn2.play();
		fadeIn3.play();
		fadeIn4.play();
		fadeIn5.play();
		fadeIn6.play();
		fadeIn7.play();

		levelPane.getChildren().add(bg);
		levelPane.getChildren().addAll(back, header, level1, level2, level3, level4, level5);
		levelPane.getChildren().addAll(btLevel1, btLevel2, btLevel3, btLevel4, btLevel5);

		primaryStage.setScene(levelMenu);

	}

	public static void finito(ArrayList roadtiles,
			ArrayList buildings) { /*
									 * It performs the task of returning from the map, clearing the objects left
									 * over from the previous situation in case of winning the level and moving to
									 * the next level, and ending the task.
									 */
		for (int i = 0; i < cars.size(); i++) {
			cars.get(i).pt.stop();
		}
		for (int i = 0; i < cars.size(); i++) {

			a.getChildren().remove(cars.get(i));
		}
		for (int i = 0; i < lights.size(); i++) {
			a.getChildren().remove(lights.get(i));
		}
		for (int i = 0; i < roadtiles.size(); i++) {
			a.getChildren().remove(roadtiles.get(i));
		}
		for (int i = 0; i < buildings.size(); i++) {

			a.getChildren().remove(buildings.get(i));
		}

		for (int i = 0; i < paths.length; i++) {
			a.getChildren().remove(paths[i]);
		}
		cars.clear(); // clear arraylists
		roadtiles.clear();
		buildings.clear();
		lights.clear();
	}

	public static void controllLose(MetaData meta1, String levelName, Timeline animation, Stage primaryStage) {
		/*
		 * It checks whether the user has lost the level he is on by looking at the
		 * number of accidents he has made. If the user lose, are given the opportunity
		 * to return to the level selection menu or replay the level.
		 */
		if (Car.crashCounter == meta1.getAllowedAccident()) {
			for (int i = 0; i < cars.size(); i++) {
				cars.get(i).pt.stop();
				a.getChildren().remove(cars.get(i));
			}
			try {
				animation.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			Rectangle rec = new Rectangle();
			rec.setArcWidth(25); // X ekseni boyunca yuvarlaklık
			rec.setArcHeight(25);
			rec.setFill(Color.DARKORANGE);
			rec.setWidth(300);
			rec.setHeight(100);
			rec.setX(250);
			rec.setY(350);

			Label text = new Label();
			text.setText("You Lost !!");
			text.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
			text.setLayoutX(360);
			text.setLayoutY(355);

			Button btExit = new Button("Exit");
			btExit.setCursor(Cursor.HAND);
			btExit.setStyle(
					"-fx-background-color: darkorange; -fx-font-size: 15px; -fx-text-fill: red; -fx-font-weight: bold;");
			btExit.setLayoutX(300);
			btExit.setLayoutY(420);

			Button btAgain = new Button("Again");
			btAgain.setCursor(Cursor.HAND);
			btAgain.setStyle(
					"-fx-background-color: darkorange; -fx-font-size: 15px; -fx-text-fill: green; -fx-font-weight: bold;");
			btAgain.setLayoutX(450);
			btAgain.setLayoutY(420);

			a.getChildren().addAll(rec, text, btExit, btAgain);

			String audioFile = "sounds/button-click-sound.mp3";
			Media media = new Media(new File(audioFile).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setVolume(0.5);

			btExit.setOnMouseClicked(e1 -> { // If it wants to return to the level selection menu, delete and
				// reset the necessary data and objects and then redirect to the level selection
				// menu.
				mediaPlayer.seek(Duration.seconds(0));
				mediaPlayer.play();

				Car.crashCounter = 0;
				finishedCars = 0;
				finito(roadtiles, buildings);
				a.getChildren().removeAll(rec, text, btExit, btAgain);
				levelSelect(primaryStage);
			});

			btAgain.setOnMouseClicked(e1 -> { // If user wants to play again, clear everything, reset and open the
												// current level again.
				mediaPlayer.seek(Duration.seconds(0));
				mediaPlayer.play();

				Car.crashCounter = 0;
				finishedCars = 0;
				finito(roadtiles, buildings);
				a.getChildren().removeAll(rec, text, btExit, btAgain);

				if (levelName.equals("levels/level1.txt")) {
					try {
						Scene level = null;
						level = openLevel("levels/level1.txt", primaryStage);
						currentLevelName = "levels/level1.txt";
						primaryStage.setScene(level);

					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
				} else if (levelName.equals("levels/level2.txt")) {
					try {
						Scene level = null;
						level = openLevel("levels/level2.txt", primaryStage);
						currentLevelName = "levels/level2.txt";
						primaryStage.setScene(level);

					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
				} else if (levelName.equals("levels/level3.txt")) {
					try {
						Scene level = null;
						level = openLevel("levels/level3.txt", primaryStage);
						currentLevelName = "levels/level3.txt";
						primaryStage.setScene(level);

					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
				} else if (levelName.equals("levels/level4.txt")) {
					try {
						Scene level = null;
						level = openLevel("levels/level4.txt", primaryStage);
						currentLevelName = "levels/level4.txt";
						primaryStage.setScene(level);

					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
				} else if (levelName.equals("levels/level5.txt")) {
					try {
						Scene level = null;
						level = openLevel("levels/level5.txt", primaryStage);
						currentLevelName = "levels/level5.txt";
						primaryStage.setScene(level);

					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
				}
			});

		}
	}

	public static void controlWin(MetaData meta, String levelName, Timeline animation, Stage primaryStage) {
		/*
		 * It checks whether the user has earned the current level by looking at the
		 * number of vehicles they have used to reach their destination. If the user
		 * wins, they are given the opportunity to return to the level selection menu or
		 * move on to the next level
		 */

		if (Main.finishedCars == meta.getWinCondition()) {
			for (int i = 0; i < cars.size(); i++) {
				cars.get(i).pt.stop();
				a.getChildren().remove(cars.get(i));
			}
			try {
				animation.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			Rectangle rec = new Rectangle();
			rec.setArcWidth(25); // X ekseni boyunca yuvarlaklık
			rec.setArcHeight(25);
			rec.setFill(Color.DARKORANGE);
			rec.setWidth(300);
			rec.setHeight(100);
			rec.setX(250);
			rec.setY(350);

			Label text = new Label();
			text.setText("YOU WON!!!");
			text.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
			text.setLayoutX(360);
			text.setLayoutY(355);

			Button btExit = new Button("Exit ");
			btExit.setCursor(Cursor.HAND);
			btExit.setStyle(
					"-fx-background-color: darkorange; -fx-font-size: 15px; -fx-text-fill: red; -fx-font-weight: bold;");
			btExit.setLayoutX(330);
			btExit.setLayoutY(420);

			Button btNextLevel = new Button("Next Level");
			btNextLevel.setCursor(Cursor.HAND);
			btNextLevel.setStyle(
					"-fx-background-color: darkorange; -fx-font-size: 15px; -fx-text-fill: green; -fx-font-weight: bold;");
			btNextLevel.setLayoutX(450);
			btNextLevel.setLayoutY(420);

			a.getChildren().addAll(rec, text, btExit, btNextLevel);

			if (currentLevelName.equals("levels/level5.txt")) { // if user win the last level , onyl there will be exit
																// buton
				a.getChildren().remove(btNextLevel);
			}

			String audioFile = "sounds/button-click-sound.mp3";
			Media media = new Media(new File(audioFile).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setVolume(0.5);

			btExit.setOnMouseClicked(e1 -> {
				// If it wants to return to the level selection menu, delete and
				// reset the necessary data and objects and then redirect to the level selection
				// menu
				mediaPlayer.seek(Duration.seconds(0));
				mediaPlayer.play();

				Car.crashCounter = 0;
				finishedCars = 0;
				finito(roadtiles, buildings);
				a.getChildren().removeAll(rec, text, btExit, btNextLevel);
				levelSelect(primaryStage);
			});

			btNextLevel.setOnMouseClicked(e1 -> { // If the user wants to move to another level, open the next level
													// from the current level.
				mediaPlayer.seek(Duration.seconds(0));
				mediaPlayer.play();

				Car.crashCounter = 0;
				finishedCars = 0;
				finito(roadtiles, buildings);
				a.getChildren().removeAll(rec, text, btExit, btNextLevel);

				if (levelName.equals("levels/level1.txt")) {
					try {
						Scene level = openLevel("levels/level2.txt", primaryStage);
						currentLevelName = "levels/level2.txt";
						primaryStage.setScene(level);

					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
				} else if (levelName.equals("levels/level2.txt")) {
					try {
						Scene level = openLevel("levels/level3.txt", primaryStage);
						currentLevelName = "levels/level3.txt";
						primaryStage.setScene(level);

					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
				} else if (levelName.equals("levels/level3.txt")) {
					try {
						Scene level = openLevel("levels/level4.txt", primaryStage);
						currentLevelName = "levels/level4.txt";
						primaryStage.setScene(level);

					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
				} else if (levelName.equals("levels/level4.txt")) {
					try {
						Scene level = openLevel("levels/level5.txt", primaryStage);
						currentLevelName = "levels/level5.txt";
						primaryStage.setScene(level);

					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
				}
			});
		}
	}
}
