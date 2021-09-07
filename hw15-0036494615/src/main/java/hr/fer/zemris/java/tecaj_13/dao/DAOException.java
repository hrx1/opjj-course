package hr.fer.zemris.java.tecaj_13.dao;

/**
 * Thrown when some DAO method failes
 * @author Hrvoje
 *
 */
public class DAOException extends RuntimeException {

	/**
	 * DAO Exception with message and exception
	 * 
	 * @param string message
	 * @param ex exception
	 */
	public DAOException(String string, Exception ex) {
		super(string, ex);
	}

	private static final long serialVersionUID = 1L;

}