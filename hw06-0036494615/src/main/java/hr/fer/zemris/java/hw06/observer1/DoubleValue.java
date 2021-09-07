package hr.fer.zemris.java.hw06.observer1;

/**
 * Observer which doubles current stored value in IntegerStorage,
 * n times before it dereferences from IntegerStorage.
 * @author Hrvoje
 *
 */
public class DoubleValue implements IntegerStorageObserver {
	/** count calls left **/
	private int callsLeft;

	/**
	 * Constructor for DoubleValue which initializes number of calls.
	 * @param numberOfCalls
	 */
	public DoubleValue(int numberOfCalls) {
		callsLeft = numberOfCalls;
	}
	
	@Override
	public void valueChanged(IntegerStorage istorage) {
		
		System.out.format("Double value: %d\n", istorage.getValue() * 2);
		
		--callsLeft;
		if(callsLeft == 0) {
			istorage.removeObserver(this);
		}
		
	}

}
