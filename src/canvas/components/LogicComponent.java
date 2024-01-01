package canvas.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class LogicComponent extends FunctionalCanvasComponent{

	
	public LogicComponent(byte size, int width, int height, int input_count, int output_count) {
		super(size,width, height, input_count, output_count);
		// TODO Auto-generated constructor stub
	}
	
	public static void setStandardImage(Image standard_image) {
		//Override in higher classes
	}
	
	@Override
	protected void createImageView() {
		//Creating ImageView with changing the direction of the ImageView
		image_view= new ImageView();
		image_view.setImage(this);
		image_view.setRotate(getRotation());
		image_view.setLayoutX(X);
		image_view.setLayoutY(Y);
		
		if(rotation == CanvasComponent.VERTICAL) {
			image_view.setLayoutY(image_view.getLayoutY()+0.5*width-0.5*getHeight());
			image_view.setLayoutX(image_view.getLayoutX()-0.5*width+0.5*getHeight());
		}
	}
}
