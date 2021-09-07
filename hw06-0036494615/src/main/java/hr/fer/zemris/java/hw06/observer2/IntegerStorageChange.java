package hr.fer.zemris.java.hw06.observer2;

/**
 * Class describes change on IntegerStorage. 
 * Holds data about previous value, current value, and reference to integerStorage.
 * 
 * @author Hrvoje
 */
public class IntegerStorageChange {
	/** Reference on IntegerStorage which is tracked **/
	private IntegerStorage integerStorage;
	/** Old Value **/
	private int oldValue;
	/** New Value **/
	private int newValue;
	
	/**
	 * Constructor for IntegerStorageChange
	 * @param integerStorage which is tracked
	 * @param oldValue old value
	 * @param newValue new value
	 */
	public IntegerStorageChange(IntegerStorage integerStorage, int oldValue, int newValue) {
		super();
		this.integerStorage = integerStorage;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	/**
	 * Returns tracked integer storage
	 * @return tracked integer storage
	 */
	public IntegerStorage getIntegerStorage() {
		return integerStorage;
	}
	
	/**
	 * Returns old value.
	 * @return old value
	 */
	public int getOldValue() {
		return oldValue;
	}
	
	/**
	 * Returns new value;
	 * @return new value
	 */
	public int getNewValue() {
		return newValue;
	}
	
	
	
}
