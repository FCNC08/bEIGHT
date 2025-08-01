package canvas.components.StandardComponents;


import canvas.components.FunctionalCanvasComponent;
import canvas.components.State;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import util.Info;

public class HexInput extends FunctionalCanvasComponent{

	//Ratio: 44x28
	
	protected EventHandler<MouseEvent> click_handler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent me) {
			double x = me.getX();
			double y = me.getY();
			int number = 0;
			
			if(y<height/4) {	
			}else if(y<2*height/4) {
				number = 4;
			}else if(y<3*height/4) {
				number = 8;
			}else {
				number = 12;
			}
			
			if(x<width/4) {
			}else if(x<2*width/4) {
				number += 1;
			}else if(x<3*width/4) {
				number += 2;
			}else {
				number += 3;
			}
			current_number = number;
			simulater();
			paintImage();
		}
	};
	
	protected int current_number = 0;
	public HexInput(int width, int height, String size) throws IllegalArgumentException {
		super(width, height, 0, 4, size);
		
		paintImage();
		image_view.addEventFilter(MouseEvent.MOUSE_CLICKED, click_handler);
		
	}
	
	public HexInput(int width, int height) {
		super(width, height, 0,0,"xsmall");
		
		current_number = 99;
		paintImage();
	}
	
	public void paintImage() {
		int counter = 0;
		for(int y = 0; y<4; y++) {
			for(int x = 0; x<4; x++) {
				if(counter == current_number) {
					Image image = new Image("/"+counter+".png", 0.9*width/4, 0.9*height/4, false, false);
					PixelReader preader = image.getPixelReader();
					int x_start = x*(width/4);
					int y_start = y*(height/4);
					int x_end = (int) (width/4);
					int y_end = (int) (height/4);
					

					for(int x1 = 0; x1<x_end; x1++) {
						for(int y1 = 0; y1<y_end;y1++) {
							pwriter.setColor(x_start+x1, y_start+y1, Color.DARKGRAY);
						}
					}
					

					x_end = (int) (0.965*width/4);
					y_end = (int) (0.955*height/4);
					
					for(int x1 = (int) (0.065*width/4); x1<x_end; x1++) {
						for(int y1 = (int) (0.055*height/4); y1<y_end;y1++) {
							pwriter.setColor(x_start+x1, y_start+y1, preader.getColor(x1-(int) (0.065*width/4), y1-(int) (0.055*height/4)));
						}
					}
				}else {
					Image image = new Image("/"+counter+".png", width/4, height/4, false, false);
					PixelReader preader = image.getPixelReader();
					int x_start = x*(width/4);
					int y_start = y*(height/4);
					int x_end = (width/4);
					int y_end = (height/4);
					for(int x1 = 0; x1<x_end; x1++) {
						for(int y1 = 0; y1<y_end;y1++) {
							pwriter.setColor(x_start+x1, y_start+y1, preader.getColor(x1, y1));
						}
					}
				}

				counter++;
			}
		}
	}

	@Override
	public void simulater() {
		State[] states = new State[4];
		int number = current_number;
		if(number>=8) {
			states[3] = State.ON;
			number-=8;
		}else {
			states[3] = State.OFF;
		}
		
		if(number>=4) {
			states[2] = State.ON;
			number-=4;
		}else {
			states[2] = State.OFF;
		}
		
		if(number>=2) {
			states[1] = State.ON;
			number-=2;
		}else {
			states[1] = State.OFF;
		}
		
		if(number==1) {
			states[0] = State.ON;
		}else {
			states[0] = State.OFF;
		}
		
		setOutputStates(states);
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		int height;
		int width;
		
		switch(size) {
		case SIZE_BIG:
			height = 440;
			width = 280;
			break;
		case SIZE_MIDDLE:
			height = 330;
			width = 210;
		default:
			height = 220;
			width = 140;
			break;
		}
		return new HexInput(width, height, size);
	}

	@Override
	protected void createInfo() {
		info = new Info();
		info.setHeadline("HexInput");
	}
	
	@Override
	public void setFocus(boolean focus) {
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

	@Override
	protected void resetStandardImage() {
		// TODO Auto-generated method stub
		
	}

}
