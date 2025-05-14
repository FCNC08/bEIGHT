package canvas.components.StandardComponents;

import canvas.LogicSubScene;
import canvas.components.FunctionalCanvasComponent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import util.Info;

public class SevenSegmentDisplay extends FunctionalCanvasComponent{

	protected static int StandardWidth_big = LogicSubScene.cross_distance * 8;

	protected static int StandardWidth_middle = LogicSubScene.cross_distance * 4;

	protected static int StandardWidth_small = LogicSubScene.cross_distance*2;
	
	protected boolean[] segments = new boolean[7];
	
	
	public SevenSegmentDisplay(int width, int height, int input_count, int output_count, String size)
			throws IllegalArgumentException {
		super(width, height, 4, 0, size);
	}

	@Override
	public void simulater() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void paintImage() {
		Canvas canvas = new Canvas();
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		double height = getHeight()/3;
		double width = getHeight()/15;
		
		
		
	}
	
	@Override
	protected void resetStandardImage() {
		paintImage();
	}

	@Override
	protected void createInfo() {
		info = new Info();
		info.setHeadline("7-Segment Display");
	}

	@Override
	protected void setVerilogString(short[] comp_count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setArduinoString(short[] comp_count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createLayerGate() {
		// TODO Auto-generated method stub
		
	}
}
