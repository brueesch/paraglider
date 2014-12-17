package ch.zhaw.paraglider.controller;

import ch.zhaw.paraglider.physics.Constants;
import ch.zhaw.paraglider.physics.Glider;
import ch.zhaw.paraglider.view.Main;

/**
 * This class controls the game. It contains the method run, in which first the new
 * Positions are calculated and then view repainted.
 * 
 * @author Christian Brüesch / Jonas Gschwend
 * 
 */
public class RunGame implements Runnable {
	

	private Glider glider;
	private Main main;


	public RunGame(Main main) {
		this.main = main;
		glider = Glider.getInstance();
	}

	/**
	 * The Method handles all the things that should happen every Interval.
	 *  - makeNextStep
	 *  - repaint
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
