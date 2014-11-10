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
	private double OPTIMAL_GLIDE_RATIO = 9;
	/**
	 * Constant for the speed when flying with the optimal glide ratio. Default
	 * = 9,72 m/s
	 */
	private double SPEED_OPTIMAL_GLIDING = 9.722;
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
	
	private boolean inFullStall = false;
	private double verticalSpeed;

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
	
	public void setHorizontalSpeed(double speed) {
		currentSpeed = speed;
	}

	/**
	 * This Method calculates and returns the current rate of descent. In m/s
	 * 
	 * @return double speed
	 */
	public double getVerticalSpeed() {

		if(!inFullStall) {
		return (1 / (2 * OPTIMAL_GLIDE_RATIO))
				* (Math.pow(getHorizontalSpeed() / SPEED_OPTIMAL_GLIDING, 2) + Math.pow(SPEED_OPTIMAL_GLIDING / getHorizontalSpeed(), 2))
				* getHorizontalSpeed();
		}
		else {
			return verticalSpeed;
		}
	}
	
	public void setVerticalSpeed(double speed) {
		inFullStall = true;
		verticalSpeed = speed;
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
	
	//Method just for tests with slider
	public void setSpeedOptimalGliding(double speedOptimalGliding) {
		SPEED_OPTIMAL_GLIDING = speedOptimalGliding;
		constant1 = 1 / (2 * Math.pow(SPEED_OPTIMAL_GLIDING, 2) * OPTIMAL_GLIDE_RATIO);
		constant2 = Math.pow(SPEED_OPTIMAL_GLIDING, 2)
				/ (2 * OPTIMAL_GLIDE_RATIO);
		currentSpeed = SPEED_OPTIMAL_GLIDING;
	}
	
	//Method jsut for tests with slider
	public void setOptmialGlideRatio (double glideRatio) {
		OPTIMAL_GLIDE_RATIO = glideRatio;
		constant1 = 1 / (2 * Math.pow(SPEED_OPTIMAL_GLIDING, 2) * OPTIMAL_GLIDE_RATIO);
		constant2 = Math.pow(SPEED_OPTIMAL_GLIDING, 2)
				/ (2 * OPTIMAL_GLIDE_RATIO);
	}

	public boolean isInFullStall() {
		return inFullStall;
	}

	public void setInFullStall(boolean inFullStall) {
		this.inFullStall = inFullStall;
	}
}
