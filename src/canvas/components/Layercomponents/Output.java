package canvas.components.Layercomponents;

import java.util.ListIterator;

import canvas.components.Dot;
import canvas.components.State;

public class Output extends Connection{
	Dot output;
	public Output(Dot output) {
		this.output = output;
	}
	
	@Override
	public void setState(State state) {
		if(state != this.state) {
			this.state = state;
			output.setState(state);
			for(LayerGate lg : connected_gates) {
				lg.simulate();
			}
		}else {
			System.out.println("test");
		}
	}
	
	@Override
	public void setConnectedLayerConnection(Connection con) {
		if(control_color) {
			return;
		}else {
			control_color = true;
			ListIterator<LayerGate> li = connected_gates.listIterator();
			while(li.hasNext()) {
				LayerGate lg = li.next();
				lg.addLayerInput(this, con);
			}
			output.setConnectedLayerConnection(con);
			control_color = false;
		}
	}
	@Override
	public void setConnectedLayerOutput(Connection con) {
		if(control_color) {
			return;
		}else {
			control_color = true;
			ListIterator<LayerGate> li = connected_gates.listIterator();
			while(li.hasNext()) {
				LayerGate lg = li.next();
				lg.setLayerOutput(this, con);
			}
			control_color = false;
		}
	}
	
}
