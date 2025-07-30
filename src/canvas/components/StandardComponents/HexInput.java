package canvas.components.StandardComponents;


import canvas.components.FunctionalCanvasComponent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class HexInput extends FunctionalCanvasComponent{

	protected static EventHandler<MouseEvent> click_handler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent me) {
			
		}
	};
	
	public HexInput(int width, int height, String size) throws IllegalArgumentException {
		super(width, height, 0, 4, size);
		
		paintImage();
		image_view.addEventFilter(MouseEvent.MOUSE_CLICKED, click_handler);
		
	}
	
	public void paintImage() {
		
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
		// TODO Auto-generated method stub
		
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
