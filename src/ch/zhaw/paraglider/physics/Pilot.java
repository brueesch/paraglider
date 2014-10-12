package ch.zhaw.paraglider.physics;

import java.util.ArrayList;

import ch.zhaw.paraglider.controller.RunGame;
import ch.zhaw.paraglider.view.Main;

import com.sun.istack.internal.logging.Logger;

/**
 * This Class handels the physics and the position of the pilot. In addition it
 * includes a method which animates the pilot.
 * 
 * @author Christian Brüesch
 * 
 */
public final class Pilot {

	private static Logger log = Logger.getLogger(Main.class.getName(),
			Main.class);
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
	 * End position of a movement. x-axis.
	 */
	private double endPositionX = ZERO_X_POSITION;
	/**
	 * End position of a movement. y-axis.
	 */
	private double endPositionY = ZERO_Y_POSITION;
	/**
	 * Boolean which defines in which direction the movement is.
	 */
	private boolean movesForward = true;
	/**
	 * Boolean which defines if there is a movement happening.
	 */
	private boolean inMovement = false;
	ArrayList<Double> changeInX = new ArrayList<Double>();
	ArrayList<Double> changeInY = new ArrayList<Double>();
	/**
	 * Pilot instance for the Singleton pattern.
	 */
	private static Pilot instance;

	private double speed;
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
	 * Calculates the new end position, where the pilot goes in the x and
	 * y-axis.
	 * 
	 * @param speed
	 * @param verticalSpeed
	 */
	public void calculateNewEndposition(double speed, double verticalSpeed) {
		this.speed = speed;
		this.verticalSpeed = verticalSpeed;
		boolean negativeSpeed = false;
		if (speed < 0) {
			speed = -speed;
			negativeSpeed = true;
		}

		speed = speed * CONVERT_KMH_INTO_MS;
		double changeInY = Math.pow(speed, 2) / (2 * GRAVITATIONAL_FORCE);
		double acceleration = speed / TIME_FROM_0_TO_TOP;
		double diagonalLength = (speed * TIME_FROM_0_TO_TOP)
				- ((acceleration * Math.pow(TIME_FROM_0_TO_TOP, 2)) / 2);
		double changeInX = getChangeInX(changeInY, diagonalLength);

		calculateNewXPosition(negativeSpeed, changeInX);
		endPositionY -= changeInY * ONE_METER_IN_PIXEL;

		log.info("Change for X: " + changeInX + " Change for Y: " + changeInY
				+ " Speed: " + speed + " Diagonal Length: " + diagonalLength);
	}

	/**
	 * Calculates the new Position on the x-axis.
	 * 
	 * @param negative
	 * @param changeInX
	 */
	private void calculateNewXPosition(final boolean negative,
			final double changeInX) {
		if (negative) {
			endPositionX += changeInX * ONE_METER_IN_PIXEL;
		} else {
			endPositionX -= changeInX * ONE_METER_IN_PIXEL;
		}
	}

	/**
	 * Returns the value of the change in the x-axis.
	 * 
	 * @param changeInY
	 * @param diagonalLength
	 * @return double
	 */
	private double getChangeInX(double changeInY, double diagonalLength) {
		double changeInX;
		if (changeInY < diagonalLength) {
			changeInX = Math.sqrt(Math.pow(diagonalLength, 2)
					- Math.pow(changeInY, 2));
		} else {
			changeInX = Math.sqrt(Math.pow(changeInY, 2)
					- Math.pow(diagonalLength, 2));
		}
		return changeInX;
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
	 * Calculates the next step of the animation of the pilot.
	 */
	public void makeNextStep() {
		// TODO: Erhöhe Animationsgenauigkeit

		double nintyDegreesInRadian = 1.57079633;
		double steps = TIME_FROM_0_TO_TOP * CONVERT_S_AND_MS
				/ RunGame.REFRESHRATE;
		double sinSteps = nintyDegreesInRadian / steps;
		
		if (changeInX.size() == 0) {
			for (int i = 0; i < steps; i++) {
				changeInX.add(speed * Math.sin(sinSteps * i)
						* (RunGame.REFRESHRATE / CONVERT_S_AND_MS)
						* ONE_METER_IN_PIXEL);
				changeInY.add(verticalSpeed * Math.sin(sinSteps * i)
						* (RunGame.REFRESHRATE / CONVERT_S_AND_MS)
						* ONE_METER_IN_PIXEL);
			}
		}

		if (movesForward) {
			moveForward(steps, sinSteps);
		} else {
			moveBackward(steps, sinSteps);

		}
	}

	private void moveForward(double steps, double sinSteps) {
		
		currentPositionX -= changeInX.remove(changeInX.size() - 1);
		currentPositionY -= changeInY.remove(changeInY.size() - 1);

		if (changeInX.size() == 0) {
			movesForward = false;
		}
	}

	private void moveBackward(double steps, double sinSteps) {
//		if (changeInX.size() == 0) {
//			for (int i = (int) (steps - 1); i >= 0; i--) {
//				changeInX.add(speed * Math.sin(sinSteps * i)
//						* (RunGame.REFRESHRATE / CONVERT_S_AND_MS)
//						* ONE_METER_IN_PIXEL);
//				changeInY.add(verticalSpeed * Math.sin(sinSteps * i)
//						* (RunGame.REFRESHRATE / CONVERT_S_AND_MS)
//						* ONE_METER_IN_PIXEL);
//			}
//		}
		currentPositionX += changeInX.remove(0);
		currentPositionY += changeInY.remove(0);

		if (changeInX.size() == 0) {
			movesForward = true;
			setInMovement(false);
		}

	}

}
