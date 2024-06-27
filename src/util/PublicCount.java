package util;

public class PublicCount {
	
	private int count = 0;
	public PublicCount(int start) {
		count = start;
	}
	public void increase() {
		count++;
	}
	public int getCount() {
		return count;
	}
}
