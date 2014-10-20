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
	 * Constant defines which angle the paraglider flies per km/h 
	 * speed difference between the two wings.
	 */
	private final double ANGLE_PER_KMH = 5.2;

	/**
	 * Instance of the glider for the Singleton pattern.
	 */
	private static Glider instance;
	
	/**
	 * Constructor. Initializes the left and the right wing.
	 * Private because of the Singleton pattern.
	 */
	private Glider() {
		wings[LEFT_WING] = new Wing("Left");
		wings[RIGHT_WING] = new Wing("Right");
	}
	
	/**
	 * Returns the Instance of the Glider.
	 * @return Glider
	 */
	public static Glider getInstance() {
		if(instance == null) {
			instance = new Glider();
		}
		
		return instance;
	}
	
	public double getHorizontalSpeed() {
		//TODO Calculate new horizontal speed.
		return wings[LEFT_WING].getHorizontalSpeed();
	}
	
	public double getVerticalSpeed() {
		//TODO Calculate new vertical speed.
		return wings[LEFT_WING].getVerticalSpeed();
	}
	
	public double getCurrentGlideRatio() {
		//TODO Calculate new glide ratio.
		return wings[LEFT_WING].getCurrentGlideRatio();
	}
	
	public Wing getWing(String name) throws Exception {
		if(name.equalsIgnoreCase("left")) {
			return wings[LEFT_WING];
		}
		else if(!name.equalsIgnoreCase("right")) {
			throw new Exception("There only exist a right and left wing");
		}
		
		return wings[RIGHT_WING];
		
	}
	
	/**
	 * Returns the angle of the glider in relation to the ground.
	 * @return double angle in radian.
	 */
	public double getAngleOfTheGlider() {
		double currentSpeedLeftWing = wings[LEFT_WING].getHorizontalSpeed();
		double currentSpeedRightWing = wings[RIGHT_WING].getHorizontalSpeed();
		double speedDifferenz = currentSpeedRightWing -currentSpeedLeftWing;
		
		//Left Turn
		if(speedDifferenz>0) {
			double angle = speedDifferenz*ANGLE_PER_KMH;
			return Math.toRadians(angle);
		}
		//Right Turn
		else if(speedDifferenz<0) {
			double angle = speedDifferenz*ANGLE_PER_KMH;
			return Math.toRadians(angle);
		}
		else {
			return 0;
		}
		
		
	}
	

}
