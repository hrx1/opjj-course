package hr.fer.zemris.java.hw05.db.lexer;

import static java.lang.Character.isWhitespace;

import java.util.Objects;

/**
 * Class provides simple lexer of queries. Every unit is classified in a token type and it's value.
 * Lexer can split string into tokens defined by TokenType.
 * Lexer throws {@link LexerException} if error occurs.
 * @author Hrvoje
 *
 */
public class QueryLexer {
	/** Input data */
	private char[] data;
	/** Newest Token */
	private Token token;
	/** Index of first non tokenised character*/
	private int currentIndex; //
	
	/**
	 * Constructor for query lexer
	 * @param query string to lex
	 */
	public QueryLexer(String query) {
		Objects.requireNonNull(query);
		data = query.toCharArray();
		token = null;
		currentIndex = 0;
		skipWhitespaces();
	}
	
	/**
	 * Returns next token of a string given in constructor
	 * @return next token of a string given in constructor
	 */
	public Token nextToken() {
		if (token != null && token.getType().equals(TokenType.EOF)) {
			throw new LexerException("No more tokens.");
		}

		skipWhitespaces(); // preskace ('\r', '\n', '\t', ' ') *

		// kraj?
		if (currentIndex >= data.length) {
			return token = new Token(TokenType.EOF, null);
		}
				
		//and?
		else if (isAND()) {
			currentIndex += 3;
			return token = new Token(TokenType.AND, " and ");
		}
			
		//string lit?
		else if (data[currentIndex] == '"') {
			++currentIndex; //preskoci "
			return token = parseStringLiteral(); //parsira do "
		}
		
		//oper?
		else if(isOperator(data[currentIndex])) {
			return token = parseOperator(); //parsira jedan do 2 znaka ili LIKE
		}
		
		else {
			return token = parseAtributeOrLIKE();
		}

	}
	
	/**
	 * Returns current token.
	 * @return current token
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Parses operator.
	 * @return token with type OPERATOR
	 */
	private Token parseOperator() {
		StringBuilder sb = new StringBuilder();
		sb.append(data[currentIndex]);
		
		if(currentIndex + 1 < data.length && isOperator(data[currentIndex + 1])) {
			sb.append(data[currentIndex + 1]);
			++currentIndex;
		}
		++currentIndex;
		
		return new Token(TokenType.OPERATOR, sb.toString());
	}
	
	/**
	 * Checks whether c is operator
	 * @param c to check
	 * @return true if c is operator
	 */
	private boolean isOperator(char c) {
		char [] operators = {'<', '>', '='};
		for(char oper : operators) 
			if (oper == c) return true;
		
		return false;
	}
	
	/**
	 * Parses string literal.
	 * @return token with token type STRING_LITERAL and value of a string literal.
	 */
	private Token parseStringLiteral() {
		int oldIndex = currentIndex;
		while(data[currentIndex] != '"') {
			++currentIndex;
			if(currentIndex >= data.length) break;
		}
		
		StringBuilder sb = new StringBuilder(currentIndex - oldIndex + 1);
		sb.append(data, oldIndex, currentIndex - oldIndex);
		
		++currentIndex; //preskace "
		
		return new Token(TokenType.STRING_LITERAL, sb.toString());
	}
	
	/**
	 * Parses atribute or LIKE operator
	 * @return Token with type ATRIBUTE or OPERATOR
	 */
	private Token parseAtributeOrLIKE() {
		int oldIndex = currentIndex;
		while(Character.isAlphabetic(data[currentIndex])) {
			++currentIndex;
			if(currentIndex >= data.length) break;
		}
		
		StringBuilder sb = new StringBuilder(currentIndex - oldIndex + 1);
		sb.append(data, oldIndex, currentIndex - oldIndex);
				
		String result = sb.toString();
		
		if(result.equals("LIKE")) return new Token(TokenType.OPERATOR, result);
		
		return new Token(TokenType.ATRIBUTE, sb.toString());
	}
	
	/**
	 * Returns true if next char sequence is AND operator
	 * @return true if next char sequence is AND operator
	 */
	private boolean isAND() {
		return Character.toLowerCase(data[currentIndex]) == 'a' && 
				currentIndex + 3 < data.length && //TODO +3 ili +4
				Character.toLowerCase(data[currentIndex + 1]) == 'n' &&
				Character.toLowerCase(data[currentIndex + 2]) == 'd' &&
				isWhitespace(data[currentIndex - 1]) && isWhitespace(data[currentIndex + 3]);
	}

	/**
	 * Skips whitespaces
	 */
	private void skipWhitespaces() {
		while (currentIndex < data.length && isWhitespace(data[currentIndex])) { 
			++currentIndex;
		}
	}
}
