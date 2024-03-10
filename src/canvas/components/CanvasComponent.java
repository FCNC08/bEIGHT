package canvas.components;

import canvas.LogicSubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class CanvasComponent extends WritableImage {

	// rotation Modes
	public static boolean HORIZONTAL = true;
	public static boolean VERTICAL = false;

	// Standard States
	public static State OFF = State.OFF;
	public static State ON = State.ON;
	public static State UNSET = State.UNSET;
	public static State ERR = State.ERROR;

	// Objects rotation, id
	public boolean rotation;

	// Is Component focused
	protected boolean focus;

	// SetState for missing double simulation
	protected boolean set_state = false;

	// ImageView to add to LogicSubScene
	protected ImageView image_view;

	// PixelWriter to paint Pixels
	protected PixelWriter pwriter;
	
	//name in verilogfile
	public String verilog_string;

	// Height and width, X and Y offset
	public int width;
	public int height;

	public int point_width;
	public int point_height;

	public int X = 0;
	public int Y = 0;

	// Point_x and Point_y (which point they lay on)
	public int point_X = 0;
	public int point_Y = 0;

	public int point_X_rest = 0;
	public int point_Y_rest = 0;
	

	// Constructor
	public CanvasComponent(int NewWidth, int NewHeight) {
		super(NewWidth, NewHeight);
		width = NewWidth;
		height = NewHeight;
		point_width = width / LogicSubScene.cross_distance;
		point_height = height / LogicSubScene.cross_distance;

		pwriter = getPixelWriter();
		// Adding standard values
		rotation = HORIZONTAL;
		this.createImageView();
		image_view = new ImageView(this);
		createImageView();
	}

	// Function to init a CanvasComponent with an ImageURL
	public static CanvasComponent initImage(String url) {
		// Override in higher classes
		return null;
	}

	// Method to set a image width URL
	public void setImage(String url) {
		Image temp_img = new Image(url);
		ImageView temp_view = new ImageView(temp_img);
		temp_view.snapshot(null, this);
		temp_img = null;
		temp_view = null;
		System.gc();
	}

	// Method to set a image
	public void setImage(Image image) {
		ImageView temp_view = new ImageView(image);
		temp_view.snapshot(null, this);
		temp_view = null;
		System.gc();
	}

	// Cleaning all Pixels
	public void clearPixels() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pwriter.setColor(x, y, Color.TRANSPARENT);
			}
		}
	}

	// Setter/Getter setState for simulation
	public void setSetState(boolean new_set_state) {
		set_state = new_set_state;
	}

	public boolean getSetState() {
		return set_state;
	}

	public abstract void setFocus(boolean status);

	// Setter/Getter for X/Y position
	public void addX(int X_coord) {
		int overflow = (X + X_coord + point_X_rest) % LogicSubScene.cross_distance;
		if (overflow <= LogicSubScene.cross_distance) {
			this.X = (X + X_coord + point_X_rest) - overflow;
			this.point_X_rest = overflow;
		} else {
			this.X = (X + X_coord + point_X_rest) + LogicSubScene.cross_distance - overflow;
			this.point_X_rest = -overflow;
		}
		this.point_X = X / LogicSubScene.cross_distance;
		image_view.setLayoutX(X);
	}

	public void addY(int Y_coord) {
		int overflow = (Y + Y_coord + point_Y_rest) % LogicSubScene.cross_distance;
		if (overflow <= LogicSubScene.cross_distance) {
			this.Y = (Y + Y_coord + point_Y_rest) - overflow;
			this.point_Y_rest = overflow;
		} else {
			this.Y = (Y + Y_coord + point_Y_rest) + LogicSubScene.cross_distance - overflow;
			this.point_Y_rest = -overflow;
		}
		this.point_Y = Y / LogicSubScene.cross_distance;
		image_view.setLayoutY(Y);
	}
	
	public void addXPoint(int X_point) {
		this.point_X+=X_point;
		this.X += X_point*LogicSubScene.cross_distance;
		image_view.setLayoutX(point_X);
	}
	
	public void addYPoint(int Y_point) {
		this.point_Y+=Y_point;
		this.Y += Y_point*LogicSubScene.cross_distance;
		image_view.setLayoutY(Y);
	}

	public void setX(int X_coord) {
		int overflow = (X_coord) % LogicSubScene.cross_distance;
		if (overflow <= LogicSubScene.cross_distance) {
			this.X = (X_coord) - overflow;
			this.point_X_rest = 0;
		} else {
			this.X = (X_coord) + LogicSubScene.cross_distance - overflow;
			this.point_X_rest = 0;
		}
		this.point_X = X / LogicSubScene.cross_distance;
		image_view.setLayoutX(X);
	}

	public void setY(int Y_coord) {
		int overflow = (Y_coord) % LogicSubScene.cross_distance;
		if (overflow <= LogicSubScene.cross_distance) {
			this.Y = (Y_coord) - overflow;
			this.point_Y_rest = 0;
		} else {
			this.Y = (Y_coord) + LogicSubScene.cross_distance - overflow;
			this.point_Y_rest = 0;
		}
		this.point_Y = Y / LogicSubScene.cross_distance;
		image_view.setLayoutY(Y);
	}

	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

	// Setter/Getter for X/Y position in Points
	public void setXPoint(int point_x) {
		this.X = point_x * LogicSubScene.cross_distance;
		this.point_X = point_x;
		image_view.setLayoutX(image_view.getLayoutX() + X);
	}

	public void setYPoint(int point_y) {
		this.Y = point_y * LogicSubScene.cross_distance;
		this.point_Y = point_y;
		image_view.setLayoutX(image_view.getLayoutX() + X);
	}

	public int getXPoint() {
		return point_X;
	}

	public int getYPoint() {
		return point_Y;
	}

	public int getWidthPoint() {
		return point_width;
	}

	public int getHeightPoint() {
		return point_height;
	}

	// Setter/Getter for rotation
	public void setRotation(boolean New_Rotation) {
		rotation = New_Rotation;
		changeRotation();
	}

	protected int getRotationDegree() {
		if (rotation) {
			return 0;
		} else {
			return 90;
		}
	}
	protected abstract void changeRotation();

	public boolean getRotation() {
		return rotation;
	}

	protected abstract void createImageView();

	// Getting ImageView to add to LogicSubScene
	public ImageView getImageView() {
		return image_view;
	}

}
