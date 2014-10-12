/**
 * @author Christian Brüesch
 *
 *This is the Main Classe, it opens and shows a View.
 *
 */
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

public class Main extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1624980403895301036L;
	private static final int FRAME_HEIGHT = 1000;
	private static final int FRAME_WIDTH = 1300;
	private static Logger log = Logger.getLogger(Main.class.getName(),
			Main.class);

	private Wing wing;
	private Pilot pilot;

	private JButton increaseSpeed, decreaseSpeed;

	public static void main(String[] args) {
		Main main = new Main();
		JFrame frame = new JFrame();
		frame.add(main);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public Main() {
		init();
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		drawLeftView(g);
		drawRightView(g);

	}

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

	private void init() {
		pilot = Pilot.getInstance();
		wing = new Wing("Left");
		increaseSpeed = new JButton("+ 2 km/h");
		this.add(increaseSpeed);
		decreaseSpeed = new JButton("- 2 km/h");
		this.add(decreaseSpeed);
		increaseSpeed.addActionListener(this);
		decreaseSpeed.addActionListener(this);
		RunGame runGame = new RunGame(this);
		new Thread(runGame).start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == increaseSpeed) {
			if (wing.getHorizontalSpeed() <= 55) {
				wing.changeCurrentSpeed(2);
				pilot.calculateNewEndposition(2, wing.getVerticalSpeed());
				pilot.setInMovement(true);
				log.info("Horizontal Speed: " + wing.getHorizontalSpeed()
						+ " km/h  Vertical Speed: " + wing.getVerticalSpeed()
						+ " m/s  Glide Ratio: " + wing.getCurrentGlideRatio());
			}

		}
		
		if (e.getSource() == decreaseSpeed) {
			if (wing.getHorizontalSpeed() >= 28.6) {
				wing.changeCurrentSpeed(-2);
				pilot.calculateNewEndposition(-2, wing.getVerticalSpeed());
				pilot.setInMovement(true);
				log.info("Horizontal Speed: " + wing.getHorizontalSpeed()
						+ " km/h  Vertical Speed: " + wing.getVerticalSpeed()
						+ " m/s  Glide Ratio: " + wing.getCurrentGlideRatio());
			}

		}


	}
}
