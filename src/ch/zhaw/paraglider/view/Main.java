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

import ch.zhaw.paraglider.controller.RunGame;
import ch.zhaw.paraglider.controller.XBoxController;
import ch.zhaw.paraglider.physics.Glider;

/**
 * Main Class. Extends JPanel to draw the paraglider into the JFrame. Implements
 * ActionListener to receive the inputs of the user.
 * 
 * @author Christian Brüesch
 * 
 */
public class Main extends JPanel implements ChangeListener, ActionListener {

	public static JSlider leftSlider, rightSlider;
	private JSlider bothSlider;
	
	private static final long serialVersionUID = -1624980403895301036L;
	private static final int FRAME_HEIGHT = 700;
	private static final int FRAME_WIDTH = 1300;

	private Glider glider;
	private double oldRightValue = 0, oldLeftValue = 0, oldValue = 0;
	private JButton reset;
	private DrawView drawView;

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
		drawView = new DrawView();
		initSliders();
		initButtons();
		RunGame runGame = new RunGame(this);
		new Thread(runGame).start();
		XBoxController xBoxController = new XBoxController();
		if (xBoxController.isControllerConnected()) {
			new Thread(xBoxController).start();
		}
	}
	
	/**
	 * Main paint method. Paints all components on the view.
	 */
	public void paintComponent(Graphics g) {
		Color color = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		drawView.drawView(g);
		g.setColor(color);
	}
	
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

			if(value >= 0.90*bothSlider.getMaximum()) {
				glider.setInFullStall(true);
			}
			else {
				glider.setInFullStall(false);
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
			drawView.resetBackgroundLinePositions();
			glider.reset();
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
}