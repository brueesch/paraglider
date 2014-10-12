package ch.zhaw.paraglider.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.zhaw.paraglider.controller.RunGame;
import ch.zhaw.paraglider.physics.Pilot;
import ch.zhaw.paraglider.physics.Wing;

import com.sun.istack.internal.logging.Logger;

/**
 * Main Class. Extends JPanel to draw the paraglider into the JFrame. Implements
 * ActionListener to receive the inputs of the user.
 * 
 * @author Christian Brüesch
 * 
 */
public class Main extends JPanel implements ActionListener {

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
	 * Standard Logger variable.
	 */
	private static Logger log = Logger.getLogger(Main.class.getName(),
			Main.class);

	/**
	 * Variable for the wing instance.
	 */
	private Wing wing;
	/**
	 * Variable for the pilot instance.
	 */
	private Pilot pilot;

	/**
	 * JButtons for the View. - increaseSpeed - decreaseSpeed
	 */
	private JButton increaseSpeed, decreaseSpeed;

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
		wing = new Wing("Left");
		increaseSpeed = new JButton("+ 1 km/h");
		this.add(increaseSpeed);
		decreaseSpeed = new JButton("- 1 km/h");
		this.add(decreaseSpeed);
		increaseSpeed.addActionListener(this);
		decreaseSpeed.addActionListener(this);
		RunGame runGame = new RunGame(this);
		new Thread(runGame).start();
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
		g.drawString("Geschwindigkeit: " + wing.getHorizontalSpeed(), 50, 125);
		g.drawString("Horizontalgeschwindigkeit: " + wing.getVerticalSpeed(),
				50, 140);
		g.drawString("Gleitrate: " + wing.getCurrentGlideRatio(), 50, 155);

		g.fillOval((int) pilot.getCurrentXPosition(),
				(int) pilot.getCurrentYPosition(), diameter, diameter);
		g.drawLine((int) pilot.getCurrentXPosition() + diameter / 2,
				(int) pilot.getCurrentYPosition() + diameter / 2, 270, 240);
		g.drawLine((int) pilot.getCurrentXPosition() + diameter / 2,
				(int) pilot.getCurrentYPosition() + diameter / 2, 330, 240);
		g.drawLine((int) pilot.getCurrentXPosition() + diameter / 2,
				(int) pilot.getCurrentYPosition() + diameter / 2, 390, 240);

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
		Color color = g.getColor();
		g.setColor(Color.BLACK);
		g.drawRect(640, 40, 580, 800);
		g.fillOval(910, 640, 40, 40);
		g.drawLine(930, 660, 720, 240);
		g.drawLine(930, 660, 825, 240);
		g.drawLine(930, 660, 930, 240);
		g.drawLine(930, 660, 1035, 240);
		g.drawLine(930, 660, 1140, 240);

		g.setColor(Color.RED);
		int[] xPoints = { 680, 846, 1012, 1180 };
		int[] yPoints = { 240, 180, 180, 240 };
		g.fillPolygon(xPoints, yPoints, 4);
		g.setColor(color);
	}

	/**
	 * ActionPerformed method implements the two JButton and their actions.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		double oldVerticalSpeed = wing.getVerticalSpeed();
		if (e.getSource() == increaseSpeed) {
			if (wing.getHorizontalSpeed() <= 55) {
				wing.changeCurrentSpeed(1);
				pilot.setNewSpeedChangeParameters(1, wing.getVerticalSpeed()
						- oldVerticalSpeed);
				pilot.setInMovement(true);
				log.info("Horizontal Speed: " + wing.getHorizontalSpeed()
						+ " km/h  Vertical Speed: " + wing.getVerticalSpeed()
						+ " m/s  Glide Ratio: " + wing.getCurrentGlideRatio());
			}

		}

		if (e.getSource() == decreaseSpeed) {
			if (wing.getHorizontalSpeed() >= 28.6) {
				wing.changeCurrentSpeed(-1);
				pilot.setNewSpeedChangeParameters(-1, wing.getVerticalSpeed()
						- oldVerticalSpeed);
				pilot.setInMovement(true);
				log.info("Horizontal Speed: " + wing.getHorizontalSpeed()
						+ " km/h  Vertical Speed: " + wing.getVerticalSpeed()
						+ " m/s  Glide Ratio: " + wing.getCurrentGlideRatio());
			}

		}

	}
}
