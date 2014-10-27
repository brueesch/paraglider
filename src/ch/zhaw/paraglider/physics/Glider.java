package ch.zhaw.paraglider.physics;

/**
 * Class contains the "two" wings of the glider.
 * 
 * @author Christian Brüesch
 * 
 */
public class Glider {

	private Wing leftWing = new Wing("Left");
	private Wing rightWing = new Wing("Right");
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
	
	public void reset()
	{
		pitchAngle = 0;
		rollAngle = 0;
		yawAngle = 0;
		pilot.reset();
	}
	
	public double getHorizontalSpeed() {
		//TODO Calculate new horizontal speed.
		return (leftWing.getHorizontalSpeed() + rightWing.getHorizontalSpeed())/2;
	}
	
	public double getVerticalSpeed() {
		return (leftWing.getVerticalSpeed() + rightWing.getVerticalSpeed())/2;
	}
	
	public double getCurrentGlideRatio() {
		//TODO Calculate new glide ratio.
		return (leftWing.getCurrentGlideRatio() + rightWing.getHorizontalSpeed())/2;
	}
	
	public void changeLeftWingSpeed(double ms)
	{
		leftWing.changeCurrentSpeed(ms);
	}
	public void changeRightWingSpeed(double ms)
	{
		rightWing.changeCurrentSpeed(ms);
	}
	
	public void changePilotSpeed(double ms)
	{
		pilot.setChangeInSpeed(ms);
	}
	public Vector getPilotPosition()
	{
		return pilot.getCurrentPosition();
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
	
	private double calculatePitchAngle()
	{
		return pilot.getPitchAngle();
	}
	
	private double calculateRollAngle()
	{		
		return pilot.getRollAngle();
	}
	
	private void calculateYawAngle()
	{		
		
		double pathLeft = leftWing.getHorizontalSpeed() * Constants.TIME_INTERVALL;
		double pathRight = rightWing.getHorizontalSpeed() * Constants.TIME_INTERVALL;
		
		if(pathLeft == pathRight)
		{
			return;
		}
		
		double radius = 0;
		
		/* Left Curve */
		if(pathLeft < pathRight)
		{
			double xa = pathLeft;
			double xb = pathRight;
			double rb = Constants.GLIDER_WINGSPAN;
			double ra = (xa*rb)/(xb-xa);
			double r = ra + rb;
			yawAngle += Math.toDegrees(xb/r);
		}
		/* Right Curve */
		else
		{
			double xa = pathRight;
			double xb = pathLeft;
			double rb = Constants.GLIDER_WINGSPAN;
			double ra = (xa*rb)/(xb-xa);
			double r = ra + rb;
			yawAngle -= Math.toDegrees(xb/r);
		}
		
	}
	
	
	/**
	 * Returns the angle of the glider in relation to the ground.
	 * @return double angle in radian.
	 */
	public double getAngleOfTheGlider() {
		double currentSpeedLeftWing = leftWing.getHorizontalSpeed();
		double currentSpeedRightWing = rightWing.getHorizontalSpeed();
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

	public void makeNextStep() {
		pilot.makeNextStep(leftWing.getHorizontalSpeed(), rightWing.getHorizontalSpeed());
	}

	
	

}
