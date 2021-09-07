package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Class describes Complex Rooted Polynomial and provides simple operations with them.
 * 
 * Complex Rooted Polynomial is a product of (z-zi) members, where zi is i-th root of Complex Polynomial
 * 
 * @author Hrvoje
 *
 */
public class ComplexRootedPolynomial {
	
	/** Roots of ComplexRootedPolynomial **/
	private Complex[] roots;
	
	/**
	 * Constructor for ComplexRootedPolynomial
	 * @param roots of ComplexRootedPolynomial
	 */
	public ComplexRootedPolynomial(Complex ...roots) {
		this.roots = roots;
	}

	/**
	 * Returns value of ComplexRootedPolynomial in complex number z 
	 * @param z Complex Number
	 * @return value of ComplexRootedPolynomial in complex number z
	 */
	public Complex apply(Complex z) {
		Objects.requireNonNull(z);
		
		Complex result = new Complex(1, 0);
		for(Complex c : roots) {
			result.multiply(z.sub(c));
		}
		
		return result;
	}
	
	
	/**
	 * Converts ComplexRootedPolynomial to ComplexPolynomial
	 * @return ComplexPolynomial
	 */
	public ComplexPolynomial toComplexPolynom() {
		/*
		 * TRIK:
		 * zamisljam svaki root kao complex polynomial i mnozim ih
		 * samo z mijenjam s 1
		 * i na prvo mjesto stavljam root negiran zato da odrzim poredak 
		 * 
		 */
		ComplexPolynomial result;
		
		result = new ComplexPolynomial(roots[0].negate(), Complex.ONE);
		
		for(int i = 1; i < roots.length; ++i) {
			result = result.multiply(new ComplexPolynomial(roots[i].negate(), Complex.ONE));
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(roots.length * 3); //Magic number, ali jasno je da ce biti vise od 3*roots.length charova
																// za svaki broj imamo jos i zagrade
		sb.append("f(z) = ");
		for(Complex c : roots) {
			sb.append("( z - (" + c + "))");
		}
		
		return sb.toString();
	}

	/**
	 * finds index of closest root for given complex number z that is within
	 * treshold; if there is no such root, returns -1
	 *  
	 * @param z to inspect
	 * @param threshold to consider
	 * @return index of closest root or -1 if it doesn't exist
	 */
	public int indexOfClosestRootFor(Complex z, double threshold) {
		int i;
		
		for(i = 0; i < roots.length; ++i) {
			if(Complex.similar(roots[i], z, threshold)) {
				return i;
			}
			
		}
		
		return -1;
	}
}
