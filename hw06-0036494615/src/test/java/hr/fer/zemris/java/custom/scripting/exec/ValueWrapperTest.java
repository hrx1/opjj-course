
package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ValueWrapperTest {
	ValueWrapper v1, v2, v3, v4, vNull;
	int p1, p3;
	double p2, p4;
	
	@Before
	public void setUp() throws Exception {
		p1 = 4;
		p2 = 3.3;
		p3 = 3;
		p4 = 6.4;
		
		v1 = new ValueWrapper(String.valueOf(p1));
		v2 = new ValueWrapper(String.valueOf(p2));
		v3 = new ValueWrapper(p3);
		v4 = new ValueWrapper(p4);
		vNull = new ValueWrapper(null);
		
	}

	@Test
	public void testGetValue() {
		assertEquals(String.valueOf(p1), v1.getValue());
		assertEquals(String.valueOf(p2), v2.getValue());
		assertEquals(p3, v3.getValue());
		assertEquals(p4, v4.getValue());
		assertEquals(null, vNull.getValue());
	}

	//2 nulla
	//null + nesto
	//double + int
	//double + double
	//int + int
	@Test
	public void testAddNulls() {
		vNull.add(null);
		assertEquals(0, vNull.getValue());
	}
	@Test
	public void testAddNullAndNumber() {
		int old1 = p1;
		int old3 = p3;
		
		v1.add(null);
		v2.add(null);
		
		assertEquals(old1, v1.getValue());
		assertEquals(old3, v3.getValue());
	}
	@Test 
 	public void testAddNumbers() {
		double tmp1B = -3.1;
		int tmp2B = 3;
		
		ValueWrapper tmp1 = new ValueWrapper(tmp1B);
		ValueWrapper tmp2 = new ValueWrapper(tmp2B);
		
		tmp1.add(v1.getValue());
		tmp2.add(v2.getValue());
		
		assertEquals(p1 + tmp1B, (Double) tmp1.getValue(), 0.0001);
		assertEquals(p2 + tmp2B, (Double) tmp2.getValue(), 0.0001);
		
		ValueWrapper tmp = new ValueWrapper(tmp2B);
		tmp.add(tmp2B);
		assertEquals(tmp2B * 2, tmp.getValue());
	}
	
	@Test
	public void testSubtract() {
		ValueWrapper tmp1 = new ValueWrapper(null);
		ValueWrapper tmp2 = new ValueWrapper(p1);
		
		tmp1.subtract(null);
		assertEquals(0, tmp1.getValue());
		tmp1.subtract(p1);
		assertEquals(-p1, tmp1.getValue());
		
		tmp2.subtract(p2);
		assertEquals(p1-p2, tmp2.getValue());
	}

	@Test
	public void testMultiply() {
		ValueWrapper tmp1 = new ValueWrapper(null);
		ValueWrapper tmp2 = new ValueWrapper(p1);

		tmp1.multiply(null);
		assertEquals(0, tmp1.getValue());
		tmp1.multiply("4324");
		assertEquals(0, tmp1.getValue());
		tmp2.multiply(p4);
		assertEquals(p1 * p4, (Double) tmp2.getValue(), 0.00001);
	}
	
	@Test(expected=ArithmeticException.class)
	public void testZeroDivision() {
		ValueWrapper tmp = new ValueWrapper(p1);
		tmp.divide(null);
	}

	@Test
	public void testDivide() {
		ValueWrapper tmp1 = new ValueWrapper(null);
		ValueWrapper tmp2 = new ValueWrapper(p1);

		tmp1.divide("4324");
		assertEquals(0, tmp1.getValue());
		tmp2.divide(p4);
		assertEquals(p1 / p4, (Double) tmp2.getValue(), 0.00001);

	}
	
	@Test
	public void testNumCompare() {
		ValueWrapper v1 = new ValueWrapper("1");
		ValueWrapper v2 = new ValueWrapper(-3);
		ValueWrapper v3 = new ValueWrapper(null);
		
		assertEquals(0, v3.numCompare(0));
		assertEquals(-1, v2.numCompare(1));
		assertEquals(1, v1.numCompare(-1));

	}

}
