package hr.fer.zemris.java.gui.charts;

/**
 * XYValue is described with X and Y value
 * @author Hrvoje
 *
 */
public class XYValue {
	/* Values which describe XYValue */
	private int x, y;

	/**
	 * Constructor for XYValue
	 * @param x value
	 * @param y value
	 */
	public XYValue(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter for x
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/** 
	 * Getter for y
	 * @return y
	 */
	public int getY() {
		return y;
	}
	
	
}
