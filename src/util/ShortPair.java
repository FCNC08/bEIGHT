package util;

import application.Main;

public class ShortPair {
	public short HorizontalShort;
	public short VerticalShort;
	
	public ShortPair(){}
	public ShortPair(short horizontal, short vertical){
		this.HorizontalShort = horizontal;
		this.VerticalShort = vertical;
	}
	public short pickRandom() {
		return Main.random.nextBoolean()? this.HorizontalShort : this.VerticalShort;
	}
}
