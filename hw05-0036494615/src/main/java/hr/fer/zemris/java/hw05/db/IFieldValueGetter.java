package hr.fer.zemris.java.hw05.db;

/**
 * Interface abstracts FieldValueGetter. Requires get method.
 * @author Hrvoje
 *
 */
public interface IFieldValueGetter {
	/**
	 * Returns Field Value of StudentRecord record
	 * @param record from which FieldValue will be taken.
	 * @return field value of student record
	 */
	public String get(StudentRecord record);
}
