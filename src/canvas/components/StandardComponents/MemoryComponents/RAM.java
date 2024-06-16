package canvas.components.StandardComponents.MemoryComponents;

import canvas.components.FunctionalCanvasComponent;
import canvas.components.MemoryCanvasComponent;
import canvas.components.State;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import util.ErrorStateExeption;
import util.Info;

public class RAM extends MemoryCanvasComponent {

	public static Image MemoryComponent_Image = new Image("RAM.png");

	public RAM(int width, int height, int memory_width, int memory_height) throws IllegalArgumentException {
		super(width, height, memory_width, memory_height);
	}

	@Override
	public void simulater() {
		State[] states = getInputStates();
		boolean set_state = false;
		try {
			set_state = states[0].getStateBoolean();
		} catch (ErrorStateExeption e) {
			e.printStackTrace();
		}
		if(set_state) {
			
		}
		
	}

	public static RAM get8bitRAM(String size, int inputs) throws IllegalArgumentException {
		// Creating a RAM with standard sizes
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
		// Creating it/painting the Image
		RAM component = new RAM(width, height, 8, inputs);
		ImageView temp_view = new ImageView(MemoryComponent_Image);
		temp_view.setFitHeight(height);
		temp_view.setFitWidth(width);
		temp_view.snapshot(null, component);
		temp_view = null;
		// Removing all background/Color of the pixel 0|0
		PixelReader reader = component.getPixelReader();
		PixelWriter writer = component.getPixelWriter();
		Color background = reader.getColor(width / 3, height / 3);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (background.equals(reader.getColor(x, y))) {
					writer.setColor(x, y, Color.TRANSPARENT);
				}
			}
		}
		reader = null;
		writer = null;
		System.gc();
		return component;
	}

	public static RAM getSolidRAM(String size, int inputs) throws IllegalArgumentException {
		// Creating like getANDGate without removing the background
		// Used in ComponentChooser
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
		RAM component = new RAM(width, height, 8, inputs);
		ImageView temp_view = new ImageView(MemoryComponent_Image);
		temp_view.setFitHeight(height);
		temp_view.setFitWidth(width);
		temp_view.snapshot(null, component);
		temp_view = null;
		System.gc();
		return component;
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		RAM ram = RAM.get8bitRAM(size, memory_height);
		return ram;
	}

	@Override
	protected void resetStandardImage() {
		ImageView temp_view = new ImageView(MemoryComponent_Image);
		temp_view.setFitHeight(height);
		temp_view.setFitWidth(width);
		temp_view.snapshot(null, this);
		// Removing all background/Color of the pixel 0|0
		PixelReader reader = getPixelReader();
		PixelWriter writer = getPixelWriter();
		Color background = reader.getColor(width / 3, height / 3);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (background.equals(reader.getColor(x, y))) {
					writer.setColor(x, y, Color.TRANSPARENT);
				}
			}
		}
		reader = null;
		writer = null;
		System.gc();
	}
	
	@Override
	protected void createInfo() {
		info = new Info();
		info.setHeadline("RAM");
	}
	
	@Override
	protected void setVerilogString(short[] comp_count) {
		verilog_string = "RAM"+comp_count[7];
		comp_count[7]++;
	}
	
	@Override
	protected void setArduinoString(short[] comp_count) {
		arduino_string = "RAM"+comp_count[7];
		comp_count[7]++;
	}

	@Override
	public void createLayerGate() {
		// TODO Auto-generated method stub
		
	}

}
