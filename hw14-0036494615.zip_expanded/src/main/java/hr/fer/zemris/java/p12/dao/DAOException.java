package hr.fer.zemris.java.p12.dao;

/**
 * Exception thrown when underlying data storage structure throws Exception.
 * 
 * @author Hrvoje
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor
	 */
	public DAOException() {
	}

	/**
	 * Constructor for DAOExeption
	 * 
	 * @param message of exception
	 * @param cause of exception
	 * @param enableSuppression 
	 * @param writableStackTrace
	 */
	public DAOException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructor for DAOExeption
	 * 
	 * @param message of exception
	 * @param cause of exception
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for DAOException
	 * 
	 * @param message message
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Constructor for DAOExcpetion
	 * 
	 * @param cause Cause of exception
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}
