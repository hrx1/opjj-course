package hr.fer.zemris.java.gui.calc;

import java.util.LinkedList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.components.CalcValueListener;

/**
 * Implements CalcModel, and defines operations which classic Windows calculator has.
 * Implementation stores active operand, current operand and pending binary operation.
 * 
 * Forbids adding digits to current operand if it cannot be stored as double.
 * Allows subscription to value change.
 * 
 * @author Hrvoje
 *
 */
public class ClassicCalculator implements CalcModel {
	/** List of listeners **/
	private List<CalcValueListener> listeners;
	/** Active operand **/
	private double activeOperand;
	/** Flag which checks is Active operand set **/
	private boolean activeOperandSet = false;
	
	/** Current Operand **/
	private String currentOperand;
	/** Pending binary operation **/
	private DoubleBinaryOperator pendingBinaryOperation;
	
	/** Maximal number of digits operand can have **/
	private static final double DOUBLE_MAX_EXPONENT = 307;
	
	/**
	 * Default constructor
	 */
	public ClassicCalculator() {
		listeners = new LinkedList<>();
	}
	
	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}

	@Override
	public double getValue() {
		return (currentOperand == null) ? 0 : Double.parseDouble(currentOperand);
	}

	@Override
	public void setValue(double value) {
		currentOperand = String.valueOf(value);
		valueChanged();
	}

	@Override
	public void clear() {
		currentOperand = null;
		
		valueChanged();
	}

	@Override
	public void clearAll() {
		currentOperand = null;
		activeOperand = 0;
		activeOperandSet = false;
		pendingBinaryOperation = null;
		
		valueChanged();
	}

	@Override
	public void swapSign() {
		if(currentOperand == null) {
			return ;
		}
		
		if(currentOperand.startsWith("-")) {
			currentOperand = currentOperand.replaceFirst("-", "");
		}else {
			currentOperand = "-" + currentOperand;
		}
		
		valueChanged();
	}

	@Override
	/**
	 * Inserts decimal point if it doesn't exist.
	 */
	public void insertDecimalPoint() {
		if(currentOperand == null) {
			currentOperand = "0.";
		}
		else if(!currentOperand.contains(".")) currentOperand += ".";
		
		valueChanged();
	}

	@Override
	public void insertDigit(int digit) {
		
		if(currentOperand != null && currentOperand.length() >= DOUBLE_MAX_EXPONENT) {
			return ;
		}
		
		if(currentOperand == null) {
			currentOperand = "";
		}
		
		if(pendingBinaryOperation == null) {
			currentOperand += String.valueOf(digit);
		} 
		else if(!activeOperandSet) {
			setActiveOperand(getValue());
			clear();
			currentOperand = String.valueOf(digit);
		} else {
			currentOperand += String.valueOf(digit);		
		}
		
		valueChanged();
	}

	@Override
	public boolean isActiveOperandSet() {
		return activeOperandSet;
	}

	@Override
	public double getActiveOperand() {
		if(!activeOperandSet) throw new IllegalStateException("Operand not set.");
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		activeOperandSet = true;
		this.activeOperand = activeOperand;
	}

	@Override
	public void clearActiveOperand() {
		activeOperandSet = false;
		this.activeOperand = 0;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingBinaryOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		pendingBinaryOperation = op;
	}
	
	@Override
	public String toString() {
		return (currentOperand == null) ? "0" : currentOperand;
	}
	
	/**
	 * Tells observers that value has changed.
	 */
	private void valueChanged() {
		for(CalcValueListener listener : listeners) {
			listener.valueChanged(this);
		}
	}

}
