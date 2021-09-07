package hr.fer.zemris.math;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ComplexTest {
	Complex c1, c2, c3, c4;
	int expectedModule1 = 5;
	int expectedModule2 = 3;
	final double THRESHOLD = 1e-5;
	

	@Before
	public void setUp() throws Exception {
		c1 = new Complex(-3, 4);
		c2 = new Complex(-3, 0);
	}

	@Test
	public void testModule() {
		assertEquals(expectedModule1, c1.module(), THRESHOLD);
		assertEquals(expectedModule2, c2.module(), THRESHOLD);
	}

	@Test
	public void testAdd() {
		Complex new1 = c1.add(c2);
		assertEquals(new Complex(c1.getReal() + c2.getReal(), c1.getImaginary() + c2.getImaginary()), new1);
	}

	@Test
	public void testSub() {
		Complex new1 = c1.sub(c2);
		assertEquals(new Complex(c1.getReal() - c2.getReal(), c1.getImaginary() - c2.getImaginary()), new1);
	}

	@Test
	public void testMultiply() {
		Complex new1 = c1.multiply(c2);
		assertEquals(new Complex(9, -12), new1);
	}

	@Test
	public void testDivide() {
		Complex new1 = c1.divide(c2);
		assertEquals(new Complex(1, -4./3), new1);
	}

	@Test
	public void testPower() {
		Complex new1 = c1.power(3);
		assertEquals(new Complex(117, 44), new1);
	}

	@Test
	public void testRoot() {
		List<Complex> new1 = c1.root(3);
		List<Complex> result = new ArrayList<>();
		result.add(new Complex(1.264953, 1.150614));
		result.add(new Complex(-1.628937, 0.520175));
		result.add(new Complex(0.363984, - 1.670788));
		
		for(Complex r : result) {
			assertTrue(new1.contains(r));
		}
		
		assertEquals(new1.size(), result.size());
	}

	@Test
	public void testNegate() {
		Complex new1 = c1.negate();
		assertEquals(new Complex(-c1.getReal(), -c1.getImaginary()), new1);

	}

}
