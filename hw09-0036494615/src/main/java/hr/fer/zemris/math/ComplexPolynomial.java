package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Class describes Complex Polynomials and provides simple operations with them.
 * Complex Polynomial is composed of a sum whose members are zk * z^k, where zk is k-th coefficient.
 * 
 * @author Hrvoje
 *
 */
public class ComplexPolynomial {
	/** factors of complex polynomial **/
	Complex[] factors;
	
	/**
	 * Constructor for ComplexPolynomial defined with it's factors
	 * @param factors
	 */
	public ComplexPolynomial(Complex ...factors) {
		Objects.requireNonNull(factors);
		
		this.factors = factors;
	}
	/**
	 * Returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
	 * @return order of this polynom
	 */
	public short order() {
		return (short) (factors.length - 1);
	}
	
	/**
	 * Computes a new polynomial this*p
	 * @param p to multiply with
	 * @return new polynomial this * p
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Objects.requireNonNull(p);
		
		int firstOrder = this.factors.length;
		int secondOrder = p.factors.length;
		
		short newSize = (short) (firstOrder + secondOrder - 1);
		Complex[] newFactors = new Complex[newSize];

		
		for(int i = 0; i < firstOrder; ++i) { 
			for(int j = 0; j < secondOrder; ++j) {
				if(newFactors[i + j] == null) {
					newFactors[i + j] = factors[i].multiply(p.factors[j]);
				}
				else {
					newFactors[i+j] = newFactors[i + j].add(factors[i].multiply(p.factors[j]));
				}
				
			}
			
		}
		
		return new ComplexPolynomial(newFactors);
	}
	/**
	 * Computes first derivative of this polynomial.
	 * For example, for (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
	 * 
	 * @return first derivative of this polynomial
	 */
	public ComplexPolynomial derive() {
		Complex[] newFactors = new Complex[factors.length - 1];
		
		for (int i = 0; i < factors.length - 1; ++i) {
			newFactors[i] = factors[i + 1].multiply(new Complex(i + 1, 0)); 
		}
		
		return new ComplexPolynomial(newFactors);
	}


	/**
	 * Returns value of ComplexPolynomial in complex number z 
	 * @param z Complex Number
	 * @return value of ComplexPolynomial in complex number z
	 */
	public Complex apply(Complex z) {;
		Complex currentZ = z;
		
		Complex result = factors[0];
		
		for(int i = 1; i < factors.length; ++i) {
			result = result.add(currentZ.multiply(factors[i]));
			currentZ = currentZ.multiply(z);
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(factors.length * 3); // Magic number, ali jasno je da ce biti vise od
																// 3*roots.length charova
																// za svaki broj imamo jos i zagrade

		sb.append("f(z) = ");
		for (int i = 0; i < factors.length; ++i) {
			sb.append(" z^" + i +"*(" + factors[i] + ") + ");
		}
		
		sb.setLength(sb.length() - 3);
		
		return sb.toString();
	}
}
