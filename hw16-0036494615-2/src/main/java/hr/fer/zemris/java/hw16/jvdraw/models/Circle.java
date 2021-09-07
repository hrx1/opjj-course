package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JTextField;

public class Circle extends GeometricalObject {
	private int radius;
	private Point center;
	
	private static String geometricalObjectName = "Circle";
	
	public Circle(String name, Point center, int radius, Color color) {
		super(name, color);
		this.radius = radius;
		this.center = center;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}

	/**
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	/**
	 * @return the center
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * @param center the center to set
	 */
	public void setCenter(Point center) {
		this.center = center;
	}

	@Override
	public String getGeometricalObjectName() {
		return geometricalObjectName;
	}

	@Override
	public String getProperties() {
		return String.format("(%d,%d), %d", center.x, center.y, radius);
	}

	
	
	

}
