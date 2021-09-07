package hr.fer.zemris.java.hw07.shell;

/**
 * Exception which is thrown when Shell is unable to write or read to the environment.
 * @author Hrvoje
 *
 */
public class ShellIOException extends RuntimeException {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2579350164985515435L;

	/**
	 * Constructor with message
	 * @param msg
	 */
	public ShellIOException(String msg) {
		super(msg);
	}

	/**
	 * Default constructor
	 */
	public ShellIOException() {
		super();
	}

}
