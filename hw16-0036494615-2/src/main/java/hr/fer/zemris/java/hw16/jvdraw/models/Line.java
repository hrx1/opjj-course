package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Color;
import java.awt.Point;

/**
 * Line Class
 * @author Hrvoje
 *
 */
public class Line extends GeometricalObject {
	/** Points which define a line */
	private Point first, second;
	
	private static final String geometricalObjectName = "Line";
	
	/**
	 * Constructor
	 * @param name Of line
	 * @param first Point
	 * @param second Point
	 * @param color Color
	 */
	public Line(String name, Point first, Point second, Color color) {
		super(name, color);
		this.first = first;
		this.second = second;
	}
	
	/**
	 * @return the first
	 */
	public Point getFirstPoint() {
		return first;
	}

	/**
	 * @param first the first to set
	 */
	public void setFirstPoint(Point first) {
		this.first = first;
	}

	/**
	 * @return the second
	 */
	public Point getSecondPoint() {
		return second;
	}

	/**
	 * @param second the second to set
	 */
	public void setSecondPoint(Point second) {
		this.second = second;
	}



	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}
	
	
	@Override
	public String getGeometricalObjectName() {
		return geometricalObjectName;
	}

	@Override
	public String getProperties() {
		return String.format("(%d,%d)-(%d,%d)", first.x, first.y, second.x, second.y);
	}
	

}
