package canvas.components.StandardComponents.LogicComponents;

import canvas.components.CanvasComponent;
import canvas.components.LogicComponent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ORGate extends LogicComponent{

	public static Image LogicComponent_Image = new Image("OR.png");
	
	
	public ORGate(byte size, int width, int height, int input_count, int output_count) {
		super(size, width, height, input_count, output_count);
		// TODO Auto-generated constructor stub
	}
	
	public static void setStandardImage(Image standard_image) {
		LogicComponent_Image = standard_image;
	}
	
	public static ORGate getORGATE(byte size,int inputs, int outputs) {
		int height;
		int width;
		switch (size) {
        case SIZE_BIG:
            width = StandardWidth_big;
            height = StandardHeight_big;
            break;
        case SIZE_MIDDLE:
            width = StandardWidth_middle;
            height = StandardHeight_middle;
            break;
        case SIZE_SMALL:
            width = StandardWidth_small;
            height = StandardHeight_small;
            break;
        default:
            width = 1;
            height = 1;
            break;
		}
		ORGate component = new ORGate(size, width,  height, inputs, outputs);
		ImageView temp_view = new ImageView(LogicComponent_Image);
		temp_view.setFitHeight(height);
		temp_view.setFitWidth(width);
		temp_view.snapshot(null, component);
		temp_view = null;
		System.gc();
		return component;
	}

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocus(boolean status) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public LogicComponent getClone(byte size) {
		ORGate gate = ORGate.getORGATE(size, input_count, output_count);
		return gate;
	}
}
