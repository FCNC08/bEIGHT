package canvas.components;

import java.util.ArrayList;

import canvas.LogicSubScene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import util.ComponentBox;

public abstract class SingleCanvasComponent extends CanvasComponent {

	protected ArrayList<SingleCanvasComponent> connected_Components;

	protected State state = new State(State.ERROR_MODE, State.OFF_UNSET);

	public SingleCanvasComponent(int NewWidth, int NewHeight) {
		super(NewWidth, NewHeight);
		connected_Components = new ArrayList<>();
	}

	public void setState(State newState) {
		if (!state.isEqual(newState)) {
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
		// Override in higher classes
		/*
		 * Image temp_img = new Image(url); SingleCanvasComponent component = new
		 * SingleCanvasComponent((int) temp_img.getWidth(), (int) temp_img.getHeight());
		 * component.setImage(temp_img); return component;
		 */
		return null;
	}

	protected abstract void change();

	public boolean checkEnd(int x, int y) {
		// Checks if a coord is on the End of a wire
		if (rotation == HORIZONTAL) {
			return (x == getXPoint() && y == getYPoint()) || (x == (getXPoint() + getHeightPoint()) && y == getYPoint());
		} else {
			return (x == getXPoint() && y == getYPoint()) || (x == getXPoint() && y == (getYPoint() + getHeightPoint()));
		}
	}

	// Adding/Removing connectedComponents
	public void addComponent(SingleCanvasComponent connecting_comp) {
		if (connecting_comp != null && connecting_comp != ComponentBox.occupied) {
			connected_Components.add(connecting_comp);
			setState(connecting_comp.state);
		}
	}

	public void removeComponent(SingleCanvasComponent connected_comp) {
		if (connected_Components.contains(connected_comp)) {
			connected_Components.remove(connected_comp);
		}
	}

	public ArrayList<SingleCanvasComponent> getConnectedComponents() {
		return connected_Components;
	}
	
	@Override
	public void setX(int X_coord) {
		int overflow = (X_coord)%LogicSubScene.cross_distance;
		if(overflow<=LogicSubScene.cross_distance) {
			this.X = (X_coord)-overflow;
			this.point_X_rest = 0;
		}else {
			this.X = (X_coord)+LogicSubScene.cross_distance-overflow;
			this.point_X_rest = 0;
		}
		this.point_X = X/LogicSubScene.cross_distance;
		this.X-= LogicSubScene.wire_height/2;
		image_view.setLayoutX(X);
	}

	@Override
	public void setY(int Y_coord) {
		int overflow = (Y_coord)%LogicSubScene.cross_distance;
		if(overflow<=LogicSubScene.cross_distance) {
			this.Y = (Y_coord)-overflow;
			this.point_Y_rest = 0;
		}else {
			this.Y = (Y_coord)+LogicSubScene.cross_distance-overflow;
			this.point_Y_rest = 0;
		}
		this.point_Y = Y/LogicSubScene.cross_distance;
		this.Y -= LogicSubScene.wire_height/2;
		image_view.setLayoutY(Y);
	}
	
	public void addX(int X_coord) {
		int overflow = (X+X_coord+point_X_rest)%LogicSubScene.cross_distance;
		if(overflow<=LogicSubScene.cross_distance) {
			this.X = (X+X_coord+point_X_rest)-overflow;
			this.point_X_rest = overflow;
		}else {
			this.X = (X+X_coord+point_X_rest)+LogicSubScene.cross_distance-overflow;
			this.point_X_rest = -overflow;
		}
		System.out.println(point_X_rest+" ");
		this.point_X = X/LogicSubScene.cross_distance;
		this.Y-=LogicSubScene.wire_height/2;
		image_view.setLayoutX(X);
	}

	@Override
	public void addY(int Y_coord) {
		int overflow = (Y+Y_coord+point_Y_rest)%LogicSubScene.cross_distance;
		if(overflow<=LogicSubScene.cross_distance) {
			this.Y = (Y+Y_coord+point_Y_rest)-overflow;
			this.point_Y_rest = overflow;
		}else {
			this.Y = (Y+Y_coord+point_Y_rest)+LogicSubScene.cross_distance-overflow;
			this.point_Y_rest = -overflow;
		}
		this.point_Y = Y/LogicSubScene.cross_distance;
		this.Y-=LogicSubScene.wire_height/2;
		image_view.setLayoutY(Y);
	}
	
	@Override
	public void setXPoint(int point_x) {
		this.X = point_x * LogicSubScene.cross_distance-LogicSubScene.wire_height/2;
		this.point_X = point_x;
		image_view.setLayoutX(X);
	}

	@Override
	public void setYPoint(int point_y) {
		this.Y = point_y * LogicSubScene.cross_distance-LogicSubScene.wire_height/2;
		this.point_Y = point_y;
		image_view.setLayoutX(X);
	}

	public void printComponents() {
		for (SingleCanvasComponent i : connected_Components) {
			System.out.print(i + " 	");
		}
	}
}
