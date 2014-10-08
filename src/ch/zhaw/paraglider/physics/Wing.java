package ch.zhaw.paraglider.physics;


public class Wing {

	private final double OPTIMAL_GLIDE_RATIO = 9;
	private final double SPEED_BEST_GLIDING = 35; // km/h

	private double constant1 = 1 / (2 * Math.pow(SPEED_BEST_GLIDING, 2) * OPTIMAL_GLIDE_RATIO);
	private double constant2 = Math.pow(SPEED_BEST_GLIDING, 2)
			/ (2 * OPTIMAL_GLIDE_RATIO);
	private double currentSpeed = SPEED_BEST_GLIDING;

	private String name;

	public Wing(String name) {
		this.setName(name);

	}

	/**
	 * This Method calculates and returns the current speed to the ground. 
	 * In km/h.
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
				* (Math.pow(getHorizontalSpeed() / SPEED_BEST_GLIDING, 2) + Math
						.pow(SPEED_BEST_GLIDING / getHorizontalSpeed(), 2))
				* getHorizontalSpeed() / 3.6;
	}

	public void setCurrentSpeed(double currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	/**
	 * This method changes the speed. With a positive Indicator the speed
	 * increases which will reduce the angle. With a negative Indicator the
	 * opposite will happen.
	 * 
	 * @param change
	 */
	public void changeCurrentSpeed(double change) {
		this.currentSpeed += change;
	}

	public double getCurrentGlideRatio() {
		return 1 / ((constant1 * Math.pow(currentSpeed, 2)) + (constant2 / Math
				.pow(currentSpeed, 2)));
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
