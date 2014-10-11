package ch.zhaw.paraglider.physics;

import ch.zhaw.paraglider.controller.RunGame;
import ch.zhaw.paraglider.view.Main;

import com.sun.istack.internal.logging.Logger;

//TODO: Code säubern
public class Pilot {
	
	private static Logger log = Logger.getLogger(Main.class.getName(),
			Main.class);
	
	private int WEIGHT_OF_PILOT = 85;
	private final double ONE_METER_IN_PIXEL = 10;
	private final double LENGTH_OF_LINE = 7.68; //m
	private final double GRAVITATIONAL_FORCE = 9.81; //m/s2
	private final double TIME_OF_PERIOD = (2*Math.PI * Math.sqrt(LENGTH_OF_LINE/GRAVITATIONAL_FORCE));
	private final double TIME_FROM_0_TO_TOP = TIME_OF_PERIOD/4;
	private final double ZERO_X_POSITION = 310;
	private final double ZERO_Y_POSITION = 640;
	private double currentPositionX = ZERO_X_POSITION;
	private double currentPositionY = ZERO_Y_POSITION;
	private double endPositionX = ZERO_X_POSITION;
	private double endPositionY = ZERO_Y_POSITION;
	private boolean movesForward = true;
	private boolean inMovement = false;
	
	private static Pilot instance;
	
	private Pilot() {
		super();
	}
	
	public static Pilot getInstance() {
		if(instance == null) {
			instance = new Pilot();
		}
		
		return instance;
	}
	
	public double getCurrentXPosition () {
		return currentPositionX;
	}
	
	public double getCurrentYPosition() {
		return currentPositionY;
	}
	
	
	public void calculateNewEndposition(double speed) {
		boolean negativeSpeed = false;
		if(speed <0) {
			speed = -speed;
			negativeSpeed = true;
		}
		
		speed = speed*3.6; //convert km/h into m/s
		double changeInY = Math.pow(speed, 2) / (2*GRAVITATIONAL_FORCE);
		double acceleration = speed / TIME_FROM_0_TO_TOP;
		double diagonalLength = (speed * TIME_FROM_0_TO_TOP) - ((acceleration*Math.pow(TIME_FROM_0_TO_TOP, 2))/2);		
		double changeInX = getChangeInX(changeInY, diagonalLength);
		
		calculateNewXPosition(negativeSpeed, changeInX);
		endPositionY -= changeInY*ONE_METER_IN_PIXEL;
		
		log.info("Change for X: " + changeInX + " Change for Y: " + changeInY+ " Speed: " + speed + " Diagonal Length: " + diagonalLength);
	}

	private void calculateNewXPosition(boolean negative, double changeInX) {
		if(negative) {
			endPositionX += changeInX*ONE_METER_IN_PIXEL;
		} else {
			endPositionX -= changeInX*ONE_METER_IN_PIXEL;
		}
	}

	private double getChangeInX(double changeInY, double diagonalLength) {
		double changeInX;
		if(changeInY < diagonalLength) {
			changeInX = Math.sqrt(Math.pow(diagonalLength, 2) - Math.pow(changeInY, 2));
		}
		else {
			changeInX = Math.sqrt( Math.pow(changeInY, 2)-Math.pow(diagonalLength, 2));
		}
		return changeInX;
	}

	public int getWEIGHT_OF_PILOT() {
		return WEIGHT_OF_PILOT;
	}

	public void setWEIGHT_OF_PILOT(int weight) {
		WEIGHT_OF_PILOT = weight;
	}

	public boolean isInMovement() {
		return inMovement;
	}

	public void setInMovement(boolean inMovement) {
		this.inMovement = inMovement;
	}

	public void makeNextStep() {
		//TODO: make correct steps
		double differenz = endPositionX - ZERO_X_POSITION;
		double step = differenz / (TIME_FROM_0_TO_TOP*1000/RunGame.REFRESHRATE);
		
		if(movesForward) {
			currentPositionX += step;
			currentPositionY -= step/2;
			
			if(currentPositionX >= endPositionX) {
				movesForward = false;	
			}
		}
		else {
			
			currentPositionX -= step;
			currentPositionY += step/2;
			
			if(currentPositionX <= ZERO_X_POSITION) {
				setInMovement(false);
				movesForward = true;
				endPositionX = ZERO_X_POSITION;
			}
		}
		

	}
	
}
