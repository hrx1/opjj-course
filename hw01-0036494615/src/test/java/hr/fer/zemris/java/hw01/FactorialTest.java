package hr.fer.zemris.java.hw01;

import static org.junit.Assert.*;

import org.junit.Test;

public class FactorialTest {

	@Test
	public void unosNegativnogBroja() {
		int broj = -5;

		assertEquals(broj, Factorial.calculateFactorial(broj)); // po dokumentaciji, metoda treba vratit ono sto je
																// primila
	}

	@Test
	public void unosNule() {
		// po definiciji, 0! = 1
		assertEquals(1, Factorial.calculateFactorial(0));
	}

	@Test
	public void unosPozitivnogBroja() {
		int broj = 10;
		int rezultat = 3628800;

		assertEquals(rezultat, Factorial.calculateFactorial(broj));
	}

	@Test
	public void unosGornjeGranice() {
		int granica = 20;
		long rezultat = 2432902008176640000l;
		
		assertEquals(rezultat, Factorial.calculateFactorial(granica));
	}
}
