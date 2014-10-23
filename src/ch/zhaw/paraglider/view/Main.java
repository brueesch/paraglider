package ch.zhaw.paraglider.view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.zhaw.paraglider.controller.RunGame;
import ch.zhaw.paraglider.controller.XBoxController;
import ch.zhaw.paraglider.physics.Glider;
import ch.zhaw.paraglider.physics.Pilot;
import ch.zhaw.paraglider.physics.Vector;
import ch.zhaw.paraglider.physics.Wing;

/**
 * Main Class. Extends JPanel to draw the paraglider into the JFrame. Implements
 * ActionListener to receive the inputs of the user.
 * 
 * @author Christian Brüesch
 * 
 */
public class Main extends JPanel implements ChangeListener {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -1624980403895301036L;
	/**
	 * Constant to define the height of the frame.
	 */
	private static final int FRAME_HEIGHT = 1000;
	/**
	 * Constant to define the width of the frame.
	 */
	private static final int FRAME_WIDTH = 1300;
	/**
	 * Variable for the glider instance.
	 */
	private Glider glider;
	/**
	 * Variable for the pilot instance.
	 */
	private Pilot pilot;

	/**
	 * Gui Elemente
	 */
	public static JSlider leftSlider, rightSlider;
	private double oldRightValue = 0, oldLeftValue = 0;

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
		pilot = Pilot.getInstance();
		glider = Glider.getInstance();
		initSliders();
		RunGame runGame = new RunGame(this);
		new Thread(runGame).start();
		XBoxController xBoxController = new XBoxController();
		if (xBoxController.isControllerConnected()) {
			new Thread(xBoxController).start();
		}
		else {
			
		}
	}

	/**
	 * Initialises Sliders
	 */
	private void initSliders() {
		leftSlider = new JSlider();
		rightSlider = new JSlider();

		leftSlider.setMinimum(0);
		leftSlider.setMaximum(100);

		rightSlider.setMinimum(0);
		rightSlider.setMaximum(100);

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

	/**
	 * The method draws the left view of the program. It contains the paraglider
	 * from the side view.
	 * 
	 * @param g
	 */
	private void drawLeftView(Graphics g) {
		int diameter = 40;
		Color color = g.getColor();
		g.setColor(Color.black);
		g.drawRect(40, 40, 580, 800);
		g.drawString("Geschwindigkeit: " + glider.getHorizontalSpeed()
				+ " km/h", 50, 125);
		g.drawString("Vertikalgeschwindigkeit: " + glider.getVerticalSpeed()
				+ " m/s", 50, 140);
		g.drawString("Gleitrate: " + glider.getCurrentGlideRatio(), 50, 155);

		g.fillOval((int) pilot.getCurrentPosition().getX(), (int) pilot
				.getCurrentPosition().getY(), diameter, diameter);
		g.drawLine((int) pilot.getCurrentPosition().getX() + diameter / 2,
				(int) pilot.getCurrentPosition().getY() + diameter / 2, 270,
				240);
		g.drawLine((int) pilot.getCurrentPosition().getX() + diameter / 2,
				(int) pilot.getCurrentPosition().getY() + diameter / 2, 330,
				240);
		g.drawLine((int) pilot.getCurrentPosition().getX() + diameter / 2,
				(int) pilot.getCurrentPosition().getY() + diameter / 2, 390,
				240);

		g.setColor(Color.RED);
		int[] xPointsParaglider = { 230, 330, 430 };
		int[] yPointsParaglider = { 240, 180, 240 };
		g.fillPolygon(xPointsParaglider, yPointsParaglider, 3);

		g.setColor(Color.BLUE);
		int[] xPointsArrow = { 80, 120, 120, 140, 120, 120, 80 };
		int[] yPointsArrow = { 80, 80, 70, 90, 110, 100, 100 };
		g.fillPolygon(xPointsArrow, yPointsArrow, xPointsArrow.length);
		g.setColor(color);
	}

	/**
	 * The method draws the left view of the program. It contains the paraglider
	 * from the front view.
	 * 
	 * @param g
	 */
	private void drawRightView(Graphics g) {
		int diameter = 40;
		Color color = g.getColor();
		g.setColor(Color.BLACK);
		g.drawRect(640, 40, 580, 800);

		Vector pos = pilot.getCurrentPosition();

		g.fillOval((int) pos.getZ(), (int) pos.getY(), 40, 40);
		g.drawLine((int) pos.getZ() + diameter / 2, (int) pos.getY() + diameter
				/ 2, 720, 240);
		g.drawLine((int) pos.getZ() + diameter / 2, (int) pos.getY() + diameter
				/ 2, 825, 240);
		g.drawLine((int) pos.getZ() + diameter / 2, (int) pos.getY() + diameter
				/ 2, 930, 240);
		g.drawLine((int) pos.getZ() + diameter / 2, (int) pos.getY() + diameter
				/ 2, 1035, 240);
		g.drawLine((int) pos.getZ() + diameter / 2, (int) pos.getY() + diameter
				/ 2, 1140, 240);

		g.setColor(Color.RED);
		int[] xPoints = { 680, 846, 1012, 1180 };
		int[] yPoints = { 240, 180, 180, 240 };
		g.fillPolygon(xPoints, yPoints, 4);
		g.setColor(color);
	}

	/**
	 * Temporary Method: Change listener for Sliders
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		
		if(e.getSource().equals(rightSlider)) {
			double value = rightSlider.getValue();
			pilot.setChangeInSpeed(-((value - oldRightValue)));
			Wing rightWing;
			try {
				rightWing = glider.getRightWing();
				rightWing.changeCurrentSpeed(-(value - oldRightValue)
						/100 * 5);
				oldRightValue = value;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}
		
		if(e.getSource().equals(leftSlider)) {
			double value = leftSlider.getValue();
			pilot.setChangeInSpeed(-((value - oldLeftValue)));
			Wing leftWing;
			try {
				leftWing = glider.getLeftWing();
				leftWing.changeCurrentSpeed(-(value - oldLeftValue)
						/100 * 5);
				oldLeftValue = value;
			} catch (Exception e1) {
				e1.printStackTrace();
			}	
		}
	}
}
