
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class SuperPane extends Pane{
	private double width;
	private double height;
	private int numberOfCellsX;
	private int numberOfCellsY;
	
	private double gridWidth;
	private double gridHeight;
	
	public SuperPane(double width, double height, int numberOfCellsX, int numberOfCellsY) {
		this.width = width;
		this.height = height;
		this.numberOfCellsX = numberOfCellsX;
		this.numberOfCellsY = numberOfCellsY;
		gridWidth = width/numberOfCellsX;
		gridHeight = height/numberOfCellsY;
		for(int i = 0; i < numberOfCellsX;i++) {
			for(int j = 0; j < numberOfCellsY; j++) {
				Rectangle rect = new Rectangle(i*gridWidth,j*gridHeight,gridWidth,gridHeight); // background is actualyy rectangle . we add all of them
				rect.setStroke(Color.GREY);
				rect.setStrokeWidth(0.5);
				rect.setOpacity(0.8);
				rect.setFill(Color.DEEPSKYBLUE);
				this.getChildren().add(rect);
			}
		}
	}
	
	public void add(Shape shape,int x, int y) { // shapes are added to correct position according to superpane
		shape.setTranslateX(x*gridWidth);
		shape.setTranslateY(y*gridHeight);
		this.getChildren().add(shape);
	}
	
	public void add(Pane pane,int x, int y) {// panes are added to correct position according to superpane
		pane.setTranslateX(x*gridWidth);
		pane.setTranslateY(y*gridHeight);
		this.getChildren().add(pane);
	}
}
