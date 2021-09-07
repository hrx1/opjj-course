package hr.fer.zemris.math;

public class ComplexRootedPolynomialTest {
	public static void main(String[] args) {
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(new Complex(2, 3), new Complex(3, 4), new Complex(-1, -2), new Complex(-3, 3));
		System.out.println(crp);
		System.out.println(crp.toComplexPolynom()); 
		/*
		 * Wolfram kaze da valja:
		 * http://www.wolframalpha.com/input/?i=(+z+-+(2.000000+%2B+3.000000i))(+z+-+(3.000000+%2B+4.000000i))(+z+-+(-1.000000+-+2.000000i))(+z+-+(-3.000000+%2B+3.000000i))
		 */
	}
}
