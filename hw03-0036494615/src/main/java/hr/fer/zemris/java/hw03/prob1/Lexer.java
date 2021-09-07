package hr.fer.zemris.java.hw03.prob1;

import static java.lang.Character.isWhitespace;

import java.util.Arrays;
import java.util.Objects;

import static java.lang.Character.isLetter;
import static java.lang.Character.isDigit;;


/**
 * Defines simple lexer which can create tokens defined in TokenType class.
 * @author Hrvoje
 *
 */
public class Lexer {
	/**
	 * Input data
	 */
	private char[] data; // ulazni tekst
	/** Last tokenized token  */
	private Token token; // trenutni token
	/** First unanalyzed char */
	private int currentIndex; // indeks prvog neobrađenog znaka
	/** State of Lexer */
	private LexerState state;
	
	/**
	 * Constructor for lexer which will tokenise text.
	 * @param text to lex
	 */
	public Lexer(String text) {
		Objects.requireNonNull(text);
		data = text.toCharArray();
		token = null;
		state = LexerState.BASIC;
	}
	
	/**
	 * Gets next token. Throws {@link LexerException} if there is no new tokens.
	 * @return token
	 */
	public Token nextToken() {
		if (state.equals(LexerState.BASIC)) {
			token = basicNextToken();
			return token;
		} else {
			token = extendedNextToken();
			return token;
		}
	}

	/**
	 * Parsing in extended mode
	 * @return token
	 */
	private Token extendedNextToken() {
		//kraj?
		if (token != null && token.getType().equals(TokenType.EOF)) {
			throw new LexerException("No more tokens.");
		}
		
		skipWhitespaces(); //preskace ('\r', '\n', '\t', ' ') *
		
		
		//kraj?
		if(currentIndex >= data.length) {
			return new Token(TokenType.EOF, null);
		}
		
		if(data[currentIndex] == '#') {
			++currentIndex;
			return new Token(TokenType.SYMBOL, '#');
		}
		
		int startIndex = currentIndex;
		while(currentIndex < data.length) {
			if (data[currentIndex] == '#' || data[currentIndex] == ' ') break;
			++currentIndex;
		}
		
		String resultString = String.valueOf(Arrays.copyOfRange(data, startIndex, currentIndex));
		return new Token(TokenType.WORD, resultString);
	}

	/**
	 * Parsing in basic mode
	 * @return token
	 */
	private Token basicNextToken() {
		//kraj?
		if (token != null && token.getType().equals(TokenType.EOF)) {
			throw new LexerException("No more tokens.");
		}
		
		skipWhitespaces(); //preskace ('\r', '\n', '\t', ' ') *
		
		
		//kraj?
		if(currentIndex >= data.length) {
			return new Token(TokenType.EOF, null);
		}
		
		TokenType tokenType;
		Object tokenValue;
		
		if (isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
			//parseWord postavi currentSize na prvi neobradjeni znak, baci exc. 
			//ako ga ima, vrati string koji predstavlja rijec
			
			tokenValue = parseWord();
			tokenType = TokenType.WORD;
		}
		
		else if(isDigit(data[currentIndex])) {	
			Long result = parseLong();
			
			tokenValue = result;
			tokenType = TokenType.NUMBER;
		}

		else {
			tokenValue = data[currentIndex];
			tokenType = TokenType.SYMBOL;

			++currentIndex;
		}
		
		return new Token(tokenType, tokenValue);

	}
	
	/**
	 * Parse long
	 * @return Long
	 */
	private Long parseLong() {
		int startIndex = currentIndex;
				
		while(isDigit(data[currentIndex])) {
			++currentIndex;
			
			if(currentIndex == data.length) break;
			
		}

		Long result;
		try {
			result = Long.parseLong(String.valueOf(Arrays.copyOfRange(data, startIndex, currentIndex)));
		} catch (NumberFormatException e) {
			throw new LexerException("LONG couldn't be parsed: " + String.valueOf(Arrays.copyOfRange(data, startIndex, currentIndex)));
		}
		
		return result;
	}


	/**
	 * Parse word
	 * @return word
	 */
	private String parseWord() {
		int startIndex = currentIndex;
		
		while(currentIndex < data.length) {
			if (isLetter(data[currentIndex])) {
				++currentIndex;
			}
			else if(currentIndex < data.length && data[currentIndex] == '\\') {
				parseEscapeCharacters();
			}
			else break;
		}
				
		return String.valueOf(Arrays.copyOfRange(data, startIndex, currentIndex)).replace(" ", "");
	}

	/**
	 * Returns last token.
	 * @return token
	 */
	public Token getToken() {
		return token;
	}
	
	
	/**
	 * Sets state of a lexer. Throws {@link NullPointerException} if state == null.
	 * @param state state
	 */
	public void setState(LexerState state) {
		Objects.requireNonNull(state);
		this.state = state;
	}

	/**
	 * Skips whitespaces. Sets currentIndex to first non whitespace char
	 */
	private void skipWhitespaces() {
		while(currentIndex < data.length &&
				isWhitespace(data[currentIndex])) {
			++currentIndex;
		}
	}
	
	/**
	 * Parses escape chars
	 */
	private void parseEscapeCharacters() {
			if(currentIndex + 1 == data.length || (!isDigit(data[currentIndex + 1]) && data[currentIndex + 1] != '\\')) {
				throw new LexerException("String couldn't be parsed. Invalid escape character usage.");
			}
			else {
				data[currentIndex] = ' ';
				currentIndex += 2; //preskoci special character (digit ili \)
			}
	}
}
