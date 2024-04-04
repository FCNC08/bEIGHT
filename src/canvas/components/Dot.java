package canvas.components;

import canvas.LogicSubScene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot extends SingleCanvasComponent {

	public FunctionalCanvasComponent parent;
	
	public String verilog_name;
	
	public String arduino_name;
	public String arduino_type;
	
	public static String arduino_input = "Input";
	public static String arduino_connection = "Connection";
	public static String arduino_output = "Output";
	
	// Initializing it with the size of a half wireheight
	public Dot(FunctionalCanvasComponent parent) {
		super(LogicSubScene.wire_height / 2, LogicSubScene.wire_height / 2);
		this.parent = parent;
		paintCircle();
	}

	// Changing the State of a dot changes => simulating the parent again
	protected void change() {
		paintCircle();
		parent.simulate();
		for (SingleCanvasComponent connected : connected_Components) {
			connected.setState(getState());
		}
	}

	public void setFocus(boolean status) {
	}

	public void setParent(FunctionalCanvasComponent new_parent) {
		this.parent = new_parent;
	}

	public FunctionalCanvasComponent getParent() {
		return parent;
	}

	protected void createImageView() {
		image_view = new ImageView();
		image_view.setImage(this);
		image_view.setRotate(getRotationDegree());
		image_view.setLayoutX(X);
		image_view.setLayoutY(Y);
	}

	// Painting Circleimage and removing background
	protected void paintCircle() {
		Circle c = new Circle();
		c.setFill(getColor());
		c.setRadius(height / 2);
		c.setCenterX(width / 2);
		c.setCenterY(height / 2);
		c.snapshot(null, this);
		c = null;
		PixelReader reader = getPixelReader();
		PixelWriter writer = getPixelWriter();

		Color background = reader.getColor(0, 0);
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
	public void setRotation(boolean New_Rotation) {
		if (New_Rotation != rotation && New_Rotation == VERTICAL) {
			// Changing Location relative to rotation
			setX((int) (0.5 * width - 0.5 * height));
			setY((int) (-0.5 * width + 0.5 * height));

		} else if (New_Rotation != rotation && New_Rotation == HORIZONTAL) {
			setX((int) (-0.5 * width + 0.5 * height));
			setY((int) (0.5 * width - 0.5 * height));
		}

		rotation = New_Rotation;
		image_view.setRotate(getRotationDegree());
	}

}
