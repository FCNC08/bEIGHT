package canvas.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class LogicComponent extends FunctionalCanvasComponent{

	
	public LogicComponent(byte size, int width, int height, int input_count) {
		super(size,width, height, input_count, 1);
		// TODO Auto-generated constructor stub
	}
	
	public static void setStandardImage(Image standard_image) {
		//Override in higher classes
	}
	
}
