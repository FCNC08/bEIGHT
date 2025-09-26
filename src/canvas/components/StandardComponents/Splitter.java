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

public class Splitter extends FunctionalCanvasComponent{
	
	private int cabels;

	protected MenuItem addDot;
	protected boolean add_visible = true;
	protected MenuItem removeDot;
	protected boolean remove_visible = true;

	public Splitter(int width, int height, int cabels, String size)
			throws IllegalArgumentException {
		super(width, height, 1, cabels, size);
		
		this.cabels = cabels;
		single_input = true;
		
		inputs[0].setWireWidth(cabels);
		
		resetStandardImage();
		
		addDot = new MenuItem("Add new Output");
		addDot.setOnAction(en->{
			addOutput();
		});
		if(size == SIZE_SMALL) {
			addDot.setVisible(false);
			add_visible = false;
		}
		removeDot = new MenuItem("Remove Output");
		removeDot.setOnAction(en->{
			removeOutput();
		});
		menu.getItems().addAll(addDot, removeDot);
	}
	
	protected void addOutput() {
		if(point_height >= input_count) {
			System.out.println("Test");
			ArrayList<Dot> new_dots = new ArrayList<>(Arrays.asList(outputs));
			Dot dot = new Dot(this);
			new_dots.add(dot);
			output_count++;
			cabels++;
			inputs[0].setWireWidth(cabels);
			outputs = (Dot[]) new_dots.toArray(new Dot[0]);
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
	
	protected void removeOutput() {
		if(output_count > 2 ) {
			ArrayList<Dot> new_dots = new ArrayList<>(Arrays.asList(outputs));
			output_count--;
			cabels--;
			inputs[0].setWireWidth(cabels);
			parent.remove(new_dots.remove(output_count));
			outputs = (Dot[]) new_dots.toArray(new Dot[0]);
			setStandardDotLocations();
		}
		if(output_count <= 2) {
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
		System.out.println(outputs.length+" "+getInputStates().length);
		setOutputStates(getInputStates());
	}
	
	@Override
	protected void resetStandardImage() {		
		for(int x = 0; x<width; x++) {
			for(int y = 0; y<height; y++) {
				pwriter.setColor(x, y, LogicSubScene.black_grey);
			}
		}
		for(int x = width/2; x<width; x++) {
			for(int y = 0; y<height; y++) {
				pwriter.setColor(x, y, Color.BLACK);
			}
		}
		for(int x=0; x<width/2; x++) {
			for(int y = (height-LogicSubScene.wire_height)/2;y<(height+LogicSubScene.wire_height)/2;y++) {
				pwriter.setColor(x, y, Color.BLACK);
			}
		}
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		int height;
		int width;
		
		switch(size) {
		case SIZE_BIG:
			height = LogicSubScene.cross_distance*7;
			width = LogicSubScene.cross_distance*3;
			break;
		case SIZE_MIDDLE:
			height = LogicSubScene.cross_distance*5;
			width = LogicSubScene.cross_distance*2;
			break;
		default:
			height = LogicSubScene.cross_distance*3;
			width = LogicSubScene.cross_distance;
			break;
		}
		return new Splitter(width, height, 4,  size);
	}

	@Override
	protected void createContextMenu() {
		Label name_label = new Label("Splitter");
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
