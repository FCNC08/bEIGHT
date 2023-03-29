package canvas.components;

public class Dot  extends SingleCanvasComponent{
	
	FunctionalCanvasComponent parent;
	
	public Dot(FunctionalCanvasComponent parent) {
		super(3, 3);
		this.parent =  parent;
	}
	
	@Override
	protected void change() {
		parent.simulate();
	}
	

}
