package ch.zhaw.paraglider.physicsnew;

public class Glider2 {
	
	private static final int LEFT_WING = 0;	
	private static final int RIGHT_WING = 1;
	private Pilot2 passagier = Pilot2.getInstance();
	private Wing2[] wings = new Wing2[2];
	
	/**
	 * Constant defines which angle the paraglider flies per km/h 
	 * speed difference between the two wings.
	 */
	private final double ANGLE_PER_KMH = 5.2;

	
	private static Glider2 instance;
	
	private Glider2() {
		wings[LEFT_WING] = new Wing2("Left");
		wings[RIGHT_WING] = new Wing2("Right");
	}
	public static Glider2 getInstance() {
		if(instance == null) {
			instance = new Glider2();
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
	
	public Wing2 getWing(String name) throws Exception {
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
