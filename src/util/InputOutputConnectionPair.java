package util;

import canvas.components.Layercomponents.Connection;
import canvas.components.Layercomponents.LayerGate;

public class InputOutputConnectionPair {
	public Connection[] input;
	public Connection[] output;
	
	public InputOutputConnectionPair(Connection[] input, Connection[] output) {
		this.input = input;
		this.output = output;
	}
}
