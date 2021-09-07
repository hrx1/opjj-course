package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Lexer supports only two States. In Text state every input is Tokenised as a
 * Word, except all Tags. In Expression state, lexer can distinct these new
 * types: VAR, CONST_INT, CONST_DOUBLE, FUNC, STRING, OPER, EOF including all Tags.
 * 
 * @author Hrvoje
 *
 */
public enum ScriptLexerState {
	/** Everything is tokenized as a word, except tag start*/
	TEXT,
	/** Parses more types*/
	EXPRESSION
}
