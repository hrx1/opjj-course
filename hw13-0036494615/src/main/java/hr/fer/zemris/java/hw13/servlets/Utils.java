package hr.fer.zemris.java.hw13.servlets;

/**
 * General utility functions.
 * 
 * @author Hrvoje
 *
 */
public class Utils {
	
	/**
	 * Returns integer representation of A if it is possible, otherwise returns i
	 * @param A String
	 * @param i default value
	 * @return integer representation of A if it is possible, otherwise returns i
	 */
	public static int intOrDefault(String A, int i) {
		if(A == null) return i;
		try{
			return Integer.parseInt(A);
		} catch(NumberFormatException e) {
			return i;
		}
	}

}
