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
	
	private double pitchAngle = 0;
	private double rollAngle = 0;
	private double yawAngle = 0;
	
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
	
	public double getHorizontalSpeed() {
		//TODO Calculate new horizontal speed.
		return (leftWing.getHorizontalSpeed() + rightWing.getHorizontalSpeed())/2;
	}
	
	public double getVerticalSpeed() {
		return (leftWing.getVerticalSpeed() + rightWing.getVerticalSpeed())/2;
	}
	
	public double getCurrentGlideRatio() {
		//TODO Calculate new glide ratio.
		return (leftWing.getCurrentGlideRatio() + rightWing.getCurrentGlideRatio())/2;
	}	
	
	public void changeSpeed(double msLeft, double msRight )
	{
		leftWing.changeCurrentSpeed(msLeft);
		rightWing.changeCurrentSpeed(msRight);
		pilot.setChangeInSpeed((msLeft+msRight)/2);
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
	
	
	public void reset()
	{
		pitchAngle = 0;
		rollAngle = 0;
		yawAngle = 0;
		pilot.reset();
	}
	
	private double calculatePitchAngle()
	{
		return pilot.getPitchAngle();
	}
	
	private double calculateRollAngle()
	{		
		return pilot.getRollAngle(leftWing.getHorizontalSpeed(), rightWing.getHorizontalSpeed());
	}
	
	private void calculateYawAngle()
	{		
		
		double pathLeft = leftWing.getHorizontalSpeed() * Constants.TIME_INTERVALL;
		double pathRight = rightWing.getHorizontalSpeed() * Constants.TIME_INTERVALL;
		
		if(pathLeft == pathRight)
		{
			return;
		}
		
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

	public void makeNextStep() {
		pilot.calculatePosition(leftWing.getHorizontalSpeed(), rightWing.getHorizontalSpeed());
	}

	
	

}
