package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Class provides some Utility methods used for String Statistics.
 * 
 * @author Hrvoje
 *
 */
public class TextUtilities {
	/**
	 * Returns number of characters in a String
	 * @param string to observe
	 * @return number of characters in a String
	 */
	public static int numberOfCharacters(String string) {
		return string.length();
	}
	
	/**
	 * Returns number of non blank characters in a String
	 * @param string to observe
	 * @return number of non blank characters in a String
	 */
	public static int numberOfNonBlank(String string) {
		return (int) string.chars().filter(c -> !Character.isWhitespace(c)).count();
	}

	/**
	 * Returns number of lines in a String
	 * @param string to observe
	 * @return number of lines in a String
	 */
	public static int numberOfLines(String string) {
		return (int) (string.chars().filter(c -> c == '\n').count()) + 1;
	}
}
