package ch.zhaw.paraglider.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.zhaw.paraglider.util.Vector;

public class VectorTest {

	Vector v = null;
	
	@Before
	public void setUp()
	{
		v = new Vector(1,2,3);
	}
	
	@After
    public void tearDown() throws Exception {   
        v = null;
    }
	
	@Test
	public void testVector() {
		assertNotNull(v);
	}

	@Test
	public void testGetX() {
		assertEquals(1,v.getX(),0.001);
	}

	@Test
	public void testSetX() {
		v.setX(2);
		assertEquals(2,v.getX(),0.001);
	}

	@Test
	public void testGetY() {
		assertEquals(2,v.getY(),0.001);
	}

	@Test
	public void testSetY() {
		v.setX(3);
		assertEquals(3,v.getX(),0.001);
	}

	@Test
	public void testGetZ() {
		assertEquals(3,v.getZ(),0.001);
	}

	@Test
	public void testSetZ() {
		v.setX(4);
		assertEquals(4,v.getX(),0.001);
	}

	@Test
	public void testAdd() {
		assertTrue(v.add(new Vector(9,4,1)).equals(new Vector(10,6,4)));
	}

	@Test
	public void testSub() {
		assertTrue(v.sub(new Vector(1,1,1)).equals(new Vector(0,1,2)));
	}
}
