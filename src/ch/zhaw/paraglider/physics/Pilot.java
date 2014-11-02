package ch.zhaw.paraglider.physics;

import ch.zhaw.paraglider.physics.Vector2D.Unit;

/**
 * This Class handles the physics and the position of the pilot. In addition it
 * includes a method which animates the pilot.
 * 
 * @author Christian Brüesch
 * 
 */
public final class Pilot {

	private int weightOfPilot = 85;
	private final Vector ZERO_POSITION = new Vector(0, 0,
			Constants.LENGTH_OF_CORD);
	private final Vector ZERO_POINT = new Vector(0, 0, 0);
	private Vector currentPosition = new Vector(ZERO_POSITION);
	private boolean isOnPositiveSite = true;
	private static Pilot instance;
	private double fForward;
	private final Object lock = new Object();
	private double angularVelocity;

	private Pilot() {
		super();
	}

	/**
	 * Method returns the instance of the Pilot. If there is no Pilot initiated
	 * yet, it will be done.
	 * 
	 * @return Pilot
	 */
	public static Pilot getInstance() {
		if (instance == null) {
			instance = new Pilot();
		}

		return instance;
	}

	/**
	 * Returns the current position of the pilot.
	 * 
	 * @return Vector
	 */
	public Vector getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * Sets the new speed change.
	 * 
	 * @param speed
	 */
	public void setChangeInSpeed(double speed) {

		synchronized (lock) {
			fForward += (speed / Constants.TIME_INTERVALL) * weightOfPilot;
		}
	}

	/**
	 * Returns the weight of the pilot.
	 * 
	 * @return int
	 */
	public int getWeightOfPilot() {
		return weightOfPilot;
	}

	/**
	 * Sets the new weight of the pilot.
	 * 
	 * @param weight
	 */
	public void setWeightOfPilot(int weight) {
		weightOfPilot = weight;
	}

	/**
	 * Resets all position and forces to the start position.
	 */
	public void reset() {
		currentPosition = new Vector(ZERO_POSITION);
		synchronized (lock) {
			fForward = 0;
		}
	}

	/**
	 * Calculates the next step of the animation of the pilot and sets the
	 * current position of the x and y - axis to the calculated values.
	 */
	public void calculatePosition(double speedLeftWing, double speedRightWing) {
		calculateX();
		calculateY(speedLeftWing, speedRightWing);
		calculateZ();
	}

	/**
	 * Returns the current PitchAngle of the Glider. The Pitch Angle is the
	 * angle of the x -axis in relationship to the ground.
	 * 
	 * @return double in Degree
	 */
	public synchronized double getPitchAngle() {
		Vector2D u = new Vector2D(ZERO_POSITION.getX() - ZERO_POINT.getX(),
				ZERO_POSITION.getZ() - ZERO_POINT.getZ());
		Vector2D v = new Vector2D(currentPosition.getX() - ZERO_POINT.getX(),
				currentPosition.getZ() - ZERO_POINT.getZ());

		double cosAngle = u.getAngleToVector2D(v, Unit.Radian);

		if (currentPosition.getX() < ZERO_POSITION.getX()) {
			isOnPositiveSite = false;
		} else {
			isOnPositiveSite = true;
		}

		return Math.toDegrees(Math.acos(cosAngle));
	}

	/**
	 * Returns the current Roll Angle of the Glider. The Roll Angle is the angle
	 * of the y - axis in relationship to the ground.
	 * 
	 * @return double in Degree
	 */
	public synchronized double getRollAngle(double speedLeftWing,
			double speedRightWing) {

		if (speedLeftWing == speedRightWing) {
			angularVelocity = 0;
			return 0;
		}

		double fCen = getCentrifugalForce(speedLeftWing, speedRightWing);
		double fG = Constants.GRAVITATIONAL_FORCE * weightOfPilot;

		return Math.atan(fCen / fG);
	}

	private double getCentrifugalForce(double speedLeftWing,
			double speedRightWing) {

		double pilotPath = (speedLeftWing + speedRightWing) / 2
				* Constants.TIME_INTERVALL;
		double radius = getCurveRadius(
				speedLeftWing * Constants.TIME_INTERVALL, speedRightWing
						* Constants.TIME_INTERVALL)
				- (Constants.GLIDER_WINGSPAN / 2);
		double alpha = (pilotPath * 360) / (2 * radius * Math.PI);
		alpha = Math.toRadians(alpha);
		angularVelocity = alpha / Constants.TIME_INTERVALL;

		return weightOfPilot * Math.pow(angularVelocity, 2) * radius;
	}

	private void calculateX() {
		calculateForcesInTheXAxis();
		calculateChangeInXAxis();
	}

	private void calculateChangeInXAxis() {
		double acceleration = (fForward) / weightOfPilot;

		double changeX = (acceleration * Math.pow(Constants.TIME_INTERVALL, 2)) / 2;

		currentPosition.setX(currentPosition.getX() - changeX);

	}

	private void calculateForcesInTheXAxis() {
		double fg = weightOfPilot * Constants.GRAVITATIONAL_FORCE;
		double fBackwards = fg * Math.sin(Math.toRadians(getPitchAngle()));

		synchronized (lock) {
			if (isOnPositiveSite) {
				fForward += fBackwards;
			} else {
				fForward -= fBackwards;
			}

			if (fForward > 0) {
				fForward -= getDamping();
			} else {
				fForward += getDamping();
			}
		}
	}

	private double getDamping() {
		// TODO Formel korrigieren, nicht ganz richtig.
		return weightOfPilot
				/ (Math.pow((Constants.TIME_OF_PERIOD / (2 * Math.PI)), 2))
				/ 10;
	}

	private void calculateZ() {
		double x = currentPosition.getX() - ZERO_POSITION.getX();
		double y = currentPosition.getY() - ZERO_POSITION.getY();
		double a = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		double z = Math.sqrt(Math.pow(Constants.LENGTH_OF_CORD, 2)
				- Math.pow(a, 2));
		currentPosition.setZ(z);
	}

	private void calculateY(double speedLeftWing, double speedRightWing) {

		double newY = Constants.LENGTH_OF_CORD
				* Math.sin(getRollAngle(speedLeftWing, speedRightWing));

		currentPosition.setY(newY);

	}

	private double getCurveRadius(double pathLeft, double pathRight) {
		double xa = pathRight;
		double xb = pathLeft;
		double rb = Constants.GLIDER_WINGSPAN;
		double ra = (rb * xb) / (xa - xb);
		return (ra + rb);
	}

	public double getAngularVelocity() {
		return angularVelocity;
	}

}