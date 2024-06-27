package canvas.components.Layercomponents;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import canvas.components.State;
import util.PublicCount;

public abstract class LayerGate {
	
	protected int input_count;
	protected int output_count;
	public Connection inputs[];
	public Connection outputs[];
	protected long[] times = new long[5];
	protected int time_pointer = 0;
	protected boolean color = false;
	
	protected LayerGate gate;
	
	protected JSONObject object;
	protected JSONArray json_inputs;
	protected JSONArray json_output;
	
	public Output[] output;
	
	
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
			try {
				states[i] = inputs[i].getState();
			}catch(NullPointerException npe) {
				states[i] = State.OFF;
			}
			
		}
		return states;
	}
	
	public abstract void simulater();
	
	public void simulate() {
		long actual_time = times[time_pointer] = System.currentTimeMillis();
		time_pointer++;
		time_pointer%=5;
		long latest_time = times[time_pointer];
		if(latest_time != 0 && (actual_time-latest_time)<10) {
			System.out.println("Blocked "+this);
		}else {
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
	public void setLayerOutput(Connection output_dot, Output output_connection) {
		int index = Arrays.asList(outputs).indexOf(output_dot);
		if(index != -1) {
			if(output == null) {
				output = new Output[output_count];
			}
			output[index] = output_connection;
		}
	}
	public void addLayerInput(Connection input_dot, Connection input_connection) {
		if(gate == null) {
			createLayerGate();
		}
		try {
			int index = Arrays.asList(inputs).indexOf(input_dot);
			if(index != -1) {
				gate.inputs[index] = input_connection;
				input_connection.addComponent(gate);
				System.out.println("Test");
			}else {
				System.out.println("Error");
			}
			
		}catch(NullPointerException e) {
			System.out.println(this);
		}
		
	}
	
	public void resetLayerComponent() {
		gate = null;
		output = null;
		for(Connection con : outputs) {
			con.resetLayerComponent();
		}
	}
	
	public abstract void generateJSONObject();
	
	public void generateJSON(PublicCount pc, JSONArray functionals, int number, Connection input_connection) {
		if(object == null) {
			generateJSONObject();
			if(json_output == null) {
				json_output = new JSONArray();
			}
			for(int i = 0; i<output_count; i++) {
				pc.increase();
				json_output.put(pc.getCount());
				outputs[i].generateConnectedJSON(pc, functionals, pc.getCount());
			}
			object.put("outputs", json_output);
			functionals.put(object);
		}
		json_inputs.put(Arrays.asList(inputs).indexOf(input_connection), number);
	}
	
	public void resetJSON() {
		gate = null;
		json_output = null;
		json_inputs = null;
		for(Connection con : outputs) {
			con.resetJSON();
		}
	}
}
