package ch.zhaw.paraglider.physics;

import ch.zhaw.paraglider.view.Main;

import com.sun.istack.internal.logging.Logger;

public class Pilot {
	
	private static Logger log = Logger.getLogger(Main.class.getName(),
			Main.class);
	
	private int WEIGHT_OF_PILOT = 85;
	private final double ONE_METER_IN_PIXEL = 10;
	private final double LENGTH_OF_LINE = 7.68; //m
	private final double GRAVITATIONAL_FORCE = 9.81; //m/s2
	private final double TIME_OF_PERIOD = (2*Math.PI * Math.sqrt(LENGTH_OF_LINE/GRAVITATIONAL_FORCE));
	private final double TIME_FROM_0_TO_TOP = TIME_OF_PERIOD/4;
	private double currentXPosition = 310;
	private double currentYPosition = 640;
	
	
	public double getCurrentXPosition () {
		return currentXPosition;
	}
	
	public double getCurrentYPosition() {
		return currentYPosition;
	}
	
	public void calculateNewPositionOfPilot(double speed) {
		speed = speed*3.6; //convert km/h into m/s
		double changeInY = Math.pow(speed, 2) / (2*GRAVITATIONAL_FORCE);
		double acceleration = speed / TIME_FROM_0_TO_TOP;
		double diagonalLength = (speed * TIME_FROM_0_TO_TOP) - ((acceleration*Math.pow(TIME_FROM_0_TO_TOP, 2))/2);
		
		
		double changeInX = Math.sqrt(Math.pow(diagonalLength, 2) - Math.pow(changeInY, 2));
		
		log.info("Change for X: " + changeInX + " Change for Y: " + changeInY+ " Speed: " + speed + " Diagonal Length: " + diagonalLength);
		
		currentXPosition += changeInX*ONE_METER_IN_PIXEL;
		currentYPosition -= changeInY*ONE_METER_IN_PIXEL;
	}
	
	public int getWEIGHT_OF_PILOT() {
		return WEIGHT_OF_PILOT;
	}

	public void setWEIGHT_OF_PILOT(int weight) {
		WEIGHT_OF_PILOT = weight;
	}
	
}
