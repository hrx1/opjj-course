package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
/**
 * Class abstracts node in syntax tree 
 * @author Hrvoje
 *
 */
public class Node {
	/** Children of a Node in a tree	 */
	ArrayIndexedCollection children;
	
	/**
	 * Adds child to a node
	 * @param child to add
	 */
	public void addChildNode (Node child) {
		Objects.requireNonNull(child);
		
		if(children == null) {
			children = new ArrayIndexedCollection();	
		}
		
		children.add(child);
	}
	
	/**
	 * Returns number of direct children of a node.
	 * @return number of direct children of a node.
	 */
	public int numberOfChildren() {
		if(children == null) return 0;
		else return children.size();
	}
	
	/***
	 * Returns child at index. 
	 * Throws {@link IndexOutOfBoundsException} if index is out of bounds
	 * @param index of a child
	 * @return child node
	 */
	public Node getChild(int index) {
		try {
			return (Node) children.get(index);
		}catch(IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("Cannot get child on index " + index);
		}
	}
	
	/**
	 * Returns String representation of a Node and it's values.
	 * Doesn't include information about child nodes.
	 * @return
	 */
	public String nodeAndElementsAsText() {
		return "";
	}
}
