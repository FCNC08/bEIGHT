package canvas.components.StandardComponents.LogicComponents;

import canvas.components.LogicComponent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class ANDGate extends LogicComponent{

	public static Image LogicComponent_Image = new Image("AND.png");
	
	
	public ANDGate(byte size, int width, int height, int input_count, int output_count) {
		super(size, width, height, input_count, output_count);
	}
	
	public static void setStandardImage(Image standard_image) {
		LogicComponent_Image = standard_image;
	}
	
	public static ANDGate getANDGATE(byte size,int inputs, int outputs) {
		//Creating a ANDGate with standard sizes
		int height;
		int width;
		switch (size) {
        case SIZE_BIG:
            width = StandardWidth_big;
            height = StandardHeight_big;
            break;
        case SIZE_MIDDLE:
            width = StandardWidth_middle;
            height = StandardHeight_middle;
            break;
        case SIZE_SMALL:
            width = StandardWidth_small;
            height = StandardHeight_small;
            break;
        default:
            width = 1;
            height = 1;
            break;
		}
		//Creating it/painting the Image
		ANDGate component = new ANDGate(size, width,  height, inputs, outputs);
		ImageView temp_view = new ImageView(LogicComponent_Image);
		temp_view.setFitHeight(height);
		temp_view.setFitWidth(width);
		temp_view.snapshot(null, component);
		temp_view = null;
		PixelReader reader = component.getPixelReader();
		PixelWriter writer = component.getPixelWriter();
		Color background = reader.getColor(0, 0);
		//Removing all background/Color of the pixel 0|0
		for(int x = 0; x < width; x++) {
			for(int y = 0; y< height; y++) {
				if(background.equals(reader.getColor(x, y))) {
					writer.setColor(x, y, Color.TRANSPARENT);
				}
			}
		}
		reader = null;
		writer = null;
		System.gc();
		return component;
	}
	
	public static ANDGate getSolidANDGATE(byte size,int inputs, int outputs) {
		//Creating like getANDGate without removing the background
		//Used in ComponentChooser
		int height;
		int width;
		switch (size) {
        case SIZE_BIG:
            width = StandardWidth_big;
            height = StandardHeight_big;
            break;
        case SIZE_MIDDLE:
            width = StandardWidth_middle;
            height = StandardHeight_middle;
            break;
        case SIZE_SMALL:
            width = StandardWidth_small;
            height = StandardHeight_small;
            break;
        default:
            width = 1;
            height = 1;
            break;
		}
		ANDGate component = new ANDGate(size, width,  height, inputs, outputs);
		ImageView temp_view = new ImageView(LogicComponent_Image);
		temp_view.setFitHeight(height);
		temp_view.setFitWidth(width);
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
	public LogicComponent getClone(byte size) {
		//Function to clone a component in this case a ANDGate
		ANDGate gate = ANDGate.getANDGATE(size, input_count, output_count);
		return gate;
	}
}
