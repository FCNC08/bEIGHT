package canvas.components.StandardComponents;

import canvas.LogicSubScene;
import canvas.components.FunctionalCanvasComponent;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import util.Info;

public class SevenSegmentDisplay extends FunctionalCanvasComponent{

	//Proportions: 10/18
	
	protected static int StandardWidth_big = LogicSubScene.cross_distance * 8;

	protected static int StandardWidth_middle = LogicSubScene.cross_distance * 4;

	protected static int StandardWidth_small = LogicSubScene.cross_distance*2;
	
	protected boolean[] segments = new boolean[7];
	
	
	public SevenSegmentDisplay(int width, int height, String size)
			throws IllegalArgumentException {
		super(width, height, 4, 1, size);
		paintImage();
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
		Canvas canvas = new Canvas(width, height);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		double height1 = getWidth()/10*6;
		double width1 = getHeight()/5;
		gc.setFill(Color.TRANSPARENT);
		gc.fillRect(0,0,width, height);
		gc.setFill(Color.BLACK);
		gc.fillRect(width1, 0, height1, width1);
		gc.fillRect(0, width1, width1, height1);
		gc.fillRect(width1+height1, width1, width1, height1);
		gc.fillRect(width1, height1+width1, height1, width1);
		gc.fillRect(0, height1+2*width1, width1, height1);
		gc.fillRect(width1+height1, height1+2*width1, width1, height1);
		gc.fillRect(width1, (height1+width1)*2, height1, width1);
		
		canvas.snapshot(new SnapshotParameters(), this);
		
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
