/**
 * @author Christian Brüesch
 *
 *This is the Main Classe, it opens and shows a View.
 *
 */
package ch.zhaw.paraglider.view;


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

	private static Logger log = Logger.getLogger(Main.class.getName(), Main.class);
	
	private Wing wing;
	
	

	
	private JButton test;
	public static void main(String[] args) {
		Main main = new Main();
		JFrame frame = new JFrame();
		frame.add(main);
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	
	public Main() {
		init();
	}


	private void init() {
		
		wing = new Wing("Left");
		test = new JButton("test");
		this.add(test);
		test.addActionListener(this);
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==test) {
			wing.changeCurrentSpeed(2);
			log.info("Horizontal Speed: "+wing.getHorizontalSpeed()+" km/h  Vertical Speed: "+wing.getVerticalSpeed() + " m/s  Glide Ratio: "+wing.getCurrentGlideRatio());
			
		}
		
	}

}
