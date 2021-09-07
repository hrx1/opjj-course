package hr.fer.zemris.java.gui.prim;

import java.util.LinkedList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Class defines Prime numbers model list.
 * @author Hrvoje
 *
 */
public class PrimListModel implements ListModel<Integer> {
	/** Listeners */
	private List<ListDataListener> listeners;
	/** Generated prime numbers */
	private List<Integer> generated;
	
	/**
	 * Default Constructor
	 */
	public PrimListModel() {
		super();
		listeners = new LinkedList<>();
		generated = new LinkedList<>();
	}

	@Override
	public void addListDataListener(ListDataListener listener) {
		listeners.add(listener);
	}

	@Override
	public Integer getElementAt(int index) {
		return generated.get(index);
	}

	@Override
	public int getSize() {
		return generated.size();
	}

	@Override
	public void removeListDataListener(ListDataListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Calculates next prime number and notifies listeners that change is made.
	 */
	public void next() {
		
		if(generated.size() == 0) {
			generated.add(2);
			notifyListeners();
			return ;
		}
		if(generated.size() == 1) {
			generated.add(3);
			notifyListeners();
			return ;
		}
		
		int currentPrime = generated.get(generated.size() - 1);
		while (true) {
			currentPrime += 2;
			if (isPrime(currentPrime))
				break;
		}
		
		generated.add(currentPrime);
		notifyListeners();
	}
	
	/**
	 * Notifies listeners that change is made
	 */
	private void notifyListeners() {
		int position = generated.size();
		
		ListDataEvent dataChanged = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, position, position);
		
		for(ListDataListener listener : listeners) {
			listener.intervalAdded(dataChanged);
		}
	}
	

	/**
	 * Returns <code>true</code> if number is prime number, <code>false</code> otherwise.
	 * @param number to test.
	 * @return <code>true</code> if number is prime number, <code>false</code> otherwise.
	 */
	private static boolean isPrime(int number) {
		if (number == 1) return false;
		if (number == 2) return true;
		
		if (number % 2 == 0)
			return false;

		// ako je number u int, onda je i njegov korijen sigurno
		int upperLimit = (int) (Math.round(Math.sqrt(number)) + 1); 
		
		for (int i = 3; i < upperLimit; i += 2) {
			if (number % i == 0)
				return false;
		}

		return true;
	}

}
