package hr.fer.zemris.java.hw01;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Scanner;

/**
 * Class provides calculations of basic rectangle properties.
 * 
 * @author Hrvoje
 *
 */
public class Rectangle {

	/**
	 * Calculates rectangle properties from arguments or data from standard input.
	 * 
	 * @param args
	 *            two positive numbers (width and height) or nothing
	 */
	public static void main(String[] args) {
		double width, height;

		if (args.length == 0) {
			Scanner consoleInput = new Scanner(System.in);

			width = requestNumber("Unesite sirinu > ", consoleInput);
			height = requestNumber("Unesite visinu > ", consoleInput);

			consoleInput.close();

		} else if (args.length == 2) {
			try {
				width = NumberFormat.getInstance().parse(args[0]).doubleValue();
				height = NumberFormat.getInstance().parse(args[1]).doubleValue();

				if (width <= 0 || height <= 0) {
					System.out.println("Argumenti moraju biti pozitivni brojevi!");
					return;
				}

			} catch (ParseException ex) {
				System.out.println("Argumenti smiju biti samo dva broja!");
				return;
			}

		} else {
			System.out.println("Tocno dva broja trebaju biti argumenti!");
			return;
		}

		System.out.format("Pravokutnik širine %.1f i visine %.1f ima površinu %.1f te opseg %.1f.%n", width, height,
				width * height, 2 * width + 2 * height);
	}

	/**
	 * Writes out message and reads from stream sc until first nonnegative number is
	 * read.
	 * 
	 * @param message
	 *            Message to user on every entry
	 * @param sc
	 *            text scanner from which numbers are loaded
	 * @return first nonnegative number from stream sc
	 */
	private static double requestNumber(String message, Scanner sc) {
		String inputLine;
		double inputNumber;

		while (true) {
			System.out.print(message);
			inputLine = sc.nextLine();

			try {
				inputNumber = NumberFormat.getInstance().parse(inputLine).doubleValue();
			} catch (ParseException ex) {
				System.out.println("'" + inputLine + "' se ne moze protumaciti kao broj");
				continue;
			}

			if (inputNumber < 0) {
				System.out.println("Unijeli ste negativnu vrijednost.");
			} else {
				break;
			}
		}

		return inputNumber;
	}

}
