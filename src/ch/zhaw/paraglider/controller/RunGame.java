package ch.zhaw.paraglider.controller;

import ch.zhaw.paraglider.physics.Pilot;
import ch.zhaw.paraglider.view.Main;

/**
 * This class handles the runtime game.
 * It contains the method run, which tells the pilot to 
 * calculate his new position. And then tells the View to repaint.
 * @author Christian Brüesch
 *
 */
public class RunGame implements Runnable {

	/**
	 * Constant for the Refreshrate.
	 * Default: 5 ms
	 */
	public static int REFRESHRATE = 5;
	
	/**
	 * Variable for the pilot Instance.
	 */
	private Pilot pilot;
	
	/**
	 * Variable for the main Instance
	 */
	private Main main;

	/**
	 * Constructor
	 * @param main
	 */
	public RunGame(Main main) {
		this.main = main;
		pilot = Pilot.getInstance();
	}

	/**
	 * The Method handles all the things that should happen every Interval.
	 * - makeNextStep
	 * - repaint
	 */
	@Override
	public void run() {
		while (true) {
			if (pilot.isInMovement()) {
				pilot.makeNextStep();
				main.repaint();
			}
			try {
				Thread.sleep(REFRESHRATE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
