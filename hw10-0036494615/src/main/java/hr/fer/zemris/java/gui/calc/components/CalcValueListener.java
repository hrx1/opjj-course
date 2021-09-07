package hr.fer.zemris.java.gui.calc.components;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Listener for Calculator value.
 * @author Hrvoje
 *
 */
public interface CalcValueListener {
	
	/**
	 * Method run when value change is observed.
	 * @param model in which value is changed
	 */
	void valueChanged(CalcModel model);
}