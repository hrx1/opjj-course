package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Class describes basic Comparison Operators which implement IComparisonOperator.
 * LESS, LESS_OR_EQUALS, GREATER, GREATER_OR_EQUALS, EQUALS, NOT_EQUALS and LIKE.
 * 
 * Their String representations (respectively):
 * <, <=, >, >=, =, !=, LIKE
 * 
 * Each Comparison Operator uses compareTo method, except LIKE.
 * 
 * LIKE checks whether String matches Pattern. Pattern can contain one WILD_CARD.
 * 
 * @author Hrvoje
 *
 */

public class ComparisonOperators {
	/** Wild card symbol **/
	private static final char WILD_CARD = '*';
	
	/** checks whether o1 is less then o2 **/
	public static IComparisonOperator LESS = ((o1, o2) -> o1.compareTo(o2) < 0);
	
	/** checks whether o1 is less or equal then o2 **/
	public static IComparisonOperator LESS_OR_EQUALS = ((o1, o2) -> o1.compareTo(o2) <= 0);
	
	/** checks whether o1 is greater then o2 **/
	public static IComparisonOperator GREATER = ((o1, o2) -> o1.compareTo(o2) > 0);
	
	/** checks whether o1 is greater or equal o2 **/
	public static IComparisonOperator GREATER_OR_EQUALS = ((o1, o2) -> o1.compareTo(o2) >= 0);
	
	/** checks whether o1 equals o2 **/
	public static IComparisonOperator EQUALS = ((o1, o2) -> o1.equals(o2));
	
	/** checks whether o1 is not equal to o2 **/
	public static IComparisonOperator NOT_EQUALS = ((o1, o2) -> !o1.equals(o2));
	
	/** checks whether string matches pattern **/
	public static IComparisonOperator LIKE = ((string, pattern) -> like(string, pattern));

	/**
	 * Returns <code>true</code> if string matches pattern. 
	 * 
	 * Pattern can contain a wildcard * .
	 * This character, if present, can occur at most once, but it can be at the
	 * beginning, at the end or somewhere in the middle). If user enters more
	 * wildcard characters, throw an exception (and catch it where appropriate and
	 * write error message to user; don't terminate the program).
	 * 
	 * @param string to check
	 * @param pattern to match
	 * @throws IllegalArgumentException if pattern contains two or more wildcards
	 * @return <code>true</code> if string matches pattern
	 */
	public static boolean like(String string, String pattern) {
		Objects.requireNonNull(string, "String cannot be null");
		Objects.requireNonNull(pattern, "Pattern cannot be null");
		
		int wildCardIndex = pattern.indexOf(WILD_CARD);
		
		//TODO
		/*	query lastName LIKE "* ovo je korektan query u ovom rješenju, a ne bi trebao biti 
		 * (isto kao i query lastName LIKE "***, a i query lastName LIKE "******").
		 */
		
		if(wildCardIndex == -1) {
			return string.equals(pattern);
		}
		if(wildCardIndex == 0) {
			return string.endsWith(pattern.substring(1));
		}
		if(wildCardIndex == pattern.length()-1) {
			return string.startsWith(pattern.substring(0, pattern.length() - 1));
		}
		else {
			String[] parts = pattern.split("\\*");
			if(parts.length != 2) throw new IllegalArgumentException("Invalid pattern. Pattern can only have 1 wildcard " + WILD_CARD);
			
			return string.startsWith(parts[0]) && string.substring(parts[0].length()).endsWith(parts[1]);
		}
	}
	
	/**
	 * Returns comparison operator which matches it's string representation comparisonOperator. <code>null</code> is returned if no match is found.
	 * @param comparisonOperator string representation of some comparison operator.
	 * @return
	 */
	public static IComparisonOperator getComparisonOperator(String comparisonOperator) {

		switch(comparisonOperator) {
			case "<":
				return LESS;
			case "<=":
				return LESS_OR_EQUALS;
			case ">":
				return GREATER;
			case ">=":
				return GREATER_OR_EQUALS;
			case "=":
				return EQUALS;
			case "!=":
				return NOT_EQUALS;
			case "LIKE":
				return LIKE;
			default:
				return null;
		}
	}
}
