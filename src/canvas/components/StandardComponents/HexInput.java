package canvas.components.StandardComponents;


import canvas.components.FunctionalCanvasComponent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import util.Info;

public class HexInput extends FunctionalCanvasComponent{

	//Ratio: 44x28
	
	protected EventHandler<MouseEvent> click_handler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent me) {
			
		}
	};
	
	protected int current_number = 0;
	public HexInput(int width, int height, String size) throws IllegalArgumentException {
		super(width, height, 0, 4, size);
		
		paintImage();
		image_view.addEventFilter(MouseEvent.MOUSE_CLICKED, click_handler);
		
	}
	
	public void paintImage() {
		int counter = 0;
		for(int y = 0; y<4; y++) {
			for(int x = 0; x<4; x++) {
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
				counter++;
			}
		}
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

	@Override
	protected void createInfo() {
		info = new Info();
		info.setHeadline("HexInput");
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
