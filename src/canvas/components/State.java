package canvas.components;

import javafx.scene.paint.Color;
import util.ErrorStateExeption;

public class State {
	public static boolean STANDARD_MODE = true;
	public static boolean ERROR_MODE = !STANDARD_MODE;

	public static boolean ON_ERROR = true;
	public static boolean OFF_UNSET = !ON_ERROR;

	public static boolean DARK_MODE = true;
	public static boolean WHITE_MODE = false;

	public static State ON = new State(STANDARD_MODE, ON_ERROR);
	public static State OFF = new State(STANDARD_MODE, OFF_UNSET);
	public static State ERROR = new State(ERROR_MODE, ON_ERROR);
	public static State UNSET = new State(ERROR_MODE, OFF_UNSET);

	private static Color white_off = new Color(0.023, 0.6, 0.14, 1);
	private static Color white_on = new Color(0.023, 0.9, 0.2, 1);
	private static Color white_unset = new Color(0.5, 0.5, 0.5, 1);
	private static Color white_err = new Color(01, 0.03, 0.06, 1);

	public static Color off = white_off;
	public static Color on = white_on;
	public static Color unset = white_unset;
	public static Color err = white_err;

	public boolean mode;
	public boolean state;

	public static void setMode(boolean state) {
		if (state == WHITE_MODE) {
			off = white_off;
			on = white_on;
			unset = white_unset;
			err = white_err;
		}
	}

	public static State getState(boolean set_mode, boolean set_state) {
		if (set_mode == STANDARD_MODE) {
			return set_state == ON_ERROR ? ON : OFF;
		} else {
			return set_state == ON_ERROR ? ERROR : UNSET;
		}
	}

	public State(boolean set_mode, boolean set_state) {
		mode = set_mode;
		state = set_state;
	}

	public Color getColor() {
		// Getting each color depending of the State
		if (mode == STANDARD_MODE) {
			if (state == ON_ERROR) {
				return on;
			} else {
				return off;
			}
		} else {
			if (state == ON_ERROR) {
				return err;
			} else {
				return unset;
			}
		}
	}

	public boolean getStateBoolean() throws ErrorStateExeption {
		if (mode == ERROR_MODE) {
			throw new ErrorStateExeption();
		} else {
			return state;
		}
	}

	public boolean isEqual(State s2) {
		return mode == s2.mode && state == s2.state;
	}
}
