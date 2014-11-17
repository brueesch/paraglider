package ch.zhaw.paraglider.physics;

public class Constants {

	/**
	 * Constant which defines the wingspan of the Glider in meter.
	 */
	public static final double GLIDER_WINGSPAN = 12;

	/**
	 * Constant for the Gravitational Force in m per second squared.
	 */
	public static final double GRAVITATIONAL_FORCE = 9.81;

	/**
	 * Constant which defines the Length of the paraglider cord in meter.
	 */
	public static final double LENGTH_OF_CORD = 7.68;
	
	/**
	 * Time interval in Seconds,
	 */
	public static final double TIME_INTERVALL = 0.01;

	/**
	 * Constant which defines the factor of the damping in relation to the
	 * acceleration.
	 */
	public static final double DAMPING_FACTOR = 0.1 * TIME_INTERVALL;

	public static double convertMeterToPixel(double meter) {
		return meter * 20;
	}

	public static double convertPixelToMeter(double pixel) {
		return pixel / 20;
	}

	public static double convertKmhToMs(double kmh) {
		return kmh / 3.6;
	}

	public static double convertMsToKmh(double ms) {
		return ms * 3.6;
	}

	public static double convertSecToMs(double sec) {
		return sec * 1000;
	}

	public static double convertMsToSec(double ms) {
		return ms / 1000;
	}
}
