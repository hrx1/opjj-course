package hr.fer.zemris.java.gui.layouts;

/**
 * Class defines Row and Column position
 * @author Hrvoje
 *
 */
public class RCPosition {
	int row, column;

	/**
	 * Constructor for RCPosition
	 * @param row Row
	 * @param column Column
	 */
	public RCPosition(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Getter for row
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Getter for column
	 * @return column
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RCPosition other = (RCPosition) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
}
