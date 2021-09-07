package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.components.CalcValueListener;

/**
 * Interface describes simple calculator model.
 * @author Hrvoje
 *
 */
public interface CalcModel {
	
	/**
	 * Adds listener to Calculator model.
	 * @param l listener to add
	 */
	void addCalcValueListener(CalcValueListener l);

	/**
	 * Removes listener to Calculator model.
	 * @param l listener to remove
	 */
	void removeCalcValueListener(CalcValueListener l);

	/**
	 * Returns current value as String which Calculator model has.
	 * If there is no current value stored, then returns 0.
	 * 
	 * @return current value which Calculator model has
	 */
	String toString();

	/**
	 * Returns current value as double which Calculator model has.
	 * @return current value as double which Calculator model has
	 */
	double getValue();

	/**
	 * Sets current value of Calculator model to value 
	 * @param value new value of currently stored value
	 */
	void setValue(double value);

	/**
	 * Clears current value.
	 */
	void clear();

	/**
	 * Clears current value, last operand and operator.
	 */
	void clearAll();

	/**
	 * Swaps sign of current value.
	 */
	void swapSign();

	/**
	 * Inserts decimal point to current value if current value doesn't contain decimal point.
	 */
	void insertDecimalPoint();

	/**
	 * Inserts digit to current value.
	 * @param digit to insert to current value
	 */
	void insertDigit(int digit);

	/**
	 * Returns true if active operand is set, false otherwise.
	 * @return true if active operand is set, false otherwise
	 */
	boolean isActiveOperandSet();

	/**
	 * Returns active operand as double.
	 * @return active operand as double
	 */
	double getActiveOperand();

	/**
	 * Sets active operand.
	 * @param activeOperand to set
	 */
	void setActiveOperand(double activeOperand);

	/**
	 * Clears active operand.
	 */
	void clearActiveOperand();

	/**
	 * Returns pending double binary operator.
	 * @return pending double binary operator
	 */
	DoubleBinaryOperator getPendingBinaryOperation();

	/**
	 * Sets pending binary operation.
	 * @param op to set 
	 */
	void setPendingBinaryOperation(DoubleBinaryOperator op);
}