package canvas.components;

import canvas.LogicSubScene;
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
		
	}
	
	//Painting Circleimage
	protected void paintCircle() {
		Circle c = new Circle();
		c.setFill(getColor());
		c.setRadius(height/2);
		c.setCenterX(width/2);
		c.setCenterY(height/2);
		c.snapshot(null, this);
		c = null;
	}
	

}
