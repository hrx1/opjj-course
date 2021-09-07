package hr.fer.zemris.java.hw16.jvdraw.models;

/**
 * GeometricalObjectVisitor
 * 
 * @author Hrvoje
 *
 */
interface GeometricalObjectVisitor {
	/**
	 * Line visitor
	 * @param line
	 */
	public abstract void visit(Line line);

	/**
	 * Circle Visitor
	 * @param circle
	 */
	public abstract void visit(Circle circle);

	/**
	 * FilledCircle Visitor
	 * @param filledCircle
	 */
	public abstract void visit(FilledCircle filledCircle);
}