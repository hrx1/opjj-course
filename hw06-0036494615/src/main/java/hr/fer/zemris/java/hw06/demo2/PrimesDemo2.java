package hr.fer.zemris.java.hw06.demo2;

/**
 * Demo taken from homework
 * @author Hrvoje
 *
 */
public class PrimesDemo2 {
	
	/**
	 * Main method taken from homework
	 * @param args neglected
	 */
	public static void main(String[] args) {
		PrimesCollection primes = new PrimesCollection(581);
		
		int i = 1;
		for(int prime : primes) {
			System.out.println(i + ". " + prime);
			++i;
		}
	}

}
