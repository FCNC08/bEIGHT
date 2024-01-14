package util;

import application.Main;
import canvas.components.Dot;
import canvas.components.SingleCanvasComponent;

public class ComponentBox {
	public static SingleCanvasComponent occupied = new Dot(null);
	// IDs for a horizontal/Vertical Wire and a Dot in one place used in used array
	public SingleCanvasComponent HorizontalComponent;
	public SingleCanvasComponent VerticalComponent;
	public SingleCanvasComponent Dot;

	public ComponentBox() {
	}

	public ComponentBox(SingleCanvasComponent horizontal, SingleCanvasComponent vertical, Dot Dot) {
		this.HorizontalComponent = horizontal;
		this.VerticalComponent = vertical;
		this.Dot = Dot;
	}

	public SingleCanvasComponent pickRandom() {
		if ((HorizontalComponent == null || HorizontalComponent == occupied) && VerticalComponent != null && VerticalComponent != occupied) {
			return VerticalComponent;
		}
		if ((VerticalComponent == null || VerticalComponent == occupied) && HorizontalComponent != null && HorizontalComponent != occupied) {
			return HorizontalComponent;
		}
		return Main.random.nextBoolean() ? HorizontalComponent : VerticalComponent;
	}
}
