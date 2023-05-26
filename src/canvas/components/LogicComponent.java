package canvas.components;

import javafx.scene.image.Image;

public abstract class LogicComponent extends FunctionalCanvasComponent{

	public static final byte  SIZE_BIG = 2;
	public static final byte SIZE_MIDDLE = 1;
	public static final byte SIZE_SMALL = 0;
	
	protected static int StandardWidth_big = 400, StandardHeight_big = 400; 
	
	protected static int StandardWidth_middle = 200, StandardHeight_middle = 200;
	
	protected static int StandardWidth_small = 100, StandardHeight_small = 100; 
	
	protected byte Size;
	
	public LogicComponent(byte size, int width, int height, int input_count, int output_count, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y) {
		super(width, height, input_count, output_count, inputs_x, inputs_y, outputs_x, outputs_y);
		// TODO Auto-generated constructor stub
		this.Size = size;
	}
	
	public static void setStandardImage(Image standard_image) {
		//Override in higher classes
	}
}
