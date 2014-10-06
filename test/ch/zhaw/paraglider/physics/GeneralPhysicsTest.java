package ch.zhaw.paraglider.physics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GeneralPhysicsTest {

	private GeneralPhysics generalPhysics;
	@Before
	public void init() {
		generalPhysics  = GeneralPhysics.getInstance();
		//Set to default after every Test
		generalPhysics.setWEIGHT_OF_EQUIPMENT(20);
		generalPhysics.setWEIGHT_OF_PILOT(85);
	}
	
	@Test
	public void testStandardGetFg() {
		assertEquals(1030.05, generalPhysics.getFg(), 0.01);
	}
	
	@Test
	public void testVariableGetFg() {
		generalPhysics.setWEIGHT_OF_EQUIPMENT(15);
		generalPhysics.setWEIGHT_OF_PILOT(80);
		assertEquals(931.95, generalPhysics.getFg(), 0.01);	
	}
}
