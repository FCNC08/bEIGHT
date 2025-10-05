package canvas.components;

import java.util.ArrayList;
import java.util.Arrays;

import canvas.LogicSubScene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
public abstract class LogicComponent extends FunctionalCanvasComponent {
	
	protected static int StandardWidth_big = LogicSubScene.cross_distance * 8, StandardHeight_big = LogicSubScene.cross_distance * 8;

	protected static int StandardWidth_middle = LogicSubScene.cross_distance * 4, StandardHeight_middle = LogicSubScene.cross_distance * 4;

	protected static int StandardWidth_small = LogicSubScene.cross_distance * 2, StandardHeight_small = LogicSubScene.cross_distance * 2;
	
	protected MenuItem addDot;
	protected boolean add_visible = true;
	protected MenuItem removeDot;
	protected boolean remove_visible = true;

	public LogicComponent(String size, int width, int height, int input_count) throws IllegalArgumentException {
		super(width, height, input_count, 1, size);
		
		changeOutput.setVisible(false);
		
		addDot = new MenuItem("Add new Input");
		addDot.setOnAction(en->{
			addInput();
		});
		removeDot = new MenuItem("Remove Input");
		removeDot.setOnAction(en->{
			removeInput();
		});
		removeDot.setVisible(false);
		remove_visible = false;
		menu.getItems().addAll(addDot, removeDot);
		
	}

	protected void addInput() {
		if(point_width >= input_count) {
			ArrayList<Dot> new_dots = new ArrayList<>(Arrays.asList(inputs));
			Dot dot = new Dot(this);
			new_dots.add(dot);
			input_count++;
			inputs = (Dot[]) new_dots.toArray(new Dot[0]);
			setStandardDotLocations();
		}
		if(point_width < input_count) {
			addDot.setVisible(false);
			add_visible = false;
		}
		if(!remove_visible) {
			removeDot.setVisible(true);
			remove_visible = true;
		}
	}
	
	public void setInputCount(int new_input_count) {
		if(new_input_count == input_count) return;
		ArrayList<Dot> new_dots = new ArrayList<>(Arrays.asList(inputs));
		for(int i = input_count; i<new_input_count; i++) {
			new_dots.add(new Dot(this));
		}
		input_count = new_input_count;
		inputs = (Dot[]) new_dots.toArray(new Dot[0]);
		setStandardDotLocations();
	}
	
	protected void removeInput() {
		if(input_count > 2 ) {
			ArrayList<Dot> new_dots = new ArrayList<>(Arrays.asList(inputs));
			input_count--;
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
	
	public static void setStandardImage(Image standard_image) {
		// Override in higher classes
	}

}
