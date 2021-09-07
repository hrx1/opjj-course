package hr.fer.zemris.java.hw06.observer1;

/**
 * Observer which counts number of changes made on IntegerStorage and prints number of tracked changes.
 * @author Hrvoje
 *
 */
public class ChangeCounter implements IntegerStorageObserver {
	 /** counter **/
	private int counter = 0;
	
	@Override
	public void valueChanged(IntegerStorage istorage) {
		++counter;
		System.out.println("Number of value changes since tracking: " + counter);
	}
	
	/**
	 * Returns counter
	 * @return counter
	 */
	public int getCounter() {
		return counter;
	}

}
