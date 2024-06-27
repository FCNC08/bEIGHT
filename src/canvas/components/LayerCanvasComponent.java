package canvas.components;


import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import canvas.LogicSubScene;
import canvas.components.Layercomponents.AND;
import canvas.components.Layercomponents.Connection;
import canvas.components.Layercomponents.NAND;
import canvas.components.Layercomponents.NOR;
import canvas.components.Layercomponents.NOT;
import canvas.components.Layercomponents.OR;
import canvas.components.Layercomponents.Output;
import canvas.components.Layercomponents.XNOR;
import canvas.components.Layercomponents.XOR;
import javafx.scene.paint.Color;
import util.Info;
import util.InputOutputConnectionPair;
import util.PublicCount;

public class LayerCanvasComponent extends FunctionalCanvasComponent{
	public static int Standard_height_small = LogicSubScene.cross_distance;
	public static int Standard_height_middle = LogicSubScene.cross_distance*2;
	public static int Standard_height_big = LogicSubScene.cross_distance*4;
	public static int Standard_width_multiplier_small = 1;
	public static int Standard_width_multiplier_middle = 2;
	public static int Standard_width_multiplier_big = 3;
	
	public InputOutputConnectionPair inoutput;
	public String name = "";
	
	
	public LayerCanvasComponent(String size, int width, int height, LogicSubScene logicscene) throws IllegalArgumentException {
		super(width, height, logicscene.getInputs().size(), logicscene.getOutputs().size(), size);
		this.rotation = VERTICAL;
		this.name = logicscene.name;
		inoutput = logicscene.initLayerComponent(outputs);
		resetStandardImage();
		info.setHeadline(name);
	}
	
	public LayerCanvasComponent(JSONObject jo, int width, int height, String size, int input_count, int output_count) {
		super(width, height, input_count, output_count, size);
		Connection[] connections = new Connection[jo.getInt("connection_count")+1];
		Connection[] new_inputs = new Connection[input_count];
		Output[] new_outputs = new Output[output_count];
		for(int i = 0; i<jo.getJSONArray("inputs").length(); i++) {
			connections[i] = new_inputs[i]= new Connection();
		}
		JSONArray ja = jo.getJSONArray("outputs");
		for(int i = 0; i<ja.length(); i++) {
			connections[ja.getInt(i)] = new_outputs[i]  = new Output(outputs[i]);
		}
		
		for(int i = 1; i<connections.length; i++) {
			if(connections[i] == null) {
				connections[i] = new Connection();
			}
		}
		for(Object o : jo.getJSONArray("functionals")) {
			if(o instanceof JSONObject) {
				JSONObject fjo = (JSONObject) o;
				switch(fjo.getString("Name")) {
				case("AND"):{
					JSONArray finputs = fjo.getJSONArray("inputs");
					AND comp = new AND(finputs.length());
					for(int i = 0; i<fjo.length(); i++) {
						comp.inputs[i] = connections[finputs.getInt(i)];
						comp.inputs[i].addComponent(comp);
					}
					comp.outputs[0] = connections[fjo.getJSONArray("inputs").getInt(0)];
					break;
				}
				case("NAND"):{
					JSONArray finputs = fjo.getJSONArray("inputs");
					NAND comp = new NAND(finputs.length());
					for(int i = 0; i<fjo.length(); i++) {
						comp.inputs[i] = connections[finputs.getInt(i)];
						comp.inputs[i].addComponent(comp);
					}
					comp.outputs[0] = connections[fjo.getJSONArray("inputs").getInt(0)];
					break;
				}
				case("NOR"):{
					JSONArray finputs = fjo.getJSONArray("inputs");
					NOR comp = new NOR(finputs.length());
					for(int i = 0; i<fjo.length(); i++) {
						comp.inputs[i] = connections[finputs.getInt(i)];
						comp.inputs[i].addComponent(comp);
					}
					comp.outputs[0] = connections[fjo.getJSONArray("inputs").getInt(0)];
					break;
				}
				case("NOT"):{
					JSONArray finputs = fjo.getJSONArray("inputs");
					NOT comp = new NOT();
					comp.inputs[0] = connections[finputs.getInt(0)];
					comp.inputs[0].addComponent(comp);
					comp.outputs[0] = connections[fjo.getJSONArray("inputs").getInt(0)];
					break;
				}
				case("OR"):{
					JSONArray finputs = fjo.getJSONArray("inputs");
					OR comp = new OR(finputs.length());
					for(int i = 0; i<fjo.length(); i++) {
						comp.inputs[i] = connections[finputs.getInt(i)];
						comp.inputs[i].addComponent(comp);
					}
					comp.outputs[0] = connections[fjo.getJSONArray("inputs").getInt(0)];
					break;
				}
				case("XNOR"):{
					JSONArray finputs = fjo.getJSONArray("inputs");
					XNOR comp = new XNOR(finputs.length());
					for(int i = 0; i<fjo.length(); i++) {
						comp.inputs[i] = connections[finputs.getInt(i)];
						comp.inputs[i].addComponent(comp);
					}
					comp.outputs[0] = connections[fjo.getJSONArray("inputs").getInt(0)];
					break;
				}
				case("XOR"):{
					JSONArray finputs = fjo.getJSONArray("inputs");
					XOR comp = new XOR(finputs.length());
					for(int i = 0; i<fjo.length(); i++) {
						comp.inputs[i] = connections[finputs.getInt(i)];
						comp.inputs[i].addComponent(comp);
					}
					comp.outputs[0] = connections[fjo.getJSONArray("inputs").getInt(0)];
					break;
				}
				
				}
			}
		}
		
		inoutput = new InputOutputConnectionPair(new_inputs, new_outputs);
	}

	public static LayerCanvasComponent init(String size, LogicSubScene scene) {
		int width, height;
		switch (size) {
		case SIZE_BIG:
			width = LogicSubScene.cross_distance*(Math.max(scene.getInputs().size(), scene.getOutputs().size())+2)*Standard_width_multiplier_big;
			height = Standard_height_big;
			break;
		case SIZE_MIDDLE:
			width = LogicSubScene.cross_distance*(Math.max(scene.getInputs().size(), scene.getOutputs().size())+2)*Standard_width_multiplier_middle;
			height = Standard_height_middle;
			break;
		case SIZE_SMALL:
			width = LogicSubScene.cross_distance*(Math.max(scene.getInputs().size(), scene.getOutputs().size())+2)*Standard_width_multiplier_small;
			height = Standard_height_small;
			break;
		default:
			width = 1;
			height = 1;
			break;
		}
		LayerCanvasComponent component = new LayerCanvasComponent(size, width, height, scene);
		return component;
	}
	
	public static LayerCanvasComponent init(JSONObject jo, String size, int input_count, int output_count) {
		int width, height;
		switch (size) {
		case SIZE_BIG:
			width = LogicSubScene.cross_distance*(Math.max(input_count, output_count)+2)*Standard_width_multiplier_big;
			height = Standard_height_big;
			break;
		case SIZE_MIDDLE:
			width = LogicSubScene.cross_distance*(Math.max(input_count, output_count)+2)*Standard_width_multiplier_middle;
			height = Standard_height_middle;
			break;
		case SIZE_SMALL:
			width = LogicSubScene.cross_distance*(Math.max(input_count, output_count)+2)*Standard_width_multiplier_small;
			height = Standard_height_small;
			break;
		default:
			width = 1;
			height = 1;
			break;
		}
		return new LayerCanvasComponent(jo, width, height, size, input_count, output_count);
		
	}
	
	@Override
	public void simulater() {
		State[] states = getInputStates();
		for(int i = 0; i<input_count; i++) {
			inoutput.input[i].printEvery();
			System.out.println("");
			inoutput.input[i].setState(states[i]);
		}
	}
	
	@Override
	public FunctionalCanvasComponent getClone(String size) {
		return new LayerCanvasComponent(size, width, height, null);
	}

	@Override
	protected void createInfo() {
		info = new Info();
	}

	@Override
	protected void resetStandardImage() {
		double border_size = Math.min(width*0.1, height*0.3);
		for(int x = 0; x<width; x++) {
			for(int y = 0; y<height; y++) {
				if((x<border_size||x > width-border_size)&&(y<border_size||y<height-border_size)) {
					pwriter.setColor(x, y, Color.BLACK);
				}
			}
		}
	}
	@Override
	protected void setVerilogString(short[] comp_count) {
	}
	@Override
	protected void setArduinoString(short[] comp_count) {
	}

	@Override
	public void createLayerGate() {}
	
	@Override
	public void setLayerOutput(Dot output_dot, Output output_connection) {
		int index = Arrays.asList(outputs).indexOf(output_dot);
		if(index != -1) {
			inoutput.output[index].setConnectedLayerOutput(output_connection);
		}else {
			System.out.println("Error");
		}
	}
	
	@Override
	public void addLayerInput(Dot input_dot, Connection input_connection) {
		try {
			System.out.println("This is a message from LayerCanvasComponent");
			inoutput.input[Arrays.asList(inputs).indexOf(input_dot)].setConnectedLayerConnection(input_connection);
		}catch(NullPointerException e) {
			System.out.println(this);
		}
		
	}
	
	@Override
	public void resetLayerComponent() {
		for(Connection con : inoutput.input) {
			con.resetLayerComponent();
		}
	}
	
	public JSONObject getJSONObject() {
		JSONArray inputs = new JSONArray();
		JSONArray functionals = new JSONArray();
		PublicCount pc = new PublicCount(input_count);
		for(int i = 0; i<input_count; i++) {
			inputs.put(i+1);
			inoutput.input[i].generateConnectedJSON(pc, functionals, i+1);
		}
		
		JSONArray json_outputs = new JSONArray();
		for(Output o : inoutput.output) {
			json_outputs.put(o.json_number);
		}
		
		JSONObject object = new JSONObject();
		object.put("inputs", inputs);
		object.put("functionals", functionals);
		object.put("outputs", json_outputs);
		object.put("connection_count", pc.getCount());
		object.put("name", name);
		return object;
	}
}
