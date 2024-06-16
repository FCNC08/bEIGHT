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
			outputs[0].setState(State.getState(State.STANDARD_MODE, !bool ? State.ON_ERROR : State.OFF_UNSET));
		} catch (ErrorStateExeption e) {
			outputs[0].setState(State.getState(State.ERROR_MODE, State.ON_ERROR));
			
		}

	}
}
