package canvas.components;

import java.util.ArrayList;

import canvas.LogicSubScene;
import canvas.components.StandardComponents.Input;
import canvas.components.StandardComponents.Output;
import javafx.scene.paint.Color;
import util.Info;

public class LayerCanvasComponent extends FunctionalCanvasComponent{
	public static int Standard_height_small = LogicSubScene.cross_distance;
	public static int Standard_height_middle = LogicSubScene.cross_distance*2;
	public static int Standard_height_big = LogicSubScene.cross_distance*4;
	public static int Standard_width_multiplier_small = 1;
	public static int Standard_width_multiplier_middle = 2;
	public static int Standard_width_multiplier_big = 3;
	
	public LogicSubScene logic_subscene;
	
	public LayerCanvasComponent(String size, int width, int height, LogicSubScene logicscene) throws IllegalArgumentException {
		super(width, height, logicscene.getInputs().size(), logicscene.getOutputs().size() );
		this.size = size;
		this.logic_subscene = logicscene;
		this.rotation = VERTICAL;
		System.out.println(logic_subscene);
		resetStandardImage();
		info.setHeadline(logic_subscene.getName());
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
	public void simulate() {
		State[] states = getInputStates();
		ArrayList<Input> input = logic_subscene.getInputs();
		for(int i = 0; i < states.length; i++) {
			input.get(i).setState(states[i]);
		}
		logic_subscene.last_changing_component = this;
		System.out.println();
	}

	public void changeOutputs() {
		ArrayList<Output> output = logic_subscene.getOutputs();
		State[] states = new State[output.size()];
		for(int i = 0; i < states.length; i++) {
			states[i] = output.get(i).getState();
		}
		setOutputStates(states);
	}
	
	@Override
	public FunctionalCanvasComponent getClone(String size) {
		return new LayerCanvasComponent(size, width, height, logic_subscene);
	}

	@Override
	protected void createInfo() {
		info = new Info();
		System.out.println(logic_subscene);
		info.setHeadline("test");
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
		// TODO Auto-generated method stub
	}
}
