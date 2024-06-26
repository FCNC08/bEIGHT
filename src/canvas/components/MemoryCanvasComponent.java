package canvas.components;

import canvas.LogicSubScene;

public abstract class MemoryCanvasComponent extends FunctionalCanvasComponent {
	
	protected static int StandardWidth_big = LogicSubScene.cross_distance * 20, StandardHeight_big = LogicSubScene.cross_distance * 8;

	protected static int StandardWidth_middle = LogicSubScene.cross_distance * 17, StandardHeight_middle = LogicSubScene.cross_distance * 4;

	protected static int StandardWidth_small = LogicSubScene.cross_distance * 9, StandardHeight_small = LogicSubScene.cross_distance * 2;
	
	public State[][] memory;
	public int memory_width;
	public int memory_height;
	protected String name;

	public MemoryCanvasComponent(String size, int width, int height, int memory_width, int memory_height) throws IllegalArgumentException {
		super(width, height, memory_width + memory_height+1, memory_width, size);
		memory = new State[memory_height][memory_width];
		for(int x = 0; x<memory_height; x++) {
			for(int y = 0; y<memory_width; y++) {
				memory[x][y] = State.UNSET;
			}
		}
		this.memory_width = memory_width;
		this.memory_height = memory_height;
		resetStandardImage();
	}

}
