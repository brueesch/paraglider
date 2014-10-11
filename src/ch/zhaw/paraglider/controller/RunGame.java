package ch.zhaw.paraglider.controller;

import ch.zhaw.paraglider.physics.Pilot;
import ch.zhaw.paraglider.view.Main;

public class RunGame implements Runnable {

	public static int REFRESHRATE = 5;
	private Pilot pilot;
	private Main main;

	public RunGame(Main main) {
		this.main = main;
		pilot = Pilot.getInstance();
	}

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
