package canvas.components.StandardComponents;

import canvas.LogicSubScene;
import canvas.components.FunctionalCanvasComponent;
import canvas.components.State;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import util.ErrorStateExeption;
import util.Info;

public class SevenSegmentDisplay extends FunctionalCanvasComponent{

	//Proportions: 10/18
	
	protected boolean[] segments = new boolean[7];
	
	protected int number = 0;
	
	public SevenSegmentDisplay(int width, int height, String size)
			throws IllegalArgumentException {
		super(width, height, 4, 0, size);
		paintImage();
	}
	public SevenSegmentDisplay(int width, int height) {
		super(width, height, 0,0,"xsmall");
		paintImage();
	}

	@Override
	public void simulater() {
		State[] input = getInputStates();
		number = 0;
		try {
			for(int i = 0; i<input_count; i++) {
				number+=(input[i].getStateBoolean()?1:0)*Math.pow(2, i);
			}
		}catch(ErrorStateExeption e) {
			e.printStackTrace();
		}
		paintImage();
		System.out.println(number);
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		// Creating a ANDGate with standard sizes
		int height;
		int width;
		switch (size) {
		case SIZE_BIG:
			width = LogicSubScene.cross_distance*10;
			height = LogicSubScene.cross_distance*18;
			break;
		default:
			width = LogicSubScene.cross_distance*5;
			height = LogicSubScene.cross_distance*9;
			break;
		}
		SevenSegmentDisplay ssd = new SevenSegmentDisplay(width, height, size);
		return ssd;
	}
	
	protected void paintImage() {
		Canvas canvas = new Canvas(width, height);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		this.clearPixels();;
		
		double height1 = (getWidth())/10*6;
		double width1 = (getWidth())/5;
		Color background = LogicSubScene.black_grey;
		gc.setFill(background);
		gc.fillRect(0,0,width, height);
		if(number == 0 || number == 2 || number == 3 || (number>=5 && number!=11 && number!=13)) {
			gc.setFill(Color.WHITE);
		}else {
			gc.setFill(Color.DIMGRAY);
		}
		gc.fillRect(width1, 0, height1, width1);
		
		if(number==0 || (number>=4 && number<=6) || (number>=8 && number<=12) || number>=14) {
			gc.setFill(Color.WHITE);
		}else {
			gc.setFill(Color.DIMGRAY);
		}
		gc.fillRect(0, width1, width1, height1);

		if(number<=4 || (number>=7 && number<=10) || number==13) {
			gc.setFill(Color.WHITE);
		}else {
			gc.setFill(Color.DIMGRAY);
		}
		gc.fillRect(width1+height1, width1, width1, height1);

		if((number>=2 && number<=6) || (number>=8 && number<=11) || number>=13) {
			gc.setFill(Color.WHITE);
		}else {
			gc.setFill(Color.DIMGRAY);
		}
		gc.fillRect(width1, height1+width1, height1, width1);
		

		if(number==0 || number==2 || number == 6 || number == 8 || number >=10) {
			gc.setFill(Color.WHITE);
		}else {
			gc.setFill(Color.DIMGRAY);
		}
		gc.fillRect(0, height1+2*width1, width1, height1);

		if(number<=1 || (number>=3 && number<=11) || number==13) {
			gc.setFill(Color.WHITE);
		}else {
			gc.setFill(Color.DIMGRAY);
		}
		gc.fillRect(width1+height1, height1+2*width1, width1, height1);
		
		if(number==0 || number == 2 || number == 3 || number ==5 || number == 6 || number == 8 || number == 9 ||(number>=11 && number<=14)) {
			gc.setFill(Color.WHITE);
		}else {
			gc.setFill(Color.DIMGRAY);
		}
		gc.fillRect(width1, (height1+width1)*2, height1, width1);
		
		canvas.snapshot(new SnapshotParameters(), this);
		//Make the Background disappear
		/*PixelReader reader = getPixelReader();
		PixelWriter writer = getPixelWriter();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (background.equals(reader.getColor(x, y))) {
					writer.setColor(x, y, Color.TRANSPARENT);
				}
			}
		}*/
		
	}
	
	public void setNumber(int number) {
		this.number = number;
		paintImage();
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
