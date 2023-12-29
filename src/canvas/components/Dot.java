package canvas.components;

import canvas.LogicSubScene;

public class Dot  extends SingleCanvasComponent{
	
	FunctionalCanvasComponent parent;
	
	//Initializing it with the size of a half wireheight
	public Dot(FunctionalCanvasComponent parent) {
		super(LogicSubScene.wire_height/2, LogicSubScene.wire_height/2);
		this.parent =  parent;
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
		for(int x = 0; x <width; x++) {
			for(int y = 0; y < height; y++) {
				if(Math.sqrt(Math.abs(x-width/2)*Math.abs(x-width/2)+Math.abs(y-height/2)*Math.abs(y-height/2))<=width/2) {
					pwriter.setColor(x, y, getColor());
				}
			}
		}
	}
	

}
