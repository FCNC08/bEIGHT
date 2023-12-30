package canvas.components;

import java.util.HashMap;

import canvas.LogicSubScene;
import javafx.scene.image.ImageView;

public abstract class FunctionalCanvasComponent extends CanvasComponent{
	public static final byte  SIZE_BIG = 2;
	public static final byte SIZE_MIDDLE = 1;
	public static final byte SIZE_SMALL = 0;
	
	protected static int StandardWidth_big = 400, StandardHeight_big = 400; 
	
	protected static int StandardWidth_middle = 200, StandardHeight_middle = 200;
	
	protected static int StandardWidth_small = 100, StandardHeight_small = 100; 
	
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
	
	protected void setStandardDotLocations() {
		//creates dot position depending of the width and the dot count
		int distance = width/(inputs.length+1);
		int y = Y;
		int x= X;
		if(rotation==HORIZONTAL) {
			x+=distance*0.5;
			for(Dot d : inputs) {
				d.setX(x);
				d.setY(y);
				x+=distance;
			}
			distance = width/(outputs.length);
			x = X;
			x+=distance*0.5;
			y = Y+height;
			for(Dot d : outputs) {
				d.setX(x);
				d.setY(y);
				x+=distance;
			}
			
		}else {
			y+=distance*0.5;
			for(Dot d : inputs) {
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
}
