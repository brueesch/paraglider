package ch.zhaw.paraglider.physics;

public class Glider {

	private static final int LEFT_WING = 0;
	private static final int RIGHT_WING = 1;
	private Wing[] wings = new Wing[2];
	
	public Glider() {
		wings[LEFT_WING] = new Wing("Left");
		wings[RIGHT_WING] = new Wing("Right");
	}

	
	
	
	
	
}
