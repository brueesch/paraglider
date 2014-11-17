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
	private boolean isOnRightSite = true;
	private static Pilot instance;
	private double fForward;
	private double fSideway = 0;
	private final Object fForwardLock = new Object();
	private final Object rollAngleLock = new Object();
	private final Object fSidewayLock = new Object();
	private double angularVelocity;
	private double rollAngle = 0;
	private boolean inFullStall = false;

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
		synchronized (fForwardLock) {
			fForward += (speed / Constants.TIME_INTERVALL) * weightOfPilot;
		}
	}

	public void setChangeInSpeedY(double speed) {
		if (speed > 0) {
			fSideway--;
		} else {
			fSideway++;
		}
		// synchronized (fSidewayLock) {
		// fSideway -= ((speed / Constants.TIME_INTERVALL) * weightOfPilot);
		// inChange = true;
		// }
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
		synchronized (fForwardLock) {
			fForward = 0;
		}
		synchronized (fSidewayLock) {
			fSideway = 0;
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

	private void calculateY(double speedLeftWing, double speedRightWing) {
		calculateForcesInTheYAxis(speedLeftWing, speedRightWing);
		calculateChangeInYAxis();
	}

	private void calculateChangeInYAxis() {
		double acceleration = (fSideway) / weightOfPilot;

		double changeY = (acceleration * Math.pow(Constants.TIME_INTERVALL, 2)) / 2;

		currentPosition.setY(currentPosition.getY() - changeY);

	}

	private void calculateForcesInTheYAxis(double speedLeftWing,
			double speedRightWing) {

		double fg = weightOfPilot * Constants.GRAVITATIONAL_FORCE;
		double centrifugalForce;
		double fBackwards;
		calculateRollAngle();
		if (speedLeftWing == speedRightWing) {
			centrifugalForce = 0;
		} else {
			centrifugalForce = getCentrifugalForce(speedLeftWing,
					speedRightWing);
			
		}
		fBackwards = fg * Math.sin(getRollAngle());
		if(fBackwards == Double.NaN) {
			fBackwards = 0;
		}
		
		if (Math.round(speedLeftWing - speedRightWing) == 0) {
			fSideway += calculateDamping(fSideway) * 4;
		}

		double fBackwardsRoundedAndPositive = Math.round(Math.sqrt(Math.pow(
				fBackwards, 2)));
		double centrifugalForceRoundedAndPositive = Math.round(Math.sqrt(Math
				.pow(centrifugalForce, 2)));
		if (fBackwardsRoundedAndPositive >= centrifugalForceRoundedAndPositive
				&& fBackwardsRoundedAndPositive <= centrifugalForceRoundedAndPositive + 5
				&& fBackwardsRoundedAndPositive > 5) {
			fSideway = 0;
		}
		synchronized (fSidewayLock) {
			if (isOnRightSite) {
				fSideway += fBackwards;
				fSideway += centrifugalForce;
			} else {
				fSideway -= fBackwards;
				fSideway += centrifugalForce;
			}
		}
	}

	private void calculateRollAngle() {
		Vector2D u = new Vector2D(ZERO_POSITION.getY() - ZERO_POINT.getY(),
				ZERO_POSITION.getZ() - ZERO_POINT.getZ());
		Vector2D v = new Vector2D(currentPosition.getY() - ZERO_POINT.getY(),
				currentPosition.getZ() - ZERO_POINT.getZ());

		double cosAngle = u.getAngleToVector2D(v, Unit.Radian);

		if (currentPosition.getY() < ZERO_POSITION.getY()) {
			isOnRightSite = false;
		} else {
			isOnRightSite = true;
		}

		setRollAngle(Math.acos(cosAngle));

	}

	/**
	 * Returns the current PitchAngle of the Glider. The Pitch Angle is the
	 * angle of the x -axis in relationship to the ground.
	 * 
	 * @return double in Radian
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

		return Math.acos(cosAngle);
	}

	/**
	 * Returns the current Roll Angle of the Glider. The Roll Angle is the angle
	 * of the y - axis in relationship to the ground.
	 * 
	 * @return double in Radian
	 */

	public double calculateAngleOfHighestPoint(double speedLeftWing,
			double speedRightWing) {

		if (speedLeftWing == speedRightWing) {
			angularVelocity = 0;
			return 0;
		}

		double fCen = getCentrifugalForce(speedLeftWing, speedRightWing);
		double fG = Constants.GRAVITATIONAL_FORCE * weightOfPilot;
		double resultAngle = Math.atan(fCen / fG);
		setRollAngle(resultAngle);
		return resultAngle;
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
		double fBackwards = fg * Math.sin(getPitchAngle());

		synchronized (fForwardLock) {
			if (isOnPositiveSite) {
				fForward += fBackwards;
			} else {
				fForward -= fBackwards;
			}
			if(inFullStall) {
				fForward += calculateDamping(fForward)*8;
			} else {
				fForward += calculateDamping(fForward);
			}
			
		}
	}

	private double calculateDamping(double force) {
		double acceleration = force / weightOfPilot;
		return -acceleration * Constants.DAMPING_FACTOR * weightOfPilot;
	}

	private void calculateZ() {
		double x = currentPosition.getX() - ZERO_POSITION.getX();
		double y = currentPosition.getY() - ZERO_POSITION.getY();
		double a = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		double z = Math.sqrt(Math.pow(Constants.LENGTH_OF_CORD, 2)
				- Math.pow(a, 2));
		currentPosition.setZ(z);
	}

	/*
	 * private void calculateY(double speedLeftWing, double speedRightWing) {
	 * 
	 * double newY = Constants.LENGTH_OF_CORD
	 * Math.sin(calculateRollAngle(speedLeftWing, speedRightWing));
	 * 
	 * currentPosition.setY(newY);
	 * 
	 * }
	 */

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

	public double getRollAngle() {
		synchronized (rollAngleLock) {
			return rollAngle;
		}
	}

	public void setRollAngle(double rollAngle) {
		synchronized (rollAngleLock) {
			this.rollAngle = rollAngle;
		}
	}

	public boolean isOnPositiveSite() {
		return isOnPositiveSite;
	}

	public boolean isOnRightSite() {
		return isOnRightSite;
	}

	public void setInFullStall(boolean inFullStall) {
		this.inFullStall = inFullStall;
	}
}