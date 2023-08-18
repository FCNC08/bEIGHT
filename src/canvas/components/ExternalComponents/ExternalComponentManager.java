package canvas.components.ExternalComponents;

import util.IllegalComponentExeption;

public class ExternalComponentManager {
	private boolean imageUsed = false;
	private int width;
	private int height;
	private int inputs;
	private int outputs;
	private int[] input_x;
	private int[] input_y;
	private int[] output_x;
	private int[] output_y;
	
	public ExternalComponentManager(int temp_width, int temp_height, int[] temp_input_x, int[] temp_input_y, int[] temp_output_x, int[] temp_output_y, int temp_inputs, int temp_outputs) throws IllegalComponentExeption {
		this.imageUsed = true;
		this.width = temp_width;
		this.height = temp_height;
		this.input_x = temp_input_x;
		this.input_y = temp_input_y;
		this.output_x = temp_output_x;
		this.output_y = temp_output_y;
		this.inputs = temp_inputs;
		this.outputs = temp_outputs;
		
		if(input_x.length != inputs || inputs != input_y.length || output_x.length != outputs || outputs != output_y.length) {
			throw new IllegalArgumentException();
		}
		
	}
	public ExternalComponentManager(int inputs, int outputs){
		this.imageUsed = false;
		this.inputs = inputs;
		this.outputs = outputs;
	}
}
