package canvas.components.StandardComponents;

import java.util.ArrayList;

import canvas.LogicSubScene;
import canvas.components.CanvasComponent;
import canvas.components.SingleCanvasComponent;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Wire extends SingleCanvasComponent{
	
	protected boolean front_dot = false;
	protected boolean back_dot = false;
	protected boolean focus;
	
	public Wire(int Startwidth){
		//Creating Wire with height and painting it using PaintWire()
		super(Startwidth, LogicSubScene.wire_height);
		setHeight(LogicSubScene.wire_height/2);
		width = Startwidth;
		focus = false;
		pwriter = getPixelWriter();
		connected_Components = new ArrayList<>();
		PaintWire();
	}
	
	private void PaintWire() {
		clearPixels();
		Color c = getColor();
		//Painting each pixel with a pixelwriter in the WritableImage
		for(int x = LogicSubScene.wire_height/4; x < width; x++) {
			for(int y = LogicSubScene.wire_height/4; y < getHeight()-LogicSubScene.wire_height/4; y++) {
				pwriter.setColor(x, y, c);
			}
		}
		
		c = null;
	}
	
	public void setFrontDot(boolean set) {
		front_dot = set;
	}
	public void setBackDot(boolean set) {
		back_dot = set;
	}
	
	public void change() {
		//Changing the state means changing the state of each connected component
		for(short i: connected_Components) {
			SingleCanvasComponent c = logic_scene.getCanvasComponent(i);
			if(c.getSetState() == LogicSubScene.actual_set_state) {
				if (this.state !=c.getState()) {
					c.setState(ERR);
					this.setState(ERR);
				}
			}else {
				c.setState(this.state);
				c.setSetState(LogicSubScene.actual_set_state);
			}
			System.out.println(c);
		}
			
		PaintWire();
	}
	
	@Override
	public void setFocus(boolean focus) {
		//Setting focus and adding Squares at the end or printing the wire new if it turns off
		if(focus != this.focus) {
			if(focus) {
				for(int x = 2; x < LogicSubScene.wire_height-1; x ++) {
					for(int y = 2; y < LogicSubScene.wire_height-1; y++) {
						pwriter.setColor(x, y, LogicSubScene.focus_square_main);
					}
				}
				for(int x = 1; x < LogicSubScene.wire_height; x++) {
					//Paint horizontal lines
					pwriter.setColor(x, 1, LogicSubScene.focus_square_secondary);
					pwriter.setColor(x, LogicSubScene.wire_height-1, LogicSubScene.focus_square_secondary);
					
					//Paint vertical lines
					pwriter.setColor(1, x, LogicSubScene.focus_square_secondary);
					pwriter.setColor(LogicSubScene.wire_height-1, x, LogicSubScene.focus_square_secondary);
				}
				for(int x = (int) getWidth(); x > getWidth()-LogicSubScene.wire_height; x--) {
					for(int y = (int) getHeight(); y > getHeight()-LogicSubScene.wire_height; y--) {
						pwriter.setColor(x, y, LogicSubScene.focus_square_main);
					}
				}
			}else {
				System.out.println("test");
				PaintWire();
			}
			this.focus = focus;
		}
	}
	public ImageView getImageView() {
		createImageView();
		return image_view;
	}

	@Override
	protected void createImageView() {
		image_view= new ImageView();
		image_view.setImage(this);
		image_view.setRotate(getRotation());
		image_view.setLayoutX(image_view.getLayoutX() + X);
		image_view.setLayoutY(image_view.getLayoutY() + Y);
		if(rotation == CanvasComponent.VERTICAL) {
			image_view.setLayoutY(image_view.getLayoutY()+0.5*width-0.5*getHeight());
			image_view.setLayoutX(image_view.getLayoutX()-0.5*width+0.5*getHeight());
		}
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
