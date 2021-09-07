package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Types of a Token which Lexer can recognize.
 * @author Hrvoje
 *
 */
public enum TokenType {
	TEXT, 
	
	FOR_TAG, //{$FOR
	ECHO_TAG, //{$ = i ... $}
	END_TAG, //{$END$}
	CLOSE_TAG, //$}
	
	VAR,	
	CONST_INT,
	CONST_DOUBLE,
	FUNC,
	STRING,
	OPER,
	EOF
}
