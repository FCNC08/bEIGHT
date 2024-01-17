package canvas.components;

import canvas.LogicSubScene;
import javafx.scene.image.Image;

public abstract class LogicComponent extends FunctionalCanvasComponent {
	
	protected static int StandardWidth_big = LogicSubScene.cross_distance * 8, StandardHeight_big = LogicSubScene.cross_distance * 8;

	protected static int StandardWidth_middle = LogicSubScene.cross_distance * 4, StandardHeight_middle = LogicSubScene.cross_distance * 4;

	protected static int StandardWidth_small = LogicSubScene.cross_distance * 2, StandardHeight_small = LogicSubScene.cross_distance * 2;

	public LogicComponent(byte size, int width, int height, int input_count) throws IllegalArgumentException {
		super(width, height, input_count, 1);
		// TODO Auto-generated constructor stub
	}

	public static void setStandardImage(Image standard_image) {
		// Override in higher classes
	}

}
