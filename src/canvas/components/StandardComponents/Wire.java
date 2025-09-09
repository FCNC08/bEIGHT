package canvas.components.StandardComponents;

import canvas.LogicSubScene;
import canvas.components.CanvasComponent;
import canvas.components.SingleCanvasComponent;
import canvas.components.State;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import util.ComponentBox;
import util.IncompatibleWireWidthException;

public class Wire extends SingleCanvasComponent {
	
	private boolean wires_set = false;
	
	public Wire(int Startwidth) {
		// Creating Wire with height and painting it using PaintWire()
		super(Startwidth, LogicSubScene.wire_height);
		this.wires = 1;
		focus = false;
		PaintWire();
	}

	private void PaintWire() {
		clearPixels();
		if(wires>1) {
			if(state.isEqual(State.ERROR)) {
				for (int x = LogicSubScene.wire_height / 4; x < width; x++) {
					for (int y = 0; y < getHeight(); y++) {
						pwriter.setColor(x, y, State.ERROR.getColor());
					}
				}
			}else {
				for (int x = LogicSubScene.wire_height / 4; x < width; x++) {
					for (int y = 0; y < getHeight(); y++) {
						pwriter.setColor(x, y, Color.GREY);
					}
				}
			}

		}else {
			Color c = getColor();
			// Painting each pixel with a pixelwriter in the WritableImage
			for (int x = LogicSubScene.wire_height / 4; x < width; x++) {
				for (int y = LogicSubScene.wire_height / 4; y < getHeight() - LogicSubScene.wire_height / 4; y++) {
					pwriter.setColor(x, y, c);
				}
			}
			c = null;
		}
	}

	public void change() {
		// Changing the state means changing the state of each connected component
		if(getState().isEqual(State.ERROR)) {
			for (SingleCanvasComponent connected : connected_Components) {
				connected.setErrorMessage(error_message);
				connected.setState(getState());
			}
		}else {
			for (SingleCanvasComponent connected : connected_Components) {
				connected.setState(getState());
			}
		}

		PaintWire();
	}
	
	protected void changeStates() {
		for(SingleCanvasComponent connected : connected_Components) {
			connected.setStates(states);
		}
	}
	
	@Override
	public void setWireWidth(int wires) {
		if(wires > 1) {
			this.wires = wires;
			states = new State[wires];
			PaintWire();
		}else {
			this.wires = wires;
			states = null;
			PaintWire();
		}
	}
	
	public void resetWireSet() {
		wires = 1;
		wires_set = false;
	}
	
	
	// Adding/Removing connectedComponents
	@Override
	public void addComponent(SingleCanvasComponent connecting_comp) throws IncompatibleWireWidthException {
		if (connecting_comp != null && connecting_comp != ComponentBox.occupied) {
			if(wires == connecting_comp.wires) {
				connected_Components.add(connecting_comp);
				setState(connecting_comp.state);
			}else if(!wires_set){
				this.wires = connecting_comp.wires;
				connected_Components.add(connecting_comp);
				setState(connecting_comp.state);
				wires_set = true;
				PaintWire();
			}else {
				throw new IncompatibleWireWidthException();
			}
		}
	}
	
	@Override
	public void setFocus(boolean focus) {
		// Setting focus and adding Squares at the end or printing the wire new if it
		// turns off
		if (focus != this.focus) {
			if (focus) {

				for (int y = 1; y < LogicSubScene.wire_height - 1; y++) {
					for (int x = 1; x < LogicSubScene.wire_height - 1; x++) {
						// Painting inner square of the focus square at the start
						pwriter.setColor(x, y, LogicSubScene.focus_square_main);
					}
					for (int x = width - 1; x > width - LogicSubScene.wire_height + 1; x--) {
						// Painting inner square of the focus square at the end
						pwriter.setColor(x, y, LogicSubScene.focus_square_main);
					}
				}
				for (int x = 1; x < LogicSubScene.wire_height; x++) {
					// Painting horizontal outlines at the start
					pwriter.setColor(x, 0, LogicSubScene.focus_square_secondary);
					pwriter.setColor(x, LogicSubScene.wire_height - 1, LogicSubScene.focus_square_secondary);

					// Painting vertical outlines at the start
					pwriter.setColor(0, x, LogicSubScene.focus_square_secondary);
					pwriter.setColor(LogicSubScene.wire_height - 1, x, LogicSubScene.focus_square_secondary);

					// Painting horizontal outlines at the end
					pwriter.setColor(x + width - LogicSubScene.wire_height, 0, LogicSubScene.focus_square_secondary);
					pwriter.setColor(x + width - LogicSubScene.wire_height, LogicSubScene.wire_height - 1, LogicSubScene.focus_square_secondary);

					// Painting vertical outlines at the end
					pwriter.setColor(width - 1, x, LogicSubScene.focus_square_secondary);
					pwriter.setColor(width - LogicSubScene.wire_height + 1, x, LogicSubScene.focus_square_secondary);
				}
				pwriter.setColor(0, 0, LogicSubScene.focus_square_secondary);
			} else {
				PaintWire();
			}
			this.focus = focus;
		}
	}

	public ImageView getImageView() {
		return image_view;
	}

	@Override
	protected void createImageView() {
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

	public void setHeight(int height) {
		this.height = height;
	}

}
