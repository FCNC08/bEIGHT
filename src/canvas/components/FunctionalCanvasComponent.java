package canvas.components;

import java.util.HashMap;

import canvas.LogicSubScene;
import javafx.scene.image.ImageView;
import util.OcupationExeption;

public abstract class FunctionalCanvasComponent extends CanvasComponent {
	public static final byte SIZE_BIG = 2;
	public static final byte SIZE_MIDDLE = 1;
	public static final byte SIZE_SMALL = 0;

	protected static int StandardWidth_big = LogicSubScene.cross_distance * 8, StandardHeight_big = LogicSubScene.cross_distance * 8;

	protected static int StandardWidth_middle = LogicSubScene.cross_distance * 4, StandardHeight_middle = LogicSubScene.cross_distance * 4;

	protected static int StandardWidth_small = LogicSubScene.cross_distance * 2, StandardHeight_small = LogicSubScene.cross_distance * 2;

	protected HashMap<State[], State[]> truth_table;

	protected int input_count;
	protected int output_count;

	public Dot[] inputs;
	public Dot[] outputs;

	public FunctionalCanvasComponent(int width, int height, int input_count, int output_count) throws IllegalArgumentException {
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
		int distance = point_width / (inputs.length);
		int y = point_Y;
		int x = point_X;
		if(logic_scene==null) {
			if (rotation == VERTICAL) {
				x += distance * 0.5;
				for (int i = inputs.length-1; i >= 0; i--) {
					inputs[i].setXPoint(x);
					inputs[i].setYPoint(y);
					x += distance;
				}
				distance = point_width / (outputs.length);
				x = point_X;
				x += distance * 0.5;
				y = point_Y + point_height;
				for (int i = outputs.length-1; i>=0; i--) {
					outputs[i].setXPoint(x);
					outputs[i].setYPoint(y);
					x += distance;
				}
	
			} else {
				y += distance * 0.5;
				for (Dot d : inputs) {
					d.setXPoint(x);
					d.setYPoint(y);
					y += distance;
				}
	
				distance = point_width / (outputs.length);
				x = point_X + point_height;
				y = point_Y;
				y += distance * 0.5;
				for (Dot d : outputs) {
					d.setXPoint(x);
					d.setYPoint(y);
					y += distance;
				}
			}
		}else {
				if (rotation == VERTICAL) {
					x += distance * 0.5;
					for (int i = inputs.length-1; i >= 0; i--) {
						logic_scene.remove(inputs[i]);
						inputs[i].setXPoint(x);
						inputs[i].setYPoint(y);
						try {
							logic_scene.add(inputs[i]);
						} catch (OcupationExeption e) {
						}
						x += distance;
					}
					distance = point_width / (outputs.length);
					x = point_X;
					x += distance * 0.5;
					y = point_Y + point_height;
					for (int i = outputs.length-1; i>=0; i--) {
						logic_scene.remove(outputs[i]);
						outputs[i].setXPoint(x);
						outputs[i].setYPoint(y);
						try {
							logic_scene.add(outputs[i]);
						} catch (OcupationExeption e) {
						}
						x += distance;
					}
		
				} else {
					y += distance * 0.5;
					for (Dot d : inputs) {
						logic_scene.remove(d);
						d.setXPoint(x);
						d.setYPoint(y);
						try {
							logic_scene.add(d);
						} catch (OcupationExeption e) {
						}
						y += distance;
					}
		
					distance = point_width / (outputs.length);
					x = point_X + point_height;
					y = point_Y;
					y += distance * 0.5;
					for (Dot d : outputs) {
						logic_scene.remove(d);
						d.setXPoint(x);
						d.setYPoint(y);
						try {
							logic_scene.add(d);
						} catch (OcupationExeption e) {
						}
						y += distance;
					}
				}
		}
	}

	public abstract void simulate();

	public abstract FunctionalCanvasComponent getClone(byte size);

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
		if (overflow <= LogicSubScene.cross_distance / 2) {
			this.X = (X + X_coord + point_X_rest) - overflow;
			this.point_X_rest = overflow;
		} else {
			this.X = (X + X_coord + point_X_rest) + LogicSubScene.cross_distance - overflow;
			this.point_X_rest = -overflow;
		}
		this.point_X = X / LogicSubScene.cross_distance;
		image_view.setLayoutX(X);
		if (logic_scene != null) {
			for (Dot d : inputs) {
				logic_scene.move(d, X_coord, 0);
			}
			for (Dot d : outputs) {
				logic_scene.move(d, X_coord, 0);
			}
		} else {
			setStandardDotLocations();
		}

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
		image_view.setLayoutY(Y);
		if (logic_scene != null) {
			for (Dot d : inputs) {
				logic_scene.move(d, 0, Y_coord);
			}
			for (Dot d : outputs) {
				logic_scene.move(d, 0, Y_coord);
			}
		} else {
			setStandardDotLocations();
		}

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
		image_view.setLayoutX(X);
		setStandardDotLocations();
	}

	// Overrides method to change dot location
	@Override
	public void setRotation(boolean New_Rotation) {
		rotation = New_Rotation;
		setStandardDotLocations();
		createImageView();
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
				System.out.println("Reset");
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
	}

	protected abstract void resetStandardImage();
}
