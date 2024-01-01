package canvas.components;

import canvas.LogicSubScene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot  extends SingleCanvasComponent{
	
	FunctionalCanvasComponent parent;
	
	//Initializing it with the size of a half wireheight
	public Dot(FunctionalCanvasComponent parent) {
		super(LogicSubScene.wire_height/2, LogicSubScene.wire_height/2);
		this.parent =  parent;
		paintCircle();
	}
	
	//Changing the State of a dot changes => simulating the parent again
	protected void change() {
		parent.simulate();
	}

	public void setFocus(boolean status) {
		// TODO Auto-generated method stub
		
	}

	protected void createImageView() {
		image_view= new ImageView();
		image_view.setImage(this);
		image_view.setRotate(getRotation());
		image_view.setLayoutX(X);
		image_view.setLayoutY(Y);
	}
	
	//Painting Circleimage and removing background
	protected void paintCircle() {
		Circle c = new Circle();
		c.setFill(getColor());
		c.setRadius(height/2);
		c.setCenterX(width/2);
		c.setCenterY(height/2);
		c.snapshot(null, this);
		c = null;
		PixelReader reader = getPixelReader();
		PixelWriter writer = getPixelWriter();
		
		Color background = reader.getColor(0, 0);
		for(int x = 0; x < width; x++) {
			for(int y = 0; y< height; y++) {
				if(background.equals(reader.getColor(x, y))) {
					writer.setColor(x, y, Color.TRANSPARENT);
				}
			}
		}
		reader = null;
		writer = null;
		System.gc();
	}
	

}
