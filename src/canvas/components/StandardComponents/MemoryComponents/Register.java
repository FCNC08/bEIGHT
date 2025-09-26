package canvas.components.StandardComponents.MemoryComponents;

import canvas.components.FunctionalCanvasComponent;
import canvas.components.MemoryCanvasComponent;
import canvas.components.State;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.paint.Color;
import util.ErrorStateException;

public class Register extends MemoryCanvasComponent {
	protected int bit_size;
	public Register(String size, int width, int height, int bit_size) {
		super(size, width, height, bit_size,1);
		this.bit_size = bit_size;
		rotation = VERTICAL;
		setStandardDotLocations();
	}

	@Override
	public void simulater() {
		State[] states = getInputStates();
		boolean set_state = false;
		try {
			set_state = states[0].getStateBoolean();
		} catch (ErrorStateException e) {
			e.printStackTrace();
		}
		if(set_state) {
			for(int i = 0; i<bit_size; i++) {
				memory[0][i] = states[i+1];
				outputs[i].setState(states[i+1]);
			}
		}else {
			for(int i = 0; i<bit_size; i++) {
				try{
					outputs[i].setState(memory[0][i]);
				}catch(NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		return getRegister(size, bit_size);
	}

	@Override
	protected void resetStandardImage() {
		for(int x = 0; x<width; x++) {
			for(int y = 0; y<height; y++) {
				if(x<width*0.1 || y<width*0.1 || x>width*0.9 || y>width*0.9) {
					pwriter.setColor(x, y, Color.BLACK);
				}else {
					pwriter.setColor(x, y, Color.TRANSPARENT);
				}
			}
		}
	}
	
	@Override
	protected void createContextMenu() {
		Label name_label = new Label("Register");
		name_label.getStyleClass().add("cm-header");
		name_label.setMouseTransparent(true);
		CustomMenuItem name_item = new CustomMenuItem(name_label);
		name_item.getStyleClass().add("cm-header-item");
		menu.getItems().add(name_item);
		menu.getItems().add(new SeparatorMenuItem());
		menu.getItems().add(turn);
	}
	
	@Override
	protected void setVerilogString(short[] comp_count) {
		verilog_string = "Register"+comp_count[8];
		comp_count[8]++;
	}
	
	@Override
	protected void setArduinoString(short[] comp_count) {
		arduino_string = "Register"+comp_count[8];
		comp_count[8]++;
	}
	
	public static Register getRegister(String size, int bit_size) {
		int width, height;
		switch(size) {
		case SIZE_BIG:
			width = StandardWidth_big;
			height = StandardHeight_big;
			break;
		case SIZE_MIDDLE:
			width = StandardWidth_middle;
			height = StandardHeight_middle;
			break;
		case SIZE_SMALL:
			width = StandardWidth_small;
			height = StandardHeight_small;
			break;
		default:
			width =height = 0;
		}
		return new Register(size, width, height, bit_size);
	}

	@Override
	public void createLayerGate() {
		// TODO Auto-generated method stub
		
	}

}

