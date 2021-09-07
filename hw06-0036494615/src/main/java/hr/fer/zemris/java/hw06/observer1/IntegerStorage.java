package hr.fer.zemris.java.hw06.observer1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class holds int value and accepts observers which are called when Storage value is changed.
 * @author Hrvoje
 *
 */
public class IntegerStorage {
	/** Internal value **/
	private int value;
	/** List of observers **/
	private List<IntegerStorageObserver> observers; 

	/**
	 * Constructor for integer storage which sets value to initialValue
	 * @param initialValue
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
		observers = new ArrayList<>();
	}

	/**
	 * Adds observer to integer storage
	 * @param observer to add
	 * @throws NullPointerException if observer is <code>null</code>
	 */
	public void addObserver(IntegerStorageObserver observer) {
		Objects.requireNonNull(observer);
		observers.add(observer);
	}

	/**
	 * Removes observer from IntegerStorage if observer is present.
	 * @param observer to remove
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		observers.remove(observer);
	}

	/**
	 * Clears all observers.
	 */
	public void clearObservers() {
		observers.clear();
	}
	
	/**
	 * Returns stored value.
	 * @return stored value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets value
	 * @param value to set
	 */
	public void setValue(int value) {
		// Only if new value is different than the current value:
		if (this.value != value) {
			// Update current value
			this.value = value;
			
			// Notify all registered observers
			if (observers != null && observers.size() > 0) {
				
				List<IntegerStorageObserver> tmp = new ArrayList<>(observers);
				for (IntegerStorageObserver observer : tmp) {
					observer.valueChanged(this);
				}
			}
			
		}
		
	}
}
