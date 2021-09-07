package hr.fer.zemris.java.gui.calc.components;

import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Describes buttons for operation.
 * @author Hrvoje
 *
 */
public class BlueOperationButton extends BlueButton {
	private static final long serialVersionUID = -985888316591042027L;

	/** Binary operator **/
	private DoubleBinaryOperator binaryOperator;
	
	/**
	 * Constructor for operation button.
	 * 
	 * @param text of button
	 * @param calculator to use
	 * @param binaryOperator to use
	 */
	public BlueOperationButton(String text, CalcModel calculator, DoubleBinaryOperator binaryOperator) {
		super(text, calculator);
		this.binaryOperator = binaryOperator;
	}

	@Override
	public void pressed() {
		CalcModel model = getCalcModel();
		
		DoubleBinaryOperator pendingOperator = model.getPendingBinaryOperation();
		
		if(model.isActiveOperandSet() && model.getPendingBinaryOperation() != null) {
			model.setValue(pendingOperator.applyAsDouble(model.getActiveOperand(), model.getValue()));
			model.clearActiveOperand();
		}
		model.setPendingBinaryOperation(binaryOperator);
	}

}
