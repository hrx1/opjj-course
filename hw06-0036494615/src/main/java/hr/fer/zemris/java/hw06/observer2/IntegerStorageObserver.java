package hr.fer.zemris.java.hw06.observer2;

/**
 * Abstracts Observer for IntegerStorage
 * 
 * @author Hrvoje
 *
 */
public interface IntegerStorageObserver {

	/**
	 * Action performed on istorage when observation is made
	 * 
	 * @param istorage
	 */
	public void valueChanged(IntegerStorageChange istorage);
}
