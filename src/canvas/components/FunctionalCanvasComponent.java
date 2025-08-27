package canvas.components;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;

import canvas.LogicSubScene;
import canvas.components.Layercomponents.Connection;
import canvas.components.Layercomponents.LayerGate;
import canvas.components.Layercomponents.Output;
import canvas.components.StandardComponents.Wire;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import util.OcupationException;

public abstract class FunctionalCanvasComponent extends CanvasComponent {
	public static final String SIZE_BIG = "BIG";
	public static final String SIZE_MIDDLE = "MIDDLE";
	public static final String SIZE_SMALL = "SMALL";
	
	public String verilog_string;
	public String arduino_string;
	
	public LayerGate gate;
	public Output[] output;
	
	
	public String size;
	
	protected ContextMenu menu = new ContextMenu();
	protected MenuItem turn;
	protected MenuItem remove;
	
	protected LogicSubScene parent;

	public int input_count;
	public int output_count;

	public Dot[] inputs;
	public Dot[] outputs;
	
	protected long[] times = new long[5];
	protected int time_pointer = 0;
	

	public FunctionalCanvasComponent(int width, int height, int input_count, int output_count, String size) throws IllegalArgumentException {
		super(width, height);

		if (point_width < input_count || point_width < output_count) {
			throw new IllegalArgumentException("Unable to have that much in/outputs");
		}

		this.input_count = input_count;
		this.output_count = output_count;

		// Initializing each Dot
		inputs = new Dot[input_count];
		outputs = new Dot[output_count];
		for (int i = 0; i < input_count; i++) {
			inputs[i] = new Dot(this);
			inputs[i].setParent(this);
		}
		for (int i = 0; i < output_count; i++) {
			outputs[i] = new Dot(this);
			outputs[i].setParent(this);
		}
		
		this.size = size;

		turn = new MenuItem("Turn");
		turn.setOnAction(en->{
			
			parent.remove(this);
			setRotation(!getRotation());
			try {
				parent.add(this);
			}catch(OcupationException e) {
				setRotation(!getRotation());
				try {
					parent.add(this);
				} catch (OcupationException e1) {
					e1.printStackTrace();
				}
			}
			System.out.println("rotated");
		});
		remove = new MenuItem("Remove");
		remove.setOnAction(en->{
			parent.remove(this);
		});
		menu.getStyleClass().add("app-context-menu");
		createContextMenu();
		menu.getItems().add(turn);
		menu.getItems().add(remove);
	}

	public static FunctionalCanvasComponent initImage(String url, int inputs, int outputs, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y) {
		// Override in higher classes
		return null;
	}

	public static FunctionalCanvasComponent initImage(ImageView image, int input_count, int output_count, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y) {
		// Override in higher classes
		return null;
	}

	public void setStandardDotLocations() {
		// creates dot position depending of the width and the dot count
		int x;
		int y;
		int distance;
		int offset;
		int border;
		if(rotation == VERTICAL) {
			if(input_count != 0) {
				offset = point_width + 1 - input_count;
				distance = 1+offset/input_count;
				border = (point_width - distance*(input_count-1))/2;
				
				x = point_X+border;
				y = point_Y;
				for (int i = 0; i < inputs.length; i++) {
					inputs[i].setXPoint(x);
					inputs[i].setYPoint(y);
					x += distance;
				}
			}
			
			if(output_count != 0) {
				offset = point_width + 1 - output_count;
				distance = 1+offset/output_count;
				border = (point_width - distance*(output_count-1))/2;
				
				x = point_X+border;
				y = point_Y+point_height;
				for (int i = 0; i < outputs.length; i++) {
					outputs[i].setXPoint(x);
					outputs[i].setYPoint(y);
					x += distance;
				}
			}
		}else {
			if(input_count != 0) {
				offset = point_height + 1 - input_count;
				distance = 1+offset/input_count;
				border = (point_height - distance*(input_count-1))/2;
				
				x = point_X;
				y = point_Y+border;
				for (int i = 0; i < inputs.length; i++) {
					inputs[i].setXPoint(x);
					inputs[i].setYPoint(y);
					y += distance;
				}
			}
			
			if(output_count != 0) {
				offset = point_height + 1 - output_count;
				distance = 1+offset/output_count;
				border = (point_height - distance*(output_count-1))/2;
				
				x = point_X+point_width;
				y = point_Y+border;
				for (int i = 0; i < outputs.length; i++) {
					outputs[i].setXPoint(x);
					outputs[i].setYPoint(y);
					y += distance;
				}
			}
		}
	}

	public void simulate() {
		long actual_time = times[time_pointer] = System.currentTimeMillis();
		time_pointer++;
		time_pointer%=5;
		long latest_time = times[time_pointer];
		if(latest_time != 0 && (actual_time-latest_time)<100) {
			System.out.println("Blocked "+this);
		}else {
			simulater();
		}
	}
	
	public abstract void simulater();

	public abstract FunctionalCanvasComponent getClone(String size);

	protected State[] getInputStates() {
		// Getting all InputStates of each dot
		State[] states = new State[inputs.length];
		for (int i = 0; i < inputs.length; i++) {
			states[i] = inputs[i].getState();
		}
		return states;
	}

	protected void setOutputStates(State[] states) {
		// Setting all InputStates of each dot
		if (states.length != outputs.length) {
			throw new IllegalArgumentException();
		} else {
			for (int i = 0; i < states.length; i++) {
				outputs[i].setState(states[i]);
			}
		}
	}

	// Setter/Getter for X/Y position
	@Override
	public void addX(int X_coord) {
		int overflow = (X + X_coord + point_X_rest) % LogicSubScene.cross_distance;
		if (overflow <= (LogicSubScene.cross_distance / 2)) {
			this.X = (X + X_coord + point_X_rest) - overflow;
			this.point_X_rest = overflow;
		} else {
			this.X = (X + X_coord + point_X_rest) + LogicSubScene.cross_distance - overflow;
			this.point_X_rest = LogicSubScene.cross_distance-overflow;
		}
		this.point_X = X / LogicSubScene.cross_distance;
		image_view.setLayoutX(X);
		setStandardDotLocations();
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
		System.out.println(point_Y_rest+"");
		this.point_Y = Y / LogicSubScene.cross_distance;
		image_view.setLayoutY(Y);
		setStandardDotLocations();
	}

	@Override
	public void setX(int X_coord) {
		int overflow = (X_coord) % LogicSubScene.cross_distance;
		if (overflow <= LogicSubScene.cross_distance/2) {
			this.X = (X_coord) - overflow;
			this.point_X_rest = 0;
		} else {
			this.X = (X_coord) + LogicSubScene.cross_distance - overflow;
			this.point_X_rest = 0;
		}
		this.point_X = X / LogicSubScene.cross_distance;
		image_view.setLayoutX(X);
		setStandardDotLocations();
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
		image_view.setLayoutY(Y);
		setStandardDotLocations();
	}

	// Setter/Getter for X/Y position in Dots
	@Override
	public void setXPoint(int point_x) {
		this.X = point_x * LogicSubScene.cross_distance;
		this.point_X = point_x;
		image_view.setLayoutX(X);
		setStandardDotLocations();
	}

	@Override
	public void setYPoint(int point_y) {
		this.Y = point_y * LogicSubScene.cross_distance;
		this.point_Y = point_y;
		image_view.setLayoutY(Y);
		setStandardDotLocations();
	}
	
	protected abstract void createContextMenu();
	
	public void setParent(LogicSubScene scene) {
		parent = scene;
	}

	@Override
	public void setFocus(boolean focus) {
		if (focus != this.focus) {
			if (focus) {
				// Painting the inner square of the focus square in top left corner
				for (int x = 1; x < LogicSubScene.wire_height - 1; x++) {
					for (int y = 1; y < LogicSubScene.wire_height - 1; y++) {
						pwriter.setColor(x, y, LogicSubScene.focus_square_main);
					}
				}
				// Painting outlines for focus square in top left corner
				for (int x = 0; x < LogicSubScene.wire_height; x++) {
					// Painting horizontal lines
					pwriter.setColor(x, 0, LogicSubScene.focus_square_secondary);
					pwriter.setColor(x, LogicSubScene.wire_height - 1, LogicSubScene.focus_square_secondary);

					// Paint vertical lines
					pwriter.setColor(0, x, LogicSubScene.focus_square_secondary);
					pwriter.setColor(LogicSubScene.wire_height - 1, x, LogicSubScene.focus_square_secondary);
				}

				// Painting the inner square of the focus square in top right corner
				for (int x = (int) getWidth() - 2; x > getWidth() - LogicSubScene.wire_height + 1; x--) {
					for (int y = 0; y < LogicSubScene.wire_height - 1; y++) {
						pwriter.setColor(x, y, LogicSubScene.focus_square_main);
					}
				}
				// Painting outlines for focus square in top right corner
				// Painting horizontal lines
				for (int x = (int) getWidth() - 1; x > getWidth() - LogicSubScene.wire_height; x--) {
					pwriter.setColor(x, 0, LogicSubScene.focus_square_secondary);
					pwriter.setColor(x, LogicSubScene.wire_height - 1, LogicSubScene.focus_square_secondary);
				}
				// Painting vertical lines
				for (int y = 0; y < LogicSubScene.wire_height; y++) {
					pwriter.setColor((int) getWidth() - 1, y, LogicSubScene.focus_square_secondary);
					pwriter.setColor((int) getWidth() - LogicSubScene.wire_height + 1, y, LogicSubScene.focus_square_secondary);
				}

				// Painting the inner square of the focus square in bottom left corner
				for (int x = 1; x < LogicSubScene.wire_height - 1; x++) {
					for (int y = (int) getHeight() - 2; y > getHeight() - LogicSubScene.wire_height + 1; y--) {
						pwriter.setColor(x, y, LogicSubScene.focus_square_main);
					}
				}
				// Painting outlines for focus square in bottom left corner
				// Painting horizontal lines
				for (int x = 0; x < LogicSubScene.wire_height; x++) {
					pwriter.setColor(x, (int) getHeight() - 1, LogicSubScene.focus_square_secondary);
					pwriter.setColor(x, (int) getHeight() - LogicSubScene.wire_height + 1, LogicSubScene.focus_square_secondary);

				}
				// Painting vertical lines
				for (int y = (int) getHeight() - 1; y > getHeight() - LogicSubScene.wire_height; y--) {
					pwriter.setColor(0, y, LogicSubScene.focus_square_secondary);
					pwriter.setColor(LogicSubScene.wire_height - 1, y, LogicSubScene.focus_square_secondary);
				}

				// Painting the inner square of the focus square in bottom right corner
				for (int x = (int) getWidth() - 2; x > getWidth() - LogicSubScene.wire_height + 1; x--) {
					for (int y = (int) getHeight() - 2; y > getHeight() - LogicSubScene.wire_height + 1; y--) {
						pwriter.setColor(x, y, LogicSubScene.focus_square_main);
					}
				}
				// Painting outlines for focus square in bottom right corner
				// Painting horizontal lines
				for (int x = (int) getWidth() - 1; x > getWidth() - LogicSubScene.wire_height; x--) {
					pwriter.setColor(x, (int) getHeight() - 1, LogicSubScene.focus_square_secondary);
					pwriter.setColor(x, (int) getHeight() - LogicSubScene.wire_height + 1, LogicSubScene.focus_square_secondary);
				}
				// Painting vertical lines
				for (int y = (int) getHeight() - 1; y > getHeight() - LogicSubScene.wire_height; y--) {
					pwriter.setColor((int) getHeight() - 1, y, LogicSubScene.focus_square_secondary);
					pwriter.setColor((int) getWidth() - LogicSubScene.wire_height + 1, y, LogicSubScene.focus_square_secondary);
				}
			} else {
				resetStandardImage();
			}
			this.focus = focus;
		}

	}

	@Override
	protected void createImageView() {
		// Creating ImageView with changing the direction of the ImageView
		image_view = new ImageView();
		image_view.setImage(this);
		image_view.setRotate(getRotationDegree());
		image_view.setLayoutX(X);
		image_view.setLayoutY(Y);

		if (rotation == CanvasComponent.VERTICAL) {
			image_view.setLayoutY(image_view.getLayoutY() + 0.5 * width - 0.5 * getHeight());
			image_view.setLayoutX(image_view.getLayoutX() - 0.5 * width + 0.5 * getHeight());
		}
		
		image_view.setOnContextMenuRequested((e ->  {
			if(menu!=null) menu.show(image_view, e.getScreenX(), e.getScreenY());
			e.consume();
		}));
	}
	@Override
	protected void changeRotation() {
		setStandardDotLocations();
		image_view.setRotate(getRotationDegree());
		if (rotation == CanvasComponent.VERTICAL) {
			image_view.setLayoutY(image_view.getLayoutY() + 0.5 * width - 0.5 * getHeight());
			image_view.setLayoutX(image_view.getLayoutX() - 0.5 * width + 0.5 * getHeight());
		}else {
			image_view.setLayoutY(image_view.getLayoutY() - 0.5 * width + 0.5 * getHeight());
			image_view.setLayoutX(image_view.getLayoutX() + 0.5 * width - 0.5 * getHeight());			
		}
	}
	
	public boolean isInput(Dot d) {
		for(Dot input : inputs) {
			if(input == d) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isOutput(Dot d) {
		for(Dot output : outputs) {
			if(output == d) {
				return true;
			}
		}
		return true;
	}
	
	protected abstract void setVerilogString(short[] comp_count);
	
	protected abstract void setArduinoString(short[] comp_count);
	
	public abstract void createLayerGate();
	
	public void setLayerOutput(Dot output_dot, Output output_connection) {
		int index = Arrays.asList(outputs).indexOf(output_dot);
		if(index != -1) {
			if(output == null) {
				output = new Output[output_count];
			}
			output[index] = output_connection;
		}
	}
	public void addLayerInput(Dot input_dot, Connection input_connection) {
		if(gate == null) {
			createLayerGate();
		}
		try {
			gate.inputs[Arrays.asList(inputs).indexOf(input_dot)] = input_connection;
			input_connection.addComponent(gate);
		}catch(NullPointerException e) {
			System.out.println(this);
		}
		
	}
	
	public void resetLayerComponent() {
		gate = null;
		output = null;
		for(Dot o : outputs) {
			o.resetLayerComponents();
		}
		
	}
	
	public void createVerilogString(LinkedHashSet<FunctionalCanvasComponent> functional_components, short[] comp_count) {
		setVerilogString(comp_count);
		for(int i = 0; i<outputs.length; i++) {
			outputs[i].verilog_name = verilog_string+"_out"+i;
			outputs[i].setConnectedVerilog(verilog_string+"_out"+i, functional_components, comp_count);
		}
	}
	public void createArduinoString(LinkedHashSet<FunctionalCanvasComponent> functional_components, short[] comp_count) {
		setArduinoString(comp_count);
		for(int i = 0; i<outputs.length; i++) {
			outputs[i].arduino_name = arduino_string+"_out"+i;
			outputs[i].setConnectedArduino(outputs[i].arduino_name, Dot.arduino_connection, functional_components, comp_count);
		}
	}
	
	protected abstract void resetStandardImage();
}
