package canvas.components.Layercomponents;

import java.util.ArrayList;
import java.util.ListIterator;

import org.json.JSONArray;

import canvas.components.Dot;
import canvas.components.State;
import util.ErrorStateException;
import util.PublicCount;

public class Output extends Connection{
	protected Dot output;
	protected ArrayList<LayerGate> input_gates = new ArrayList<>();
	public int json_number;
	
	public Output(Dot output) {
		this.output = output;
	}
	
	@Override
	public void setState(State state) {
		if(state != this.state || this.state != output.getState()) {
			this.state = state;
			output.setState(state);
			for(LayerGate lg : connected_gates) {
				lg.simulate();
			}
		}else {
			try {
				System.out.println(state.getStateBoolean());
			} catch (ErrorStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addInputGate(LayerGate lg) {
		input_gates.add(lg);
	}
	
	@Override
	public void printEvery() {
		if(color) {
		}else {
			color = true;
			System.out.println(this);
			System.out.println(output);
			for(LayerGate lg:connected_gates) {
				lg.printEvery();
			}
			color = false;
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
			System.out.println("SEnd to OutputDot");
			control_color = false;
		}
	}
	@Override
	public void setConnectedLayerOutput(Output con) {
		if(control_color) {
			return;
		}else {
			control_color = true;
			ListIterator<LayerGate> li = input_gates.listIterator();
			while(li.hasNext()) {
				LayerGate lg = li.next();
				lg.setLayerOutput(this, con);
			}
			control_color = false;
		}
	}
	
	@Override
	public void resetLayerComponent() {
		if(control_color) {
			return;
		}else {
			control_color = true;
			ListIterator<LayerGate> li = connected_gates.listIterator();
			while(li.hasNext()) {
				LayerGate lg = li.next();
				lg.resetLayerComponent();
			}
			output.resetLayerComponents();
			control_color = false;
		}
	}
	
	@Override
	public void generateConnectedJSON(PublicCount pc, JSONArray functionals, int inputnumber) {
		if(color) {
		}else {
			color = true;
			json_number = inputnumber;
			for(LayerGate lg : connected_gates) {
				lg.generateJSON(pc, functionals, inputnumber, this);
			}
			color = false;
		}
	}
	
}
