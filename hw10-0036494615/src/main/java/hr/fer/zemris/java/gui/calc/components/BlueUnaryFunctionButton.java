package hr.fer.zemris.java.gui.calc.components;

import java.util.function.DoubleUnaryOperator;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Describes Buttons of Unary function.
 * @author Hrvoje
 *
 */
public class BlueUnaryFunctionButton extends BlueButton {
	private static final long serialVersionUID = -8448218724857528388L;

	/** Function **/
	private DoubleUnaryOperator function;

	/**
	 * Constructor 
	 * @param name of button
	 * @param calculator to communicate with
	 * @param function
	 */
	public BlueUnaryFunctionButton(String name, CalcModel calculator, DoubleUnaryOperator function) {
		super(name, calculator);
		this.function = function;
	}

	@Override
	public void pressed() {
		double newValue = function.applyAsDouble(super.getCalcModel().getValue());
		super.getCalcModel().setValue(newValue);
	}
	
	
}
