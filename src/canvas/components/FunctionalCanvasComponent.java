package canvas.components;

import java.util.HashMap;

import canvas.LogicSubScene;
import javafx.scene.image.ImageView;

public abstract class FunctionalCanvasComponent extends CanvasComponent{
	public static final byte  SIZE_BIG = 2;
	public static final byte SIZE_MIDDLE = 1;
	public static final byte SIZE_SMALL = 0;
	
	protected static int StandardWidth_big = 200, StandardHeight_big = 200; 
	
	protected static int StandardWidth_middle = 100, StandardHeight_middle = 100;
	
	protected static int StandardWidth_small = 50, StandardHeight_small = 50; 
	
	protected byte Size;
	
	protected HashMap<State[], State[]> truth_table;
	
	protected int input_count;
	protected int output_count;
	
	public Dot[] inputs;
	public Dot[] outputs;
	
	public FunctionalCanvasComponent(byte size,int width, int height,int input_count, int output_count) {
		super(width, height);
		
		this.Size = size;
		
		this.input_count = input_count;
		this.output_count = output_count;
		
		//Initializing each Dot 
		inputs = new Dot[input_count];
		outputs = new Dot[output_count];
		for(int i = 0; i<input_count; i++) {
			inputs[i] = new Dot(this);
		}
		for(int i = 0; i<output_count; i++) {
			outputs[i] = new Dot(this);
		}
	}

	public static FunctionalCanvasComponent initImage(String url, int inputs, int outputs, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y ) {
		//Override in higher classes
		return null;
	}
	public static FunctionalCanvasComponent initImage(ImageView image, int input_count, int output_count, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y) {
		//Override in higher classes
		return null;
	}
	
	public void setStandardDotLocations() {
		//creates dot position depending of the width and the dot count
		//System.out.println(width);
		int distance = width/(inputs.length);
		int y = Y;
		int x= X;
		if(rotation==VERTICAL) {
			x+=distance*0.5;
			for(Dot d : inputs) {
				d.setX(x);
				d.setY(y);
				x+=distance;
			}
			distance = width/(outputs.length+1);
			x = X;
			x+=distance*0.5;
			y = Y+height;
			for(Dot d : outputs) {
				d.setX(x);
				d.setY(y);
				x+=distance;
			}
			
		}else {
			//System.out.println("Y: "+Y);
			//System.out.println("Y2: "+(Y+width));
			y+=distance*0.5;
			//System.out.println("distance: "+distance);
			for(Dot d : inputs) {
				//System.out.println("DOT"+x+" "+y);
				d.setX(x);
				d.setY(y);
				y+=distance;
			}
			
			distance = width/(outputs.length);
			x=X+height;
			y=Y;
			y+=distance*0.5;
			for(Dot d : outputs) {
				d.setX(x);
				d.setY(y);
				y+=distance;
			}
		}
	}
	
	public abstract void simulate();
	
	public abstract FunctionalCanvasComponent getClone(byte size);
	
	protected State[] getInputStates() {
		//Getting all InputStates of each dot
		State[] states = new State[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			states[i] = inputs[i].getState();
		}
		return states;
	}
	
	protected void setOutputStates(State[] states) {
		//Setting all InputStates of each dot
		if(states.length != outputs.length) {
			throw new IllegalArgumentException();
		}else {
			for(int i = 0; i < states.length; i++) {
				outputs[i].setState(states[i]);
			}
		}
	}
	
	//Setter/Getter for X/Y position
	@Override
	public void addX(int X_coord){
		this.X=this.X+X_coord;
		this.point_X = (X_coord+LogicSubScene.wire_height/2)/LogicSubScene.cross_distance;
		image_view.setLayoutX(image_view.getLayoutX()+X_coord);
		for(Dot d: inputs) {
			d.addX(X_coord);
		}
		for(Dot d: outputs) {
			d.addX(X_coord);
		}
	}
	@Override
	public void addY(int Y_coord) {
		this.Y=this.Y+Y_coord;
		this.point_Y = (Y_coord+LogicSubScene.wire_height/2)/LogicSubScene.cross_distance;
		image_view.setLayoutY(image_view.getLayoutY()+Y_coord);
		for(Dot d: inputs) {
			d.addY(Y_coord);
		}
		for(Dot d: inputs) {
			d.addY(Y_coord);
		}
	}
	@Override
	public void setX(int X_coord){
		this.X=X_coord;
		this.point_X = (X_coord+LogicSubScene.wire_height/2)/LogicSubScene.cross_distance;
		image_view.setLayoutX(X_coord);
		setStandardDotLocations();		
	}
	@Override
	public void setY(int Y_coord) {
		this.Y=Y_coord;
		this.point_Y = (Y_coord+LogicSubScene.wire_height/2)/LogicSubScene.cross_distance;
		image_view.setLayoutY(Y_coord);
		setStandardDotLocations();
	}
	
	//Setter/Getter for X/Y position in Dots
	@Override
	public void setXPoint(int point_x) {
		this.X = point_x*LogicSubScene.cross_distance;
		this.point_X = point_x;
		image_view.setLayoutX(image_view.getLayoutX()+X);
		setStandardDotLocations();
	}
	@Override
	public void setYPoint(int point_y) {
		this.Y = point_y*LogicSubScene.cross_distance;
		this.point_Y = point_y;
		image_view.setLayoutX(image_view.getLayoutX()+X);
		setStandardDotLocations();
	}
	
	//Overrides method to change dot location
	@Override
	public void setRotation(boolean New_Rotation) {
		if(New_Rotation != rotation)
		{		if(New_Rotation == VERTICAL){
			//Changing Location relative to rotation
			setX((int) (0.5*width-0.5*height));
			setY((int) (-0.5*width+0.5*height));
			
			}else if(New_Rotation == HORIZONTAL) {
				setX((int) (-0.5*width+0.5*height));
				setY((int) (0.5*width-0.5*height));
			}
		}
		
		rotation = New_Rotation;
		image_view.setRotate(getRotationDegree());
		setStandardDotLocations();
	}
	

	@Override
	protected void createImageView() {
		//Creating ImageView with changing the direction of the ImageView
		image_view= new ImageView();
		image_view.setImage(this);
		image_view.setRotate(getRotationDegree());
		image_view.setLayoutX(X);
		image_view.setLayoutY(Y);
		
		if(rotation == CanvasComponent.VERTICAL) {
			image_view.setLayoutY(image_view.getLayoutY()+0.5*width-0.5*getHeight());
			image_view.setLayoutX(image_view.getLayoutX()-0.5*width+0.5*getHeight());
		}
	}
}
