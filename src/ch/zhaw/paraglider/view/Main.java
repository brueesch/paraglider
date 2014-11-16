package ch.zhaw.paraglider.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Jama.Matrix;
import ch.zhaw.paraglider.controller.RunGame;
import ch.zhaw.paraglider.controller.XBoxController;
import ch.zhaw.paraglider.physics.Constants;
import ch.zhaw.paraglider.physics.Glider;
import ch.zhaw.paraglider.physics.Vector;

/**
 * Main Class. Extends JPanel to draw the paraglider into the JFrame. Implements
 * ActionListener to receive the inputs of the user.
 * 
 * @author Christian Brüesch
 * 
 */
public class Main extends JPanel implements ChangeListener, ActionListener {

	/**
	 * Gui Elemente
	 */

	public static JSlider leftSlider, rightSlider;
	private JSlider bothSlider;

	private static final long serialVersionUID = -1624980403895301036L;
	private static final int FRAME_HEIGHT = 700;
	private static final int FRAME_WIDTH = 1300;

	private Glider glider;
	private double oldRightValue = 0, oldLeftValue = 0, oldValue = 0;
	private JButton reset;

	private double[][] backgroundLinePositions;

	/**
	 * Starting Point of the program. Creates JFrame and adds the main panel on
	 * it.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		JFrame frame = new JFrame();
		frame.add(main);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * Constructor. Initializes various objects and starts the runGame Thread.
	 */
	public Main() {
		glider = Glider.getInstance();
		initSliders();
		initButtons();
		RunGame runGame = new RunGame(this);
		new Thread(runGame).start();
		XBoxController xBoxController = new XBoxController();
		if (xBoxController.isControllerConnected()) {
			new Thread(xBoxController).start();
		}
	}

	private void initButtons() {
		reset = new JButton("Reset");
		reset.addActionListener(this);
		add(reset);

	}

	private void initSliders() {
		JLabel labelLeftSlider = new JLabel("Left Break");
		leftSlider = new JSlider();

		JLabel labelRightSlider = new JLabel("Right Break");
		rightSlider = new JSlider();

		JLabel labelBothSlider = new JLabel("Both Breaks");
		bothSlider = new JSlider();

		// bothSlider, maxSpeed, glideRatio, damping;

		leftSlider.setMinimum(0);
		leftSlider.setMaximum(34);

		rightSlider.setMinimum(0);
		rightSlider.setMaximum(34);

		bothSlider.setMinimum(0);
		bothSlider.setMaximum(34);

		leftSlider.setMinorTickSpacing(1);
		leftSlider.setMajorTickSpacing(10);

		rightSlider.setMinorTickSpacing(1);
		rightSlider.setMajorTickSpacing(10);

		bothSlider.setMinorTickSpacing(1);
		bothSlider.setMajorTickSpacing(10);

		leftSlider.createStandardLabels(1);
		rightSlider.createStandardLabels(1);
		bothSlider.createStandardLabels(1);

		leftSlider.setPaintTicks(true);
		rightSlider.setPaintTicks(true);
		bothSlider.setPaintTicks(true);

		leftSlider.setPaintLabels(true);
		rightSlider.setPaintLabels(true);
		bothSlider.setPaintLabels(true);

		leftSlider.setValue(0);
		rightSlider.setValue(0);
		bothSlider.setValue(0);

		leftSlider.addChangeListener(this);
		rightSlider.addChangeListener(this);
		bothSlider.addChangeListener(this);

		add(labelLeftSlider);
		add(leftSlider);
		add(labelRightSlider);
		add(rightSlider);
		add(labelBothSlider);
		add(bothSlider);
	}

	/**
	 * Main paint method. Paints all components on the view.
	 */
	public void paintComponent(Graphics g) {
		Color color = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		drawLeftView(g);
		drawRightView(g);
		g.setColor(color);
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

	/**
	 * Temporary Method: Change listener for Sliders
	 */
	@Override
	public void stateChanged(ChangeEvent e) {

		if (e.getSource().equals(rightSlider)
				|| e.getSource().equals(leftSlider)) {
			double rightValue = rightSlider.getValue();
			double leftValue = leftSlider.getValue();
			glider.changeSpeed(-((rightValue - oldRightValue) / 3.6),
					-((leftValue - oldLeftValue) / 3.6));
			oldRightValue = rightValue;
			oldLeftValue = leftValue;
		}

		if (e.getSource().equals(bothSlider)) {
			double value = bothSlider.getValue();

			if(value >= 0.95*bothSlider.getMaximum()) {
				//glider.setInFullStall(true);
			}
			else {
				//glider.setInFullStall(false);
			}

			glider.changeSpeed(-((value - oldValue) / 3.6),
					-((value - oldValue) / 3.6));
			oldValue = value;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == reset) {
			leftSlider.setValue(0);
			rightSlider.setValue(0);
			bothSlider.setValue(0);
			backgroundLinePositions = null;
			glider.reset();
		}

	}
}