package hr.fer.zemris.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class Vector3Test {
	private static final double THRESHOLD = 1e-5;
	
	Vector3 i = new Vector3(1, 0, 0);
	Vector3 j = new Vector3(0, 1, 0);
	Vector3 k = i.cross(j);
	Vector3 l = new Vector3(3, 4, 5);

	@Test
	public void testNorm() {
		assertEquals(1, i.norm(), THRESHOLD);
		assertEquals(5 * Math.sqrt(2), l.norm(), THRESHOLD);

	}

	@Test
	public void testNormalized() {
		Vector3 m = l.normalized();
		assertEquals(1, m.norm(), THRESHOLD);
	}

	@Test
	public void testAdd() {
		Vector3 res = l.add(i);
		assertEquals(new Vector3(4, 4, 5), res);
	}

	@Test
	public void testSub() {
		Vector3 res = l.sub(i);
		assertEquals(new Vector3(2, 4, 5), res);
	}

	@Test
	public void testDot() {
		double r1 = i.dot(j);
		assertEquals(0, r1, THRESHOLD);
		double r2 = l.dot(j);
		assertEquals(4, r2, THRESHOLD);
	}

	@Test
	public void testCross() {
		Vector3 v2 = new Vector3(5, 3, 1);
		assertEquals(new Vector3(11, -22, 11), v2.cross(l));
	}

	@Test
	public void testScale() {
		Vector3 r1 = l.scale(5);
		assertEquals(new Vector3(3.*5, 4.*5, 5*5), r1);
	}

	@Test
	public void testCosAngle() {
		Vector3 v2 = new Vector3(5, 3, 1);
		
		assertEquals(0.7649463, v2.cosAngle(l), THRESHOLD);
	}

}
