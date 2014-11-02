package ch.zhaw.paraglider.controller;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

import ch.zhaw.paraglider.physics.Glider;
import ch.zhaw.paraglider.view.Main;

public class XBoxController implements Runnable {

	private Controller controller;
	private int positionOfXboxController = -1;
	private final double MULTIPLICATOR_CONTROLLER_SPEED = 6;//2.8;
	private Glider glider;
	private boolean controllerIsConnected = false;

	/**
	 * Initializes the Xbox Controller.
	 */
	public XBoxController() {
		this.glider = Glider.getInstance();
		try {
			Controllers.create();
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Controllers.poll();
		for(int i = 0; i<Controllers.getControllerCount();i++) {
			if(Controllers.getController(i).getName().toUpperCase().contains("Xbox".toUpperCase())) {
				positionOfXboxController = i;

				System.out.println(Controllers.getController(i).getName());
				for(int j=0; j<Controllers.getController(i).getAxisCount();j++)
				{
					System.out.println(Controllers.getController(i).getAxisName(j) + " - Axis: " +j );
				}
				for(int j=0; j<Controllers.getController(i).getButtonCount();j++)
				{
					System.out.println(Controllers.getController(i).getButtonName(j) + " - Button: " +j );
				}
			}
			
		}
		if(positionOfXboxController != -1) {
			controller = Controllers.getController(positionOfXboxController);
			controllerIsConnected = true;
		}
		
		//controller.setDeadZone(0, (float) 0.2);
		//controller.setDeadZone(2, (float) 0.2);

	}
	
	public boolean isControllerConnected() {
		return controllerIsConnected;
	}

	/**
	 * Run Method Handles all inputs from the controller.
	 */
	@Override
	public void run() {
		while (true) {
			controller.poll();
			if (controller.getAxisValue(0) == 0	&& controller.getAxisValue(2) == 0) {
				activateController();
			}

		}
	}


	private void activateController() {
		/*double oldValueLeftWing = 0;
		double oldValueRightWing = 0;*/
		final int leftWing = 0;
		final int rightWing = 2;
		while (true) {
			controller.poll();
			if(controller.isButtonPressed(6)) {
				glider.reset();
			}
			
			Main.leftSlider.setValue((int) (controller.getAxisValue(leftWing)*Main.leftSlider.getMaximum()));
			Main.rightSlider.setValue((int) (controller.getAxisValue(rightWing)*Main.rightSlider.getMaximum()));
			/*oldValueLeftWing = controlLeftWing(oldValueLeftWing);
			oldValueRightWing = controlRightWing(oldValueRightWing);*/
		}
	}


	private double controlRightWing(double oldValueRightWing) {
		double valueRightWing = controller.getAxisValue(2);
		if (oldValueRightWing != valueRightWing) {
			if (valueRightWing >= 0) {
				glider.changeSpeed(0,-(valueRightWing - oldValueRightWing) * MULTIPLICATOR_CONTROLLER_SPEED);
				oldValueRightWing = controller.getAxisValue(2);
			}
		}
		return oldValueRightWing;
	}

	private double controlLeftWing(double oldValueLeftWing) {
		double valueLeftWing = controller.getAxisValue(0);
		if (oldValueLeftWing != valueLeftWing) {
			if (valueLeftWing >= 0) {
				glider.changeSpeed(-(valueLeftWing - oldValueLeftWing)	* MULTIPLICATOR_CONTROLLER_SPEED,0);
				oldValueLeftWing = controller.getAxisValue(0);
			}
		}
		return oldValueLeftWing;
	}

}
