package canvas.components.StandardComponents.LogicComponents;

import canvas.components.Dot;
import canvas.components.LogicComponent;
import canvas.components.State;
import canvas.components.Layercomponents.XOR;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import util.ErrorStateExeption;
import util.Info;

public class XORGate extends LogicComponent {

	public static Image LogicComponent_Image = new Image("XOR.png");

	public XORGate(String size, int width, int height, int input_count) throws IllegalArgumentException {
		super(size, width, height, input_count);
	}

	public static void setStandardImage(Image standard_image) {
		LogicComponent_Image = standard_image;
	}

	public static XORGate getXORGate(String size, int inputs) throws IllegalArgumentException {
		// Creating a ANDGate with standard sizes
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
		XORGate component = new XORGate(size, width, height, inputs);
		ImageView temp_view = new ImageView(LogicComponent_Image);
		temp_view.setFitHeight(height);
		temp_view.setFitWidth(width);
		temp_view.snapshot(null, component);
		temp_view = null;
		// Removing all background/Color of the pixel 0|0
		PixelReader reader = component.getPixelReader();
		PixelWriter writer = component.getPixelWriter();
		Color maincolor = reader.getColor(0, 0);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (!maincolor.equals(reader.getColor(x, y))) {
					writer.setColor(x, y, Color.TRANSPARENT);
				}
			}
		}
		reader = null;
		writer = null;
		System.gc();
		return component;
	}

	public static XORGate getSolidXORGATE(String size, int inputs) throws IllegalArgumentException {
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
		XORGate component = new XORGate(size, width, height, inputs);
		ImageView temp_view = new ImageView(LogicComponent_Image);
		temp_view.setFitHeight(height);
		temp_view.setFitWidth(width);
		temp_view.snapshot(null, component);
		temp_view = null;
		System.gc();
		return component;
	}

	@Override
	public void simulater() {
		State[] input = getInputStates();
		try {
			boolean[] bool_states = new boolean[input.length];
			for (int i = 0; i < input.length; i++) {
				bool_states[i] = input[i].getStateBoolean();
			}
			boolean output_state = false;
			for (boolean b : bool_states) {
				output_state = output_state ^ b;
			}
			for (Dot d : outputs) {
				d.setState(State.getState(State.STANDARD_MODE, output_state ? State.ON_ERROR : State.OFF_UNSET));
			}
		} catch (ErrorStateExeption e) {
			for (Dot o : outputs) {
				o.setState(State.getState(State.ERROR_MODE, State.ON_ERROR));
			}
		}

	}

	@Override
	public LogicComponent getClone(String size) {
		// Function to clone a component in this case a ANDGate
		XORGate gate = null;
		try {
			gate = XORGate.getXORGate(size, input_count);
		} catch (IllegalArgumentException e) {
		}
		return gate;
	}

	@Override
	protected void resetStandardImage() {
		ImageView temp_view = new ImageView(LogicComponent_Image);
		temp_view.setFitHeight(height);
		temp_view.setFitWidth(width);
		temp_view.snapshot(null, this);
		// Removing all background/Color of the pixel 0|0
		PixelReader reader = getPixelReader();
		PixelWriter writer = getPixelWriter();
		Color background = reader.getColor(width / 2, height / 2);
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
		info.setHeadline("XOR-Gate");
	}
	
	@Override
	protected void setVerilogString(short[] comp_count) {
		verilog_string = "XOR"+comp_count[10];
		comp_count[10]++;
	}
	
	@Override
	protected void setArduinoString(short[] comp_count) {
		arduino_string = "XOR"+comp_count[10];
		comp_count[10]++;
	}

	@Override
	public void createLayerGate() {
		gate = new XOR(input_count);
		if(output!=null) {
			if(output[0]!=null) {
				gate.outputs[0] = output[0]; 
				output[0].addInputGate(gate);
			}
		}
		outputs[0].setConnectedLayerConnection(gate.outputs[0]);
	}
}
