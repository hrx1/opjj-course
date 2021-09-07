package hr.fer.zemris.java.hw05.db;

/**
 * Interface abstracts Comparison Operators. Requires satisfied method.
 * @author Hrvoje
 *
 */
public interface IComparisonOperator {
	
	/**
	 * Returns true if value1 and value2 satisfy some relation.
	 * @param value1 first value
	 * @param value2 second value
	 * @return true if values satisfy some relation
	 */
	public boolean satisfied(String value1, String value2);
}
