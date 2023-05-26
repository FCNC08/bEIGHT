package canvas.components;

import javafx.scene.paint.Color;

public class State {
	public static boolean ERROR_MODE = false;
	public static boolean STANDARD_MODE = true;
	
	public static boolean ON_ERROR = true;
	public static boolean OFF_UNSET = true;
	
	public static boolean DARK_MODE = true;
	public static boolean WHITE_MODE = false;
	
	private static Color white_off = new Color(0.023, 0.6, 0.14, 1);
	private static Color white_on = new Color(0.023, 0.9, 0.2, 1);
	private static Color white_unset = new Color(0.5, 0.5, 0.5, 1);
	private static Color white_err = new Color(01, 0.03, 0.06, 1);
	
	public static Color off = white_off;
	public static Color on = white_on;
	public static Color unset = white_unset;
	public static Color err = white_err;
	
	protected boolean mode;
	protected boolean state;
	
	public static void setState(boolean state) {
		if(state == WHITE_MODE) {
			off = white_off;
			on = white_on;
			unset = white_unset;
			err = white_err;
		}
	}
	
	
	
	public State(boolean set_mode, boolean set_state) {
		mode = set_mode;
		state = set_state;
	}
	
	public Color getColor() {
		if(mode == STANDARD_MODE) {
			if(state == ON_ERROR) {
				return on;
			}else {
				return off;
			}
		}else {
			if(state == ON_ERROR) {
				return err;
			}else {
				return unset;
			}
		}
	}
	
	public boolean getStateBoolean() throws ErrorStateExeption{
		if(mode = ERROR_MODE) {
			throw new ErrorStateExeption();
		}else {
			return state;
		}
	}
}
