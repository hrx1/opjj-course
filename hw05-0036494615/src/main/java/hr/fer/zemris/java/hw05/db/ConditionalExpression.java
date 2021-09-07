package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Class describes Conditional Expressions. Every conditional expression has it's valueGetter, StringLiteral and comparisonOperator
 * @author Hrvoje
 *
 */
public class ConditionalExpression {
	/** Value getter **/
	private IFieldValueGetter valueGetter;
	/** String literal **/
	private String stringLiteral;
	/** comparison operator **/
	private IComparisonOperator comparisonOperator;
	
	/**
	 * Constructor for ConditionalExpression
	 * @param valueGetter value getter
	 * @param stringLiteral string literal
	 * @param comparisonOperator comparison operator
	 */
	public ConditionalExpression(IFieldValueGetter valueGetter, String stringLiteral,
			IComparisonOperator comparisonOperator) {
		
		super();
		
		Objects.requireNonNull(valueGetter);
		Objects.requireNonNull(stringLiteral);
		Objects.requireNonNull(comparisonOperator);
		
		this.valueGetter = valueGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}


	public IFieldValueGetter getFieldGetter() {
		return valueGetter;
	}


	public String getStringLiteral() {
		return stringLiteral;
	}


	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}
	
	@Override
	public String toString() {
		return valueGetter.toString() + " " + stringLiteral + " " + comparisonOperator.toString();
	}
}
