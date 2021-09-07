package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception thrown when text cannot be parsed.
 * @author Hrvoje
 *
 */
public class SmartScriptParserException extends RuntimeException {
	
	/**
	 * serialization
	 */
	private static final long serialVersionUID = -6078437273509537532L;

	/**
	 * Constructor for exception 
	 * @param msg to be displayed
	 */
	public SmartScriptParserException(String msg) {
		super(msg);
	}
}
