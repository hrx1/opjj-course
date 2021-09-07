package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;

/**
 * Listener for Color Change
 * 
 * @author Hrvoje
 *
 */
public interface ColorChangeListener {
	/**
	 * New Color Selected Notifier 
	 * @param source of change
	 * @param oldColor Old color
	 * @param newColor new color
	 */
	 public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}
