package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * Class implements generic Editor methods
 * @author Hrvoje
 *
 */
public abstract class GeometricalObjectEditor extends JPanel {
	private static final long serialVersionUID = 5221561918669507856L;

	/** Check editing */
	 public abstract void checkEditing();
	 /** Store values */
	 public abstract void acceptEditing();
	 /**
	  * Return color for hex String 
	  * */
	 public static Color getColor(String hex) throws IllegalArgumentException {
		 if(hex.length() > 6) throw new IllegalArgumentException();
		 int[] rgb = getRGB(hex);
		 return new Color(rgb[0], rgb[1], rgb[2]); //takodjer baca IAE
	 }
	 
	 /**
	  * Return RGB int array for String
	  * @param rgb String 
	  * @return RGB int array
	  */
	 public static int[] getRGB(String rgb) {
		 rgb = rgb.toUpperCase();
	     final int[] ret = new int[3];
	     for (int i = 0; i < 3; i++)
	     {
	         ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16);
	     }
	     return ret;
	 }
	 
	 /**
	  * Throws invalid argument exception if any number is negative.
	  * @param numbers to check
	  */
	 public static void checkNegative(int ... numbers) {
		 for (int n : numbers) {
			 if (n < 0) throw new IllegalArgumentException();
		 }
	 }
}