package canvas.components.ExternalComponents;

import canvas.components.FunctionalCanvasComponent;

public abstract class ExternalComponent extends FunctionalCanvasComponent{
	public ExternalComponent(byte size,int width, int height,int input_count, int output_count) {
		super(size, width, height, input_count, output_count);
		
	}

	@Override
	public void setFocus(boolean status) {
		// TODO Auto-generated method stub
		
	}
}
