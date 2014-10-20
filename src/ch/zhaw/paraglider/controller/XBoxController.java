package ch.zhaw.paraglider.controller;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

import ch.zhaw.paraglider.physics.Glider;
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
	private int positionOfXboxController;
	/**
	 * Constant for convertion from the Controller input to the speed change.
	 */
	private final double MULTIPLICATOR_CONTROLLER_SPEED = 8.5;
	/**
	 * Pilot instance.
	 */
	private Pilot pilot;
	/**
	 * Glider instance.
	 */
	private Glider glider;
	/**
	 * Right and Left Wing variable.
	 */
	private Wing leftWing, rightWing;

	/**
	 * Initializes the xbox Controller.
	 */
	public XBoxController() {
		pilot = Pilot.getInstance();
		this.glider = Glider.getInstance();
		try {
			Controllers.create();
			leftWing = glider.getWing("left");
			rightWing = glider.getWing("right");
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Controllers.poll();
		for(int i = 0; i<Controllers.getControllerCount();i++) {
			System.out.println(Controllers.getController(i).getName());
			if(Controllers.getController(i).getName().contains("Xbox")) {
				positionOfXboxController = i;
				
				for(int j=0; j<Controllers.getController(i).getAxisCount();j++)
				{
					System.out.println(Controllers.getController(i).getAxisName(j));
				}
				for(int j=0; j<Controllers.getController(i).getButtonCount();j++)
				{
					System.out.println(Controllers.getController(i).getButtonName(j));
				}
			}
			
		}
		
		controller = Controllers.getController(positionOfXboxController);
		
		//controller.setDeadZone(0, (float) 0.2);
		//controller.setDeadZone(2, (float) 0.2);

	}

	/**
	 * Run Method Handles all inputs from the controller.
	 */
	@Override
	public void run() {
		while (true) {
			controller.poll();
			if (controller.getAxisValue(0) == 0
					&& controller.getAxisValue(2) == 0) {
				activateController();
			}

		}
	}

	/**
	 * Controls all inputs made with the controller.
	 */
	private void activateController() {
		double oldValueLeftWing = 0;
		double oldValueRightWing = 0;
		while (true) {
			controller.poll();
			if (controller.isButtonPressed(6)) {
				pilot.reset();
			}
			
			System.out.println(controller.getAxisValue(0) + "      "
					+ controller.getAxisValue(2));
			oldValueLeftWing = controlLeftWing(oldValueLeftWing);
			oldValueRightWing = controlRightWing(oldValueRightWing);
		}
	}

	/**
	 * Controls the movement of the right wing.
	 * @param oldValueRightWing
	 * @return double for oldValueRightWing
	 */
	private double controlRightWing(double oldValueRightWing) {
		double valueRightWing = controller.getAxisValue(2);
		if (oldValueRightWing != valueRightWing) {
			if (valueRightWing >= 0) {
				pilot.setChangeInSpeed(-((valueRightWing - oldValueRightWing) * MULTIPLICATOR_CONTROLLER_SPEED));
				rightWing.changeCurrentSpeed(-(valueRightWing - oldValueRightWing)
						* MULTIPLICATOR_CONTROLLER_SPEED);
				oldValueRightWing = controller.getAxisValue(2);
			}
		}
		return oldValueRightWing;
	}

	/**
	 * Controls the movement of the left wing.
	 * @param oldValueLeftWing
	 * @return double for oldValueLeftWing
	 */
	private double controlLeftWing(double oldValueLeftWing) {
		double valueLeftWing = controller.getAxisValue(0);
		if (oldValueLeftWing != valueLeftWing) {
			if (valueLeftWing >= 0) {
				pilot.setChangeInSpeed(-((valueLeftWing - oldValueLeftWing) * MULTIPLICATOR_CONTROLLER_SPEED));
				leftWing.changeCurrentSpeed(-(valueLeftWing - oldValueLeftWing)
						* MULTIPLICATOR_CONTROLLER_SPEED);
				oldValueLeftWing = controller.getAxisValue(0);
			}
		}
		return oldValueLeftWing;
	}

}
