package canvas.components.StandardComponents.LogicComponents;

import canvas.components.Dot;
import canvas.components.LogicComponent;
import canvas.components.State;
import canvas.components.Layercomponents.NOR;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import util.ErrorStateException;

public class NORGate extends LogicComponent {

	public static Image LogicComponent_Image = new Image("NOR.png");

	public NORGate(String size, int width, int height, int input_count) throws IllegalArgumentException {
		super(size, width, height, input_count);
	}

	public static void setStandardImage(Image standard_image) {
		LogicComponent_Image = standard_image;
	}

	public static NORGate getNORGATE(String size, int inputs) throws IllegalArgumentException {
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
		NORGate component = new NORGate(size, width, height, inputs);
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

	public static NORGate getSolidNORGATE(String size, int inputs) throws IllegalArgumentException {
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
		NORGate component = new NORGate(size, width, height, inputs);
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
				output_state = output_state || b;
			}
			output_state = !output_state;
			
			State[] output = new State[output_count];
			for (int i = 0; i<output_count; i++) {
				output[i] = State.getState(State.STANDARD_MODE, output_state ? State.ON_ERROR : State.OFF_UNSET);
			}
			setOutputStates(output);
		} catch (ErrorStateException e) {
			setOutputStates(ERROR_ARRAY);
		}

	}

	@Override
	public LogicComponent getClone(String size) {
		// Function to clone a component in this case a ANDGate
		NORGate gate = null;
		try {
			gate = NORGate.getNORGATE(size, input_count);
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
	protected void createContextMenu() {
		Label name_label = new Label("NOR-Gate");
		name_label.getStyleClass().add("cm-header");
		name_label.setMouseTransparent(true);
		CustomMenuItem name_item = new CustomMenuItem(name_label);
		name_item.getStyleClass().add("cm-header-item");
		menu.getItems().add(name_item);
		menu.getItems().add(new SeparatorMenuItem());
	}
	
	@Override
	protected void setVerilogString(short[] comp_count) {
		verilog_string = "NOR"+comp_count[3];
		comp_count[3]++;
	}
	@Override
	protected void setArduinoString(short[] comp_count) {
		arduino_string = "NOR"+comp_count[3];
		comp_count[3]++;
	}

	@Override
	public void createLayerGate() {
		gate = new NOR(input_count);
		if(output!=null) {
			if(output[0]!=null) {
				gate.outputs[0] = output[0]; 
				output[0].addInputGate(gate);
			}
		}
		outputs[0].setConnectedLayerConnection(gate.outputs[0]);
	}
}
