package hr.fer.zemris.java.hw03.prob1;

/**
 * Class defines exception which will be thrown if lexer cannot create a token.
 * @author Hrvoje
 *
 */
public class LexerException extends RuntimeException {
	
	/** Serialization */
	private static final long serialVersionUID = -1173741942168406063L;
	
	/**
	 * Constructor for exception.
	 * @param message to be shown.
	 */
	public LexerException(String message) {
		super(message);
	}
}
