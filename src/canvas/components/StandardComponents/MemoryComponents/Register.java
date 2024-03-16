package canvas.components.StandardComponents.MemoryComponents;

import canvas.components.FunctionalCanvasComponent;
import canvas.components.MemoryCanvasComponent;
import util.Info;

public class Register extends MemoryCanvasComponent {

	public Register(int width, int height, int input_count, int output_count) {
		super(width, height, input_count, output_count,1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void simulate() {
		
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void resetStandardImage() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void createInfo() {
		info = new Info();
		info.setHeadline("Register");
	}
	
	@Override
	protected void setVerilogString(short[] comp_count) {
		verilog_string = "Register"+comp_count[8];
		comp_count[8]++;
	}

}
