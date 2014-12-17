package ch.zhaw.paraglider.view;

import java.awt.Color;
import java.awt.Graphics;

import Jama.Matrix;
import ch.zhaw.paraglider.physics.Constants;
import ch.zhaw.paraglider.physics.Glider;
import ch.zhaw.paraglider.util.Vector;

/**
 * This Class handles all the drawing done in this program.
 * 
 * @author Christian Brüesch / Jonas Gschwend
 * 
 */
public class DrawView {

	public double[][] backgroundLinePositions;
	private Glider glider;

	public DrawView() {
		glider = Glider.getInstance();
	}

	/**
	 * This Method will draw the complete View.
	 * 
	 * @param g
	 */
	public void drawView(Graphics g) {
		drawLeftView(g);
		drawRightView(g);
	}

	/**
	 * Resets the Array, in which the Positions of the moving Background lines
	 * are stored.
	 */
	public void resetBackgroundLinePositions() {
		backgroundLinePositions = null;
	}

	private void drawLeftView(Graphics g) {
		double[][] coordinatesParaglider = { { -100, 0 }, { 0, -60 },
				{ 100, 0 } };
		double[][] coordinatesPilot = { { 0,
				Constants.convertMeterToPixel(Constants.LENGTH_OF_CORD) } };

		drawLeftBackground(g);
		Vector zeroPoint = new Vector(330, 0, 240);
		double alpha = glider.getPitchAngle();
		coordinatesPilot = calculateRotation(coordinatesPilot, alpha);
		checkDirectionX(coordinatesPilot);
		coordinatesPilot = moveToZeroPoint(coordinatesPilot, zeroPoint);

		coordinatesParaglider = calculateRotation(coordinatesParaglider, alpha);
		checkDirectionX(coordinatesParaglider);
		coordinatesParaglider = moveToZeroPoint(coordinatesParaglider,
				zeroPoint);

		drawInfosAndFrame(g, zeroPoint);
		drawPilotAndParaglider(g, coordinatesParaglider, coordinatesPilot,
				zeroPoint);
	}

	private void drawRightView(Graphics g) {
		double[][] coordinatesParaglider = { { -250, 0 }, { -84, -60 },
				{ 84, -60 }, { 250, 0 } };
		double[][] coordinatesPilot = { { 0,
				Constants.convertMeterToPixel(Constants.LENGTH_OF_CORD) } };

		drawRightBackground(g);
		Vector zeroPoint = new Vector(930, 0, 240);
		double alpha = glider.getRollAngle();
		coordinatesPilot = calculateRotation(coordinatesPilot, alpha);
		checkDirectionY(coordinatesPilot);
		coordinatesPilot = moveToZeroPoint(coordinatesPilot, zeroPoint);
		coordinatesParaglider = calculateRotation(coordinatesParaglider, alpha);
		checkDirectionY(coordinatesParaglider);
		coordinatesParaglider = moveToZeroPoint(coordinatesParaglider,
				zeroPoint);

		drawInfosAndFrame(g, zeroPoint);
		drawPilotAndParaglider(g, coordinatesParaglider, coordinatesPilot,
				zeroPoint);
	}

	private void drawPilotAndParaglider(Graphics g,
			double[][] coordinatesParaglider, double[][] coordinatesPilot,
			Vector zeroPoint) {
		Color color = g.getColor();
		g.setColor(Color.black);
		int diameter = 40;
		g.fillOval((int) (coordinatesPilot[0][0] - diameter / 2),
				(int) (coordinatesPilot[0][1] - diameter / 2), diameter,
				diameter);
		g.drawLine((int) (coordinatesPilot[0][0]),
				(int) (coordinatesPilot[0][1]), (int) zeroPoint.getX(),
				(int) zeroPoint.getZ());
		g.drawLine((int) (coordinatesPilot[0][0]),
				(int) (coordinatesPilot[0][1]),
				(int) coordinatesParaglider[0][0],
				(int) coordinatesParaglider[0][1]);
		g.drawLine(
				(int) (coordinatesPilot[0][0]),
				(int) (coordinatesPilot[0][1]),
				(int) coordinatesParaglider[coordinatesParaglider.length - 1][0],
				(int) coordinatesParaglider[coordinatesParaglider.length - 1][1]);

		g.setColor(Color.RED);
		int[] xCoordinates = new int[coordinatesParaglider.length];
		int[] zCoordinates = new int[coordinatesParaglider.length];
		for (int i = 0; i < coordinatesParaglider.length; i++) {
			xCoordinates[i] = (int) coordinatesParaglider[i][0];
			zCoordinates[i] = (int) coordinatesParaglider[i][1];
		}
		g.fillPolygon(xCoordinates, zCoordinates, xCoordinates.length);
		g.setColor(color);
	}

	private double[][] moveToZeroPoint(double[][] coordinatesArray,
			Vector zeroPoint) {
		for (int i = 0; i < coordinatesArray.length; i++) {
			for (int j = 0; j < coordinatesArray[i].length; j++) {
				if (j == 0) {
					coordinatesArray[i][j] += zeroPoint.getX();
				} else {
					coordinatesArray[i][j] += zeroPoint.getZ();
				}
			}
		}
		return coordinatesArray;
	}

	private void drawInfosAndFrame(Graphics g, Vector zeroPoint) {
		Color color = g.getColor();
		g.setColor(Color.black);
		g.drawRect((int) (-290 + zeroPoint.getX()),
				(int) (-200 + zeroPoint.getZ()), 580, 560);
		g.drawString(
				"Geschwindigkeit: "
						+ Constants.convertMsToKmh(glider.getHorizontalSpeed())
						+ " km/h", 50, 560);
		g.drawString("Vertikalgeschwindigkeit: " + glider.getVerticalSpeed()
				+ " m/s", 50, 575);
		g.drawString("Gleitrate: " + glider.getCurrentGlideRatio(), 50, 590);
		g.setColor(color);
	}

	private double[][] calculateRotation(double[][] coordinatesArray,
			double alpha) {
		Matrix a = new Matrix(coordinatesArray);
		double[][] rotationArray = { { Math.cos(alpha), Math.sin(alpha) },
				{ -Math.sin(alpha), Math.cos(alpha) } };
		Matrix rotationMatrix = new Matrix(rotationArray);
		double[][] result = a.times(rotationMatrix).getArray();

		return result;
	}

	private void checkDirectionX(double[][] result) {
		if (glider.isOnPositiveSite()) {
			for (int i = 0; i < result.length; i++) {
				result[i][0] = -result[i][0];
			}
		}
	}

	private void checkDirectionY(double[][] result) {
		if (glider.isOnRightSite()) {
			for (int i = 0; i < result.length; i++) {
				result[i][0] = -result[i][0];
			}
		}
	}

	private void drawLeftBackground(Graphics g) {
		Color color = g.getColor();
		g.setColor(Color.black);
		int xAxis = 0, zAxis = 2;
		if (backgroundLinePositions == null) {
			initLineArray();
		}

		calculateNewPosition(xAxis);
		calculateNewPosition(zAxis);

		for (int i = 0; i < backgroundLinePositions.length; i++) {
			g.drawLine((int) backgroundLinePositions[i][xAxis], 40,
					(int) backgroundLinePositions[i][xAxis], 600);
			g.drawLine(40, (int) backgroundLinePositions[i][zAxis], 620,
					(int) backgroundLinePositions[i][zAxis]);
		}

		g.setColor(color);
	}

	private void calculateNewPosition(int axis) {
		double speed;
		if (axis == 0) {
			speed = glider.getHorizontalSpeed();
		} else if (axis == 1) {
			double angle = Math.toDegrees(glider.getAngularVelocity()
					* Constants.TIME_INTERVALL);
			double radius = 20;
			double distance = (angle * 2 * radius * Math.PI) / 360;

			speed = distance / Constants.TIME_INTERVALL;
		} else {
			speed = glider.getVerticalSpeed();
		}
		double distance = speed * Constants.TIME_INTERVALL;
		distance = Constants.convertMeterToPixel(distance);

		for (int i = 0; i < backgroundLinePositions.length; i++) {
			double currentPosition = backgroundLinePositions[i][axis];
			currentPosition -= distance;
			if (currentPosition < 40) {
				currentPosition = 600;
			}
			if (currentPosition > 620) {
				currentPosition = 40;
			}
			backgroundLinePositions[i][axis] = currentPosition;
		}
	}

	private void initLineArray() {
		backgroundLinePositions = new double[7][3];

		for (int i = 0; i < backgroundLinePositions.length; i++) {
			backgroundLinePositions[i][0] = 80 + i * 80;
			backgroundLinePositions[i][1] = 80 + i * 80;
			backgroundLinePositions[i][2] = 80 + i * 80;
		}
	}

	private void drawRightBackground(Graphics g) {
		int yAxis = 1, zAxis = 2;
		if (backgroundLinePositions == null) {
			initLineArray();
		}
		Color color = g.getColor();
		g.setColor(Color.BLACK);
		calculateNewPosition(yAxis);

		for (int i = 0; i < backgroundLinePositions.length; i++) {
			g.drawLine((int) backgroundLinePositions[i][yAxis] + 600, 40,
					(int) backgroundLinePositions[i][yAxis] + 600, 600);
			g.drawLine(640, (int) backgroundLinePositions[i][zAxis], 1220,
					(int) backgroundLinePositions[i][zAxis]);
		}
		g.setColor(color);
	}

}
