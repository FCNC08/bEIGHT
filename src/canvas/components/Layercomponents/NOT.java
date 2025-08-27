package canvas.components.Layercomponents;

import org.json.JSONArray;
import org.json.JSONObject;

import canvas.components.State;
import util.ErrorStateException;

public class NOT extends LayerGate {

	public NOT() throws IllegalArgumentException {
		super(1, 1);
	}

	@Override
	public void simulater() {
		State[] input = getInputStates();
		try {
			boolean bool = input[0].getStateBoolean();
			outputs[0].setState(State.getState(State.STANDARD_MODE, !bool ? State.ON_ERROR : State.OFF_UNSET));
		} catch (ErrorStateException e) {
			outputs[0].setState(State.getState(State.ERROR_MODE, State.ON_ERROR));
			System.out.println("test");
		}
		System.out.println(outputs[0].state.getStateName());

	}
	@Override
	public void createLayerGate() {
		gate = new NOT();
		if(output!=null) {
			if(output[0]!=null) {
				gate.outputs[0] = output[0]; 
				output[0].addInputGate(gate);
			}
		}
		outputs[0].setConnectedLayerConnection(gate.outputs[0]);
	}
	
	@Override
	public void generateJSONObject() {
		object = new JSONObject();
		object.put("Name", "NOT");
		if(json_inputs == null) {
			json_inputs = new JSONArray();
		}
		json_inputs.put(0);
		object.put("inputs", json_inputs);
		object.put("outputs", json_output);
	}
}
