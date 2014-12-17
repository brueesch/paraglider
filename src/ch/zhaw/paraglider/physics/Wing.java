package ch.zhaw.paraglider.physics;

/**
 * The wing class contains the physical basics for the wing movements. It
 * calculates the absolute speed, the vertical speed and the glide ratio. This
 * is calculated with the help of a polar curve.
 * 
 * @author Christian Brüesch / Jonas Gschwend
 * 
 */
public class Wing {

	private double OPTIMAL_GLIDE_RATIO = 9;
	private double SPEED_OPTIMAL_GLIDING = 9.722;
	private double constant1 = 1 / (2 * Math.pow(SPEED_OPTIMAL_GLIDING, 2) * OPTIMAL_GLIDE_RATIO);
	private double constant2 = Math.pow(SPEED_OPTIMAL_GLIDING, 2)
			/ (2 * OPTIMAL_GLIDE_RATIO);
	private double currentSpeed = SPEED_OPTIMAL_GLIDING;
	private String name;

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
				* (Math.pow(getHorizontalSpeed() / SPEED_OPTIMAL_GLIDING, 2) + Math
						.pow(SPEED_OPTIMAL_GLIDING / getHorizontalSpeed(), 2))
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
	 * This Method calculates and returns the current glide ratio. 
	 * 
	 * @return double glideRatio
	 */
	public double getCurrentGlideRatio() {
		return 1 / ((constant1 * Math.pow(currentSpeed, 2)) + (constant2 / Math
				.pow(currentSpeed, 2)));
	}

	public String getName() {
		return name;
	}
}
