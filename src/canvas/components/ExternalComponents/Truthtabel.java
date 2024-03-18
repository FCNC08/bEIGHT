package canvas.components.ExternalComponents;

import java.util.ArrayList;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONObject;

import canvas.components.State;
import util.ErrorStateExeption;

public class Truthtabel{
	protected int input_count;
	protected int output_count;
	protected State[] states;
	protected ArrayList<Outputvalue> truth_tabel;
	
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
			truth_tabel.add(new Outputvalue(input, output));
		}
	}
	public State[] getState(State[] input_states) throws ErrorStateExeption {
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
			ListIterator<Outputvalue> li = ret_values.listIterator();
			while(li.hasNext()) {
				Outputvalue ov = li.next();
				if((ov.input[i] !=inputs[i])||(ov.input[i]<2)) {
					ret_values.remove(ov);
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
