
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
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

public class Main extends Application  {
	
    public static Path[] paths = new Path[0];
    public static ArrayList<TrafficLight> lights = new ArrayList<>();
    public static ArrayList<Car> cars = new ArrayList<Car>();
    
    public static double[] pathsLength;
    public static int finishedCars; //count of car which reached finished
    public static SuperPane a;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		String audioFile = "sounds/MainMusic.mp3";
        Media media = new Media(new File(audioFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        
        mediaPlayer.setVolume(0.5);
        
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(() -> {
        	System.out.println("End of media reached");
    	    mediaPlayer.seek(Duration.ZERO);
    	});
        
        
		//------------------------------------
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
		ImageView bg = new ImageView(new Image("images/anaMenu.jpg"));
		bg.setFitHeight(mainMenu.getHeight());
		bg.setFitWidth(mainMenu.getWidth());
	    
		
		ImageView header = new ImageView(new Image("images/tcs.png"));
		header.setX(100);
		ImageView startGame = new ImageView(new Image("images/Start-Game.png"));
		ImageView exitGame = new ImageView(new Image("images/Exit.png"));
		
		startGame.setFitHeight(45*1.5);
		startGame.setFitWidth(160*1.5);
		exitGame.setFitHeight(35*1.5);
		exitGame.setFitWidth(61*1.5);
		startGame.setX(400-startGame.getFitWidth()/2);
		startGame.setY(400-startGame.getFitHeight()/2);
		exitGame.setX(400-exitGame.getFitWidth()/2);
		exitGame.setY(startGame.getY() +  startGame.getFitHeight() + 15);
		
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
        exitGame.setOnMouseClicked(e -> {
        	
        	mediaPlayer.seek(Duration.seconds(0));
        	mediaPlayer.play();
        	
	    	Rectangle rec = new Rectangle();
	    	rec.setStrokeWidth(2.5);
	    	rec.setStroke(Color.BLACK);
	    	rec.setFill(Color.DARKORANGE);
	    	rec.setWidth(300);
	    	rec.setHeight(100);
	    	rec.setX(250);
	    	rec.setY(350);
	    	rec.setArcHeight(10);
	    	rec.setArcWidth(10);
	    	
	    	Label text = new Label();
	    	text.setText("Are you really sure you want to exit?");
	    	text.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
	    	text.setLayoutX(275);
	    	text.setLayoutY(355);
	    	
	    	Button btYes = new Button("Yes");
	    	btYes.setStyle("-fx-background-color: darkorange; -fx-font-size: 15px; -fx-text-fill: red; -fx-font-weight: bold;");
	    	btYes.setLayoutX(330);
	    	btYes.setLayoutY(410);
	    	
	    	Button btNo = new Button("No");
	    	btNo.setStyle("-fx-background-color: darkorange; -fx-font-size: 15px; -fx-text-fill: green; -fx-font-weight: bold;");
	    	btNo.setLayoutX(450);
	    	btNo.setLayoutY(410);
	    	
	    	mainMenu.getChildren().addAll(rec, text, btYes, btNo);
	    	
	    	btYes.setCursor(Cursor.HAND);
	    	btNo.setCursor(Cursor.HAND);
	    	
	    	btYes.setOnMouseEntered(event -> {
	    		Random rand = new Random();
	    		int randX = rand.nextInt(50, 750);
	    		int randY = rand.nextInt(50,750);
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
	    		mainMenu.getChildren().removeAll(rec, text, btYes, btNo);
	    	});
	    });
	    
        startGame.setOnMouseClicked(e -> {
        	mediaPlayer.seek(Duration.seconds(0));
        	mediaPlayer.play();
			levelSelect(primaryStage);
         });    
	}
	
	public static Scene openLevel(String levelName) {
		
		ArrayList<RoadTile> roadtiles = new ArrayList<>();
        ArrayList<Building> buildings = new ArrayList<>();
       
        File file = new File(levelName);
        Scanner scan = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        MetaData meta = null;
        
		double previousPathX = 0;
		double previousPathY = 0;
        while(scan.hasNext()) {
        	String tempString = scan.nextLine();
			String [] words = tempString.split(" ");
			String className = words[0];
			if(className.equals("Metadata")) {
				Double width = Double.parseDouble(words[1]);
				Double height = Double.parseDouble(words[2]);
				
				int numberOfXCell = Integer.parseInt(words[3]);
				int numberOfYCell = Integer.parseInt(words[4]);
				int numberOfPaths = Integer.parseInt(words[5]);
				paths = new Path[numberOfPaths];
				pathsLength = new double[numberOfPaths];
				int winCondition  = Integer.parseInt(words[6]);
				int allowedAccident = Integer.parseInt(words[7]);
				
			    meta = new MetaData(width,height,numberOfXCell,numberOfYCell,numberOfPaths,winCondition,allowedAccident);
			}
			else if ( className.equals("RoadTile")) {
				int type = Integer.parseInt(words[1]);
				int rotate = Integer.parseInt(words[2]);
				int gridx = Integer.parseInt(words[3]);
				int gridy = Integer.parseInt(words[4]);
				
				RoadTile road = new RoadTile(type,rotate,gridx,gridy);
				roadtiles.add(road);
			}
			else if ( className.equals("Building")) {
				int type = Integer.parseInt(words[1]);
				int rotation = Integer.parseInt(words[2]);
				int colorIndex = Integer.parseInt(words[3]);
				int gridx = Integer.parseInt(words[4]);
				int gridy = Integer.parseInt(words[5]);
				
				Building building = new Building(type,rotation,colorIndex,gridx,gridy);
				
				buildings.add(building);
			}
			else if(className.equals("TrafficLight")){
				Double x1 = Double.parseDouble(words[1]);
				Double y1 = Double.parseDouble(words[2]);
				Double x2 = Double.parseDouble(words[3]);
				Double y2 = Double.parseDouble(words[4]);
				
				TrafficLight light = new TrafficLight (x1,y1,x2,y2);
				
				lights.add(light);
			}
			else if(className.equals("Path")) {
				int index = Integer.parseInt(words[1]);
				double pathX = Double.parseDouble(words[3]);
				double pathY = Double.parseDouble(words[4]);
				if(words[2].equals("MoveTo")) {
					paths[index] = new Path();
					paths[index].getElements().add(new MoveTo(pathX, pathY));
					previousPathX = pathX;
					previousPathY = pathY;
				}
				if(words[2].equals("LineTo")) {
					//if(Math.abs(pathX-previousPathX)<1||Math.abs(pathY-previousPathY)<1) {
					paths[index].getElements().add(new LineTo(pathX, pathY));
					pathsLength[index] += Math.sqrt(Math.pow(pathY-previousPathY, 2)+Math.pow(pathX-previousPathX, 2));
					previousPathX = pathX;
					previousPathY = pathY;
				}

			}
        }
        
        a = new SuperPane(meta.getSceneWidth(),meta.getSceneHeight(),meta.getNumberOfXCell(),meta.getNumberOfYCell());
        
     
        for(int i = 0 ; i<roadtiles.size() ; i++) {
        	roadtiles.get(i).draw(meta);
        	a.add(roadtiles.get(i), roadtiles.get(i).getGridx(), roadtiles.get(i).getGridy());
        }
        for(int i = 0 ; i < buildings.size() ; i++) {
        	buildings.get(i).draw(meta);
        	a.add(buildings.get(i),buildings.get(i).getGridX(), buildings.get(i).getGridY());
        }
        for(int i = 0 ; i < lights.size() ; i++) {
        	a.getChildren().add(lights.get(i));
        }
        for(int i = 0 ; i < paths.length ; i++) {
        	paths[i].setStroke(Color.RED);
        	a.getChildren().add(paths[i]);
        }
		
        Scene scene = new Scene(a,meta.getSceneWidth(),meta.getSceneHeight());
        int pathCount = paths.length;
		Timeline animation = new Timeline(new KeyFrame(Duration.millis(2000), e-> {
			Car newCar = spawnCar(pathCount);
			if(newCar != null) {
				a.getChildren().add(newCar);
				cars.add(newCar);
				
			}
		}));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
        
		scan.close();
		return scene;
	
	}
	public static Car spawnCar(int pathCount) {
		if(Math.random() < 1) {
			int randomPath = ((int)(Math.random()*pathCount));
			return new Car(paths[randomPath], pathsLength[randomPath], randomPath);
		} 
		return null;
	}

	public static void levelSelect(Stage primaryStage) {
		
		Pane levelPane = new Pane();
		Scene levelMenu = new Scene(levelPane, 800, 800);
		
		ImageView bg = new ImageView(new Image("images/anaMenu.jpg"));
		bg.setFitHeight(levelMenu.getHeight());
		bg.setFitWidth(levelMenu.getWidth());
		
		ImageView header = new ImageView(new Image("images/Select-Level.png"));
		header.setX(200);
		header.setY(100);
		
		ImageView back = new ImageView(new Image("images/back.png"));
		
		back.setX(0);
		back.setY(800-51);
		
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
		
		Rectangle btLevel1 = new Rectangle(level1.getX()+21, level1.getY()+17, 158, 110);
		btLevel1.setFill(Color.TRANSPARENT);
		btLevel1.setRotate(5);
		
		Rectangle btLevel2 = new Rectangle(level2.getX()+22, level2.getY()+16, 156, 108);
		btLevel2.setFill(Color.TRANSPARENT);
		btLevel2.setRotate(5);
		
		Rectangle btLevel3 = new Rectangle(level3.getX()+22, level3.getY()+16, 156, 108);
		btLevel3.setFill(Color.TRANSPARENT);
		btLevel3.setRotate(5);
		
		Rectangle btLevel4 = new Rectangle(level4.getX()+22, level4.getY()+16, 156, 108);
		btLevel4.setFill(Color.TRANSPARENT);
		btLevel4.setRotate(5);
		
		Rectangle btLevel5 = new Rectangle(level5.getX()+22, level5.getY()+16, 156, 108);
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
		
		back.setOnMouseClicked(e -> {
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			openMainMenu(primaryStage);
		});
		
		btLevel1.setOnMouseClicked(e -> {
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			Scene level = openLevel("levels/level1.txt");
			primaryStage.setScene(level);
		});
		
		btLevel2.setOnMouseClicked(e -> {
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			Scene level = openLevel("levels/level2.txt");
			primaryStage.setScene(level);
		});
		
		btLevel3.setOnMouseClicked(e -> {
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			Scene level = openLevel("levels/level3.txt");
			primaryStage.setScene(level);
		});
		
		btLevel4.setOnMouseClicked(e -> {
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			Scene level = openLevel("levels/level4.txt");
			primaryStage.setScene(level);
		});
		
		btLevel5.setOnMouseClicked(e -> {
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			Scene level = openLevel("levels/level5.txt");
			primaryStage.setScene(level);
		});
		
		level1.setOnMouseClicked(e -> {
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			Scene level = openLevel("levels/level1.txt");
			primaryStage.setScene(level);
		});
		
		level2.setOnMouseClicked(e -> {
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			Scene level = openLevel("levels/level2.txt");
			primaryStage.setScene(level);
		});
		
		level3.setOnMouseClicked(e -> {
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			Scene level = openLevel("levels/level3.txt");
			primaryStage.setScene(level);
		});
		
		level4.setOnMouseClicked(e -> {
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			Scene level = openLevel("levels/level4.txt");
			primaryStage.setScene(level);
		});
		
		level5.setOnMouseClicked(e -> {
			mediaPlayer.seek(Duration.seconds(0));
			mediaPlayer.play();
			Scene level = openLevel("levels/level5.txt");
			primaryStage.setScene(level);
		});
	
		levelPane.getChildren().add(bg);
		levelPane.getChildren().addAll(back, header, level1, level2, level3, level4, level5);
		levelPane.getChildren().addAll(btLevel1, btLevel2, btLevel3, btLevel4, btLevel5);
		
		primaryStage.setScene(levelMenu);
	}
	
}