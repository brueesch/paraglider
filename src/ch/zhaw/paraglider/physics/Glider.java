package ch.zhaw.paraglider.physics;

/**
 * Class contains the "two" wings of the glider.
 * 
 * @author Christian Brüesch
 * 
 */
public class Glider {

	private Wing LeftWing = new Wing("Left");
	private Wing RightWing = new Wing("Right");
	private Pilot pilot = Pilot.getInstance();
	
	/* Angle caused by the pendulum-effect */
	private double pitchAngle = 0;
	
	/* Angle caused by the centrifugal-forces*/
	private double rollAngle = 0;
	
	/* Angle caused by the curveflight */
	private double yawAngle = 0;
	
	/**
	 * Constant defines which angle the paraglider flies per km/h 
	 * speed difference between the two wings.
	 */
	private final double ANGLE_PER_KMH = 5.2;
	
	private static Glider instance;
	private Glider() {}
	
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
		return LeftWing.getHorizontalSpeed();
	}
	
	public double getVerticalSpeed() {
		//TODO Calculate new vertical speed.
		return LeftWing.getVerticalSpeed();
	}
	
	public double getCurrentGlideRatio() {
		//TODO Calculate new glide ratio.
		return LeftWing.getCurrentGlideRatio();
	}
	
	public Wing getLeftWing() {
		return RightWing;
	}
	public Wing getRightWing() {
		return RightWing;
	}

	public double getPitchAngle() {
		return pitchAngle;
	}

	public double getRollAngle() {
		return rollAngle;
	}

	public double getYawAngle() {
		return yawAngle;
	}
	
	private void calculateYawAngle()
	{
		
	}
	
	
	/**
	 * Returns the angle of the glider in relation to the ground.
	 * @return double angle in radian.
	 */
	public double getAngleOfTheGlider() {
		double currentSpeedLeftWing = LeftWing.getHorizontalSpeed();
		double currentSpeedRightWing = RightWing.getHorizontalSpeed();
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
