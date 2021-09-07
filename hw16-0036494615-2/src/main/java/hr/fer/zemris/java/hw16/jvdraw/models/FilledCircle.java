package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Color;
import java.awt.Point;

public class FilledCircle extends Circle {
	private Color fillColor;
	
	private static String geometricalObjectName = "FilledCircle";
	
	public FilledCircle(String name, Point center, int radius, Color outlineColor, Color fillColor) {
		super(name, center, radius, outlineColor);
		this.fillColor = fillColor;
	}

	public Color getFillColor() {
		return fillColor;
	}
	
	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}
	
	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}
	
	@Override
	public String getGeometricalObjectName() {
		return geometricalObjectName;
	}

	@Override
	public String getProperties() {
		return String.format("(%d,%d), %d, #%s",
				super.getCenter().x,
				super.getCenter().y,
				super.getRadius(),
				Integer.toHexString(fillColor.getRGB())
						.substring(2)
						.toUpperCase()
			);
	}
	
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	
}
