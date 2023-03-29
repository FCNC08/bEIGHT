package canvas.components;

import javafx.scene.paint.Color;

public class StateColor {
	
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
	
	public static void setState(boolean state) {
		if(state == WHITE_MODE) {
			off = white_off;
			on = white_on;
			unset = white_unset;
			err = white_err;
		}
	}
	
}
