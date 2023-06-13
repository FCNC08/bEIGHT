package canvas.components;

import canvas.LogicSubScene;

public class Dot  extends SingleCanvasComponent{
	
	FunctionalCanvasComponent parent;
	
	public Dot(FunctionalCanvasComponent parent) {
		super(LogicSubScene.wire_height/2, LogicSubScene.wire_height/2);
		this.parent =  parent;
	}
	
	protected void change() {
		parent.simulate();
	}

	public void setFocus(boolean status) {
		// TODO Auto-generated method stub
		
	}

	protected void createImageView() {
		
	}
	
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
