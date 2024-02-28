package canvas.components.StandardComponents.LogicComponents;

import canvas.components.Dot;
import canvas.components.LogicComponent;
import canvas.components.State;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import util.ErrorStateExeption;
import util.Info;

public class ORGate extends LogicComponent {

	public static Image LogicComponent_Image = new Image("/OR.png");

	public ORGate(String size, int width, int height, int input_count) throws IllegalArgumentException {
		super(size, width, height, input_count);
	}

	public static void setStandardImage(Image standard_image) {
		LogicComponent_Image = standard_image;
	}

	public static ORGate getORGATE(String size, int inputs) throws IllegalArgumentException {
		// Creating a ORGate with default sizes
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
		ORGate component = new ORGate(size, width, height, inputs);
		// Painting the StandardImage for ORGates in the WritableImage
		ImageView temp_view = new ImageView(LogicComponent_Image);
		temp_view.setFitHeight(height);
		temp_view.setFitWidth(width);
		temp_view.snapshot(null, component);
		temp_view = null;
		// Removing all Backgroundpixels (Color of pixel 1|1
		PixelReader reader = component.getPixelReader();
		PixelWriter writer = component.getPixelWriter();
		Color background = reader.getColor(width / 2, height / 2);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (background.equals(reader.getColor(x, y))) {
					writer.setColor(x, y, Color.TRANSPARENT);
				}
			}
		}
		System.gc();
		return component;
	}

	public static ORGate getSolidORGATE(String size, int inputs) throws IllegalArgumentException {
		// Creating a ORGate like getORGate without removing the Background used in
		// ComponentChooser
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
		ORGate component = new ORGate(size, width, height, inputs);
		ImageView temp_view = new ImageView(LogicComponent_Image);
		temp_view.setFitHeight(height);
		temp_view.setFitWidth(width);
		temp_view.snapshot(null, component);
		temp_view = null;
		System.gc();
		return component;
	}

	@Override
	public void simulate() {
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
		ORGate gate = null;
		try {
			gate = ORGate.getORGATE(size, input_count);
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
		info.setHeadline("OR-Gate");
	}
}
