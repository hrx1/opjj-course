package hr.fer.zemris.java.hw02;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ComplexNumberTest {
	
	ComplexNumber first, second, third, fourth;
	double real, imaginary;
	double angle, magnitude;
	
	@Before
	public void setUp() {
		
		real = 2;
		imaginary = 3.3;
		magnitude = 3.8587563;
		angle = 1.0259324;
		
		first = new ComplexNumber(real, imaginary);
		second = new ComplexNumber(-real, imaginary);
	 	third = new ComplexNumber(-real, -imaginary);
	 	fourth = new ComplexNumber(real, -imaginary);
	}
	
	@Test
	public void testFromReal() {
		double real = 3;
		ComplexNumber c = ComplexNumber.fromReal(real);
		
		assertEquals(real, c.getReal(), ComplexNumber.TOLERANCE);
		assertEquals(0, c.getImaginary(), ComplexNumber.TOLERANCE);
		
	}

	@Test
	public void testFromImaginary() {
		double imaginary = 3;
		ComplexNumber c = ComplexNumber.fromImaginary(imaginary);
		
		assertEquals(imaginary, c.getImaginary(), ComplexNumber.TOLERANCE);
		assertEquals(0, c.getReal(), ComplexNumber.TOLERANCE);
	}

	@Test
	public void testFromMagnitudeAndAngle() {
		assertEquals(first, ComplexNumber.fromMagnitudeAndAngle(magnitude, angle));
		assertEquals(second, ComplexNumber.fromMagnitudeAndAngle(magnitude, second.getAngle()));
		assertEquals(third, ComplexNumber.fromMagnitudeAndAngle(magnitude, third.getAngle()));
		assertEquals(fourth, ComplexNumber.fromMagnitudeAndAngle(magnitude, fourth.getAngle()));
	}

	@Test
	public void testParse() {
		/*
		 *  (accepts strings such as: "3.51", "-3.17", "-2.71i", "i", "1", "-2.71-3.15i"),
		 */
		String[] strings = {"3.51", "-3.17", "-2.71i", "i", "1", "-2.71-3.15i"};
		ComplexNumber [] expected = {
				new ComplexNumber(3.51, 0),
				new ComplexNumber(-3.17, 0),
				new ComplexNumber(0, -2.71),
				new ComplexNumber(0, 1),
				new ComplexNumber(1, 0),
				new ComplexNumber(-2.71, -3.15)
				};
		
				for(int i = 0; i < expected.length; ++i) {
					assertEquals(expected[i], ComplexNumber.parse(strings[i]));
				}
		
		
	}

	@Test
	public void testGetMagnitude() {
		assertEquals(first.getMagnitude(), magnitude, ComplexNumber.TOLERANCE);
		assertEquals(second.getMagnitude(), magnitude, ComplexNumber.TOLERANCE);
		assertEquals(third.getMagnitude(), magnitude, ComplexNumber.TOLERANCE);
		assertEquals(fourth.getMagnitude(), magnitude, ComplexNumber.TOLERANCE);
	}

	@Test
	public void testGetAngle() {
		assertEquals(first.getAngle(), angle, ComplexNumber.TOLERANCE);
		assertEquals(second.getAngle(), Math.PI - angle, ComplexNumber.TOLERANCE);
		assertEquals(third.getAngle(), Math.PI +  angle, ComplexNumber.TOLERANCE);
		assertEquals(fourth.getAngle(), 2*Math.PI - angle, ComplexNumber.TOLERANCE);
	}

	@Test
	public void testAdd() {
		assertEquals(first.add(second), new ComplexNumber(0, 2 * imaginary));
		assertEquals(first.add(third), new ComplexNumber(0, 0));
		assertEquals(second.add(second), new ComplexNumber(-2*real, 2 * imaginary));
		assertEquals(first.add(new ComplexNumber(1.1, 2.2)), new ComplexNumber(1.1 + real, 2.2 + imaginary));

	}

	@Test
	public void testSub() {
		assertEquals(first.sub(second), new ComplexNumber(2*real, 0));
		assertEquals(first.sub(third), new ComplexNumber(2*real, 2 * imaginary));
		assertEquals(second.sub(second), new ComplexNumber(0, 0));
		assertEquals(first.sub(new ComplexNumber(1.1, 2.2)), new ComplexNumber(real - 1.1, imaginary - 2.2));
	}

	@Test
	public void testMul() {
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(magnitude * magnitude, 2*angle), first.mul(first));
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(magnitude * 2, angle + Math.PI), first.mul(ComplexNumber.fromMagnitudeAndAngle(2, Math.PI)));
	}

	@Test
	public void testDiv() {
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(1, 0), first.div(first));
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(magnitude / 2, angle - Math.PI), first.div(ComplexNumber.fromMagnitudeAndAngle(2, Math.PI)));
	}

	@Test
	public void testPower() {
		int n = 2;
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(Math.pow(magnitude, n), n*angle), first.power(n));
	}

	@Test
	public void testRoot() {
		int n = 2;
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(Math.pow(magnitude, 1./n), angle/n), first.root(n)[0]);
	}

}
