package hr.fer.zemris.java.custom.collections;

/**
 * Throw when stack is empty but pop or peak is called.
 * @author Hrvoje
 *
 */
public class EmptyStackException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5019575938076748881L;
	
	/**
	 * Default Constructor
	 */
	public EmptyStackException() {
		super();
	}
}
