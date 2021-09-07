package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Exception thrown when lexer recognises error
 * @author Hrvoje
 *
 */
public class LexerException extends RuntimeException {
	/**
	 * Used for serialisation
	 */
	private static final long serialVersionUID = -4409870663551641730L;

	/**
	 * Standard constructor for exception
	 * @param message
	 */
	public LexerException(String message) {
		super(message);
	}
}
