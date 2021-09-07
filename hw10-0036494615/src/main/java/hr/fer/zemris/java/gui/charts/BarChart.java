package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Describes BarChart with values it contains, X and Y axis descriptions, Minimum and maximum Y value, and difference between two neighboring values.
 * 
 * @author Hrvoje
 *
 */
public class BarChart {
	/* Values stored in BarChart */
	private List<XYValue> values;
	/* x-axis description */
	private String xAxisDescription;
	/* Y-axis description */
	private String yAxisDescription;
	/* Minimum Y value */
	private int minY;
	/* Maximum Y value */
	private int maxY;
	/* Difference between two neighboring values */
	private int differenceY;
	
	/**
	 * BarChart constructor
	 * 
	 * @param values which BarChar contains
	 * @param xAxisDescription X-Axis description
	 * @param yAxisDescription Y-Axis description
	 * @param minY minimum Y value
	 * @param maxY maximum Y value
	 * @param differenceY difference between two neighboring values
	 */
	public BarChart(List<XYValue> values, String xAxisDescription, String yAxisDescription, int minY, int maxY,
			int differenceY) {
		super();
		this.values = values;
		this.xAxisDescription = xAxisDescription;
		this.yAxisDescription = yAxisDescription;
		this.minY = minY;
		this.maxY = maxY;
		this.differenceY = differenceY;
	}

	/**
	 * Getter of values
	 * @return values
	 */
	public List<XYValue> getValues() {
		return values;
	}

	/**
	 * Returns X-axis description
	 * @return X-axis description
	 */
	public String getxAxisDescription() {
		return xAxisDescription;
	}

	/**
	 * Returns Y-axis description
	 * @return Y-axis description
	 */
	public String getyAxisDescription() {
		return yAxisDescription;
	}

	/**
	 * Returns minimum Y value
	 * @return minimum Y value
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Returns maximum X value
	 * @return maximum X value
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Returns difference between two neighboring values.
	 * @return difference between two neighboring values
	 */
	public int getDifferenceY() {
		return differenceY;
	}
	
	

}
