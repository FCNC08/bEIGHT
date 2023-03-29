package canvas.components;

import canvas.LogicSubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

public class CanvasComponent extends WritableImage{
	
	//rotation Modes
	public static boolean HORIZONTAL = true;
	public static boolean VERTICAL = false;
	
	//Standard States
	public static State OFF = new State(State.STANDARD_MODE, State.OFF_UNSET);
	public static State ON = new State(State.STANDARD_MODE, State.ON_ERROR);
	public static State UNSET = new State(State.ERROR_MODE, State.OFF_UNSET);
	public static State ERR = new State(State.ERROR_MODE, State.ON_ERROR);
	
	//Objects rotation, id
	public boolean rotation;  
	
	protected short id;
	
	//SetState for missing double simulation
	protected boolean set_state = false;
	
	//ImageView to add to LogicSubScene
	protected ImageView image_view;
	
	//Height and width, X and Y offset
	public int width;
	public int height;

	public int X = 0;
	public int Y = 0;
	
	//Point_x and Point_y (which point they lay on)
	public int point_X = 0;
	public int point_Y = 0;
	
	//Constructor
	public CanvasComponent(int NewWidth, int NewHeight) {
		super(NewWidth, NewHeight);
		width = NewWidth;
		height = NewHeight;
		
		//Adding standard values
		rotation = HORIZONTAL;
		image_view = new ImageView();
		image_view.setImage(this);
		
	}
	
	//Function to init a CanvasComponent with an ImageURL
	public static CanvasComponent initImage(String url) {
		Image temp_img = new Image(url);
		CanvasComponent component = new CanvasComponent((int) temp_img.getWidth(), (int) temp_img.getHeight());
		component.setImage(temp_img);
		return component;
	}
	
	//Method to set a image width URL
	public void setImage(String url) {
		Image temp_img = new Image(url);
		ImageView temp_view = new ImageView(temp_img);
		temp_view.snapshot(null, this);
		temp_img = null;
		temp_view = null;
		System.gc();
	}
	
	//Method to set a image 
	public void setImage(Image image) {
		ImageView temp_view = new ImageView(image);
		temp_view.snapshot(null, this);
		temp_view = null;
		System.gc();
	}
	
	//Setter/Getter setState for simulation
	public void setSetState(boolean new_set_state) {
		set_state = new_set_state;
	}
	public boolean getSetState() {
		return set_state;
	}
	
	//Setter/Getter for X/Y position
	public void setX(int X_coord){
		this.X=X_coord;
		this.point_X = X_coord/LogicSubScene.cross_distance;
		image_view.setLayoutX(image_view.getLayoutX()+X_coord);
	}
	public void setY(int Y_coord) {
		this.Y=Y_coord;
		this.point_Y = Y_coord/LogicSubScene.cross_distance;
		image_view.setLayoutY(image_view.getLayoutY()+Y_coord);
	}
	public int getX() {
		return X;
	}
	public int getY() {
		return Y;
	}
	
	//Setter/Getter for X/Y position in Dots
	public void setXPoint(int point_x) {
		this.X = point_x*LogicSubScene.cross_distance;
		this.point_X = point_x;
		image_view.setLayoutX(image_view.getLayoutX()+X);
	}
	public void setYPoint(int point_y) {
		this.Y = point_y*LogicSubScene.cross_distance;
		this.point_Y = point_y;
		image_view.setLayoutX(image_view.getLayoutX()+X);
	}
	public int getXPoint() {
		return point_X;
	}
	public int getYPoint() {
		return point_Y;
	}
	
	//Setter/Getter for ID (in a Hashmap)
	public void setId(short ID){
		id = ID;
	}
	public short getId() {
		return id;
	}
	
	//Setter/Getter for rotation
	public void setRotation(boolean New_Rotation) {
		if(New_Rotation != rotation && New_Rotation == VERTICAL){
			//Changing Location relative to rotation
			image_view.setLayoutX(image_view.getLayoutX()+0.5*width-0.5*height);
			image_view.setLayoutY(image_view.getLayoutY()-0.5*width+0.5*height);
			
		}else if(New_Rotation != rotation && New_Rotation == HORIZONTAL) {
			image_view.setLayoutX(image_view.getLayoutX()-0.5*width+0.5*height);
			image_view.setLayoutY(image_view.getLayoutY()+0.5*width-0.5*height);
		}
		
		rotation = New_Rotation;
		image_view.setRotate(getRotation());
	}
	protected int getRotation() {
		if(rotation) {
			return 0;
		}else {
			return 90;
		}
	}
	
	//Getting ImageView to add to LogicSubScene
	public ImageView getImageView() {
		return image_view;
	}

}
