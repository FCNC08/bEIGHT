package util;

import canvas.components.Layercomponents.Connection;
import canvas.components.Layercomponents.LayerGate;

public class InputOutputConnectionPair {
	public Connection[] input;
	public Connection[] output;
	public LayerGate[] outputgates;
	
	public InputOutputConnectionPair(Connection[] input, Connection[] output, LayerGate[] outputgates) {
		this.input = input;
		this.output = output;
		this.outputgates = outputgates;
	}
}
