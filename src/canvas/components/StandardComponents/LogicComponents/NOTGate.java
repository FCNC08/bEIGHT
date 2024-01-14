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

public class NOTGate extends LogicComponent {

	public static Image LogicComponent_Image = new Image("NOT.png");

	public NOTGate(byte size, int width, int height) throws IllegalArgumentException {
		super(size, width, height, 1);
	}

	public static void setStandardImage(Image standard_image) {
		LogicComponent_Image = standard_image;
	}

	public static NOTGate getNOTGATE(byte size) throws IllegalArgumentException {
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
		NOTGate component = new NOTGate(size, width, height);
		ImageView temp_view = new ImageView(LogicComponent_Image);
		temp_view.setFitHeight(height);
		temp_view.setFitWidth(width);
		temp_view.snapshot(null, component);
		temp_view = null;
		// Removing all background/Color of the pixel 0|0
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
		reader = null;
		writer = null;
		System.gc();
		return component;
	}

	public static NOTGate getSolidNOTGATE(byte size) throws IllegalArgumentException {
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
		NOTGate component = new NOTGate(size, width, height);
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
			boolean bool = input[0].getStateBoolean();
			outputs[0].setState(State.getState(State.STANDARD_MODE, !bool ? State.ON_ERROR : State.OFF_UNSET));
		} catch (ErrorStateExeption e) {
			for (Dot o : outputs) {
				o.setState(State.getState(State.ERROR_MODE, State.ON_ERROR));
			}
		}

	}

	@Override
	public LogicComponent getClone(byte size) {
		// Function to clone a component in this case a ANDGate
		NOTGate gate = null;
		try {
			gate = NOTGate.getNOTGATE(size);
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
}
