package canvas.components.StandardComponents;

import canvas.LogicSubScene;
import canvas.components.CanvasComponent;
import canvas.components.FunctionalCanvasComponent;
import canvas.components.State;
import javafx.event.EventHandler;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Input extends FunctionalCanvasComponent{
	protected static int StandardWidth_big = LogicSubScene.cross_distance * 4;

	protected static int StandardWidth_middle = LogicSubScene.cross_distance * 2;

	protected static int StandardWidth_small = LogicSubScene.cross_distance;
	
	private State state = State.OFF;
	public Input(int width, String size) throws IllegalArgumentException {
		super(width, width, 0, 1, size);
		paintImage();
	}

	public void setState(State state) {
		this.state = state;
		simulate();
	}
	
	@Override
	public void simulater() {
		outputs[0].setState(state);
		paintImage();
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		return getInput(size);
	}

	protected void paintImage() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
               pwriter.setColor(i, j, Color.BLACK);
            }
        }
        int radius = (int) (width*0.3);
        for (int i = width/2 - radius; i < width/2 + radius; i++) {
            for (int j = width/2 - radius; j < width/2 + radius; j++) {
                // Check if the pixel is within the circle using the circle equation
                if ((i - width/2) * (i - width/2) + (j - width/2) * (j - width/2) < radius * radius) {
                    pwriter.setColor(i, j, state.getColor());
                }
            }
        }

	}

	@Override
	protected void resetStandardImage() {
		paintImage();
	}
	
	@Override
	public void setStandardDotLocations() {
		outputs[0].setXPoint(point_X+point_width);
		outputs[0].setYPoint(point_Y+point_height/2);
	}
	
	public static Input getInput(String size) {
		int width;
		switch(size) {
		case SIZE_BIG:
			width = StandardWidth_big;
			break;
		case SIZE_MIDDLE:
			width = StandardWidth_middle;
			break;
		case SIZE_SMALL:
			width = StandardWidth_small;
			break;
		default:
			width = 1;
			break; 
		}
		return new Input(width, size);
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
		EventHandler<MouseEvent> change_mouse_event = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(me.isStillSincePress()) {
					if(state.isEqual(State.ON)) {
						state = State.OFF;
						simulate();
					}else if(state.isEqual(State.OFF)) {
						state = State.ON;
						simulate();
					}
				}
			}
		};
		image_view.addEventFilter(MouseEvent.MOUSE_CLICKED, change_mouse_event);
	}
	
	@Override
	protected void createContextMenu() {
		Label name_label = new Label("Input");
		name_label.getStyleClass().add("cm-header");
		name_label.setMouseTransparent(true);
		CustomMenuItem name_item = new CustomMenuItem(name_label);
		name_item.getStyleClass().add("cm-header-item");
		menu.getItems().add(name_item);
		menu.getItems().add(new SeparatorMenuItem());
	}
	
	@Override
	protected void setVerilogString(short[] comp_count) {
		verilog_string = "Input"+comp_count[1];
		comp_count[1]++;
	}

	@Override
	protected void setArduinoString(short[] comp_count) {
	}

	@Override
	public void createLayerGate() {		
	}

}
