package canvas.components.StandardComponents.LogicComponents;

import canvas.components.CanvasComponent;
import canvas.components.LogicComponent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ANDGate extends LogicComponent{

	public static Image LogicComponent_Image = new Image("AND.png");
	
	
	public ANDGate(int width, int height, int input_count, int output_count, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y) {
		super(width, height, input_count, output_count, inputs_x, inputs_y, outputs_x, outputs_y);
		// TODO Auto-generated constructor stub
	}
	
	public static void setStandardImage(Image standard_image) {
		LogicComponent_Image = standard_image;
	}
	
	public static ANDGate getANDGATE(int inputs, int outputs, int[] inputs_x, int[] inputs_y, int[] outputs_x, int[] outputs_y ) {
		ANDGate component = new ANDGate(200,200, inputs, outputs, inputs_x, inputs_y, outputs_x, outputs_y);
		ImageView temp_view = new ImageView(LogicComponent_Image);
		temp_view.setFitHeight(200);
		temp_view.setFitWidth(200);
		temp_view.snapshot(null, component);
		temp_view = null;
		System.gc();
		return component;
	}

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocus(boolean status) {
		// TODO Auto-generated method stub
		
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
}
