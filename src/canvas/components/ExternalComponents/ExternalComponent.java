package canvas.components.ExternalComponents;

import canvas.components.FunctionalCanvasComponent;
import net.lingala.zip4j.ZipFile;

public abstract class ExternalComponent extends FunctionalCanvasComponent {
	public ExternalComponent(byte size, int width, int height, int input_count, int output_count, ZipFile file) throws IllegalAccessException {
		super(width, height, input_count, output_count);

	}

	@Override
	public void setFocus(boolean status) {
		// TODO Auto-generated method stub

	}
}
