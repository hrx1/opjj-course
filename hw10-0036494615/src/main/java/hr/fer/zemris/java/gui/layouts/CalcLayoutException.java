package hr.fer.zemris.java.gui.layouts;

/**
 * Exception thrown from CalcLayout
 * @author Hrvoje
 *
 */
public class CalcLayoutException extends RuntimeException {
	private static final long serialVersionUID = -3503721957041493250L;
	
	/**
	 * Constructor for CalcLayoutException
	 * @param msg to show
	 */
	public CalcLayoutException(String msg) {
		super(msg);
	}	
}
