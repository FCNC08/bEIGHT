package canvas.components.StandardComponents;

import canvas.components.FunctionalCanvasComponent;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.paint.Color;

public class Combinder extends FunctionalCanvasComponent{
	
	private int cabels;

	public Combinder(int width, int height, int cabels, String size)
			throws IllegalArgumentException {
		super(width, height, cabels, 1, size);
		
		this.cabels = cabels;
		single_output = true;
		
		outputs[0].setWireWidth(cabels);
		
		resetStandardImage();
	}

	@Override
	public void simulater() {
		setOutputStates(getInputStates());
	}
	
	@Override
	protected void resetStandardImage() {
		System.out.println("Test");
		for(int x = 0; x<width; x++) {
			for(int y = 0; y<height; y++) {
				pwriter.setColor(x, y, Color.BLACK);
			}
		}
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void createContextMenu() {
		Label name_label = new Label("Combinder");
		name_label.getStyleClass().add("cm-header");
		name_label.setMouseTransparent(true);
		CustomMenuItem name_item = new CustomMenuItem(name_label);
		name_item.getStyleClass().add("cm-header-item");
		menu.getItems().add(name_item);
		menu.getItems().add(new SeparatorMenuItem());
		changeOutput.setVisible(false);
		changeInput.setVisible(false);
	}

	@Override
	protected void setVerilogString(short[] comp_count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setArduinoString(short[] comp_count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createLayerGate() {
		// TODO Auto-generated method stub
		
	}
}
