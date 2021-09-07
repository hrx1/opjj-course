package hr.fer.zemris.java.hw02;

import static java.lang.Math.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class describes Complex Numbers, and offers basic calculations with them.
 * Angle is always positive.
 * 
 * @author Hrvoje
 *
 */
public class ComplexNumber {
	double real, imaginary;
	double magnitude, angle;
	public static final double TOLERANCE = 1E-6; 

	/**
	 * Creates Complex Number with real and imaginary parts.
	 * 
	 * @param real
	 *            part of a complex number
	 * @param imaginary
	 *            part of a complex number
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;

		magnitude = sqrt(pow(real, 2) + pow(imaginary, 2));

		if (abs(real) < TOLERANCE) { // ako je realan dio 0
			if (imaginary > 0)
				angle = PI / 2;
			else
				angle = 3 * PI / 2;
		} else {
			angle = atan(imaginary / real);
		}

		if (real < 0) { // popravljanje kvadranta
			angle += PI;
		}
		
		if (angle < 0) {
			angle += 2*PI;
		}
	}

	/**
	 * Creates Complex number with real part only. Imaginary = 0.
	 * 
	 * @param real
	 *            value of real part.
	 * @return Complex number with real part, and imaginary = 0.
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}

	/**
	 * Creates Complex number with imaginary part only. Real = 0.
	 * 
	 * @param imaginary
	 *            value of imaginary part.
	 * @return Complex number with imaginary part, and Real = 0.
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}

	/**
	 * Creates Complex Number from magnitude and angle values.
	 * 
	 * @param magnitude
	 *            of a complex number.
	 * @param angle
	 *            of a complex number.
	 * @return complex number with those properties.
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		return new ComplexNumber(cos(angle) * magnitude, sin(angle) * magnitude);
	}

	/**
	 * Parses complex number from a string s. Examples of valid input formats:
	 * a; ai; a + bi;
	 * (a and b are real numbers)
	 * 
	 * Method is unpredictable when other formats are given.
	 * 
	 * @param s
	 *            to be parsed.
	 * @return parsed ComplexNumber
	 */
	public static ComplexNumber parse(String s) {
		s = s.replaceAll("\\s+", "");
		Pattern patternIm = Pattern.compile("[+-]?[0-9]*[\\.]?[0-9]*[i]"); //kupi imaginaran dio
		Pattern patternRe = Pattern.compile("[+-]?[0-9]*[\\.]?[0-9]*"); //kupi realan dio
		
		String withoutIm = s.replaceAll("[+-]?[0-9]*[\\.]?[0-9]*[i]", ""); //makne imaginaran dio
		
		Matcher matchIm = patternIm.matcher(s);
		Matcher matchRe = patternRe.matcher(withoutIm);
				
		
		double real, imaginary;
		
		//pokupi imaginaran:
		if (matchIm.find()) {
			String im = matchIm.group(0).replaceAll("i", "");
			if(im.equals("-")) {
				imaginary = -1;
			}else if(im.equals("") || im.equals(".")) {
				imaginary = 1;
			}
			else {
				imaginary = Double.parseDouble(im);
			}
			
		} else {
			imaginary = 0;
		}
		
		//pokupi realan:
		if (matchRe.find() && !matchRe.group(0).equals("")) {
			String re = matchRe.group(0);
			real = Double.parseDouble(re);
		}else {
			real = 0;
		}

		
		return new ComplexNumber(real, imaginary);
	}

	/**
	 * get real part of a complex number
	 * 
	 * @return real value
	 */
	public double getReal() {
		return real;
	}

	/**
	 * getter for imaginary part
	 * 
	 * @return imaginary value
	 */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * Getter for magnitude of a complex number.
	 * 
	 * @return magnitude
	 */
	public double getMagnitude() {
		return magnitude;
	}

	/**
	 * Getter for angle of a complex number.
	 * 
	 * @return angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Adds two complex numbers. Returns result of addition.
	 * 
	 * @param c
	 *            complex number to add with
	 * @return result
	 */
	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(this.real + c.real, this.imaginary + c.imaginary);
	}

	/**
	 * Subtract two complex numbers. Returns result of subtraction.
	 * 
	 * @param c
	 *            complex number to subtract with
	 * @return result Complex number
	 */

	public ComplexNumber sub(ComplexNumber c) {
		return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
	}

	/**
	 * Multiply two complex numbers. Returns result of multiplication.
	 * 
	 * @param c
	 *            complex number to multipy with
	 * @return result Complex number
	 */
	public ComplexNumber mul(ComplexNumber c) {
		return fromMagnitudeAndAngle(abs(this.magnitude * c.magnitude), this.angle + c.angle);
	}

	/**
	 * Divide two complex numbers. Returns result of division.
	 * 
	 * @param c
	 *            complex number to divide with
	 * @return result Complex number
	 */
	public ComplexNumber div(ComplexNumber c) {
		return fromMagnitudeAndAngle(abs(this.magnitude / c.magnitude), this.angle - c.angle);
	}

	/**
	 * Raise complex number to the power n >= 0.
	 * {@link IllegalArgumentException} if n < 0.
	 * 
	 * @param n
	 *            power
	 * @return result Complex number
	 */
	public ComplexNumber power(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n must be >= 0, but given " + n);
		return fromMagnitudeAndAngle(pow(abs(this.magnitude), n), n * this.angle);
	}

	/**
	 * Calculate nth (n>0) root of a complex number. Result is an Array of a size n > 0.
	 * Throws {@link IllegalArgumentException} if n <= 0.
	 * 
	 * @param n root degree
	 * @return results complex number
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("n must be > 0, but given " + n);
		ComplexNumber[] results = new ComplexNumber[n];

		double magnitudeResult = pow(abs(this.magnitude), 1. / n);

		for (int i = 0; i < n; ++i) {
			results[i] = fromMagnitudeAndAngle(magnitudeResult, (this.angle + 2 * PI * i) / n);
		}

		return results;
	}

	@Override
	public String toString() {
		String sign = (imaginary >= 0) ? "+" : "-";

		return String.format("%f %s %fi", real, sign, Math.abs(imaginary));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(imaginary);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(real);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		ComplexNumber other = (ComplexNumber) obj;

		if (abs(this.imaginary - other.imaginary) > TOLERANCE)
			return false;
		if (abs(this.real - other.real) > TOLERANCE)
			return false;

		return true;
	}
}
