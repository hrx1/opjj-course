package hr.fer.zemris.java.gui.calc.components;

import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Button for binary operators which have inverse.
 * @author Hrvoje
 *
 */
public class BlueInverseBinaryOperation extends BlueButton implements InverseButtonListener{
	private static final long serialVersionUID = -5259972306383226300L;
	/* Is invese set Flag */
	private boolean inverseFlag;
	/* Operation */
	DoubleBinaryOperator operation;
	/* Inverse operation */
	DoubleBinaryOperator inverse;
	
	/**
	 * Constructor
	 * @param text of a button
	 * @param calculator to use
	 * @param operation to do
	 * @param inverse to do
	 */
	public BlueInverseBinaryOperation(String text, CalcModel calculator, DoubleBinaryOperator operation, DoubleBinaryOperator inverse) {
		super(text, calculator);
		inverseFlag = false;
		
		this.operation = operation;
		this.inverse = inverse;
	}

	
	@Override
	public void pressed() {
		CalcModel model = getCalcModel();
		DoubleBinaryOperator current = (inverseFlag) ? inverse : operation;
				
		DoubleBinaryOperator pendingOperator = model.getPendingBinaryOperation();
		
		if(model.isActiveOperandSet() && model.getPendingBinaryOperation() != null) {
			model.setValue(pendingOperator.applyAsDouble(model.getActiveOperand(), model.getValue()));
			model.clearActiveOperand();
		}
		model.setPendingBinaryOperation(current);

	}

	@Override
	public void inverseChanged(BlueInverseButton inverseButton) {
		inverseFlag = !inverseFlag;
	}


}
