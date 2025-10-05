package canvas.components.StandardComponents;

import java.util.ArrayList;
import java.util.Arrays;

import canvas.LogicSubScene;
import canvas.components.Dot;
import canvas.components.FunctionalCanvasComponent;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.paint.Color;

public class Combinder extends FunctionalCanvasComponent{
	
	private int cabels;
	
	protected MenuItem addDot;
	protected boolean add_visible = true;
	protected MenuItem removeDot;
	protected boolean remove_visible = true;

	
	public Combinder(int width, int height, int cabels, String size)
			throws IllegalArgumentException {
		super(width, height, cabels, 1, size);
		
		this.cabels = cabels;
		single_output = true;
		
		outputs[0].setWireWidth(cabels);
		
		resetStandardImage();	
		
		addDot = new MenuItem("Add new Input");
		addDot.setOnAction(en->{
			addInput();
		});
		if(size == SIZE_SMALL) {
			addDot.setVisible(false);
			add_visible = false;
		}
		removeDot = new MenuItem("Remove Input");
		removeDot.setOnAction(en->{
			removeInput();
		});
		menu.getItems().addAll(addDot, removeDot);
		
	}
	
	@Override
	public void changeOutputType() {
		return;
	}

	protected void addInput() {
		if(point_height >= input_count) {
			System.out.println("Test");
			ArrayList<Dot> new_dots = new ArrayList<>(Arrays.asList(inputs));
			Dot dot = new Dot(this);
			new_dots.add(dot);
			input_count++;
			cabels++;
			outputs[0].setWireWidth(cabels);
			inputs = (Dot[]) new_dots.toArray(new Dot[0]);
			setStandardDotLocations();
		}
		if(point_height < input_count) {
			addDot.setVisible(false);
			add_visible = false;
		}
		if(!remove_visible) {
			removeDot.setVisible(true);
			remove_visible = true;
		}
	}
	
	protected void removeInput() {
		if(input_count > 2 ) {
			ArrayList<Dot> new_dots = new ArrayList<>(Arrays.asList(inputs));
			input_count--;
			cabels--;
			outputs[0].setWireWidth(cabels);
			parent.remove(new_dots.remove(input_count));
			inputs = (Dot[]) new_dots.toArray(new Dot[0]);
			setStandardDotLocations();
		}
		if(input_count <= 2) {
			removeDot.setVisible(false);
			remove_visible = false;
		}
		if(!add_visible) {
			addDot.setVisible(true);
			add_visible = true;
		}
	}


	@Override
	public void simulater() {
		setOutputStates(getInputStates());
	}
	
	@Override
	protected void resetStandardImage() {
		for(int x = 0; x<width; x++) {
			for(int y = 0; y<height; y++) {
				pwriter.setColor(x, y, LogicSubScene.black_grey);
			}
		}
		for(int x = 0; x<width/2; x++) {
			for(int y = 0; y<height; y++) {
				pwriter.setColor(x, y, Color.BLACK);
			}
		}
		for(int x=width/2; x<width; x++) {
			for(int y = (height-LogicSubScene.wire_height)/2;y<(height+LogicSubScene.wire_height)/2;y++) {
				pwriter.setColor(x, y, Color.BLACK);
			}
		}
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		return getCombinder(size);
	}
	
	public static Combinder getCombinder(String size) {
		int height;
		int width;
		
		switch(size) {
		case SIZE_BIG:
			height = LogicSubScene.cross_distance*8;
			width = LogicSubScene.cross_distance*3;
			break;
		case SIZE_MIDDLE:
			height = LogicSubScene.cross_distance*6;
			width = LogicSubScene.cross_distance*2;
			break;
		default:
			height = LogicSubScene.cross_distance*4;
			width = LogicSubScene.cross_distance;
			break;
		}
		return new Combinder(width, height, 4,  size);
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
