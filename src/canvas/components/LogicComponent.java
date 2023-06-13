package canvas.components;

import javafx.scene.image.Image;

public abstract class LogicComponent extends FunctionalCanvasComponent{

	
	public LogicComponent(byte size, int width, int height, int input_count, int output_count) {
		super(size,width, height, input_count, output_count);
		// TODO Auto-generated constructor stub
	}
	
	public static void setStandardImage(Image standard_image) {
		//Override in higher classes
	}
	
}
