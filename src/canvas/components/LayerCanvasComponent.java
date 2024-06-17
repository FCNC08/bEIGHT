package canvas.components;


import java.util.Arrays;

import canvas.LogicSubScene;
import canvas.components.Layercomponents.Connection;
import javafx.scene.paint.Color;
import util.Info;
import util.InputOutputConnectionPair;

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
		super(width, height, logicscene.getInputs().size(), logicscene.getOutputs().size() );
		this.size = size;
		this.rotation = VERTICAL;
		this.name = logicscene.name;
		inoutput = logicscene.initLayerComponent(outputs);
		resetStandardImage();
		info.setHeadline(name);
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
	
	@Override
	public void simulater() {
		State[] states = getInputStates();
		for(int i = 0; i<input_count; i++) {
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
	public void createLayerGate() {
		/*Connection newoutput[] = new Connection[outputs.length];
		for(int i = 0; i<outputs.length; i++) {
			newoutput[i] = new Connection();
			this.outputs[i].setConnectedLayerOutput(newoutput[i]);
		}
		
		Connection[] newinput = new Connection[input_count];
		for(int i = 0; i<inoutput.input.length; i++) {
			newinput[i] = new Connection();
			this.inoutput.input[i].setConnectedLayerConnection(newinput[i]);
		}
		gate = new LayerComponent(input_count, output_count, newinput, newoutput);
		for(int i = 0; i<output_count; i++) {
			if(output!=null) {
				if(output[i]!=null) {
					gate.outputs[i] = output[i];
					System.out.println(output[i]);
				}
			}
			outputs[i].setConnectedLayerConnection(gate.outputs[i]);
			System.out.println(gate.outputs[i]);
		}*/
	}
	
	@Override
	public void setLayerOutput(Dot output_dot, Connection output_connection) {
		int index = Arrays.asList(outputs).indexOf(output_dot);
		if(index != -1) {
			inoutput.outputgates[index].setLayerOutput(inoutput.output[index], output_connection);
		}
	}
	
	@Override
	public void addLayerInput(Dot input_dot, Connection input_connection) {
		try {
			inoutput.input[Arrays.asList(inputs).indexOf(input_dot)].setConnectedLayerConnection(input_connection);
		}catch(NullPointerException e) {
			System.out.println(this);
		}
		
	}
}
