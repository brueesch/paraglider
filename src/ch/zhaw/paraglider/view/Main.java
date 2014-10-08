/**
 * @author Christian Brüesch
 *
 *This is the Main Classe, it opens and shows a View.
 *
 */
package ch.zhaw.paraglider.view;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.zhaw.paraglider.physics.Wing;

import com.sun.istack.internal.logging.Logger;

public class Main extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1624980403895301036L;

	private static Logger log = Logger.getLogger(Main.class.getName(),
			Main.class);

	private Wing wing;

	private JButton test;

	public static void main(String[] args) {
		Main main = new Main();
		JFrame frame = new JFrame();
		frame.add(main);
		frame.setSize(1300, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public Main() {
		init();
	}

	public void paintComponent(Graphics g) {
		drawRightView(g);
		drawLeftView(g);

	}

	private void drawLeftView(Graphics g) {
		g.drawRect(40, 40, 580, 800);
		g.fillOval(310, 640, 40, 40);

		int[] xPoints = { 230, 330, 430 };
		int[] yPoints = { 240, 180, 240 };
		g.fillPolygon(xPoints, yPoints, 3);

		g.drawLine(330, 660, 330, 240);
	}

	private void drawRightView(Graphics g) {
		g.drawRect(640, 40, 580, 800);
		g.fillOval(910, 640, 40, 40);
		g.drawLine(930, 660, 720, 240);
		g.drawLine(930, 660, 825, 240);
		g.drawLine(930, 660, 930, 240);
		g.drawLine(930, 660, 1035, 240);
		g.drawLine(930, 660, 1140, 240);

		int[] xPoints = { 680, 846, 1012, 1180 };
		int[] yPoints = { 240, 180, 180, 240 };
		g.fillPolygon(xPoints, yPoints, 4);
	}

	private void init() {

		wing = new Wing("Left");
		test = new JButton("test");
		this.add(test);
		test.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == test) {
			wing.changeCurrentSpeed(2);
			log.info("Horizontal Speed: " + wing.getHorizontalSpeed()
					+ " km/h  Vertical Speed: " + wing.getVerticalSpeed()
					+ " m/s  Glide Ratio: " + wing.getCurrentGlideRatio());

		}

	}

}
