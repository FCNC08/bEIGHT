package canvas.components;

import canvas.LogicSubScene;
import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;

public abstract class MemoryCanvasComponent extends FunctionalCanvasComponent {
	
	protected static int StandardWidth_big = LogicSubScene.cross_distance * 20, StandardHeight_big = LogicSubScene.cross_distance * 8;

	protected static int StandardWidth_middle = LogicSubScene.cross_distance * 17, StandardHeight_middle = LogicSubScene.cross_distance * 4;

	protected static int StandardWidth_small = LogicSubScene.cross_distance * 9, StandardHeight_small = LogicSubScene.cross_distance * 2;
	
	public boolean[][] memory;
	public int memory_width;
	public int memory_height;
	protected String name;

	public MemoryCanvasComponent(int width, int height, int memory_width, int memory_height, int other_inputs) throws IllegalArgumentException {
		super(width, height, memory_width + memory_height, memory_height);
		memory = new boolean[memory_height][memory_width];
		this.memory_width = memory_width;
		this.memory_height = memory_height;
		resetStandardImage();
	}

}
