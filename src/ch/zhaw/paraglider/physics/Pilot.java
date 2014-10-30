package ch.zhaw.paraglider.physics;

import ch.zhaw.paraglider.physics.Vector2D.Unit;

/**
 * This Class handles the physics and the position of the pilot. In addition it
 * includes a method which animates the pilot.
 * 
 * @author Christian Br�esch
 * 
 */
public final class Pilot {

	/**
	 * Variable that defines the Weight of the Pilot in kg.
	 */
	private int weightOfPilot = 85;// 85;

	/**
	 * Start position of the pilot.
	 */
	private final Vector ZERO_POSITION = new Vector(0, 0,
			Constants.LENGTH_OF_CORD);

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
	public void makeNextStep(double speedLeftWing, double speedRightWing) {
		calculateXAxis();
		calculateYAxis(speedLeftWing, speedRightWing);
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

		currentPosition.setX(currentPosition.getX() - changeX);
	}

	/**
	 * Calculates the different Forces in the X-Axis.
	 */
	private void calculateForcesInTheXAxis() {
		double fg = weightOfPilot * Constants.GRAVITATIONAL_FORCE;
		double fCord = fg * getPitchAngle();
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
		return weightOfPilot
				/ (Math.pow((Constants.TIME_OF_PERIOD / (2 * Math.PI)), 2));
	}

	public double getPitchAngle() {
		Vector2D u = new Vector2D(ZERO_POSITION.getX() - ZERO_POINT.getX(),
				ZERO_POSITION.getZ() - ZERO_POINT.getZ());
		Vector2D v = new Vector2D(currentPosition.getX() - ZERO_POINT.getX(),
				currentPosition.getZ() - ZERO_POINT.getZ());

		double cosAngle = u.getAngleToVector2D(v, Unit.Radian);

		if (currentPosition.getX() < ZERO_POSITION.getX()) {
			isOnPositiveSite = false;
		} else {
			isOnPositiveSite = true;
		}
		return cosAngle;
	}

	private void calculateZAxis() {
		double x = currentPosition.getX() - ZERO_POSITION.getX();
		double y = currentPosition.getY() - ZERO_POSITION.getY();
		double a = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		double z = Math.sqrt(Math.pow(Constants.LENGTH_OF_CORD, 2)
				- Math.pow(a, 2));
		currentPosition.setZ(z);

		/*
		 * double x = Constants.convertPixelToMeter(currentPosition.getX() -
		 * ZERO_POSITION.getX()); double z =
		 * Math.sqrt(Math.pow(Constants.LENGTH_OF_CORD, 2) - Math.pow(x, 2));
		 * currentPosition.setZ(Constants.convertMeterToPixel(z) +
		 * ZERO_POINT.getZ());
		 */
	}

	private void calculateYAxis(double speedLeftWing, double speedRightWing) {
		//calculateForcesInTheYAxis(speedLeftWing, speedRightWing);
		//calculateChangeInYAxis();
		getRollAngle(speedLeftWing, speedRightWing);
	}

	public double getRollAngle(double speedLeftWing, double speedRightWing) {
		
		if(speedLeftWing == speedRightWing)
		{
			return 0;
		}		
		
		double pathLeft = speedLeftWing * Constants.TIME_INTERVALL;
		double pathRight = speedRightWing * Constants.TIME_INTERVALL;
		double pilotPath = (speedLeftWing + speedRightWing) /2 * Constants.TIME_INTERVALL;
				
		double radius = 0;
		double w = 0;
		
		/* Left Curve */
		if(pathLeft < pathRight)
		{
			radius = getRadius(pathLeft,pathRight) - (Constants.GLIDER_WINGSPAN/2);
			
		}
		/* Right Curve */
		else
		{
			radius = getRadius(pathRight,pathLeft) - (Constants.GLIDER_WINGSPAN/2);
		}
		w = Math.toRadians(pilotPath/radius);
		
		double fZen = weightOfPilot * w * w * radius;
		double fG = Constants.GRAVITATIONAL_FORCE * weightOfPilot;
		double fCord = Math.sqrt(Math.pow(fZen, 2) + Math.pow(fG, 2));	
		
		System.out.println(Math.toDegrees(Math.asin(fZen/fCord)));
		
		return Math.toDegrees(Math.asin(fZen/fCord));
	}
	
	private double getAngle(double speedLeftWing, double speedRightWing)
	{
		if(speedLeftWing == speedRightWing)
		{
			return 0;
		}
		
		double pathLeft = speedLeftWing * Constants.TIME_INTERVALL;
		double pathRight = speedRightWing * Constants.TIME_INTERVALL;
		double pilotPath = (speedLeftWing + speedRightWing) /2 * Constants.TIME_INTERVALL;
		double w = 0;
		
		/* Left Curve */
		if(pathLeft < pathRight)
		{
			double r = getRadius(pathLeft,pathRight);
			return Math.toRadians(pathLeft/r);
			
		}
		/* Right Curve */
		else
		{
			double r = getRadius(pathRight,pathLeft);
			return Math.toRadians(pathRight/r);
		}
	}
	
	private double getRadius(double pathLeft,double pathRight)
	{
		double xa = pathRight;
		double xb = pathLeft;
		double rb = Constants.GLIDER_WINGSPAN;
		double ra = (xa*rb)/(xb-xa);
		return (ra + rb);
	}
	

	private void calculateChangeInYAxis() {
		double acceleration = (fForward) / weightOfPilot;

		double changeX = (acceleration * Math.pow(Constants.TIME_INTERVALL, 2)) / 2;

		currentPosition.setX(currentPosition.getX() - changeX);
	}

	/**
	 * Calculates the different Forces in the X-Axis.
	 * 
	 * @param speedRightWing
	 * @param speedLeftWing
	 */
	private void calculateForcesInTheYAxis(double speedLeftWing,
			double speedRightWing) {
		double fg = weightOfPilot * Constants.GRAVITATIONAL_FORCE;
		double acceleration = (speedLeftWing - speedRightWing)/Constants.TIME_INTERVALL;
		double fZentripetal = acceleration * weightOfPilot;
		double fCord = Math.sqrt(Math.pow(fg, 2) + Math.pow(fZentripetal, 2));
		//System.out.println("fg: " +fg + "acceleration: " + acceleration +" fZentripetal: " + fZentripetal + "fCord: "+ fCord);
		double cos = fg / fCord;
		//System.out.println(cos);

		// if (isOnPositiveSite) {
		// fForward += fBackwards;
		// } else {
		// fForward -= fBackwards;
		// }
		//
		// if (fForward > 0) {
		// fForward -= getDamping();
		// } else {
		// fForward += getDamping();
		// }	
		
		
	}

}