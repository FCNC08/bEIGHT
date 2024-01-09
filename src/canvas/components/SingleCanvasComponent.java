package canvas.components;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class SingleCanvasComponent extends CanvasComponent {


	protected ArrayList<Short> connected_Components;
	
	protected State state = new State(State.ERROR_MODE, State.OFF_UNSET);
	
	public SingleCanvasComponent(int NewWidth, int NewHeight) {
		super(NewWidth, NewHeight);
		connected_Components = new ArrayList<>();
	}
	
	public void setState(State newState) {
		if(!state.isEqual(newState)) {
			state = newState;
			change();
		}
	}
	public State getState() {
		return state;
	}
	protected Color getColor() {
		return state.getColor();
	}
	
	public static SingleCanvasComponent initImage(String url) {
		//Override in higher classes
		/*Image temp_img = new Image(url);
		SingleCanvasComponent component = new SingleCanvasComponent((int) temp_img.getWidth(), (int) temp_img.getHeight());
		component.setImage(temp_img);
		return component;*/
		return null;
	}

	protected abstract void change();

	public boolean checkEnd(int x, int y) {
		//Checks if a coord is on the End of a wire
		if(rotation == HORIZONTAL) {
			return (x==getXPoint()&&y==getYPoint())||(x==(getXPoint()+getHeightPoint())&&y==getYPoint());
		}else {
			return (x==getXPoint()&&y==getYPoint())||(x==getXPoint()&&y==(getYPoint()+getHeightPoint()));
		}
	}
	
	//Adding/Removing connectedComponents
	public void addComponent(short ID) {
		connected_Components.add(ID);
	}
	public void removeComponent(short ID) {
		connected_Components.remove(ID);
	}
	public void printComponents() {
		for(short i : connected_Components) {
			System.out.print(i+" 	");
		}
		System.out.println(id);
	}
}
