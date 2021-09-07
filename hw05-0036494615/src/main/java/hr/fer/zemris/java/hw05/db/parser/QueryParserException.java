package hr.fer.zemris.java.hw05.db.parser;

/**
 * Exception which is thrown by QueryParser if error occurs while parsing.
 * @author Hrvoje
 *
 */
public class QueryParserException extends RuntimeException {

	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 4352533213107154256L;
	
	/**
	 * Default constructor
	 */
	public QueryParserException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Default constructor with message.
	 * @param message message
	 */
	public QueryParserException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	
	
}
