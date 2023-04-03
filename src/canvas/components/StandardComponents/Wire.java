package canvas.components.StandardComponents;

import java.util.ArrayList;

import canvas.LogicSubScene;
import canvas.components.CanvasComponent;
import canvas.components.SingleCanvasComponent;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Wire extends SingleCanvasComponent{
	
	boolean front_dot = false;
	boolean back_dot = false;
	PixelWriter pwriter;
	public int width;
	public int height;
	
	public Wire(int Startwidth){
		super(Startwidth, LogicSubScene.wire_height);
		setHeight(LogicSubScene.wire_height);
		width = Startwidth;
		pwriter = getPixelWriter();
		connected_Components = new ArrayList<>();
		PaintWire();
	}
	
	private void PaintWire() {
		Color c = getColor();
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < getHeight(); y++) {
				pwriter.setColor(x, y, c);
			}
		}
		
		c = null;
		System.gc();
	}
	
	public void setFrontDot(boolean set) {
		front_dot = set;
	}
	public void setBackDot(boolean set) {
		back_dot = set;
	}
	
	@Override
	public void change() {
		for(short i: connected_Components) {
			SingleCanvasComponent c = LogicSubScene.getCanvasComponent(i);
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
	
	public void addConnectedComponent(short ID) {
		connected_Components.add((short)ID);
	}
	public void removeConnectedComponent(short ID) {
		connected_Components.remove((short) ID);
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
