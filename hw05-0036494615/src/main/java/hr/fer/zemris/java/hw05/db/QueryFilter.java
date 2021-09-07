package hr.fer.zemris.java.hw05.db;

import java.util.List;

/**
 * Realisation of a filter. Filter filters by conditional expressions given in constructor.
 * @author Hrvoje
 *
 */
public class QueryFilter implements IFilter {
	/** Conditional Expressions by which records are filtered  **/
	List<ConditionalExpression> condExpList;
	
	/**
	 * Constructor for QueryFilter
	 * @param condExpList Conditional Expressions to filter with
	 */
	public QueryFilter(List<ConditionalExpression> condExpList) {
		super();
		this.condExpList = condExpList;
	}

	@Override
	/**
	 * Filters record by conditional expressions.
	 * @return true if accepts
	 */
	public boolean accepts(StudentRecord record) {
		
		for(ConditionalExpression condExp : condExpList) {
			if(!condExp.getComparisonOperator()
				.satisfied(condExp.getFieldGetter()
				.get(record), condExp.getStringLiteral())) 
					return false;
		}
		
		return true;
	}
}
