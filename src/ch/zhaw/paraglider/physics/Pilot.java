package ch.zhaw.paraglider.physics;

import java.util.ArrayList;

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
	 * Variable that defines the Weight of the Pilot.
	 */
	private int weightOfPilot = 85;
	/**
	 * Constant to convert pixel into meter.
	 */
	private final double ONE_METER_IN_PIXEL = 20;
	/**
	 * Constant which defines the Lenght of the paraglider cord.
	 */
	private final double LENGTH_OF_CORD = 7.68; // m
	/**
	 * Constant for the Gravitational Force.
	 */
	private final double GRAVITATIONAL_FORCE = 9.81; // m/s2
	/**
	 * Constant to konvert km/h into m/s.
	 */
	private final double CONVERT_KMH_INTO_MS = 3.6;
	/**
	 * Constant to konvert secounds into milisecounds.
	 */
	private final double CONVERT_S_AND_MS = 1000;
	/**
	 * Period time of the "pilot pendulum".
	 */
	private final double TIME_OF_PERIOD = (2 * Math.PI * Math
			.sqrt(LENGTH_OF_CORD / GRAVITATIONAL_FORCE));
	/**
	 * Constant for the time from the start point to the highest point.
	 */
	private final double TIME_FROM_0_TO_TOP = TIME_OF_PERIOD / 4;
	/**
	 * Start position of the pilot in the x-axis.
	 */
	private final double ZERO_X_POSITION = 310;
	/**
	 * Start position of the pilot in the y-axis.
	 */
	private final double ZERO_Y_POSITION = 394;
	/**
	 * The current position of the pilot x-axis.
	 */
	private double currentPositionX = ZERO_X_POSITION;
	/**
	 * The current position of the pilot y-axis.
	 */
	private double currentPositionY = ZERO_Y_POSITION;
	/**
	 * Boolean which defines in which direction the movement is.
	 */
	private boolean movesForward = true;
	/**
	 * Boolean which defines if there is a movement happening.
	 */
	private boolean inMovement = false;
	/**
	 * ArrayList filled with the change Values for the x-axis. The values are
	 * calculated as follows: speed * sin(current Position on the sinus curve) *
	 * time * Converted from pixel to meter
	 */
	ArrayList<Double> changeInX = new ArrayList<Double>();
	/**
	 * ArrayList filled with the change Values for the y-axis. The values are
	 * calculated as follows: vertical speed * sin(current Position on the sinus
	 * curve) * time * Converted from pixel to meter
	 */
	ArrayList<Double> changeInY = new ArrayList<Double>();
	/**
	 * Pilot instance for the Singleton pattern.
	 */
	private static Pilot instance;

	/**
	 * Variable with the current speed change.
	 */
	private double speed;

	/**
	 * Variable with the current vertical speed change.
	 */
	private double verticalSpeed;

	/**
	 * Private constructor - Singleton Pattern.
	 */
	private Pilot() {
		super();
	}

	/**
	 * Methode returns the instance of the Pilot. If there is no Pilot initiated
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
	 * Sets the new speed change.
	 * 
	 * @param speed
	 * @param verticalSpeed
	 */
	public void setNewSpeedChangeParameters(double speed, double verticalSpeed) {
		this.speed = speed;
		this.verticalSpeed = verticalSpeed;
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
	 * Returns a boolean which tells if the pilot is in movement.
	 * 
	 * @return boolean
	 */
	public boolean isInMovement() {
		return inMovement;
	}

	/**
	 * Sets the variable inMovment.
	 * 
	 * @param inMovement
	 */
	public void setInMovement(final boolean inMovement) {
		this.inMovement = inMovement;
	}

	/**
	 * Calculates the next step of the animation of the pilot and sets the
	 * current position of the x and y - axis to the calculated values.
	 */
	public void makeNextStep() {
		double nintyDegreesInRadian = 1.57079633;
		double numberOfStepsToTake = TIME_FROM_0_TO_TOP * CONVERT_S_AND_MS
				/ RunGame.REFRESHRATE;
		double sizeOfSinStep = nintyDegreesInRadian / numberOfStepsToTake;

		if (changeInX.size() == 0) {
			for (int i = 0; i < numberOfStepsToTake; i++) {
				changeInX.add(speed * CONVERT_KMH_INTO_MS
						* Math.sin(sizeOfSinStep * i)
						* (RunGame.REFRESHRATE / CONVERT_S_AND_MS)
						* ONE_METER_IN_PIXEL);
				changeInY.add(verticalSpeed * Math.sin(sizeOfSinStep * i)
						* (RunGame.REFRESHRATE / CONVERT_S_AND_MS)
						* ONE_METER_IN_PIXEL);
			}
		}

		if (movesForward) {
			moveForward();
		} else {
			moveBackward();

		}
	}

	/**
	 * Sets the currentPosition of the x and y - axis when the pilot is in a
	 * forward movement.
	 * 
	 * @param steps
	 * @param sinSteps
	 */
	private void moveForward() {
		currentPositionX -= changeInX.remove(changeInX.size() - 1);
		currentPositionY -= changeInY.remove(changeInY.size() - 1);

		if (changeInX.size() == 0) {
			movesForward = false;
		}
	}

	/**
	 * Sets the currentPosition of the x and y - axis when the pilot is in a
	 * backward movment.
	 * 
	 * @param steps
	 * @param sinSteps
	 */
	private void moveBackward() {
		currentPositionX += changeInX.remove(0);
		currentPositionY += changeInY.remove(0);

		if (changeInX.size() == 0) {
			movesForward = true;
			setInMovement(false);
		}
	}

}
