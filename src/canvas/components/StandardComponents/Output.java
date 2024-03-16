package canvas.components.StandardComponents;

import canvas.LogicSubScene;
import canvas.components.CanvasComponent;
import canvas.components.FunctionalCanvasComponent;
import canvas.components.State;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import util.Info;

public class Output extends FunctionalCanvasComponent{
	protected static int StandardWidth_big = LogicSubScene.cross_distance * 4;

	protected static int StandardWidth_middle = LogicSubScene.cross_distance * 2;

	protected static int StandardWidth_small = LogicSubScene.cross_distance;
	private State state = OFF;
	LogicSubScene parent;
	public Output(int width, String size) throws IllegalArgumentException {
		super(width, width, 1, 0);
		paintImage();
	}
	
	public State getState() {
		return state;
	}
	
	public void setParent(LogicSubScene scene) {
		this.parent = scene;
	}
	
	@Override
	public void simulate() {
		state = inputs[0].getState();
		if(parent != null) {
			parent.changeOutput();
		}
		paintImage();
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		return getOutput(size);
	}

	protected void paintImage() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if ((i - width*0.5) * (i - width*0.5) + (j - width*0.5) * (j - width*0.5) <= width*0.5 * width*0.5) {
                    pwriter.setColor(i, j, Color.BLACK);
                }else {
                	pwriter.setColor(i, j, Color.TRANSPARENT);
                }
            }
        }
        int radius = (int) (width*0.3);
        for (int i = width/2 - radius; i <= width/2 + radius; i++) {
            for (int j = width/2 - radius; j <= width/2 + radius; j++) {
                // Check if the pixel is within the circle using the circle equation
                if ((i - width/2) * (i - width/2) + (j - width/2) * (j - width/2) <= radius * radius) {
                    pwriter.setColor(i, j, state.getColor());
                }
            }
        }

	}
	
	@Override
	protected void createInfo() {
		info = new Info();
		info.setHeadline("Output");
	}

	@Override
	protected void resetStandardImage() {
		paintImage();
	}
	
	@Override
	public void setStandardDotLocations() {
		inputs[0].setXPoint(point_X);
		inputs[0].setYPoint(point_Y+point_height/2);
	}
	
	public static Output getOutput(String size) {
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
		return new Output(width, size);
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
	
	@Override
	protected void setVerilogString(short[] comp_count) {
		verilog_string = "Output"+comp_count[6];
		comp_count[6]++;
	}

}
