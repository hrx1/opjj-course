package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Describes Double stored in a Node
 * @author Hrvoje
 *
 */
public class ElementConstantDouble extends Element{
	/**
	 * Value stored in element
	 */
	private double value;
	
	/**
	 * Stores value in element
	 * @param value
	 */
	public ElementConstantDouble(double value) {
		super();
		this.value = value;
	}

	@Override
	public String asText() {
		return String.valueOf(value);
	}
}
