package ch.zhaw.paraglider.physics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WingTest {

	private Wing wing;
	@Before
	public void init() {
		wing  = new Wing("Left");
	}
	
	@Test
	public void testVerticalSpeedByOptimalGliding() {
		assertEquals(1.08, wing.getVerticalSpeed(),0.01);
	}
	
	@Test
	public void testCurrentGlideRatioByOptimalGliding() {
		assertEquals(9, wing.getCurrentGlideRatio(), 0.01);
	}
	
	@Test
	public void testVerticalSpeedByLeastRateOfDescent() {
		wing.setCurrentSpeed(35*0.76);
		assertEquals(0.94, wing.getVerticalSpeed(),0.01);
	}
	
	@Test
	public void testCurrentGlideRatioByLeastRateOfDescent() {
		wing.setCurrentSpeed(35*0.76);
		assertEquals(7.79, wing.getCurrentGlideRatio(), 0.01);
	}
	
}
