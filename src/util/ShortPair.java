package util;

import application.Main;

public class ShortPair {
	//IDs for a horizontal/Vertical Wire and a Dot in one place used in used array
	public short HorizontalShort;
	public short VerticalShort;
	public short Dot;
	
	public ShortPair(){}
	public ShortPair(short horizontal, short vertical, short Dot){
		this.HorizontalShort = horizontal;
		this.VerticalShort = vertical;
		this.Dot = Dot;
	}
	public short pickRandom() {
		if(HorizontalShort < 2&&VerticalShort>1) {
			return VerticalShort;
		}
		if(VerticalShort < 2&&HorizontalShort > 1) {
			return VerticalShort;
		}
		return Main.random.nextBoolean()? this.HorizontalShort : this.VerticalShort;
	}
}
