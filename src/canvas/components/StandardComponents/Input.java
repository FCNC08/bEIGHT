package canvas.components.StandardComponents;

import canvas.components.CanvasComponent;
import canvas.components.FunctionalCanvasComponent;
import canvas.components.State;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Input extends FunctionalCanvasComponent{
	private State state = OFF;
	public Input(int width) throws IllegalArgumentException {
		super(width, width, 0, 1);
		paintImage();
	}

	@Override
	public void simulate() {
		outputs[0].setState(state);
		paintImage();
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		// TODO Auto-generated method stub
		return null;
	}

	protected void paintImage() {
		for(int x = 0; x<=width; x++) {
			for(int y = 0; y<=width; y++) {
				if((x-width/2)*(x-width/2)*(y-width/2)*(y-width/2)<=width*width/4) {
					pwriter.setColor(x, y, state.getColor());
				}else {
					pwriter.setColor(x, y, Color.BLACK);
				}
			}
		}
	}
	
	@Override
	protected void createInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void resetStandardImage() {
		// TODO Auto-generated method stub
		
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
					if(state == ON) {
						state = OFF;
					}else if(state == OFF) {
						state = ON;
					}
				}
			}
		};
		image_view.addEventFilter(MouseEvent.MOUSE_CLICKED, change_mouse_event);
	}

}
