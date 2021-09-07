package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Node which stores every element inside {$= tag
 * @author Hrvoje
 *
 */
public class EchoNode extends Node {
	/** Elements inside {$= tag */
	private Element[] elements;
	
	/**
	 * Constructor for EchoNode
	 * @param elements stored
	 */
	public EchoNode(Element[] elements) {
		super();
		this.elements = elements;
	}

	/**
	 * Getter for elements
	 * @return array of elements
	 */
	public Element[] getElements() {
		return elements;
	}
	
	@Override
	public String nodeAndElementsAsText() {
		String s = "{$=";
		for(Element e : elements) {
			s += " " + e.asText();
		}
		return s + " $}";
	}
	
}
