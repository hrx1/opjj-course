package hr.fer.zemris.java.hw06.demo2;

/**
 * Demo which shows for loop nested in a for loop. Copied from homework.
 * @author Hrvoje
 *
 */
public class PrimesDemo1 {
	
	/**
	 * Main method taken from homework
	 * @param args neglected
	 */
	public static void main(String[] args) {
		
		PrimesCollection primesCollection = new PrimesCollection(2);
		
		for (Integer prime : primesCollection) {
			for (Integer prime2 : primesCollection) {
				System.out.println("Got prime pair: " + prime + ", " + prime2);
			}
		}
		
	}

}
