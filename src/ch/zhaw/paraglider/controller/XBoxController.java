package ch.zhaw.paraglider.controller;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

import ch.zhaw.paraglider.physics.Glider;
import ch.zhaw.paraglider.view.Main;

/**
 * This Class handles the input from a XBox Controller.
 * @author Christian Brüesch / Jonas Gschwend
 */
public class XBoxController implements Runnable {

	private Controller controller;
	private int positionOfXboxController = -1;
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
		initController();
		if(controllerIsConnected) {
			printControllerSpecifics();	
		}
	}
	
	public boolean isControllerConnected() {
		return controllerIsConnected;
	}

	/**
	 * Run Method handles all inputs from the controller.
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

	private void initController() {
		for(int i = 0; i<Controllers.getControllerCount();i++) {
			if(Controllers.getController(i).getName().toUpperCase().contains("Xbox".toUpperCase())) {
				positionOfXboxController = i;
			}	
		}
		if(positionOfXboxController != -1) {
			controller = Controllers.getController(positionOfXboxController);
			controllerIsConnected = true;
		}
	}

	private void printControllerSpecifics() {
				System.out.println(controller.getName());
				for(int i=0; i<controller.getAxisCount();i++)
				{
					System.out.println(controller.getAxisName(i) + " - Axis: " +i );
				}
				for(int i=0; i<controller.getButtonCount();i++)
				{
					System.out.println(controller.getButtonName(i) + " - Button: " +i );
				}
			}
			
	private void activateController() {
		final int leftWing = 0;
		final int rightWing = 2;
		while (true) {
			controller.poll();
			if(controller.isButtonPressed(6)) {
				glider.reset();
			}
			Main.leftSlider.setValue((int) (controller.getAxisValue(leftWing)*Main.leftSlider.getMaximum()));
			Main.rightSlider.setValue((int) (controller.getAxisValue(rightWing)*Main.rightSlider.getMaximum()));
		}
	}

}
