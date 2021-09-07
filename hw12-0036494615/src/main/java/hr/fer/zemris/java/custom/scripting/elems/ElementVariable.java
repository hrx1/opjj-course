package hr.fer.zemris.java.custom.scripting.elems;

/**
 *  Describes information about Variable stored in a Node
 * @author Hrvoje
 *
 */
public class ElementVariable extends Element {
	/**
	 * Name of a variable
	 */
	private String name;
	
	/**
	 * Stores name of a variable
	 * @param name
	 */
	public ElementVariable (String name) {
		this.name = name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	

}
