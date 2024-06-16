package canvas.components.Layercomponents;

import canvas.components.State;
import util.ErrorStateExeption;

public class NOT extends LayerGate {

	public NOT() throws IllegalArgumentException {
		super(1, 1);
	}

	@Override
	public void simulater() {
		State[] input = getInputStates();
		try {
			boolean bool = input[0].getStateBoolean();
			System.out.println(bool);
			outputs[0].setState(State.getState(State.STANDARD_MODE, !bool ? State.ON_ERROR : State.OFF_UNSET));
		} catch (ErrorStateExeption e) {
			outputs[0].setState(State.getState(State.ERROR_MODE, State.ON_ERROR));
			System.out.println("test");
		}

	}
	@Override
	public void createLayerGate() {
		gate = new NOT();
		if(output!=null) {
			if(output[0]!=null) {
				gate.outputs[0] = output[0]; 
			}
		}
		outputs[0].setConnectedLayerConnection(gate.outputs[0]);
	}
}
