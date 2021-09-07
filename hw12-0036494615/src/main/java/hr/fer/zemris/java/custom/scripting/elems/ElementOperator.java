package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Describes information about a function stored in a Node
 * @author Hrvoje
 *
 */
public class ElementOperator extends Element {
	/**
	 * Symbol of operator
	 */
	private String symbol;
	
	/**
	 * Stores symbol of operator
	 * @param value operator
	 */
	public ElementOperator(String value) {
		this.symbol = value;
	}

	@Override
	public String asText() {
		return symbol;
	}
}
