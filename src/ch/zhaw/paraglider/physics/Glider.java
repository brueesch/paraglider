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
	private double yawAngle = 0;
	private boolean inFullStall = false;
	
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
	
	/**
	 * Returns the current Position of the Pilot in a Vector (x,y,z).
	 * @return Vector
	 */
	public Vector getPilotPosition()
	{
		return pilot.getCurrentPosition();
	}

	/**
	 * Returns the current PitchAngle of the Glider.
	 * The Pitch Angle is the angle of the x -axis in relationship to the ground.
	 * @return double in Radian
	 */
	public double getPitchAngle() {
		return pilot.getPitchAngle();
	}

	/**
	 * Returns the current Roll Angle of the Glider.
	 * The Roll Angle is the angle of the y - axis in relationship to the ground.
	 * @return double in Radian
	 */
	public double getRollAngle() {
		return pilot.getRollAngle();
	}

	/**
	 * Returns the current Yaw Angle of the Glider.
	 * The Yawn Angle is the angle between the direction of the previous flying direction and the direction of the curve.
	 * @return double in Degree
	 */
	public double getYawAngle() {
		calculateYawAngle();
		return yawAngle;
	}
	
	/**
	 * Returns the current Speed in relationship to the ground.
	 * @return double in meter per secound.
	 */
	public double getHorizontalSpeed() {
		return (leftWing.getHorizontalSpeed() + rightWing.getHorizontalSpeed())/2;
	}
	
	/**
	 * Returns the current vertical Speed in relationship to the ground.
	 * The positive speed direction is to the ground.
	 * @return double in meter per second.
	 */
	public double getVerticalSpeed() {
		return (leftWing.getVerticalSpeed() + rightWing.getVerticalSpeed())/2;
	}
	
	/**
	 * Returns the current glide Ratio.
	 * @return double
	 */
	public double getCurrentGlideRatio() {
		return (leftWing.getCurrentGlideRatio() + rightWing.getCurrentGlideRatio())/2;
	}	
	
	/**
	 * Changes the speed of the Glider.
	 * @param msLeft double in meter per seconds
	 * @param msRight double in meter per seconds
	 */
	public void changeSpeed(double msLeft, double msRight )
	{
		leftWing.changeCurrentSpeed(msLeft);
		rightWing.changeCurrentSpeed(msRight);
		pilot.setChangeInSpeed((msLeft+msRight)/2);
	}
	
	public void reset()
	{
		yawAngle = 0;
		pilot.reset();
	}
	
	//TODO: Formel korrigieren, siehe getCurveRadius in Pilot
	private void calculateYawAngle()
	{		
		
		double pathLeft = leftWing.getHorizontalSpeed() * Constants.TIME_INTERVALL;
		double pathRight = rightWing.getHorizontalSpeed() * Constants.TIME_INTERVALL;
			
		if(pathLeft != pathRight) {
			double xa = pathLeft;
			double xb = pathRight;
			double rb = Constants.GLIDER_WINGSPAN;
			double ra = (xa*rb)/(xb-xa);
			double radius = ra + rb;
			yawAngle += Math.toDegrees(xb/radius);
		}
	}

	public void makeNextStep() {
		pilot.calculatePosition(leftWing.getHorizontalSpeed(), rightWing.getHorizontalSpeed());
	}
	
	public double getAngularVelocity() {
		return pilot.getAngularVelocity();
	}

	public boolean isOnPositiveSite() {
		return pilot.isOnPositiveSite();
	}
	
	//Method just for the sliders.
	public Wing[] getWings() {
		return new Wing[] {rightWing, leftWing};
	}
	
	//Method just for the slider tests.
	public void setDamping(double damping) {
		pilot.setDamping(damping);
	}

	public void fullStall() {
		if(!inFullStall) {
			rightWing.setHorizontalSpeed(-0.55);
			leftWing.setHorizontalSpeed(-0.55);
			rightWing.setVerticalSpeed(18);
			leftWing.setVerticalSpeed(18);
			inFullStall = true;
		}
		
	}
	
	public void setInFullStall(boolean state) {
		inFullStall = state;
		rightWing.setInFullStall(state);
		leftWing.setInFullStall(state);
		rightWing.setHorizontalSpeed(5.55);
		leftWing.setHorizontalSpeed(5.55);
	}
}
