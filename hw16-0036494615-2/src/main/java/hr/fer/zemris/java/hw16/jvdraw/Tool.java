package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * State Design Pattern Interface
 * @author Hrvoje
 *
 */
public interface Tool {
	/**
	 * Mouse Pressed
	 * @param e event
	 */
	public void mousePressed(MouseEvent e);
	/**
	 * Mouse Released
	 * @param e event
	 */
	public void mouseReleased(MouseEvent e);
	/**
	 * Mouse Clicked
	 * @param e event
	 */
	public void mouseClicked(MouseEvent e);
	/**
	 * Mouse Moved
	 * @param e event
	 */
	public void mouseMoved(MouseEvent e);
	/**
	 * Mouse Dragged
	 * @param e event
	 */
	public void mouseDragged(MouseEvent e);
	/**
	 * Paint
	 * @param g2d graphics
	 */
	public void paint(Graphics2D g2d);
}