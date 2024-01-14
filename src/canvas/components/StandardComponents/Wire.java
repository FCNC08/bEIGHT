package canvas.components.StandardComponents;

import java.util.ArrayList;

import org.apache.commons.logging.Log;

import canvas.LogicSubScene;
import canvas.components.CanvasComponent;
import canvas.components.SingleCanvasComponent;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Wire extends SingleCanvasComponent {

	public Wire(int Startwidth) {
		// Creating Wire with height and painting it using PaintWire()
		super(Startwidth, LogicSubScene.wire_height);
		// setHeight(LogicSubScene.wire_height / 2);
		width = Startwidth;
		focus = false;
		connected_Components = new ArrayList<>();
		PaintWire();
	}

	private void PaintWire() {
		clearPixels();
		Color c = getColor();
		// Painting each pixel with a pixelwriter in the WritableImage
		for (int x = LogicSubScene.wire_height / 4; x < width; x++) {
			for (int y = LogicSubScene.wire_height / 4; y < getHeight() - LogicSubScene.wire_height / 4; y++) {
				pwriter.setColor(x, y, c);
			}
		}

		c = null;
	}

	public void change() {
		// Changing the state means changing the state of each connected component
		for (SingleCanvasComponent i : connected_Components) {
			SingleCanvasComponent c = i;
			if (c.getSetState() == LogicSubScene.actual_set_state) {
				if (this.state != c.getState()) {
					c.setState(ERR);
					this.setState(ERR);
				}
			} else {
				c.setState(this.state);
				c.setSetState(LogicSubScene.actual_set_state);
			}
			System.out.println(c);
		}

		PaintWire();
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
					pwriter.setColor(x + width - LogicSubScene.wire_height, LogicSubScene.wire_height - 1,
							LogicSubScene.focus_square_secondary);

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
