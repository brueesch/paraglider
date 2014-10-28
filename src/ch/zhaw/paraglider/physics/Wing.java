package ch.zhaw.paraglider.physics;

/**
 * The wing class contains the physical basics for the wing movements. It
 * calculates the absolute speed, the vertical speed and the glide ratio. This
 * is calculated with the help of a polar.
 * 
 * @author Christian Brüesch
 * 
 */
public class Wing {

	/**
	 * Constant for the optimal glide ratio. Default = 9;
	 */
	private final double OPTIMAL_GLIDE_RATIO = 9;
	/**
	 * Constant for the speed when flying with the optimal glide ratio. Default
	 * = 35 km/h
	 */
	private final double SPEED_OPTIMAL_GLIDING = 9.72;
	/**
	 * Mathematical constant for calculations.
	 */
	private double constant1 = 1 / (2 * Math.pow(SPEED_OPTIMAL_GLIDING, 2) * OPTIMAL_GLIDE_RATIO);
	/**
	 * Mathematical constant for calculations.
	 */
	private double constant2 = Math.pow(SPEED_OPTIMAL_GLIDING, 2)
			/ (2 * OPTIMAL_GLIDE_RATIO);
	/**
	 * Variable for the current speed. Initialized with the speed by optimal
	 * gliding.
	 */
	private double currentSpeed = SPEED_OPTIMAL_GLIDING;

	/**
	 * Variable contains the Name of the wing. Wing name either: left or right
	 */
	private String name;

	/**
	 * Constructor. Sets the name of the Wing.
	 * 
	 * @param name
	 */
	public Wing(String name) {
		this.name = name;
	}

	/**
	 * This Method returns the current speed to the ground. In m/s
	 * 
	 * @return double speed
	 */
	public double getHorizontalSpeed() {
		return currentSpeed;
	}

	/**
	 * This Method calculates and returns the current rate of descent. In m/s
	 * 
	 * @return double speed
	 */
	public double getVerticalSpeed() {

		return (1 / (2 * OPTIMAL_GLIDE_RATIO))
				* (Math.pow(getHorizontalSpeed() / SPEED_OPTIMAL_GLIDING, 2) + Math.pow(SPEED_OPTIMAL_GLIDING / getHorizontalSpeed(), 2))
				* getHorizontalSpeed();
	}

	/**
	 * This method changes the speed. With a positive Indicator the speed
	 * increases. With a negative Indicator the opposite will happen.
	 * 
	 * @param change
	 */
	public void changeCurrentSpeed(double change) {
		this.currentSpeed += change;
	}

	/**
	 * Returns the current glide ratio.
	 * 
	 * @return double
	 */
	public double getCurrentGlideRatio() {
		return 1 / ((constant1 * Math.pow(currentSpeed, 2)) + (constant2 / Math.pow(currentSpeed, 2)));
	}

	/**
	 * returns the name of the glider.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}
}
