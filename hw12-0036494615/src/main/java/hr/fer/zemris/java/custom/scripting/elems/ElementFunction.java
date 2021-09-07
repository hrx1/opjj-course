package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Describes information about a function stored in a Node
 * @author Hrvoje
 *
 */
public class ElementFunction extends Element {
	/**
	 * Function name
	 */
	private String name;
	
	/**
	 * Stores function name
	 * @param value
	 */
	public ElementFunction(String value) {
		this.name = value;
	}

	@Override
	public String asText() {
		return "@" + String.valueOf(name);
	}
}
