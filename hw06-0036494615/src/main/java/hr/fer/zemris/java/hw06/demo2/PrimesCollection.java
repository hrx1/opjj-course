package hr.fer.zemris.java.hw06.demo2;

import java.util.Iterator;

/**
 * Class provides iteration over prime numbers. 
 * Number of prime numbers is passed in a constructor. 
 * @author Hrvoje
 *
 */
public class PrimesCollection implements Iterable<Integer> {
	/** Number of primes to generate **/
	private int numberOfPrimes;
	
	/**
	 * Constructor for prime numbers
	 * @param numberOfPrimes
	 */
	public PrimesCollection(int numberOfPrimes) {
		super();
		this.numberOfPrimes = numberOfPrimes;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new PrimeIterator();
	}

	/**
	 * Iterator for PrimeCollection. Each element is a prime number.
	 * @author Hrvoje
	 *
	 */
	private class PrimeIterator implements Iterator<Integer> {
		private int currentPrime = 1;
		private int generatedPrimes = 0;

		@Override
		public boolean hasNext() {
			return generatedPrimes < numberOfPrimes;
		}
		
		@Override
		public Integer next() {
			if (!hasNext()) {
				throw new IllegalStateException();
			}

			if (generatedPrimes < 2) {
				++currentPrime;
				++generatedPrimes;

				return currentPrime;
			}

			while (true) {
				currentPrime += 2;
				if (isPrime(currentPrime))
					break;
			}

			++generatedPrimes;
			return currentPrime;
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
