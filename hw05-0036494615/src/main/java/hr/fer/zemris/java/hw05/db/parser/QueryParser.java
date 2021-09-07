package hr.fer.zemris.java.hw05.db.parser;

import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw05.db.ComparisonOperators;
import hr.fer.zemris.java.hw05.db.ConditionalExpression;
import hr.fer.zemris.java.hw05.db.FieldValueGetters;
import hr.fer.zemris.java.hw05.db.IComparisonOperator;
import hr.fer.zemris.java.hw05.db.IFieldValueGetter;
import hr.fer.zemris.java.hw05.db.lexer.LexerException;
import hr.fer.zemris.java.hw05.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw05.db.lexer.TokenType;
import static hr.fer.zemris.java.hw05.db.FieldValueGetters.*; //doslovno koristim sve
import static hr.fer.zemris.java.hw05.db.ComparisonOperators.*; //koristim sve

/**
 * Class describes query parser. Extracts conditional expressions from them.
 * Classifies queries into two main categories. First category is category of direct queries. 
 * Direct queries demand only one jmbag (EQUALS Conditional operator). All of the others are in the second category.
 * 
 * @author Hrvoje
 *
 */
public class QueryParser {
	/** Parsed conditional expressions **/
	List<ConditionalExpression> condExp;
	
	/**
	 * Constructor for QueryParser. String query will be parsed into Conditional Expressions.
	 * String must be non <code>null</code>l
	 * @param query to parse
	 * @throws NullPointerException if query is <code>null</code>
	 */
	public QueryParser(String query) {
		QueryLexer lexer = new QueryLexer(query); //throws nullPointerException
		condExp = new LinkedList<>();
		
		IFieldValueGetter valueGetter;
		String stringLiteral;
		IComparisonOperator comparisonOperator;
		
		try {
		while(true) {
			valueGetter = getFieldValueGetter(lexer.nextToken().getValue());
			validateParse(valueGetter, lexer.getToken().getType(), TokenType.ATRIBUTE);
			
			comparisonOperator = getComparisonOperator(lexer.nextToken().getValue());
			validateParse(comparisonOperator, lexer.getToken().getType(), TokenType.OPERATOR);

			stringLiteral = lexer.nextToken().getValue();
			validateParse(stringLiteral, lexer.getToken().getType(), TokenType.STRING_LITERAL);
			
			condExp.add(new ConditionalExpression(valueGetter, stringLiteral, comparisonOperator));
			
			if(lexer.nextToken().getType() == TokenType.EOF) break;
			if(lexer.getToken().getType() == TokenType.AND) continue;
			
			throw new QueryParserException("Syntax error");
		}
		}catch(LexerException l) {
			throw new QueryParserException(l.getLocalizedMessage());
		}
	}

	/**
	 * Returns true if query is direct query. 
	 * @return true if query is direct query. 
	 */
	public boolean isDirectQuery() {
		if(condExp.size() != 1) return false;
		
		ConditionalExpression first = condExp.get(0);
		
		if(!first.getFieldGetter().equals(FieldValueGetters.JMBAG)) return false;
		if(!first.getComparisonOperator().equals(ComparisonOperators.EQUALS)) return false;
		
		return true;
	}
	
	/**
	 * Returns JMBAG of direct query.
	 * @return JMBAG of direct query
	 * @throws IllegalStateException if query is not direct
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery()) throw new IllegalStateException("Query is not direct. Cannot fast query jmbag. ");
		
		return condExp.get(0).getStringLiteral();
	}
	
	/**
	 * Returns list of Conditional Expressions parsed from query.
	 * @return list of Conditional Expressions parsed from query
	 */
	public List<ConditionalExpression> getQuery() {
		return condExp;
	}
	
	/**
	 * Checks whether result is not null and whether it has expected type.
	 * @param parseResult result
	 * @param type result type
	 * @param expected expected type
	 */
	private void validateParse(Object parseResult, TokenType type, TokenType expected) {
		if(parseResult == null) throw new QueryParserException();
		if(type != expected) throw new QueryParserException("Expected " + expected.toString() + " was " + type.toString());
	}

}

