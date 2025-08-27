package canvas.components.ExternalComponents;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import canvas.components.State;
import util.ErrorStateException;

public class Truthtabel{
	protected int input_count;
	protected int output_count;
	protected State[] states;
	protected ArrayList<Outputvalue> truth_tabel = new ArrayList<>();
	
	public Truthtabel(JSONArray json_array) {
		
		for(Object o : json_array) {
			JSONObject json_object = (JSONObject) o;
			JSONArray json_input = json_object.getJSONArray("input");
			JSONArray json_output = json_object.getJSONArray("output");
			this.output_count = json_output.length();
			int[] output = new int[output_count];
			int max_state_size = 0;
			int i = 0;
			for(Object z : json_output) {
				int x = (int)z;
				output[i] = x;
				max_state_size = Math.max(max_state_size, x);
				i++;
			}
			this.input_count = json_input.length();
			i = 0;
			int[] input = new int[input_count];
			for(Object z : json_input) {
				int x = (int)z;
				input[i] = x;
				max_state_size = Math.max(max_state_size, x);
				i++;
			}
			max_state_size++;
			states = new State[max_state_size];
			for(i = 0; i<max_state_size; i++) {
				states[i] = State.OFF;
			}
			truth_tabel.add(new Outputvalue(input, output));
		}
		System.out.println(output_count);
	}
	public State[] getState(State[] input_states) throws ErrorStateException {
		State[] ret_value = new State[output_count];
		ArrayList<Outputvalue> ret_values = new ArrayList<>(truth_tabel);
		byte[] inputs = new byte[input_states.length];
		for(int i = 0; i<input_count; i++) {
			if(input_states[i].getStateBoolean()) {
				inputs[i] = 1;
			}else {
				inputs[i] = 0;
			}
		}
		for(int i = 0; i<input_count; i++) {
			for(int q = ret_values.size()-1; q>=0;q--) {
				Outputvalue ov = ret_values.get(q);
				if(!((ov.input[i] ==inputs[i])||(ov.input[i]>1))) {
					ret_values.remove(q);
				}
			}
			if(ret_values.size()<2) {
				break;
			}else if(ret_values.size()==0) {
				throw new IllegalArgumentException();
			}
		}
		int[] input = ret_values.get(0).input;
		for(int i = 0; i<input_count; i++) {
			if(input[i]>1) {
				states[input[i]] = input_states[i];
			}
		}
		int[] output = ret_values.get(0).output;
		for(int i = 0; i<output_count; i++) {
			if(output[i]>1) {
				ret_value[i] = states[output[i]];
			}else {
				if(output[i]==1) {
					ret_value[i] = State.getState(State.STANDARD_MODE, State.ON_ERROR);
				}else {
					ret_value[i] = State.getState(State.STANDARD_MODE, State.OFF_UNSET);
				}
			}
		}
		ret_values = null;
		inputs = null;
		input = null;
		output = null;
		System.gc();
		return ret_value;
	}
}
