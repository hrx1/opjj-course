package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Class provides simple operations with factorials.
 * 
 * @author Hrvoje
 *
 */
public class Factorial {

	/**
	 * Calculates factorial of a integer within range of 1 to 20. Integer is
	 * obtained from standard input. Program terminates if string "kraj" is written.
	 * 
	 * @param args
	 *            not in use
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		String inputLine;
		int inputNumber;

		while (true) {
			System.out.print("Unesite broj > ");
			inputLine = sc.nextLine();

			try {
				inputNumber = Integer.parseInt(inputLine);
			} catch (NumberFormatException ex) {
				if (inputLine.equals("kraj")) {
					System.out.println("Dovidjenja.");
					break;
				} else {
					System.out.println("'" + inputLine + "' nije cijeli broj.");
					continue;
				}
			}

			if (inputNumber < 1 || inputNumber > 20) {
				System.out.println("'" + inputLine + "' nije broj u dozvoljenom rasponu.");
			} else {
				System.out.println(inputLine + "! = " + Long.toString(calculateFactorial(inputNumber)));
			}
		}

		sc.close();
	}

	/**
	 * Returns factorial of a given positive number. If number is negative then
	 * method returns number.
	 * 
	 * @param number
	 *            factorial of a number to be calculated
	 * @return factorial of given number, or number if argument is negative
	 */
	static long calculateFactorial(int number) {
		long result = number;

		if (number == 1 || number == 0) {
			return 1;
		}

		for (int i = 2; i < number; ++i) {
			result *= i;
		}
		return result;
	}
}
