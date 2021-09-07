package hr.fer.zemris.java.custom.scripting.elems;

/**
 *  Describes information about String stored in a Node
 * @author Hrvoje
 *
 */
public class ElementString extends Element{
	/**
	 * Value of a string
	 */
	private String value;
	
	/**
	 * Stores value of a string
	 * @param value of a string
	 */
	public ElementString(String value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return " \"" + String.valueOf(value).replace("\\", "\\\\").replace("\"", "\\\"") +"\"";
	}
}
