
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;


public class Building extends Pane {
	private Color[] colors = {Color.FORESTGREEN, Color.DARKBLUE, Color.BLUEVIOLET, Color.RED};
	private int type;
	private int rotation;
	private int colorIndex;
	private int gridX;
	private int gridY;
	
	public Building( int type, int rotation, int colorIndex, int gridX, int gridY )
	{
		setType(type);
		setRotation(rotation);
		setColorIndex(colorIndex);
		setGridX(gridX);
		setGridY(gridY);
		
	}
	
	public void draw(MetaData meta) {
		double cellWidth = meta.getSceneWidth()/meta.getNumberOfXCell();
		double cellHeight = meta.getSceneHeight()/meta.getNumberOfYCell();
		Rectangle outterRec = new Rectangle( cellWidth*(-0.075), cellHeight * (-0.075), cellWidth * 2.15, cellHeight * 3.15 );
		Rectangle innerRec = new Rectangle( cellWidth*0.28, cellHeight*0.28, cellWidth * 1.44, cellHeight * 1.44 );
		
		outterRec.setFill(Color.WHITE);
		innerRec.setFill(colors[colorIndex]);
		
		outterRec.setStroke(Color.GREY);
		outterRec.setStrokeWidth(2);
		
		outterRec.setArcHeight(10);
		outterRec.setArcWidth(10);
		innerRec.setArcHeight(10);
		innerRec.setArcWidth(10);
		if (this.type == 0 && rotation == 0) 
		{
			this.getChildren().add(outterRec);
			this.getChildren().add(innerRec);
		}
		else if (this.type == 0 && rotation == 90) 
		{
			Rotate rotate = new Rotate();
	        rotate.setPivotX(innerRec.getX() + innerRec.getWidth()/2); // Set pivot point as the top-left corner x-coordinate
	        rotate.setPivotY(innerRec.getY() + innerRec.getHeight()/2); // Set pivot point as the top-left corner y-coordinate
	        rotate.setAngle(-90); 
	        outterRec.getTransforms().add(rotate);
			
			this.getChildren().add(outterRec);
			this.getChildren().add(innerRec);
		}
		else if (this.type == 0 && rotation == 180) 
		{
			Rotate rotate = new Rotate();
	        rotate.setPivotX(outterRec.getX() + outterRec.getWidth()/2); // Set pivot point as the top-left corner x-coordinate
	        rotate.setPivotY(outterRec.getY() + outterRec.getHeight()/2); // Set pivot point as the top-left corner y-coordinate
	        rotate.setAngle(180); 
	        outterRec.getTransforms().add(rotate);
	        innerRec.getTransforms().add(rotate);
			
			this.getChildren().add(outterRec);
			this.getChildren().add(innerRec);
		}
		else if (this.type == 0 && rotation == 270) 
		{
			Rotate rotate1 = new Rotate();
	        rotate1.setPivotX(innerRec.getX() + innerRec.getWidth()/2); // Set pivot point as the top-left corner x-coordinate
	        rotate1.setPivotY(innerRec.getY() + innerRec.getHeight()/2); // Set pivot point as the top-left corner y-coordinate
	        rotate1.setAngle(-90); 
	        outterRec.getTransforms().add(rotate1);
	        
	        Rotate rotate2 = new Rotate();
	        rotate2.setPivotX(outterRec.getHeight()/2);
	        rotate2.setPivotY(outterRec.getWidth()/2);
	        rotate2.setAngle(180);
	        innerRec.getTransforms().add(rotate2);

			this.getChildren().add(outterRec);
			this.getChildren().add(innerRec);
		}
		
		else if (this.type == 1 && rotation == 0) 
		{
			Circle innerCirc = new Circle( cellWidth, cellHeight, cellWidth * 0.75);
			innerCirc.setFill(colors[colorIndex]);
			
			this.getChildren().add(outterRec);
			this.getChildren().add(innerCirc);
		}
		else if (this.type == 1 && rotation == 90) 
		{
			Circle innerCirc = new Circle( cellWidth, cellHeight, cellWidth * 0.75);
			innerCirc.setFill(colors[colorIndex]);
			
			Rotate rotate = new Rotate();
	        rotate.setPivotX(innerCirc.getCenterX()); // Set pivot point as the top-left corner x-coordinate
	        rotate.setPivotY(innerCirc.getCenterY()); // Set pivot point as the top-left corner y-coordinate
	        rotate.setAngle(-90); 
	        outterRec.getTransforms().add(rotate);
			
			this.getChildren().add(outterRec);
			this.getChildren().add(innerCirc);
		}
		else if (this.type == 1 && rotation == 180) 
		{
			Circle innerCirc = new Circle( cellWidth, cellHeight, cellWidth * 0.75);
			innerCirc.setFill(colors[colorIndex]);
			
			Rotate rotate = new Rotate();
	        rotate.setPivotX(outterRec.getX() + outterRec.getWidth()/2); // Set pivot point as the top-left corner x-coordinate
	        rotate.setPivotY(outterRec.getY() + outterRec.getHeight()/2); // Set pivot point as the top-left corner y-coordinate
	        rotate.setAngle(180); 
	        outterRec.getTransforms().add(rotate);
	        innerCirc.getTransforms().add(rotate);
			
			this.getChildren().add(outterRec);
			this.getChildren().add(innerCirc);
		}
		else if (this.type == 1 && rotation == 270) 
		{
			Circle innerCirc = new Circle( cellWidth, cellHeight, cellWidth * 0.75);
			innerCirc.setFill(colors[colorIndex]);
			
			Rotate rotate1 = new Rotate();
	        rotate1.setPivotX(innerCirc.getCenterX()); // Set pivot point as the top-left corner x-coordinate
	        rotate1.setPivotY(innerCirc.getCenterY()); // Set pivot point as the top-left corner y-coordinate
	        rotate1.setAngle(-90); 
	        outterRec.getTransforms().add(rotate1);
	        
	        Rotate rotate2 = new Rotate();
	        rotate2.setPivotX(outterRec.getHeight()/2);
	        rotate2.setPivotY(outterRec.getWidth()/2);
	        rotate2.setAngle(180);
	        innerCirc.getTransforms().add(rotate2);

			this.getChildren().add(outterRec);
			this.getChildren().add(innerCirc);
		}
		else if (this.type == 2) 
		{
			Rectangle rec = new Rectangle( cellWidth*(-0.075), cellHeight*(-0.075), cellWidth*1.15, cellHeight*1.15 );
			rec.setFill(colors[colorIndex]);
			rec.setArcHeight(10);
			rec.setArcWidth(10);
			
			this.getChildren().add(rec);
		}
		
		
	}
	
	public int getType() {return type;}
	public void setType(int type) {this.type = type;}
	public int getRotation() {return rotation;}
	public void setRotation(int rotation) {this.rotation = rotation;}
	public int getColorIndex() {return colorIndex;}
	public void setColorIndex(int colorIndex) {this.colorIndex = colorIndex;}
	public int getGridX() {return gridX;}
	public void setGridX(int gridX) {this.gridX = gridX;}
	public int getGridY() {return gridY;}
	public void setGridY(int gridY) {this.gridY = gridY;}
}
