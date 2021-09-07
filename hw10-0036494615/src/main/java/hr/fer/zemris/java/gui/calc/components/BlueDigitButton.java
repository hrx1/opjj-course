package hr.fer.zemris.java.gui.calc.components;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Describes button which represent digits.
 * @author Hrvoje
 *
 */
public class BlueDigitButton extends BlueButton {
	private static final long serialVersionUID = 3475588881338602696L;
	/* Digit which object represents */
	private int digit;
	
	/**
	 * Constructor
	 * @param digit which button represents
	 * @param calculator to use
	 */
	public BlueDigitButton(int digit, CalcModel calculator) {
		super(String.valueOf(digit), calculator);
		this.digit = digit;
	}
	
	/**
	 * Getter for digit
	 * @return digit
	 */
	public int getDigit() {
		return digit;
	}

	@Override
	public void pressed() {
		getCalcModel().insertDigit(digit);
	}
}
