package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Class describes Token which Lexer can recognize. Token Types are defined in a TokenType class
 * @author Hrvoje
 *
 */
public class Token {
	/** Type of a token */
	TokenType type;
	/** Value stored in a token */
	Object value;

	/**
	 * Constructor for a Token with type type and value value
	 * @param type type
	 * @param value value
	 */
	public Token(TokenType type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}

	/**
	 * Getter for a value
	 * @return value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Getter for a type
	 * @return type
	 */
	public TokenType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		String sValue = (value == null)? "null" : value.toString();
		String sType = type.toString();
		
		return "(" + sType + ", " + sValue + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
	
}
