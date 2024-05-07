
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
    	    mediaPlayer.play(); // it starts media again
    	});
		openMainMenu(primaryStage);
	    Image icon = new Image("/icon_.png"); 
		primaryStage.setTitle("Traffic Control Simulator");
		primaryStage.getIcons().add(icon); // sets game icon
		primaryStage.setResizable(false);
	    primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	public void openMainMenu(Stage primaryStage) {
		Pane mainMenu = new Pane();
		ImageView bg = new ImageView(new Image("anaMenu.jpg")); // bg is background image 
		bg.setFitHeight(mainMenu.getHeight());
		bg.setFitWidth(mainMenu.getWidth());
	    
		
		ImageView header = new ImageView(new Image("/tcs.png"));
		header.setX(100);
		ImageView startGame = new ImageView(new Image("/Start-Game.png"));
		ImageView exitGame = new ImageView(new Image("/Exit.png"));
		
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
		
	    exitGame.setOnMouseClicked(e -> {
	    	Rectangle rec = new Rectangle();
	    	rec.setArcWidth(25); // X ekseni boyunca yuvarlaklık
	        rec.setArcHeight(25);
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
	    		Random rand = new Random();
	    		int randX = rand.nextInt(50, 750);
	    		int randY = rand.nextInt(50,750);
	    		btYes.setLayoutX(randX);
	    		btYes.setLayoutY(randY);
	    		
	    	});
	    	btYes.setOnMouseClicked(event -> {System.exit(0);});
	    	btNo.setOnMouseClicked(event -> {
	    		mainMenu.getChildren().removeAll(rec, text, btYes, btNo);
	    	});
	    });
	    
        startGame.setOnMouseClicked(e -> {
			levelSelect(primaryStage);
         });    
	}
	
	public  Scene openLevel(String levelName ,Stage primaryStage) throws FileNotFoundException {
		
		ArrayList<RoadTile> roadtiles = new ArrayList<>();
        ArrayList<Building> buildings = new ArrayList<>();
	ImageView back = new ImageView(new Image("/back.png"));
		
		back.setX(0);
		back.setY(800-51);
        
        File file = new File(levelName);
        Scanner scan = new Scanner(file);
        MetaData meta = null ;
        
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
				paths = new Path[numberOfPaths]; // paths array that contains all paths in that level
				pathsLength = new double[numberOfPaths]; // this contains the length of each path
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
		a.getChildren().add(back); // 
		
        Scene scene = new Scene(a,meta.getSceneWidth(),meta.getSceneHeight());
        int pathCount = paths.length;
        MetaData meta1 = meta;
		Timeline animation = new Timeline(new KeyFrame(Duration.millis(2000), e-> {
			Car newCar = spawnCar(pathCount);
			if(newCar != null) {
				a.getChildren().add(newCar);
				cars.add(newCar);
			}
			if(Car.crashCounter==meta1.getAllowedAccident()) {
				System.out.println("crachcounter buuuuu");
				try {
					this.stop();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
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
		    	text.setText("You losed !!");
		    	text.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
		    	text.setLayoutX(275);
		    	text.setLayoutY(355);
		    	
		    	Button btYes = new Button("Exit ");
		    	btYes.setStyle("-fx-background-color: darkorange; -fx-font-size: 15px");
		    	btYes.setLayoutX(330);
		    	btYes.setLayoutY(420);
		    	btYes.setOnMouseClicked(e1 -> {
					levelSelect(primaryStage);
		         });    
		    	
		    	Button btNo = new Button("Again");
		    	btNo.setStyle("-fx-background-color: darkorange; -fx-font-size: 15px");
		    	btNo.setLayoutX(450);
		    	btNo.setLayoutY(420);
		    	btNo.setOnMouseClicked(e1 -> {
				  if(levelName.equals("level1.txt")) {
					 try {
						  finito(roadtiles,buildings);
						  a.getChildren().removeAll(rec,text,btYes,btNo);
						  openLevel("level1.txt",primaryStage);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				  }
				  else if(levelName.equals("level2.txt")) {
					  try {
						  finito(roadtiles,buildings);
						  a.getChildren().removeAll(rec,text,btYes,btNo);
							openLevel("level2.txt",primaryStage);
						} catch (FileNotFoundException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
				  }
	             else if(levelName.equals("level3.txt")) {
	       		  try {
	       			 a.getChildren().removeAll(rec,text,btYes,btNo);
						openLevel("level3.txt",primaryStage);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				  }
	             else if(levelName.equals("level4.txt")) {
	       		  try {
	       			 a.getChildren().removeAll(rec,text,btYes,btNo);
						openLevel("level4.txt",primaryStage);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
	              }
	            else if(levelName.equals("level5.txt")) {
	            	 try {
	            		 a.getChildren().removeAll(rec,text,btYes,btNo);
							openLevel("level5.txt",primaryStage);
						} catch (FileNotFoundException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
	            }
		         }); 
		    	a.getChildren().addAll(rec,text,btYes,btNo);
			}
		}));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
		
		back.setOnMouseClicked(e -> {
	        finito(roadtiles,buildings);
		    animation.stop(); // Animasyonu durdur
		    levelSelect(primaryStage); // level seçme menüsüne dön
		});
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

	public void levelSelect(Stage primaryStage) {
		
		Pane levelPane = new Pane();
		Scene levelMenu = new Scene(levelPane, 800, 800);
		
		ImageView bg = new ImageView(new Image("anaMenu.jpg"));
		bg.setFitHeight(levelMenu.getHeight());
		bg.setFitWidth(levelMenu.getWidth());
		
		ImageView header = new ImageView(new Image("/Select-Level.png"));
		header.setX(200);
		header.setY(100);
		
		ImageView back = new ImageView(new Image("/back.png"));
		
		back.setX(0);
		back.setY(800-51);
		
		ImageView level1 = new ImageView(new Image("/Level-1.png"));
		ImageView level2 = new ImageView(new Image("/Level-2.png"));
		ImageView level3 = new ImageView(new Image("/Level-3.png"));
		ImageView level4 = new ImageView(new Image("/Level-4.png"));
		ImageView level5 = new ImageView(new Image("/Level-5.png"));

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
		
		back.setOnMouseClicked(e -> {openMainMenu(primaryStage);});
		
		level1.setOnMouseClicked(e -> {
			Scene level = null;
			try {
				level = openLevel("level1.txt",primaryStage);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});
		
		level2.setOnMouseClicked(e -> {
			Scene level = null;
			try {
				level = openLevel("level2.txt",primaryStage);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});
		
		level3.setOnMouseClicked(e -> {
			Scene level = null;
			try {
				level = openLevel("level3.txt",primaryStage);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});
		
		level4.setOnMouseClicked(e -> {
			Scene level = null;
			try {
				level = openLevel("level4.txt",primaryStage);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});
		
		level5.setOnMouseClicked(e -> {
			Scene level = null;
			try {
				level = openLevel("level5.txt",primaryStage);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			primaryStage.setScene(level);
		});
		
		levelPane.getChildren().add(bg);
		levelPane.getChildren().add(back);
		levelPane.getChildren().add(header);
		levelPane.getChildren().add(level1);
		levelPane.getChildren().add(level2);
		levelPane.getChildren().add(level3);
		levelPane.getChildren().add(level4);
		levelPane.getChildren().add(level5);
		
		primaryStage.setScene(levelMenu);
	}
	public void finito ( ArrayList roadtiles , ArrayList buildings) {
		lights.clear();
		 for(int i = 0 ; i<cars.size() ; i++) {
				cars.get(i).pt.stop();	    		
	        }
		cars.clear();
		 for(int i = 0 ; i < cars.size() ; i++) {
		        
	        	a.getChildren().remove(cars.get(i));
	        }
	      for(int i = 0 ; i<roadtiles.size() ; i++) {

	    		a.getChildren().remove(roadtiles.get(i));
	        }
	        for(int i = 0 ; i < buildings.size() ; i++) {
	        
	        	a.getChildren().remove(buildings.get(i));
	        }
	        for(int i = 0 ; i < lights.size() ; i++) {
	        	a.getChildren().remove(lights.get(i));
	        }
	        for(int i = 0 ; i < paths.length ; i++) {
	        	a.getChildren().remove(paths[i]);
	        }
	}
	
}
