package hr.fer.zemris.math;
//package hr.fer.zemris.math;
//
//import static java.lang.Math.PI;
//import static java.lang.Math.abs;
//import static java.lang.Math.atan;
//import static java.lang.Math.cos;
//import static java.lang.Math.pow;
//import static java.lang.Math.sin;
//import static java.lang.Math.sqrt;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class Complex_old {
//	private double real, imaginary;
//	private double magnitude, angle;
//	private static final double TOLERANCE = 1E-6; 
//	
//	public static final Complex ONE = new Complex(1,0);
//	public static final Complex ONE_NEG = new Complex(-1,0);
//	public static final Complex IM = new Complex(0,1);
//	public static final Complex IM_NEG = new Complex(0,-1);
//
//	/**
//	 * Creates Complex Number with real and imaginary parts.
//	 * 
//	 * @param real
//	 *            part of a complex number
//	 * @param imaginary
//	 *            part of a complex number
//	 */
//	
//	/*
//	 * TODO cache:
//	 * public static final Complex ZERO = new Complex(0,0);
//	 */
//	public Complex_old(double real, double imaginary) {
//		this.real = real;
//		this.imaginary = imaginary;
//
//		magnitude = sqrt(pow(real, 2) + pow(imaginary, 2));
//
//		if (abs(real) < TOLERANCE) { // ako je realan dio 0
//			real = 0;
//			
//			if (imaginary > 0)
//				angle = PI / 2;
//			else
//				angle = 3 * PI / 2;
//		} else {
//			angle = atan(imaginary / real);
//		}
//
//		if (real < 0) { // popravljanje kvadranta
//			angle += PI;
//		}
//		
//		if (angle < 0) {
//			angle += 2*PI;
//		}
//	}
//
//	
//
//	/**
//	 * get real part of a complex number
//	 * 
//	 * @return real value
//	 */
//	public double getReal() {
//		return real;
//	}
//
//	/**
//	 * getter for imaginary part
//	 * 
//	 * @return imaginary value
//	 */
//	public double getImaginary() {
//		return imaginary;
//	}
//
//	/**
//	 * Getter for magnitude of a complex number.
//	 * 
//	 * @return magnitude
//	 */
//	public double module() {
//		return magnitude;
//	}
//
//	/**
//	 * Getter for angle of a complex number.
//	 * 
//	 * @return angle
//	 */
//	public double getAngle() {
//		return angle;
//	}
//
//	/**
//	 * Adds two complex numbers. Returns result of addition.
//	 * 
//	 * @param c
//	 *            complex number to add with
//	 * @return result
//	 */
//	public Complex add(Complex c) {
//		return new Complex(this.real + c.real, this.imaginary + c.imaginary);
//	}
//
//	/**
//	 * Subtract two complex numbers. Returns result of subtraction.
//	 * 
//	 * @param c
//	 *            complex number to subtract with
//	 * @return result Complex number
//	 */
//
//	public Complex sub(Complex c) {
//		return new Complex(this.real - c.real, this.imaginary - c.imaginary);
//	}
//
//	/**
//	 * Multiply two complex numbers. Returns result of multiplication.
//	 * 
//	 * @param c
//	 *            complex number to multipy with
//	 * @return result Complex number
//	 */
//	public Complex multiply(Complex c) {
////		return fromMagnitudeAndAngle(abs(this.magnitude * c.magnitude), this.angle + c.angle);
//		return new Complex(this.real * c.real - this.imaginary * c.imaginary, this.real * c.imaginary + this.imaginary * c.real);
//	}
//
//	/**
//	 * Divide two complex numbers. Returns result of division.
//	 * 
//	 * @param c
//	 *            complex number to divide with
//	 * @return result Complex number
//	 */
//	public Complex divide(Complex c) {
//		return fromMagnitudeAndAngle(abs(this.magnitude / c.magnitude), this.angle - c.angle);
//	}
//
//	/**
//	 * Raise complex number to the power n >= 0.
//	 * {@link IllegalArgumentException} if n < 0.
//	 * 
//	 * @param n
//	 *            power
//	 * @return result Complex number
//	 */
//	public Complex power(int n) {
//		if (n < 0)
//			throw new IllegalArgumentException("n must be >= 0, but given " + n);
//		return fromMagnitudeAndAngle(pow(abs(this.magnitude), n), n * this.angle);
//	}
//
//	/**
//	 * Calculate nth (n>0) root of a complex number. Result is an Array of a size n > 0.
//	 * Throws {@link IllegalArgumentException} if n <= 0.
//	 * 
//	 * @param n root degree
//	 * @return results complex number
//	 */
//	public List<Complex> root(int n) {
//		
//		if (n <= 0)
//			throw new IllegalArgumentException("n must be > 0, but given " + n);
//		
//		List<Complex> results = new ArrayList<>(n);
//
//		double magnitudeResult = pow(abs(this.magnitude), 1. / n);
//
//		for (int i = 0; i < n; ++i) {
//			results.add(fromMagnitudeAndAngle(magnitudeResult, (this.angle + 2 * PI * i) / n));
//		}
//
//		return results;
//	}
//	
//	public Complex negate() {
//		return new Complex(-real, -imaginary);
//	}
//	
//	/**
//	 * Includes threshold TODO
//	 * @param c1
//	 * @param c2
//	 * @param threshold
//	 * @return
//	 */
//	public static boolean similar(Complex c1, Complex c2, double threshold) {
//		if(Math.abs(c1.real - c2.real) > threshold) return false;
//		if(Math.abs(c1.imaginary - c2.imaginary) > threshold) return false;
//		
//		return true;
//	}
//
//	public static boolean modulSimilar(Complex c1, Complex c2, double threshold) {
//		double realDiff = c1.real - c2.real;
//		double imDiff = c1.imaginary - c2.imaginary;
//		return  realDiff * realDiff + imDiff * imDiff <= threshold*threshold;
//	}
//	
//	/**
//	 * Creates Complex Number from magnitude and angle values.
//	 * 
//	 * @param magnitude
//	 *            of a complex number.
//	 * @param angle
//	 *            of a complex number.
//	 * @return complex number with those properties.
//	 */
//	private static Complex fromMagnitudeAndAngle(double magnitude, double angle) {
//		return new Complex(cos(angle) * magnitude, sin(angle) * magnitude);
//	}
//	
//	@Override
//	public String toString() {
//		String sign = (imaginary >= 0) ? "+" : "-";
//
//		return String.format("%f %s %fi", real, sign, Math.abs(imaginary));
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		long temp;
//		temp = Double.doubleToLongBits(imaginary);
//		result = prime * result + (int) (temp ^ (temp >>> 32));
//		temp = Double.doubleToLongBits(real);
//		result = prime * result + (int) (temp ^ (temp >>> 32));
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//
//		Complex other = (Complex) obj;
//
//		if (abs(this.imaginary - other.imaginary) > TOLERANCE)
//			return false;
//		if (abs(this.real - other.real) > TOLERANCE)
//			return false;
//
//		return true;
//	}
//
//}
