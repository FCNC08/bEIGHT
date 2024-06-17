package canvas.components.Layercomponents;

import java.util.Arrays;

import canvas.components.State;

public abstract class LayerGate {
	
	protected int input_count;
	protected int output_count;
	public Connection inputs[];
	public Connection outputs[];
	protected long[] times = new long[5];
	protected int time_pointer = 0;
	protected boolean color = false;
	
	protected LayerGate gate;
	public Connection[] output;
	
	public LayerGate(int input_count, int output_count) {
		this.input_count = input_count;
		this.output_count = output_count;
		this.inputs = new Connection[input_count];
		this.outputs = new Connection[output_count];
		for(int i = 0; i<output_count; i++) {
			outputs[i] = new Connection();
		}
	}
	
	public State[] getInputStates() {
		State[] states = new State[input_count];
		for(int i = 0; i<input_count; i++) {
			states[i] = inputs[i].getState();
		}
		return states;
	}
	
	public abstract void simulater();
	
	public void simulate() {
		long actual_time = times[time_pointer] = System.currentTimeMillis();
		time_pointer++;
		time_pointer%=5;
		long latest_time = times[time_pointer];
		if(latest_time != 0 && (actual_time-latest_time)<100) {
			System.out.println("Blocked "+this);
			printEvery();
		}else {
			System.out.println("act:"+actual_time+" lat:"+latest_time+"	"+this);
			simulater();
		}
	}
	public void printEvery() {
		if(color) {
			
		}else {
			color = true;
			System.out.println(this);
			for(Connection out : outputs) {
				out.printEvery();
			}
			color = false;
		}
		
	}
	
	public abstract void createLayerGate();
	public void setLayerOutput(Connection output_dot, Connection output_connection) {
		int index = Arrays.asList(outputs).indexOf(output_dot);
		if(index != -1) {
			if(output == null) {
				output = new Connection[output_count];
			}
			output[index] = output_connection;
		}
	}
	public void addLayerInput(Connection input_dot, Connection input_connection) {
		if(gate == null) {
			createLayerGate();
		}
		try {
			gate.inputs[Arrays.asList(inputs).indexOf(input_dot)] = input_connection;
			input_connection.addComponent(gate);
			System.out.println("Test");
		}catch(NullPointerException e) {
			System.out.println(this);
		}
		
	}
}
