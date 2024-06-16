package canvas.components.Layercomponents;

import java.util.ArrayList;

import canvas.components.State;

public class Connection {
	protected State state = State.UNSET;
	protected ArrayList<LayerGate> connected_gates = new ArrayList<>();
	
	public Connection() {}
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
		for(LayerGate lg : connected_gates) {
			lg.simulate();
		}
	}

}
