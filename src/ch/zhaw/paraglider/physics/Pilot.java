package ch.zhaw.paraglider.physics;



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
	 * Start position of the pilot.
	 */
	private final Vector ZERO_POSITION = new Vector(0, 0, Constants.LENGTH_OF_CORD);

	/**
	 * Position of the Zero Point. In the middle of the paraglider, where the
	 * line starts.
	 */
	private final Vector ZERO_POINT = new Vector(0, 0, 0);

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

		fForward += (speed / Constants.TIME_INTERVALL) * weightOfPilot;
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
		calculateYAxis();
		calculateZAxis();
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
		
		double changeX = (acceleration * Math.pow(Constants.TIME_INTERVALL, 2)) / 2;
		currentPosition.setX(currentPosition.getX()	- Constants.convertMeterToPixel(changeX));
	}

	/**
	 * Calculates the different Forces in the X-Axis.
	 */
	private void calculateForcesInTheXAxis() {
		double fg = weightOfPilot * Constants.GRAVITATIONAL_FORCE;
		double fCord = fg * getCurrentCos();
		double fBackwards = Math.sqrt(Math.pow(fg, 2) - Math.pow(fCord, 2));

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
		return weightOfPilot / (Math.pow((Constants.TIME_OF_PERIOD / (2 * Math.PI)), 2));
	}

	/**
	 * Returns the current Angle, between the current positon of the pilot and
	 * his position in flight without speed change.
	 * 
	 * @return double cos of Angle
	 */
	private double getCurrentCos() {
		double[] u = { ZERO_POSITION.getX() - ZERO_POINT.getX(),
				ZERO_POSITION.getZ() - ZERO_POINT.getZ() };
		double[] v = { currentPosition.getX() - ZERO_POINT.getX(),
				currentPosition.getZ() - ZERO_POINT.getZ() };

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

	private void calculateYAxis() {
		Glider glider = Glider.getInstance();
		double angle = glider.getAngleOfTheGlider();
		double changeY = Math.sin(angle) * Constants.LENGTH_OF_CORD;
		currentPosition.setY(ZERO_POSITION.getY()+ Constants.convertMeterToPixel(changeY));
	}

	private void calculateZAxis() {
		double x = currentPosition.getX() - ZERO_POSITION.getX();
		double y = currentPosition.getY() - ZERO_POSITION.getY();
		double a = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
		double z = Math.sqrt(Math.pow(Constants.LENGTH_OF_CORD, 2)-Math.pow(a, 2));
		System.out.println(z);
		currentPosition.setZ(z);
		
		
		/*
		double x = Constants.convertPixelToMeter(currentPosition.getX() - ZERO_POSITION.getX());
		double z = Math.sqrt(Math.pow(Constants.LENGTH_OF_CORD, 2) - Math.pow(x, 2));
		currentPosition.setZ(Constants.convertMeterToPixel(z) + ZERO_POINT.getZ());*/
	}


}