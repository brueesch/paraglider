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
	private int weightOfPilot = 85;
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
	 * Start position of the pilot in the x-axis.
	 */
	private final double ZERO_X_POSITION = 310;
	/**
	 * Start position of the pilot in the y-axis.
	 */
	private final double ZERO_Y_POSITION = 394;
	/** 
	 * Start position of the pilot in the z-axis.
	 */
	private final double ZERO_Z_POSITION = 910;
	/**
	 * Position of the Zero Point. In the middle of the paraglider, where the
	 * line starts.
	 */
	private final double[] ZERO_POINT = { 310, 240 };
	/**
	 * The current position of the pilot x-axis.
	 */
	private double currentPositionX = ZERO_X_POSITION;
	/**
	 * The current position of the pilot y-axis.
	 */
	private double currentPositionY = ZERO_Y_POSITION;
	/**
	 * The current position of the pilot z-axis.
	 */
	private double currentPositionZ = ZERO_Z_POSITION;
	/**
	 * Boolean which defines in which direction the movement is.
	 */
	private boolean movesForward = true;
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
	 * Returns the current position of the pilot on the x-axis.
	 * 
	 * @return double
	 */
	public double getCurrentXPosition() {
		return currentPositionX;
	}

	/**
	 * Returns the current position of the pilot on the y-axis.
	 * 
	 * @return double
	 */
	public double getCurrentYPosition() {
		return currentPositionY;
	}
	/**
	 * Returns the current position of the pilot on the z-axis.
	 * 
	 * @return double
	 */
	public double getCurrentZPosition() {
		return currentPositionZ;
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
		currentPositionX = ZERO_X_POSITION;
		currentPositionY = ZERO_Y_POSITION;
		currentPositionZ = ZERO_Z_POSITION;
		fForward = 0;
	}

	/**
	 * Calculates the next step of the animation of the pilot and sets the
	 * current position of the x and y - axis to the calculated values.
	 */
	public void makeNextStep() {
		calculateForces();
		calculateXChange();
		calculateYChange();
		calculateZChange();
	}
	
	/**
	 * Calculates the different Forces.
	 */
	private void calculateForces() {
		double fg = weightOfPilot * GRAVITATIONAL_FORCE;
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
		return weightOfPilot / (Math.pow((TIME_OF_PERIOD / (2 * Math.PI)), 2));
	}

	/**
	 * Returns the current Angle, between the current positon of the pilot and
	 * his position in flight without speed change.
	 * 
	 * @return double with the angle in radian.
	 */
	private double getCurrentAngle() {
		double[] u = { ZERO_X_POSITION - ZERO_POINT[0],
				ZERO_Y_POSITION - ZERO_POINT[1] };
		double[] v = { currentPositionX - ZERO_POINT[0],
				currentPositionY - ZERO_POINT[1] };

		double upperFormula = ((u[0] * u[1]) + (v[0] * v[1]));
		double lowerFormula = (Math.sqrt(Math.pow(u[0], 2) + Math.pow(u[1], 2)))
				* (Math.sqrt(Math.pow(v[0], 2) + Math.pow(v[1], 2)));
		double cosAngle = upperFormula / lowerFormula;

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
	 * calculates the change in the y axis.
	 */
	private void calculateYChange() {
		double x = (currentPositionX - ZERO_X_POSITION)
				/ CONVERT_METER_AND_PIXEL;
		double y = Math.sqrt(Math.pow(LENGTH_OF_CORD, 2) - Math.pow(x, 2));
		currentPositionY = (y * CONVERT_METER_AND_PIXEL) + ZERO_POINT[1];
	}

	/**
	 * Calculates the change in the x-axis.
	 */
	private void calculateXChange() {
		double acceleration = (fForward) / weightOfPilot;
		double change = (acceleration * Math.pow(RunGame.REFRESHRATE
				/ CONVERT_S_AND_MS, 2)) / 2;
		currentPositionX -= change * CONVERT_METER_AND_PIXEL;
	}
	
	/**
	 * Calculates the change in the z-axis.
	 */
	private void calculateZChange() {
		Glider glider = Glider.getInstance();
		
		double angle = glider.getAngleOfTheGlider();
		
		double change = Math.sin(angle) * LENGTH_OF_CORD;
		
		currentPositionZ = ZERO_Z_POSITION+(change * CONVERT_METER_AND_PIXEL);
		
	}

}
