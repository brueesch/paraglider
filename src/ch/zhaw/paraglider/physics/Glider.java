package ch.zhaw.paraglider.physics;

/**
 * This Class represents the Glider, which contains two Wings and a Pilot.
 * 
 * @author Christian Br�esch / Jonas Gschwend
 * 
 */
public class Glider {

	private Wing leftWing = new Wing("Left");
	private Wing rightWing = new Wing("Right");
	private Pilot pilot = Pilot.getInstance();

	private static Glider instance;

	private Glider() {
	}

	/**
	 * Returns the Instance of the Glider.
	 * 
	 * @return Glider
	 */
	public static Glider getInstance() {
		if (instance == null) {
			instance = new Glider();
		}

		return instance;
	}

	/**
	 * Returns the current PitchAngle of the Glider. The Pitch Angle is the
	 * angle of the x -axis in relationship to the ground.
	 * 
	 * @return double in Radian
	 */
	public double getPitchAngle() {
		return pilot.getPitchAngle();
	}

	/**
	 * Returns the current Roll Angle of the Glider. The Roll Angle is the angle
	 * of the y - axis in relationship to the ground.
	 * 
	 * @return double in Radian
	 */
	public double getRollAngle() {
		return pilot.getRollAngle();
	}

	/**
	 * Returns the current Speed in relationship to the ground.
	 * 
	 * @return double in meter per secound.
	 */
	public double getHorizontalSpeed() {
		return (leftWing.getHorizontalSpeed() + rightWing.getHorizontalSpeed()) / 2;
	}

	/**
	 * Returns the current vertical Speed in relationship to the ground. The
	 * positive speed direction is to the ground.
	 * 
	 * @return double in meter per second.
	 */
	public double getVerticalSpeed() {
		return (leftWing.getVerticalSpeed() + rightWing.getVerticalSpeed()) / 2;
	}

	/**
	 * Returns the current glide Ratio.
	 * 
	 * @return double
	 */
	public double getCurrentGlideRatio() {
		return (leftWing.getCurrentGlideRatio() + rightWing
				.getCurrentGlideRatio()) / 2;
	}

	/**
	 * Changes the speed of the Glider.
	 * 
	 * @param msLeft
	 *            double in meter per seconds
	 * @param msRight
	 *            double in meter per seconds
	 */
	public void changeSpeed(double msLeft, double msRight) {
		leftWing.changeCurrentSpeed(msLeft);
		rightWing.changeCurrentSpeed(msRight);
		pilot.setChangeInSpeed((msLeft + msRight) / 2);
		pilot.setChangeInSidewaySpeed(msLeft - msRight);
	}

	/**
	 * Resets all variables to the same values as it was at the start.
	 */
	public void reset() {
		pilot.reset();
	}

	/**
	 * Calculates the next Step in this Interval.
	 */
	public void makeNextStep() {
		pilot.calculatePosition(leftWing.getHorizontalSpeed(),
				rightWing.getHorizontalSpeed());
	}

	public double getAngularVelocity() {
		return pilot.getAngularVelocity();
	}

	public boolean isOnPositiveSite() {
		return pilot.isOnPositiveSite();
	}

	public boolean isOnRightSite() {
		return pilot.isOnRightSite();
	}

	public void setInFullStall(boolean inFullStall) {
		pilot.setInFullStall(inFullStall);
	}
}
