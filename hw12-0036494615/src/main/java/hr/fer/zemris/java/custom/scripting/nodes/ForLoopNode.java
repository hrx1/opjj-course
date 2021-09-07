package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Node which stores every element inside {$FOR tag
 * Requires variable, start, end and (optionally, can be null) step values.
 * @author Hrvoje
 *
 */
public class ForLoopNode extends Node {
	private ElementVariable variable;
	private Element startExpression;
	private Element endExpression;
	private Element stepExpression;  //can be null
	
	/**
	 * Constructor for ForLoopNode
	 * @param variable 
	 * @param startExpression
	 * @param endExpression 
	 * @param stepExpression 
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
		super();
		
		Objects.requireNonNull(variable);
		Objects.requireNonNull(startExpression);
		Objects.requireNonNull(endExpression);
		
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}
	
	/**
	 * Getter for variable
	 * @return variable
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Getter for start expression
	 * @return start
	 */
	public Element getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Getter for end expression
	 * @return end
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Getter for step expression. Can return null.
	 * @return step
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	@Override
	public String nodeAndElementsAsText() {
		String step = (stepExpression == null)?"" : stepExpression.asText() + " ";
		return "{$FOR " + variable.asText() + " " + startExpression.asText() + " " + endExpression.asText() + " " + step + "$}"; 
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
	
}
