package canvas.components;

public abstract class MemoryCanvasComponent extends FunctionalCanvasComponent{
	public boolean[][] memory;
	
	public MemoryCanvasComponent(byte size, int width, int height, int memory_width, int memory_height) {
		super(size,width, height, memory_width, memory_height);
		memory = new boolean[memory_height][memory_width];
	}
	
}
