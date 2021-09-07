package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node which contains text, not string. String parses in Expression mode, while 
 * Text parses in TEXT mode.
 * @author Hrvoje
 *
 */
public class TextNode extends Node {
	/** parsed text */
	private String text;
	
	/**
	 * Constructor for Text Node
	 * @param text to be stored
	 */
	public TextNode(String text) {
		this.text = text;
	}
	
	/**
	 * Getter for text
	 * @return text
	 */
	public String getText() {
		return text;
	}
	
	@Override
	public String nodeAndElementsAsText() {
		return text.replace("\\", "\\\\").replace("{", "\\{");
	}

}
