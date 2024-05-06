

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class RoadTile extends Pane {
	private int type; 
	private int rotate;
	private int gridx; 
	private int gridy; 
	
	public RoadTile(int type,int rotate,int gridx,int gridy) {
		setType(type);
		setRotate2(rotate);
		setGridx(gridx);
		setGridy(gridy);
	}  
	public void draw (MetaData meta) {
		if(type==0) {
			Rectangle x  = new Rectangle( 0, meta.getGridHeight()*0.1, meta.getGridWidth() , meta.getGridHeight() * 0.8 );					   
			Line line = new Line(0, (meta.getGridHeight())/2, meta.getGridWidth(), meta.getGridHeight()/2);

			x.setFill(Color.WHITE);	
			x.setStrokeWidth(0);
			line.setStroke(Color.BLACK);
		    
			this.getChildren().add(x);	
			this.getChildren().add(line);
			
			Rotate rotate = new Rotate();
			rotate.setPivotX(meta.getGridWidth()/2);
		    rotate.setPivotY(meta.getGridHeight()/2);
		    rotate.setAngle(this.rotate);
		    this.getTransforms().add(rotate);
		}
		else if ( type==1) {
			Arc arc = new Arc( 0, meta.getGridHeight(), meta.getGridHeight() * 0.9, meta.getGridHeight() * 0.9, 0,90 );
			Arc miniarc = new Arc(0, meta.getGridHeight(), meta.getGridWidth()*0.1 , meta.getGridHeight()*0.1, 0 ,90 );
			Arc mid = new Arc(0 , meta.getGridHeight(), meta.getGridWidth()/2, meta.getGridHeight()/2,0,90);

			arc.setStrokeWidth(0);
			arc.setFill(Color.WHITE);
			arc.setType(ArcType.ROUND);
			
			miniarc.setStrokeWidth(0);
			miniarc.setFill(Color.LIGHTBLUE);
			miniarc.setType(ArcType.ROUND);
			
			mid.setType(ArcType.OPEN);
			mid.setStroke(Color.BLACK);
			mid.setFill(Color.WHITE);
			
			this.getChildren().add(arc);
			this.getChildren().add(miniarc);
			this.getChildren().add(mid);
			
			Rotate rotate = new Rotate();
	        rotate.setPivotX(meta.getGridWidth()/2); 
	        rotate.setPivotY(meta.getGridHeight()/2); 
	        rotate.setAngle(-(this.rotate)); 

	        this.getTransforms().add(rotate);
		}
		else if ( type==2) {
			Rectangle x  = new Rectangle( 0, meta.getGridHeight()*0.1, meta.getGridWidth() , meta.getGridHeight() * 0.8 );					   
			Rectangle secondX  = new Rectangle( 0, meta.getGridHeight()*0.1, meta.getGridWidth() , meta.getGridHeight() * 0.8 );
			Circle circle = new Circle( meta.getGridWidth()/2, meta.getGridHeight()/2, meta.getGridWidth()*0.1);

			x.setStrokeWidth(0);
			x.setFill(Color.WHITE);
			secondX.setStrokeWidth(0);
			secondX.setRotate(90);
			secondX.setFill(Color.WHITE);
			circle.setStroke(Color.BLACK);
			circle.setFill(Color.WHITE);
			
			this.getChildren().add(x);	
			this.getChildren().add(secondX);
			this.getChildren().add(circle);
		}
		else if (type == 3){
			Rectangle x  = new Rectangle( 0, meta.getGridHeight()*0.1, meta.getGridWidth() , meta.getGridHeight() * 0.8 );					   
			Rectangle secondx  = new Rectangle( meta.getGridWidth()*0.1, meta.getGridHeight()*0.1, meta.getGridWidth()*0.8 , meta.getGridHeight() );					   
			Circle circle = new Circle( meta.getGridWidth()/2, meta.getGridHeight()/2, meta.getGridWidth()*0.1);

			x.setStrokeWidth(0);
			x.setFill(Color.WHITE);
			secondx.setStrokeWidth(0);
			secondx.setFill(Color.WHITE);
			circle.setStroke(Color.BLACK);
			circle.setFill(Color.WHITE);
			
			this.getChildren().add(x);	
			this.getChildren().add(secondx);
			this.getChildren().add(circle);
			
			Rotate rotate = new Rotate();
	        rotate.setPivotX(meta.getGridWidth()/2); 
	        rotate.setPivotY(meta.getGridHeight()/2); 
	        rotate.setAngle(this.rotate); 
	        this.getTransforms().add(rotate); 
		}
	}

	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		try {
			if(type == 0 || type==1 || type==2 || type==3) {
				this.type = type;
			}
			else {
				throw new Exception();
			}
		}
		catch(Exception e) {
			System.out.println("Your roadtile type is not 0 or 1 or 2 or 3 !!!");
		}
		

	}
	public void setRotate2(int rotate) {
		try { 
			if(rotate == 0 || rotate == 90 || rotate == 180 || rotate == 270) {
				this.rotate = rotate;
			}
			
			else {
				throw new Exception();
			}
		}
		catch(Exception e) {
			System.out.println("Your roadtile rotate is not 0 or 90 or 180 or 270 !!!");
		}

	}
	public int getGridx() {
		return gridx;
	}
	public void setGridx(int gridx) {
		this.gridx = gridx;
	}
	public int getGridy() {
		return gridy;
	}
	public void setGridy(int gridy) {
		this.gridy = gridy;
	}
	

	

}
