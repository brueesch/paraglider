package ch.zhaw.paraglider.controller;

import ch.zhaw.paraglider.physics.Constants;
import ch.zhaw.paraglider.physics.Glider;
import ch.zhaw.paraglider.view.Main;

/**
 * This class handles the runtime game. It contains the method run, which tells
 * the pilot to calculate his new position. And then tells the View to repaint.
 * 
 * @author Christian Brüesch
 * 
 */
public class RunGame implements Runnable {
	

	private Glider glider;
	private Main main;

	/**
	 * Constructor
	 * 
	 * @param main
	 */
	public RunGame(Main main) {
		this.main = main;
		glider = Glider.getInstance();
	}

	/**
	 * The Method handles all the things that should happen every Interval. -
	 * makeNextStep - repaint
	 */
	@Override
	public void run() {
		while (true) {
			glider.makeNextStep();
			main.repaint();
			try {
				Thread.sleep((int)(Constants.TIME_INTERVALL*1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
