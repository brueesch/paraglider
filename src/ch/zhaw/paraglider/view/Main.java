package ch.zhaw.paraglider.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.zhaw.paraglider.controller.RunGame;
import ch.zhaw.paraglider.controller.XBoxController;
import ch.zhaw.paraglider.physics.Constants;
import ch.zhaw.paraglider.physics.Glider;
import ch.zhaw.paraglider.physics.Vector;

/**
 * Main Class. Extends JPanel to draw the paraglider into the JFrame. Implements
 * ActionListener to receive the inputs of the user.
 * 
 * @author Christian Br�esch
 * 
 */
public class Main extends JPanel implements ChangeListener, ActionListener {

	/**
	 * Gui Elemente
	 */
	
	public static JSlider leftSlider, rightSlider;
	
	private static final long serialVersionUID = -1624980403895301036L;
	private static final int FRAME_HEIGHT = 1000;
	private static final int FRAME_WIDTH = 1300;
	
	private Glider glider;
	private double oldRightValue = 0, oldLeftValue = 0;
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
		leftSlider = new JSlider();
		rightSlider = new JSlider();

		leftSlider.setMinimum(0);
		leftSlider.setMaximum(46);

		rightSlider.setMinimum(0);
		rightSlider.setMaximum(46);

		leftSlider.setMinorTickSpacing(1);
		leftSlider.setMajorTickSpacing(10);

		rightSlider.setMinorTickSpacing(1);
		rightSlider.setMajorTickSpacing(10);

		leftSlider.createStandardLabels(1);
		rightSlider.createStandardLabels(1);

		leftSlider.setPaintTicks(true);
		rightSlider.setPaintTicks(true);

		leftSlider.setPaintLabels(true);
		rightSlider.setPaintLabels(true);

		leftSlider.setValue(0);
		rightSlider.setValue(0);
		
		leftSlider.addChangeListener(this);
		rightSlider.addChangeListener(this);

		add(leftSlider);
		add(rightSlider);
	}

	/**
	 * Main paint method. Paints all components on the view.
	 */
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		drawLeftView(g);
		drawRightView(g);

	}

	private void drawLeftView(Graphics g) {
		drawLeftBackground(g);
		Vector zeroPoint = new Vector(330, 0, 240);
		int diameter = 40;
		Color color = g.getColor();
		g.setColor(Color.black);
		g.drawRect(40, 40, 580, 560);
		g.drawString("Geschwindigkeit: " + Constants.convertMsToKmh(glider.getHorizontalSpeed())
				+ " km/h", 50, 125);
		g.drawString("Vertikalgeschwindigkeit: " + glider.getVerticalSpeed()
				+ " m/s", 50, 140);
		g.drawString("Gleitrate: " + glider.getCurrentGlideRatio(), 50, 155);
		Vector pos = glider.getPilotPosition();

		int currentXPosition = (int) (zeroPoint.getX()+(Constants.convertMeterToPixel(pos.getX())))-diameter/2;
		int currentZPosition = (int) (zeroPoint.getZ() + (Constants.convertMeterToPixel(pos.getZ())))-diameter/2;
		g.fillOval(currentXPosition, currentZPosition, diameter, diameter);
		g.drawLine(currentXPosition+diameter/2, currentZPosition+diameter/2, 270,
				(int)zeroPoint.getZ());
		g.drawLine(currentXPosition+diameter/2, currentZPosition+diameter/2, 330,
				(int)zeroPoint.getZ());
		g.drawLine(currentXPosition+diameter/2, currentZPosition+diameter/2, 390,
				(int)zeroPoint.getZ());

		g.setColor(Color.RED);
		int[] xPointsParaglider = { 230, 330, 430 };
		int[] zPointsParaglider = { 240, 180, 240 };
		g.fillPolygon(xPointsParaglider, zPointsParaglider, 3);

		g.setColor(Color.BLUE);
		int[] xPointsArrow = { 80, 120, 120, 140, 120, 120, 80 };
		int[] zPointsArrow = { 80, 80, 70, 90, 110, 100, 100 };
		g.fillPolygon(xPointsArrow, zPointsArrow, xPointsArrow.length);
		g.setColor(color);
	}

	private void drawLeftBackground(Graphics g) {
		Color color = g.getColor();
		g.setColor(Color.black);
		int xAxis = 0, zAxis = 2;
		if(backgroundLinePositions== null) {
			initLineArray();
		}
		
		calculateNewPosition(xAxis);
		calculateNewPosition(zAxis);
		
		for(int i = 0; i < backgroundLinePositions.length; i++) {
			g.drawLine((int)backgroundLinePositions[i][xAxis], 40, (int)backgroundLinePositions[i][xAxis], 600);
			g.drawLine(40, (int)backgroundLinePositions[i][zAxis],620, (int)backgroundLinePositions[i][zAxis]);
		}
		
		
		g.setColor(color);
	}
	


	private void calculateNewPosition(int axis) {
		double speed;
		if(axis == 0) {
			speed = glider.getHorizontalSpeed();
		}
		else if(axis == 1) {
			double angle = Math.toDegrees(glider.getAngularVelocity()*Constants.TIME_INTERVALL);
			double radius = 20;
			double distance = (angle * 2* radius * Math.PI) /360;
			
			speed = -distance/Constants.TIME_INTERVALL;
			//speed = -3;
		}
		else {
			speed = glider.getVerticalSpeed();
		}
		double distanz = speed*Constants.TIME_INTERVALL;
		distanz = Constants.convertMeterToPixel(distanz);
		
		for(int i = 0; i<backgroundLinePositions.length; i++) {
			double currentPosition = backgroundLinePositions[i][axis];
			currentPosition -= distanz;
			if(currentPosition <40) {
				currentPosition = 600;
			}
			if(currentPosition > 620) {
				currentPosition = 40;
			}
			backgroundLinePositions[i][axis] = currentPosition;
		}
	}

	private void initLineArray() {
		backgroundLinePositions = new double[7][3];
		
		for(int i = 0; i < backgroundLinePositions.length; i++) {
			backgroundLinePositions[i][0] = 80+i*80;
			backgroundLinePositions[i][1] = 80+i*80;
			backgroundLinePositions[i][2] = 80+i*80;
		}
	}

	private void drawRightView(Graphics g) {
		drawRightBackground(g);
		Vector zeroPoint = new Vector(0, 930, 240);
		int diameter = 40;
		Color color = g.getColor();
		g.setColor(Color.BLACK);
		g.drawRect(640, 40, 580, 560);

		Vector pos = glider.getPilotPosition();
		
		int currentYPosition = (int) (zeroPoint.getY()+(Constants.convertMeterToPixel(pos.getY())))-diameter/2;
		int currentZPosition = (int) (zeroPoint.getZ() + (Constants.convertMeterToPixel(pos.getZ())))-diameter/2;
		g.fillOval(currentYPosition, currentZPosition, diameter, diameter);

		g.drawLine(currentYPosition+diameter/2, currentZPosition+diameter/2, 720,
				(int)zeroPoint.getZ());
		g.drawLine(currentYPosition+diameter/2, currentZPosition+diameter/2, 825,
				(int)zeroPoint.getZ());
		g.drawLine(currentYPosition+diameter/2, currentZPosition+diameter/2, 930,
				(int)zeroPoint.getZ());
		g.drawLine(currentYPosition+diameter/2, currentZPosition+diameter/2, 1035,
				(int)zeroPoint.getZ());
		g.drawLine(currentYPosition+diameter/2, currentZPosition+diameter/2, 1140,
				(int)zeroPoint.getZ());
		

		g.setColor(Color.RED);
		int[] yPoints = { 680, 846, 1012, 1180 };
		int[] zPoints = { 240, 180, 180, 240 };
		g.fillPolygon(yPoints, zPoints, 4);
		g.setColor(color);
	}
	
	private void drawRightBackground(Graphics g) {
		int yAxis = 1, zAxis = 2;
		if(backgroundLinePositions == null) {
			initLineArray();
		}
		Color color = g.getColor();
		g.setColor(Color.BLACK);
		calculateNewPosition(yAxis);
		
		for(int i = 0; i<backgroundLinePositions.length; i++) {
			g.drawLine((int)backgroundLinePositions[i][yAxis]+600,40, (int)backgroundLinePositions[i][yAxis]+600,600);
			g.drawLine(640, (int)backgroundLinePositions[i][zAxis],1220, (int)backgroundLinePositions[i][zAxis]);
		}
		g.setColor(color);
	}

	/**
	 * Temporary Method: Change listener for Sliders
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		
		if(e.getSource().equals(rightSlider) || e.getSource().equals(leftSlider)) {
			double rightValue = rightSlider.getValue();
			double leftValue = leftSlider.getValue();
			try {
				glider.changeSpeed(-((leftValue - oldLeftValue)/3.6), -((rightValue - oldRightValue)/3.6));
				oldRightValue = rightValue;
				oldLeftValue = leftValue;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == reset) {
			leftSlider.setValue(0);
			rightSlider.setValue(0);
			glider.reset();
		}
		
	}
}