package hr.fer.zemris.java.custom.scripting.lexer;

import static java.lang.Character.isWhitespace;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;

import java.util.Arrays;
import java.util.Objects;

/**
 * 
 * @author Hrvoje
 *
 */
public class ScriptLexer {
	/** Input data */
	private char[] data;
	/** Newest Token */
	private Token token;
	/** Index of first non tokenised character*/
	private int currentIndex; // 
	/** State of a Lexer*/
	private ScriptLexerState state;

	/**
	 * Constructor for lexer which will tokenize text data
	 * @param text to be tokenized
	 */
	public ScriptLexer(String text) {
		Objects.requireNonNull(text);
		data = text.toCharArray();
		token = null;
		state = ScriptLexerState.TEXT;
	}

	/**
	 * Returns next Token
	 * @return next Token
	 */
	public Token nextToken() {
		if (state.equals(ScriptLexerState.EXPRESSION)) {
			token = parseExpression();
			return token;
		} else {
			token = parseText();
			return token;
		}
	}
	
	/**
	 * Getter for token
	 * @return
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Setter for state. Throws {@link NullPointerException} if state == null.
	 * @return state
	 */
	public void setState(ScriptLexerState state) {
		Objects.requireNonNull(state);
		this.state = state;
	}

	/**
	 * Parses text and returns his Token with TokenType.TEXT.
	 * Places currentIndex on first non tokenized character.
	 * 
	 * Throws {@link LexerException} if text is not valid.
	 * 
	 * @return text token
	 */
	private Token parseText() {
		if (token != null && token.getType().equals(TokenType.EOF)) {
			throw new LexerException("No more tokens.");
		}

		skipWhitespaces(); // preskace ('\r', '\n', '\t', ' ') *

		// kraj?
		if (currentIndex >= data.length) {
			return new Token(TokenType.EOF, null);
		}

		// provjerava je li dosao do TAGa. Vrati ga ako je
		if (data[currentIndex] == '{' && (currentIndex + 1 < data.length) && data[currentIndex + 1] == '$') {
			return parseTag();
		}

		// dok ima podataka i dok ne naidjes na (^\){$
		// dozvoljeni escapeovi su \\ i \$
		int startIndex = currentIndex;

		while (currentIndex < data.length) {

			if (data[currentIndex] == '\\') { // provjeri je li dobar escape char
				if (currentIndex + 1 < data.length
						&& (data[currentIndex + 1] == '\\' || data[currentIndex + 1] == '{')) {
					currentIndex += 2;
				} else {
					throw new LexerException("String couldn't be parsed. Invalid escape character usage");
				}
			} // provjeri je li TAG
			else if (data[currentIndex] == '{' && (currentIndex + 1 < data.length) && data[currentIndex + 1] == '$') {
				break;
			} else {
				++currentIndex;
			}
		}

		String result = String.valueOf(Arrays.copyOfRange(data, startIndex, currentIndex)).replace("\\\\", "\\")
				.replace("\\{", "{"); // TODO pazi

		return new Token(TokenType.TEXT, result);
	}



	/*
	 * Parses in ScriptLexerState.Expression mode. Returns first Token with type
	 * defined in TokenType. 
	 * TokenTypes: FOR_TAG, ECHO_TAG, END_TAG, CLOSE_TAG,
	 * VAR, CONST_INT, CONST_DOUBLE, FUNC, STRING, OPER, EOF
	 * 
	 * Throws {@link LexerException} if name is not valid.
	 * 
	 * 	Places currentIndex on first non tokenized character.

	 */

	private Token parseExpression() {

		if (token != null && token.getType().equals(TokenType.EOF)) {
			throw new LexerException("No more tokens.");
		}

		skipWhitespaces(); // preskace ('\r', '\n', '\t', ' ') *

		// kraj?
		if (currentIndex >= data.length) {
			return new Token(TokenType.EOF, null);
		}

		// tag?
		if (data[currentIndex] == '{') {
			return parseTag();
		}

		// operator?
		if (isOperator(data[currentIndex])) {
			++currentIndex;
			
			if(data[currentIndex - 1] == '-' && currentIndex < data.length && isDigit(data[currentIndex])) {
				Token h = parseNumberToken();

				if(h.getType().equals(TokenType.CONST_INT)) return new Token(TokenType.CONST_INT, -1 * (int) h.getValue());
				else return new Token(TokenType.CONST_DOUBLE, -1 * (double) h.getValue());
			
			}else {
				return new Token(TokenType.OPER, String.valueOf(data[currentIndex - 1]));
			}
			
		}

		// $} ?
		if (data[currentIndex] == '$') {
			if (currentIndex + 1 < data.length && data[currentIndex + 1] == '}') {
				currentIndex += 2;
				return new Token(TokenType.CLOSE_TAG, " $}");
			}

			else
				throw new LexerException("$ is only used when closing tags.");
		}
		// funkcija?
		if (data[currentIndex] == '@') {
			++currentIndex; // skip @
			return parseFunctionToken();
		}
		// varijabla?
		if (isAlphabetic(data[currentIndex])) {
			return parseVariableToken();
		}
		// string?
		if (data[currentIndex] == '"') {
			System.out.println(data[currentIndex]);
			++currentIndex;
			return parseStringToken();
		}
		// broj?
		if (isDigit(data[currentIndex])) {
			return parseNumberToken();
		}

		else
			throw new LexerException("");
	}
	
	/*
	 * 	Parses String Token and returns it's Token.
	 * 
	 * Throws {@link LexerException} if name is not valid.
	 * 
	 * In strings (and only in strings!) parser must accept following escaping: \\
	 * sequence treat as a single string character \
	 * \" treat as a single string character " (and not the end of the string) \n,
	 * \r and \t have its usual meaning (ascii 10, 13 and 9).
	 * 
	 * Places currentIndex on first non tokenized character.
	 */

	private Token parseStringToken() {
		StringBuilder resultB = new StringBuilder();
		
		while(currentIndex < data.length) {
			if(data[currentIndex] == '"') {
				++currentIndex;
				System.out.println("izlaz");
				break;
			}
			
			else if(data[currentIndex] == '\\') {
				if (currentIndex + 1 == data.length) throw  new LexerException("Invalid escape sequence");
				
				else if(data[currentIndex + 1] == '\\' || data[currentIndex + 1] == '"') {
					resultB.append(data[currentIndex + 1]);
					currentIndex += 2;
					continue;
				}
				else if(data[currentIndex + 1] == 'n') {
					resultB.append('\n');
					currentIndex += 2;
					continue;
				}
				else if(data[currentIndex + 1] == 'r') {
					resultB.append('\r');
					currentIndex += 2;
					continue;
				}
				else if(data[currentIndex + 1] == 't') {
					resultB.append('\t');
					currentIndex += 2;
					continue;
				}
				else throw  new LexerException("Invalid escape sequence");
			}
			
			resultB.append(data[currentIndex]);
			System.out.println(data[currentIndex]);
			++currentIndex;
		}
		
		return new Token(TokenType.STRING, resultB.toString());
	}

	/**
	 * Parses number and returns it's Token.
	 * Places currentIndex on first non tokenized character.
	 * Throws {@link LexerException} if number is not valid.
	 * @return Token
	 */
	private Token parseNumberToken() {
		int startIndex = currentIndex;
		while (currentIndex < data.length && (isDigit(data[currentIndex]) || data[currentIndex] == '.')) {
			++currentIndex;
		}

		if (currentIndex == data.length || isWhitespace(data[currentIndex]) || data[currentIndex] == '$') {
			String resultString = String.valueOf(Arrays.copyOfRange(data, startIndex, currentIndex));
			int result;
			double resultD;

			try {
				result = Integer.valueOf(resultString);
				return new Token(TokenType.CONST_INT, result);

			} catch (NumberFormatException e) {
				try {
					resultD = Double.valueOf(resultString);
					return new Token(TokenType.CONST_DOUBLE, resultD);

				}catch(NumberFormatException e2) {
					throw new LexerException("Wrong number format " + resultString);
				}
			}
		} else {
			throw new LexerException("Wrong number format");
		}
	}

	/*
	 * Parses Variable and it's token.
	 * 
	 * Places currentIndex on first non tokenized character.
	 * 
	 * Valid variable name starts by letter and after follows zero or more letters,
	 * digits or underscores. If name is not valid, it is invalid. This variable
	 * names are valid: A7_bb, counter, tmp_34; these are not: _a21, 32, 3s_ee etc.
	 * 
	 * Throws {@link LexerException} if name is not valid
	 * 
	 */
	private Token parseVariableToken() {
		String msg1 = "Variable name must start with a letter";
		String msg2 = msg1 + "and after than can follow zero or more letters, digits or underscores";
		String name = parseVariableOrFunctionName(msg1, msg2);
		return new Token(TokenType.VAR, name);
	}

	/*
	 * Parses function and it's token.
	 * 
	 * Places currentIndex on first non tokenized character.
	 * 
	 * Throws {@link LexerException} if name is not valid
	 * 
	 * Valid function name starts with @ after which follows a letter and after than
	 * can follow zero or more letters, digits or underscores. If function name is
	 * not valid, it is invalid.
	 */
	private Token parseFunctionToken() {
		String msg1 = "letter must follow @ symbol. Invalid function name";
		String msg2 = "Invalid function name. Valid function name starts with @"
				+ " after which follows a letter and after than can follow zero or more letters, digits or underscores.";

		String name = parseVariableOrFunctionName(msg1, msg2);
		return new Token(TokenType.FUNC, name);
	}

	/**
	 * Parses Tag and returns it's token. Tag starts with {$
	 * Places currentIndex on first non tokenized character.
	 * 
	 * Throws {@link LexerException} if name is not valid.
	 * 
	 * Places currentIndex on first non tokenized character.
	 * 
	 * @return Token
	 */
	private Token parseTag() {

		currentIndex += 2; // preskace "{$"

		skipWhitespaces();

		// kraj?
		if (currentIndex >= data.length) {
			throw new LexerException("Tag at the end of a file doesn't have type.");
		}

		// Mozda je =
		if (data[currentIndex] == '=') {
			++currentIndex;
			return new Token(TokenType.ECHO_TAG, "{$ = ");
		}

		String tag = String.valueOf(Arrays.copyOfRange(data, currentIndex, currentIndex + 4)).toUpperCase();

		currentIndex += 3;
		if (tag.startsWith("FOR")) {
			return new Token(TokenType.FOR_TAG, "{$ FOR ");
		} else if (tag.startsWith("END")) {
			return new Token(TokenType.END_TAG, "{$ END ");
		} else {
			throw new LexerException("Unsupported TAG type: " + "{$ " + tag);
		}
	}

	/**
	 * Checks if operator is valid Valid operators are + (plus), - (minus), *
	 * (multiplication), / (division), ^ (power)
	 * 
	 * @return true if it is
	 */
	private boolean isOperator(char c) {
		if (c == '+')
			return true;
		if (c == '-')
			return true;
		if (c == '*')
			return true;
		if (c == '/')
			return true;
		if (c == '^')
			return true;

		return false;
	}
	/**
	 * 
	 * Returns valid variable/function name.
	 * 
	 * Valid variable name starts by letter and after follows zero or more letters,
	 * digits or underscores. If name is not valid, it is invalid. This variable
	 * names are valid: A7_bb, counter, tmp_34; these are not: _a21, 32, 3s_ee etc.
	 * 
	 * Throws {@link LexerException} if name is not valid
	 * 
	 * @param msg1 throws if parsing a name cannot start  
	 * @param msg2 throws if parsing a name cannot finish
	 * @return String value
	 */
	private String parseVariableOrFunctionName(String msg1, String msg2) {
		int startIndex = currentIndex;

		if (currentIndex == data.length || !isAlphabetic(data[currentIndex])) {
			throw new LexerException(msg1);
		}

		++currentIndex;

		while (currentIndex < data.length
				&& (isAlphabetic(data[currentIndex]) || isDigit(data[currentIndex]) || data[currentIndex] == '_')
				&& !isWhitespace(data[currentIndex])) {			
			++currentIndex;
		}

		if (currentIndex < data.length && !(isWhitespace(data[currentIndex]) || data[currentIndex] == '$')) {
			throw new LexerException(msg2);
		}

		return String.valueOf(Arrays.copyOfRange(data, startIndex, currentIndex));

	}

	/**
	 * Skips all whitespaces and moves current Index to first non whitespace
	 */
	private void skipWhitespaces() {
		while (currentIndex < data.length && isWhitespace(data[currentIndex]) && data[currentIndex] != '\n') { 
			if(data[currentIndex] == '\n') System.out.println("eogaagaga");
			++currentIndex;
		}
	}

}
