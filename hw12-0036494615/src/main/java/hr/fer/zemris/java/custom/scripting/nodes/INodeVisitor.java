package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Describes visitor of Parser Nodes.
 * 
 * @author Hrvoje
 *
 */
public interface INodeVisitor {
	/**
	 * Operation done with Text Node
	 * @param node Text Node
	 */
	public void visitTextNode(TextNode node);
	/**
	 * Operation done with For Loop Node
	 * @param node For Loop Node
	 */
	public void visitForLoopNode(ForLoopNode node);
	/**
	 * Operation done with Echo Node
	 * @param node Echo node
	 */
	public void visitEchoNode(EchoNode node);
	
	/**
	 * Operation done with Document Node
	 * @param node Document Node
	 */
	public void visitDocumentNode(DocumentNode node);
}
