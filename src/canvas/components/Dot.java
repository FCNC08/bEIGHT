package canvas.components;

import canvas.LogicSubScene;

public class Dot  extends SingleCanvasComponent{
	
	FunctionalCanvasComponent parent;
	
	public Dot(FunctionalCanvasComponent parent) {
		super(LogicSubScene.wire_height/2, LogicSubScene.wire_height/2);
		this.parent =  parent;
	}
	
	@Override
	protected void change() {
		parent.simulate();
	}

	@Override
	public void setFocus(boolean status) {
		// TODO Auto-generated method stub
		
	}
	

}
