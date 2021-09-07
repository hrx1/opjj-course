package hr.fer.zemris.java.hw03.prob1;
/**
 * Class defines Token Types which lexer can reconize
 * @author Hrvoje
 *
 */
public enum TokenType {
	/** End Of File */
	EOF, 
	/** Array of isAlphabetic chars */
	WORD, 
	/** number */
	NUMBER, 
	/** Operation symbols */
	SYMBOL
}
