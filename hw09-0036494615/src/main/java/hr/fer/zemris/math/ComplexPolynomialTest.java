package hr.fer.zemris.math;

public class ComplexPolynomialTest {
	
	public static void main(String[] args) {
		ComplexPolynomial cp = new ComplexPolynomial(new Complex(2, 3), new Complex(3, 4), new Complex(-1, -2), new Complex(-3, 3));
		
		System.out.println(cp);
		System.out.println(cp.derive());
		
		System.out.println(cp.derive().multiply(cp));
		System.out.println(cp.derive().multiply(cp).apply(new Complex(1, 1))); //valja, kaze wolframAlpha
		
		System.out.println(cp.derive().multiply(cp).apply(new Complex(3, -2))); //valja, kaze wolframAlpha
		
		
//		System.out.println(cp.apply(new Complex(1, 0)));
	}
	
}
