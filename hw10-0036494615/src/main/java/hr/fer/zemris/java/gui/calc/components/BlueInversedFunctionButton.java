package hr.fer.zemris.java.gui.calc.components;

import java.util.function.DoubleUnaryOperator;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Button for functions which have inverse.
 * @author Hrvoje
 *
 */
public class BlueInversedFunctionButton extends BlueButton implements InverseButtonListener {
	private static final long serialVersionUID = 3866841985444115683L;
	/* Is invese set Flag */
	private boolean inverseFlag;
	/* Operation */
	DoubleUnaryOperator operation;
	/* Inverse operation */
	DoubleUnaryOperator inverse;
	
	/**
	 * Constructor
	 * @param text of a button
	 * @param calculator to use
	 * @param operation to do
	 * @param inverse to do
	 */
	public BlueInversedFunctionButton(String text, CalcModel calculator, DoubleUnaryOperator operation, DoubleUnaryOperator inverse) {
		super(text, calculator);
		inverseFlag = false;
		
		this.operation = operation;
		this.inverse = inverse;
	}

	
	@Override
	public void pressed() {
		double result;
		
		if(!inverseFlag) {
			result = operation.applyAsDouble(super.getCalcModel().getValue());
		}else {
			result = inverse.applyAsDouble(super.getCalcModel().getValue());
		}
		
		super.getCalcModel().setValue(result);
	}

	@Override
	public void inverseChanged(BlueInverseButton inverseButton) {
		inverseFlag = !inverseFlag;
	}

}
