package canvas.components.StandardComponents.LogicComponents;

import canvas.components.LogicComponent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ANDGate extends LogicComponent{

	public static Image LogicComponent_Image = new Image("AND.png");
	
	
	public ANDGate(int width, int height, int input_count, int output_count, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y) {
		super(width, height, input_count, output_count, inputs_x, inputs_y, outputs_x, outputs_y);
		// TODO Auto-generated constructor stub
	}
	
	public static void setStandardImage(Image standard_image) {
		//Override in higher classes
	}
	
	public static ANDGate getANDGATE(int inputs, int outputs, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y ) {
		ANDGate component = new ANDGate((int)LogicComponent_Image.getWidth(),(int)LogicComponent_Image.getHeight(), inputs, outputs, inputs_x, inputs_y, outputs_x, outputs_y);
		ImageView temp_view = new ImageView(LogicComponent_Image);
		temp_view.snapshot(null, component);
		temp_view = null;
		System.gc();
		return component;
	}

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}
}
