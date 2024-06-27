package util;

import canvas.components.Layercomponents.Connection;
import canvas.components.Layercomponents.Output;

public class InputOutputConnectionPair {
	public Connection[] input;
	public Output[] output;
	
	public InputOutputConnectionPair(Connection[] input, Output[] output) {
		this.input = input;
		this.output = output;
	}
}
