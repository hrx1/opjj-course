package hr.fer.zemris.java.hw06.observer2;

/**
 * Observer which counts number of changes made on IntegerStorage
 * @author Hrvoje
 *
 */
public class ChangeCounter implements IntegerStorageObserver {
	 /** counter **/
	private int counter = 0;
	
	@Override
	public void valueChanged(IntegerStorageChange istorage) {
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
