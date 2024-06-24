package canvas.components.Layercomponents;

import canvas.components.State;
import util.ErrorStateExeption;

public class NOR extends LayerGate {

	public NOR(int input_count) throws IllegalArgumentException {
		super(input_count, 1);
	}

	@Override
	public void simulater() {
		State[] input = getInputStates();
		try {
			boolean[] bool_states = new boolean[input.length];
			for (int i = 0; i < input.length; i++) {
				bool_states[i] = input[i].getStateBoolean();
			}
			boolean output_state = false;
			for (boolean b : bool_states) {
				output_state = output_state || b;
			}
			output_state = !output_state;
			for (Connection d : outputs) {
				d.setState(State.getState(State.STANDARD_MODE, output_state ? State.ON_ERROR : State.OFF_UNSET));
			}
		} catch (ErrorStateExeption e) {
			for (Connection o : outputs) {
				o.setState(State.getState(State.ERROR_MODE, State.ON_ERROR));
			}
		}

	}
	
	@Override
	public void createLayerGate() {
		gate = new NOR(input_count);
		if(output!=null) {
			if(output[0]!=null) {
				gate.outputs[0] = output[0];
				output[0].addInputGate(gate); 
			}
		}
		outputs[0].setConnectedLayerConnection(gate.outputs[0]);
	}
}
