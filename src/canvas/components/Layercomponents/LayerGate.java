package canvas.components.Layercomponents;

import canvas.components.State;

public abstract class LayerGate {
	
	protected int input_count;
	protected int output_count;
	public Connection inputs[];
	public Connection outputs[];
	protected long[] times = new long[5];
	protected int time_pointer = 0;
	
	public LayerGate(int input_count, int output_count) {
		this.input_count = input_count;
		this.output_count = output_count;
		this.inputs = new Connection[input_count];
		this.outputs = new Connection[output_count];
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
		}else {
			System.out.println("act:"+actual_time+" lat:"+latest_time+"	"+this);
			simulater();
		}
	}
}
