package hr.fer.zemris.java.hw05.db;

/**
 * Class provides getters of field values for StudentRecord.
 * @author Hrvoje
 *
 */
public class FieldValueGetters {
	/** Getter for firstName **/
	public static IFieldValueGetter FIRST_NAME = (o -> o.getFirstName());
	/** Getter for lastName **/
	public static IFieldValueGetter LAST_NAME = (o -> o.getLastName());
	/** Getter for jmbag **/
	public static IFieldValueGetter JMBAG  = (o -> o.getJmbag());
	
	/**
	 * Getter for FieldValue getter which is represented by fieldValueGetter String.
	 * Returns null if there is no such match.
	 * @param fieldValueGetter 
	 * @return IFieldValueGetter which matches fieldValueGetter string
	 */
	public static IFieldValueGetter getFieldValueGetter(String fieldValueGetter) {
		switch (fieldValueGetter) {
		case "firstName":
			return FIRST_NAME;
		case "lastName":
			return LAST_NAME;
		case "jmbag":
			return JMBAG;
		default:
			return null;
		}
	}
}
