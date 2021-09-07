package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;

/**
 * Interface for Color Providers
 * @author Hrvoje
 *
 */
public interface IColorProvider {
	/**
	 * Returns current Color
	 * @return
	 */
	public Color getCurrentColor();
	/**
	 * Notifies Listeners Color changed
	 * @param l
	 */
	public void addColorChangeListener(ColorChangeListener l);
	/**
	 * Notifies Lisneters Color removed
	 * @param l
	 */
	public void removeColorChangeListener(ColorChangeListener l);
}
