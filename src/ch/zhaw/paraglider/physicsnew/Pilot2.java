package ch.zhaw.paraglider.physicsnew;

import ch.zhaw.paraglider.controller.RunGame;
import ch.zhaw.paraglider.physicsnew.Vector2.Unit;

/**
 * Just Testing
 * 
 * @author Jonas Gschwend
 * 
 */
public class Pilot2 {

	/**
	 * Variable that defines the Weight of the Pilot in kg.
	 */
	private int weightOfPilot = 85;

	/**
	 * Constant to convert pixel into meter.
	 */
	private final double CONVERT_METER_AND_PIXEL = 20;	

	/**
	 * Constant which defines the Lenght of the paraglider cord in m.
	 */
	private final double LENGTH_OF_CORD = 7.68;
	

	/**
	 * Start position of the pilot.
	 */
	private final Vector2 ZERO_POSITION = new Vector2(310, 394, 910);

	/**
	 * Position of the Zero Point. In the middle of the paraglider, where the
	 * line starts.
	 */
	private final Vector2 ZERO_POINT = new Vector2(310, 240, 0);

	/**
	 * The current position of the pilot.
	 */
	private Vector2 currentPosition = new Vector2(ZERO_POSITION.getX(),ZERO_POSITION.getY(), ZERO_POSITION.getZ());

	/**
	 * Boolean which defines in which direction the movement is.
	 */
	private boolean movesForward = true;	


	/**
	 * Constant with the current Forward Force.
	 */
	double fForward;

	/**
	 * Pilot instance for the Singleton pattern.
	 */
	private static Pilot2 instance;

	/**
	 * Private constructor - Singleton Pattern.
	 */
	private Pilot2() {
		super();
	}

	/**
	 * Method returns the instance of the Pilot. If there is no Pilot initiated
	 * yet, it will be done.
	 * 
	 * @return Pilot
	 */
	public static Pilot2 getInstance() {
		if (instance == null) {
			instance = new Pilot2();
		}

		return instance;
	}

	/**
	 * Returns the current position of the pilot.
	 * 
	 * @return Vector
	 */
	public Vector2 getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * Sets the new speed change.
	 * 
	 * @param speed
	 */
	public void setChangeInSpeed(double speed) {

		fForward += (speed * Physics.KMH_TO_MS / (RunGame.REFRESHRATE *Physics.MS_TO_S)) * weightOfPilot;
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

	public void reset() {
		currentPosition = new Vector2(ZERO_POSITION.getX(),
				ZERO_POSITION.getY(), ZERO_POSITION.getZ());
		fForward = 0;
	}

	/**
	 * Calculates the next step of the animation of the pilot and sets the
	 * current position of the x and y - axis to the calculated values.
	 */
	public void makeNextStep() {
		calculateForces();
		calculateChanges();
	}

	/**
	 * Calculates the different Forces.
	 */
	private void calculateForces() {
		double fg = weightOfPilot * Physics.GRAVITATIONAL_FORCE;
		double fCord = fg * Math.cos(getCurrentAngle());
		double fBackwards = Math.sqrt(Math.pow(fg, 2) - Math.pow(fCord, 2));

		if (movesForward) {
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

	/**
	 * Returns the damping of the pendel.
	 * 
	 * @return double in Newton
	 */
	private double getDamping() {
		// TODO Formel korrigieren, nicht ganz richtig.
		return weightOfPilot / (Math.pow((Physics.getPendulumPeriod(LENGTH_OF_CORD) / (2 * Math.PI)), 2));
	}

	/**
	 * Returns the current Angle, between the current positon of the pilot and
	 * his position in flight without speed change.
	 * 
	 * @return double with the angle in radian.
	 */
	private double getCurrentAngle() {
		Vector2 u = ZERO_POSITION.sub(ZERO_POINT);
		Vector2 v = currentPosition.sub(ZERO_POINT);

		double cosAngle = u.getAngleToVector(v, Unit.Radian);

		if (cosAngle < 0) {
			cosAngle = 1 + cosAngle;
			movesForward = false;
		} else {
			cosAngle = 1 - cosAngle;
			movesForward = true;
		}
		return Math.acos(cosAngle);
	}

	/**
	 * calculates the change of all axis
	 */
	private void calculateChanges() {
		/**
		 * Calculates the change in the x-axis.
		 */
		double acceleration = (fForward) / weightOfPilot;
		double changeX = (acceleration * Math.pow(RunGame.REFRESHRATE * Physics.MS_TO_S, 2)) / 2;
		currentPosition.setX(currentPosition.getX()	- (changeX * CONVERT_METER_AND_PIXEL));

		/**
		 * calculates the change in the y axis.
		 */
		double x = (currentPosition.getX() - ZERO_POSITION.getX())
				/ CONVERT_METER_AND_PIXEL;
		double y = Math.sqrt(Math.pow(LENGTH_OF_CORD, 2) - Math.pow(x, 2));
		currentPosition.setY((y * CONVERT_METER_AND_PIXEL) + ZERO_POINT.getY());

		/**
		 * Calculates the change in the z-axis.
		 */
		Glider2 glider = Glider2.getInstance();
		double angle = glider.getAngleOfTheGlider();
		double changeZ = Math.sin(angle) * LENGTH_OF_CORD;
		currentPosition.setZ(ZERO_POSITION.getZ()
				+ (changeZ * CONVERT_METER_AND_PIXEL));
	}
}
