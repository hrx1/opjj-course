package hr.fer.zemris.java.gui.calc.components;

import java.awt.Color;

import javax.swing.JButton;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Describes Blue Buttons
 * @author Hrvoje
 *
 */
public abstract class BlueButton extends JButton {
	private static final long serialVersionUID = -2565111827444250736L;
	
	/* Calculator model used */
	private CalcModel calculator;
	
	/**
	 * Constructor for Blue button.
	 * Adds ActionListener which calls pressed() method when button is pressed.
	 * 
	 * @param text of button
	 * @param calculator used
	 */
	public BlueButton(String text, CalcModel calculator) {
		super(text);
		setBackground(new Color(66, 134, 244));
		setForeground(Color.BLACK);
		
		this.addActionListener(e -> this.pressed());
		
		this.calculator = calculator;
	}
	
	/**
	 * Getter for CalcModel
	 * @return CalcModel
	 */
	public CalcModel getCalcModel() {
		return calculator;
	}
	
	/**
	 * Method called when button is pressed
	 */
	public abstract void pressed();
	
}
