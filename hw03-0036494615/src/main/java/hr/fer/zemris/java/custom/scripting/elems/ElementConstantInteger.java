package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Describes Integer stored in a Node
 * @author Hrvoje
 *
 */
public class ElementConstantInteger extends Element {
	/**
	 * Value stored in element
	 */
	private int value;
	
	/**
	 * Stores value in element
	 * @param value
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return String.valueOf(value);
	}
}
