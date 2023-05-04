package canvas.components;

import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class FunctionalCanvasComponent extends CanvasComponent{

	protected HashMap<State[], State[]> truth_table;
	
	protected int input_count;
	protected int output_count;
	
	public Dot[] inputs;
	public Dot[] outputs;
	
	protected int[] input_x;
	protected int[] input_y;
	protected int[] output_x;
	protected int[] output_y;
	
	public FunctionalCanvasComponent(int width, int height,int input_count, int output_count, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y) {
		super(width, height);
		
		this.input_count = input_count;
		this.output_count = output_count;
		
		inputs = new Dot[input_count];
		outputs = new Dot[output_count];
		
		input_x = inputs_x;
		input_y = inputs_y;
		output_x = outputs_x;
		output_y = outputs_y;
	}

	public static FunctionalCanvasComponent initImage(String url, int inputs, int outputs, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y ) {
		//Override in higher classes
		/*Image temp_img = new Image(url);
		FunctionalCanvasComponent component = new FunctionalCanvasComponent((int)temp_img.getWidth(),(int)temp_img.getHeight(), inputs, outputs, inputs_x, inputs_y, outputs_x, outputs_y);
		ImageView temp_view = new ImageView(temp_img);
		temp_view.snapshot(null, component);
		temp_img = null;
		temp_view = null;
		System.gc();
		return component;*/
		return null;
	}
	public static FunctionalCanvasComponent initImage(ImageView image, int input_count, int output_count, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y) {
		//Override in higher classes
		/*FunctionalCanvasComponent component = new FunctionalCanvasComponent(
				(int)image.getImage().getWidth(), (int) image.getImage().getHeight(), input_count, output_count, inputs_x, inputs_y, outputs_x, outputs_y );
		image.snapshot(null, component);
		return component;*/
		return null;
	}
	
	public abstract void simulate();
	
	protected State[] getInputStates() {
		State[] states = new State[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			states[i] = inputs[i].getState();
		}
		return states;
	}
	
	protected void setOutputStates(State[] states) {
		if(states.length != outputs.length) {
			throw new IllegalArgumentException();
		}else {
			for(int i = 0; i < states.length; i++) {
				outputs[i].setState(states[i]);
			}
		}
	}
}
