package canvas.components;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.ListIterator;

import canvas.LogicSubScene;
import canvas.components.Layercomponents.Connection;
import canvas.components.Layercomponents.Output;
import canvas.components.StandardComponents.Wire;
import javafx.scene.paint.Color;
import util.ComponentBox;

public abstract class SingleCanvasComponent extends CanvasComponent {

	protected ArrayList<SingleCanvasComponent> connected_Components;

	protected State state = State.OFF;
	
	protected boolean control_color = false;
	protected boolean control_type = false;

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
			return y==getYPoint()&&(x==getXPoint()||x==(getXPoint()+getWidthPoint()));
		} else {
			//return (x == getXPoint() && y == getYPoint()) || (x == getXPoint() && y == (getYPoint() + getHeightPoint()));
			return x==getXPoint()&&(y==getYPoint()||y==(getYPoint()+getWidthPoint()));
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
		int overflow = (X_coord) % LogicSubScene.cross_distance;
		if (overflow <= LogicSubScene.cross_distance) {
			this.X = (X_coord) - overflow;
			this.point_X_rest = 0;
		} else {
			this.X = (X_coord) + LogicSubScene.cross_distance - overflow;
			this.point_X_rest = 0;
		}
		this.point_X = X / LogicSubScene.cross_distance;
		this.X -= LogicSubScene.wire_height / 2;
		image_view.setLayoutX(X);
	}

	@Override
	public void setY(int Y_coord) {
		int overflow = (Y_coord) % LogicSubScene.cross_distance;
		if (overflow <= LogicSubScene.cross_distance) {
			this.Y = (Y_coord) - overflow;
			this.point_Y_rest = 0;
		} else {
			this.Y = (Y_coord) + LogicSubScene.cross_distance - overflow;
			this.point_Y_rest = 0;
		}
		this.point_Y = Y / LogicSubScene.cross_distance;
		this.Y -= LogicSubScene.wire_height / 2;
		image_view.setLayoutY(Y);
	}

	public void addX(int X_coord) {
		int overflow = (X + X_coord + point_X_rest) % LogicSubScene.cross_distance;
		if (overflow <= (LogicSubScene.cross_distance / 2)) {
			this.X = (X + X_coord + point_X_rest) - overflow;
			this.point_X_rest = overflow;
		} else {
			this.X = (X + X_coord + point_X_rest) + LogicSubScene.cross_distance - overflow;
			this.point_X_rest = LogicSubScene.cross_distance-overflow;
		}
		System.out.println(X);
		this.point_X = X / LogicSubScene.cross_distance;
		System.out.println(point_X);
		this.X -= LogicSubScene.wire_height / 2;
		image_view.setLayoutX(X);
	}

	@Override
	public void addY(int Y_coord) {
		int overflow = (Y + Y_coord + point_Y_rest) % LogicSubScene.cross_distance;
		if (overflow <= LogicSubScene.cross_distance / 2) {
			this.Y = (Y + Y_coord + point_Y_rest) - overflow;
			this.point_Y_rest = overflow;
		} else {
			this.Y = (Y + Y_coord + point_Y_rest) + LogicSubScene.cross_distance - overflow;
			this.point_Y_rest = -overflow;
		}
		this.point_Y = Y / LogicSubScene.cross_distance;
		this.Y -= LogicSubScene.wire_height / 2;
		image_view.setLayoutY(Y);
	}

	@Override
	public void setXPoint(int point_x) {
		this.X = point_x * LogicSubScene.cross_distance - Dot.dot_width/2;
		this.point_X = point_x;
		image_view.setLayoutX(X);
	}

	@Override
	public void setYPoint(int point_y) {
		this.Y = point_y * LogicSubScene.cross_distance - Dot.dot_width/2;
		this.point_Y = point_y;
		image_view.setLayoutY(Y);
	}

	public ArrayList<FunctionalCanvasComponent> getConnected() {
		if(control_color) {
			return new ArrayList<>();
		}else {
			control_color = true;
			ArrayList<ArrayList<FunctionalCanvasComponent>> connected = new ArrayList<>();
			ArrayList<FunctionalCanvasComponent> con = new ArrayList<>();
			ListIterator<SingleCanvasComponent> li = connected_Components.listIterator();
			while(li.hasNext()) {
				try {
					SingleCanvasComponent comp = li.next();
					if(comp instanceof Wire) {
						connected.add(((Wire) (comp)).getConnected());
					}else if(comp instanceof Dot) {
						Dot d = (Dot) comp;
						if(d.parent.isInput(d)) {
							con.add(d.parent);
						}
					}
				}catch(ConcurrentModificationException e) {
					e.printStackTrace();
				}
			}
			for(ArrayList<FunctionalCanvasComponent> connected_list : connected) {
				con.addAll(connected_list);
			}
			control_color = false;
			return con;
		}
		
		
	}
	
	public void setConnectedVerilog(String verilog_name, LinkedHashSet<FunctionalCanvasComponent> functional_components, short[] comp_count) {
		if(control_color) {
			return;
		}else {
			control_color = true;
			ListIterator<SingleCanvasComponent> li = connected_Components.listIterator();
			while(li.hasNext()) {
				try {
					SingleCanvasComponent comp = li.next();
					if(comp instanceof Wire) {
						((Wire)comp).setConnectedVerilog(verilog_name, functional_components, comp_count);
					}else if(comp instanceof Dot) {
						Dot d = (Dot) comp;
						if(d.parent.isInput(d)) {
							d.verilog_name = verilog_name;
							if(functional_components.add(d.parent)) {
								d.parent.createVerilogString(functional_components,comp_count);
							}
						}
					}
				}catch(ConcurrentModificationException e) {
					e.printStackTrace();
				}
			}
			control_color = false;
			return;
		}
		
		
	}
	
	public void setConnectedArduino(String arduino_name, String arduino_type, LinkedHashSet<FunctionalCanvasComponent> functional_components, short[] comp_count) {
		if(control_color) {
			return;
		}else {
			control_color = true;
			ListIterator<SingleCanvasComponent> li = connected_Components.listIterator();
			while(li.hasNext()) {
				try {
					SingleCanvasComponent comp = li.next();
					if(comp instanceof Wire) {
						((Wire)comp).setConnectedArduino(arduino_name,arduino_type, functional_components, comp_count);
					}else if(comp instanceof Dot) {
						Dot d = (Dot) comp;
						if(d.parent.isInput(d)) {
							d.arduino_name = arduino_name;
							d.arduino_type = arduino_type;
							if(functional_components.add(d.parent)) {
								d.parent.createArduinoString(functional_components, comp_count);
							}
						}
					}
				}catch(ConcurrentModificationException e) {
					e.printStackTrace();
				}
			}
			control_color = false;
		}
	}
	
	public void setConnectedArduinoType(String arduino_type) {
		if(control_type) {
			return;
		}else {
			control_type = true;
			ListIterator<SingleCanvasComponent> li = connected_Components.listIterator();
			while(li.hasNext()) {
				try {
					SingleCanvasComponent comp = li.next();
					if(comp instanceof Wire) {
						((Wire)comp).setConnectedArduinoType(arduino_type);
					}else if(comp instanceof Dot) {
						Dot d = (Dot) comp;
						if(d.parent.isInput(d)) {
							d.arduino_type = arduino_type;
						}
					}
				}catch(ConcurrentModificationException e) {
					e.printStackTrace();
				}
			control_type = false;
			}
		}
	}
	
	public void setConnectedLayerConnection(Connection con) {
		if(control_color) {
			return;
		}else {
			control_color = true;
			ListIterator<SingleCanvasComponent> li = connected_Components.listIterator();
			while(li.hasNext()) {
				try {
					SingleCanvasComponent comp = li.next();
					if(comp instanceof Wire) {
						((Wire)comp).setConnectedLayerConnection(con);
					}else if(comp instanceof Dot) {
						Dot d = (Dot) comp;
						if(d.parent.isInput(d)) {
							d.parent.addLayerInput(d, con);
						}
					}
				}catch(ConcurrentModificationException e) {
					e.printStackTrace();
				}
			}
			control_color = false;
		}
	}
	public void setConnectedLayerOutput(Output con) {
		if(control_color) {
		}else {
			control_color = true;
			ListIterator<SingleCanvasComponent> li = connected_Components.listIterator();
			while(li.hasNext()) {
				try {
					SingleCanvasComponent comp = li.next();
					if(comp instanceof Wire) {
						((Wire)comp).setConnectedLayerOutput(con);
					}else if(comp instanceof Dot) {
						Dot d = (Dot) comp;
						if(d.parent.isOutput(d)) {
							d.parent.setLayerOutput(d, con);
						}
					}
				}catch(ConcurrentModificationException e) {
					e.printStackTrace();
				}
			}
			control_color = false;
		}
	}
	public void resetLayerComponents() {
		if(control_color) {
		}else {
			control_color = true;
			ListIterator<SingleCanvasComponent> li = connected_Components.listIterator();
			while(li.hasNext()) {
				try {
					SingleCanvasComponent comp = li.next();
					if(comp instanceof Wire) {
						((Wire)comp).resetLayerComponents();
					}else if(comp instanceof Dot) {
						Dot d = (Dot) comp;
						d.parent.resetLayerComponent();
					}
				}catch(ConcurrentModificationException e) {
					e.printStackTrace();
				}
			}
			control_color = false;
		}
	}
	
	public void printComponents() {
		for (SingleCanvasComponent i : connected_Components) {
			System.out.print(i + " 	");
		}
	}
	
	@Override
	protected void changeRotation() {
		image_view.setRotate(getRotationDegree());
		if (rotation == CanvasComponent.VERTICAL) {
			image_view.setLayoutY(image_view.getLayoutY() + 0.5 * width - 0.5 * getHeight());
			image_view.setLayoutX(image_view.getLayoutX() - 0.5 * width + 0.5 * getHeight());
		}
	}
}
