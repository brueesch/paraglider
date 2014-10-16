package ch.zhaw.paraglider.controller;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

import ch.zhaw.paraglider.physics.Pilot;
import ch.zhaw.paraglider.physics.Wing;

public class XBoxController implements Runnable {

	/**
	 * Instance for the XBox Controller
	 */
	private Controller controller;
	/**
	 * Position of the XBox Controller
	 */
	private final int POSITION_OF_XBOX_CONTROLLER = 4;
	/**
	 * Constant for convertion from the Controller input to the speed change.
	 */
	private final double MULTIPLICATOR_CONTROLLER_SPEED = 8.5;
	/**
	 * Pilot instance.
	 */
	private Pilot pilot;
	/**
	 * Wing instance.
	 */
	private Wing wing;

	/**
	 * Initializes the xbox Controller.
	 */
	public XBoxController(Wing wing) {
		pilot = Pilot.getInstance();
		this.wing = wing;
		try {
			Controllers.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Controllers.poll();

		controller = Controllers.getController(POSITION_OF_XBOX_CONTROLLER);
		controller.setDeadZone(0, (float) 0.2);
		controller.setDeadZone(2, (float) 0.2);

	}
	
	
	/**
	 * Run Method
	 * Handles all inputs from the controller.
	 */
	@Override
	public void run() {
		while (true) {
			controller.poll();
			if (controller.getAxisValue(0) == 0
					&& controller.getAxisValue(2) == 0) {
				double oldValue = 0;
				while (true) {					
					controller.poll();
					double value = controller.getAxisValue(0);
					System.out.println(controller.getAxisValue(0) + "      "
							+ controller.getAxisValue(2));
					if(oldValue != value) {							
							pilot.setChangeInSpeed(-((value-oldValue)*MULTIPLICATOR_CONTROLLER_SPEED));
							wing.changeCurrentSpeed(-(value-oldValue)*MULTIPLICATOR_CONTROLLER_SPEED);
							oldValue = controller.getAxisValue(0);
					}			
				}
			}

		}
	}

}
