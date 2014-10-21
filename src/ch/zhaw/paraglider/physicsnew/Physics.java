package ch.zhaw.paraglider.physicsnew;

public class Physics {
	
	public static final double KMH_TO_MS = 3.6;
	public static final double MS_TO_KMH = 1.0/3.6;
	public static final double S_TO_MS = 1000;
	public static final double MS_TO_S = 1.0/1000;
	public static final double GRAVITATIONAL_FORCE = 9.81;
	
	/*
	 * Returns the pendulum period
	 * @param double cordlenght in m
	 * @return double pendulum Period
	 */
	public static double getPendulumPeriod(double cordLenght)
	{
		return (2 * Math.PI) / Physics.getPendulumAngularSpeed(cordLenght);
	}
	
	/*
	 * Returns the pendulum angular speed in Degree
	 * @param double cordlenght in m
	 * @return double pendulum angular speed in Degree
	 */
	public static double getPendulumAngularSpeed(double cordLenght)
	{
		return Math.sqrt(Physics.GRAVITATIONAL_FORCE/cordLenght);
	}
	
	
}
