package ch.zhaw.paraglider.physics;

import ch.zhaw.paraglider.controller.RunGame;

/**
 * This Class handles the physics and the position of the pilot. In addition it
 * includes a method which animates the pilot.
 * 
 * @author Christian Brüesch
 * 
 */
public final class Pilot {

	/**
	 * Variable that defines the Weight of the Pilot in kg.
	 */
	private int weightOfPilot = 120;//85;

	/**
	 * Constant to convert pixel into meter.
	 */
	private final double CONVERT_METER_AND_PIXEL = 20;

	/**
	 * Constant to konvert km/h into m/s.
	 */
	private final double CONVERT_KMH_AND_MS = 3.6;

	/**
	 * Constant to konvert secounds into milisecounds.
	 */
	private final double CONVERT_S_AND_MS = 1000;

	/**
	 * Constant which defines the Lenght of the paraglider cord in m.
	 */
	private final double LENGTH_OF_CORD = 7.68;

	/**
	 * Constant for the Gravitational Force in m per second squared.
	 */
	private final double GRAVITATIONAL_FORCE = 9.81;

	/**
	 * Period time of the "pilot pendulum".
	 */
	private final double TIME_OF_PERIOD = (2 * Math.PI * Math
			.sqrt(LENGTH_OF_CORD / GRAVITATIONAL_FORCE));

	/**
	 * Start position of the pilot.
	 */
	private final Vector ZERO_POSITION = new Vector(310, 394, 910);

	/**
	 * Position of the Zero Point. In the middle of the paraglider, where the
	 * line starts.
	 */
	private final Vector ZERO_POINT = new Vector(310, 240, 0);

	/**
	 * The current position of the pilot.
	 */
	private Vector currentPosition = new Vector(ZERO_POSITION.getX(),
			ZERO_POSITION.getY(), ZERO_POSITION.getZ());

	/**
	 * Boolean which defines in which direction the movement is.
	 */
	private boolean isOnPositiveSite = true;

	/**
	 * Pilot instance for the Singleton pattern.
	 */
	private static Pilot instance;

	/**
	 * Constant with the current Forward Force.
	 */
	double fForward;

	/**
	 * Private constructor - Singleton Pattern.
	 */
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

		fForward += (speed * CONVERT_KMH_AND_MS / (RunGame.REFRESHRATE / CONVERT_S_AND_MS))
				* weightOfPilot;
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
		currentPosition = new Vector(ZERO_POSITION.getX(),
				ZERO_POSITION.getY(), ZERO_POSITION.getZ());
		fForward = 0;
	}

	/**
	 * Calculates the next step of the animation of the pilot and sets the
	 * current position of the x and y - axis to the calculated values.
	 */
	public void makeNextStep() {
		calculateXAxis();
		calculateZAxis();
		calculateYAxis();
	}
	
	/**
	 * Calculates the X Axis.
	 */
	private void calculateXAxis() {
		calculateForcesInTheXAxis();
		calculateChangeInXAxis();
	}

	private void calculateChangeInXAxis() {
		double acceleration = (fForward) / weightOfPilot;
		double changeX = (acceleration * Math.pow(RunGame.REFRESHRATE
				/ CONVERT_S_AND_MS, 2)) / 2;
		currentPosition.setX(currentPosition.getX()
				- (changeX * CONVERT_METER_AND_PIXEL));
	}

	/**
	 * Calculates the different Forces in the X-Axis.
	 */
	private void calculateForcesInTheXAxis() {
		double fg = weightOfPilot * GRAVITATIONAL_FORCE;
		double fCord = fg * getCurrentCos();
		double fBackwards = Math.sqrt(Math.pow(fg, 2) - Math.pow(fCord, 2));

		System.out.println("Forward Force: " + fForward + " Backward Force"
				+ fBackwards + " Fg: " + fg);

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

	/**
	 * Returns the damping of the pendel.
	 * 
	 * @return double in Newton
	 */
	private double getDamping() {
		// TODO Formel korrigieren, nicht ganz richtig.
		return weightOfPilot / (Math.pow((TIME_OF_PERIOD / (2 * Math.PI)), 2));
	}

	/**
	 * Returns the current Angle, between the current positon of the pilot and
	 * his position in flight without speed change.
	 * 
	 * @return double cos of Angle
	 */
	private double getCurrentCos() {
		double[] u = { ZERO_POSITION.getX() - ZERO_POINT.getX(),
				ZERO_POSITION.getY() - ZERO_POINT.getY() };
		double[] v = { currentPosition.getX() - ZERO_POINT.getX(),
				currentPosition.getY() - ZERO_POINT.getY() };

		double upperFormula = ((u[0] * v[0]) + (u[1] * v[1]));
		double lowerFormula = (Math.sqrt(Math.pow(u[0], 2) + Math.pow(u[1], 2)))
				* (Math.sqrt(Math.pow(v[0], 2) + Math.pow(v[1], 2)));
		double cosAngle = upperFormula / lowerFormula;

		if (currentPosition.getX() < ZERO_POSITION.getX()) {
			isOnPositiveSite = false;
		} else {
			isOnPositiveSite = true;
		}
		return cosAngle;
	}

	private void calculateZAxis() {
		Glider glider = Glider.getInstance();
		double angle = glider.getAngleOfTheGlider();
		double changeZ = Math.sin(angle) * LENGTH_OF_CORD;
		currentPosition.setZ(ZERO_POSITION.getZ()
				+ (changeZ * CONVERT_METER_AND_PIXEL));
	}

	private void calculateYAxis() {
		double x = (currentPosition.getX() - ZERO_POSITION.getX())
				/ CONVERT_METER_AND_PIXEL;
		double y = Math.sqrt(Math.pow(LENGTH_OF_CORD, 2) - Math.pow(x, 2));
		currentPosition.setY((y * CONVERT_METER_AND_PIXEL) + ZERO_POINT.getY());
	}


}
