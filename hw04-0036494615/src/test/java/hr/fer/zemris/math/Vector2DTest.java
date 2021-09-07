package hr.fer.zemris.math;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Vector2DTest {
	Vector2D v1, v2, v3;
	
	
	@Before
	public void setUp() {
		v1 = new Vector2D(5, 0);
		v2 = new Vector2D(3, 4);
		v3 = new Vector2D(5, 6);
	}
	
	@Test
	public void testTranslated() {
		double eX1 = v3.getX() + v2.getX();
		double eY1 = v3.getY() + v2.getY();
		
		Vector2D v1t = v3.translated(v2);
		
		assertEquals(eX1, v1t.getX(), 0.0001);
		assertEquals(eY1, v1t.getY(), 0.0001);
		
		
	}

	@Test
	public void testRotated() {
		Vector2D v1r = v1.rotated(30);
		Vector2D v2r = v2.rotated(-30);
		Vector2D v3r = v1.rotated(90);
		
		assertEquals(5, Math.sqrt(Math.pow(v1r.getX(), 2) + Math.pow(v1r.getY(), 2)), 0.0001);
		assertEquals(5, Math.sqrt(Math.pow(v2r.getX(), 2) + Math.pow(v2r.getY(), 2)), 0.0001);
		
		assertEquals(0, v3r.getX(), 0.0001);
		assertEquals(5, v3r.getY(), 0.0001);
	}

	@Test
	public void testScaled() {
		fail("Not yet implemented");
	}

}
