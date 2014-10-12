package ch.zhaw.paraglider.physics;

/**
 * Class contains the "two" wings of the glider.
 * 
 * @author Christian Brüesch
 * 
 */
public class Glider {

	/**
	 * Constant for left wing.
	 */
	private static final int LEFT_WING = 0;
	/**
	 * Constant for the right wing.
	 */
	private static final int RIGHT_WING = 1;
	/**
	 * Array of wings. Contains space for two wing objects.
	 */
	private Wing[] wings = new Wing[2];

	/**
	 * Constructor. Initializes the left and the right wing.
	 */
	public Glider() {
		wings[LEFT_WING] = new Wing("Left");
		wings[RIGHT_WING] = new Wing("Right");
	}

}
