package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Exception which lexer throws if error occurs.
 * @author Hrvoje
 *
 */
public class LexerException extends RuntimeException {

	/**
	 * Serialization number
	 */
	private static final long serialVersionUID = -152329139363238521L;

	/**
	 * Constructor for lexer exception
	 * @param string message
	 */
	public LexerException(String string) {
		super(string);
	}

}
