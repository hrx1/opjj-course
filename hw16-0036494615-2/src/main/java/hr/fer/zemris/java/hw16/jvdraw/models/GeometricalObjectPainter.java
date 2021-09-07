package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Graphics2D;

/**
 * Paints all visited component.
 * 
 * @author Hrvoje
 *
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {
	private Graphics2D g;
	
	/**
	 * Constructor
	 * @param g graphics
	 */
	public GeometricalObjectPainter(Graphics2D g) {
		this.g = g;
	}

	@Override
	public void visit(Line line) {
		int x1 = line.getFirstPoint().x;
		int x2 = line.getSecondPoint().x;
		int y1 = line.getFirstPoint().y;
		int y2 = line.getSecondPoint().y;
		
		g.setColor(line.getColor());
		
		g.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void visit(Circle circle) {
		int x = circle.getCenter().x;
		int y = circle.getCenter().y;
		int radius = circle.getRadius();
		
		int topLeftX = x - radius;
		int topLeftY = y - radius;
		
		g.setColor(circle.getColor());

		g.drawOval(topLeftX, topLeftY, radius*2, radius*2);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		int x = filledCircle.getCenter().x;
		int y = filledCircle.getCenter().y;
		int radius = filledCircle.getRadius();
		
		int topLeftX = x - radius;
		int topLeftY = y - radius;
		
		
		g.setColor(filledCircle.getFillColor());
		g.fillOval(topLeftX, topLeftY, radius*2, radius*2);
		g.setColor(filledCircle.getColor());
		g.drawOval(topLeftX, topLeftY, radius*2, radius*2);
	}

}
