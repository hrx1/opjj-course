package hr.fer.zemris.java.hw05.db;

/**
 * Interface abstracts Filter of StudentRecord. Requires accepts method.
 * @author Hrvoje
 *
 */
public interface IFilter {
	
	/**
	 * Returns true if record passes filter.
	 * @param record to check
	 * @return true if record passes filter
	 */
	public boolean accepts(StudentRecord record);
}
