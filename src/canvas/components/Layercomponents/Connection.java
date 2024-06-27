package canvas.components.Layercomponents;

import java.util.ArrayList;
import java.util.ListIterator;

import org.json.JSONArray;

import canvas.components.State;
import util.PublicCount;

public class Connection {
	protected State state = State.UNSET;
	protected ArrayList<LayerGate> connected_gates = new ArrayList<>();
	protected boolean color = false;
	protected boolean control_color = false;
	
	public Connection() {}
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		if(state != this.state) {
			this.state = state;
			System.out.println(state.getStateName());
			for(LayerGate lg : connected_gates) {
				lg.simulate();
			}
		}else {
			/*try {
				System.out.println(state.getStateBoolean());
			} catch (ErrorStateExeption e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}
	
	public void addComponent(LayerGate lg) {
		connected_gates.add(lg);
	}
	
	public void printEvery() {
		if(color) {
		}else {
			color = true;
			System.out.println(this);
			for(LayerGate lg:connected_gates) {
				lg.printEvery();
			}
			color = false;
		}
			
	}
	
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
			control_color = false;
		}
	}
	public void setConnectedLayerOutput(Output con) {
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
	
	public void resetLayerComponent() {
		if(control_color) {
		}else {
			control_color = true;
			ListIterator<LayerGate> li = connected_gates.listIterator();
			while(li.hasNext()) {
				LayerGate lg = li.next();
				lg.resetLayerComponent();
			}
			control_color = false;
		}
	}
	
	public void generateConnectedJSON(PublicCount pc, JSONArray functionals, int inputnumber) {
		if(color) {
		}else {
			color = true;
			//TODO
			
			
			color = false;
		}
	}

}
