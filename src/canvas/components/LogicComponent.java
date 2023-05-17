package canvas.components;

import javafx.scene.image.Image;

public abstract class LogicComponent extends FunctionalCanvasComponent{

	protected static double StandardWidth = 200;
	protected static double StandardHeight = 200;
	
	public LogicComponent(int width, int height, int input_count, int output_count, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y) {
		super(width, height, input_count, output_count, inputs_x, inputs_y, outputs_x, outputs_y);
		// TODO Auto-generated constructor stub
	}
	
	public static void setStandardImage(Image standard_image) {
		//Override in higher classes
	}
}
