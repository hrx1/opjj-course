package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Rectangle;

/**
 * Calculates Boundaries for visited Objects.
 * 
 * @author Hrvoje
 *
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {
	/** Result */
	private Rectangle result;
	
	@Override
	public void visit(Line line) {
		
		if(result == null) {
			result = new Rectangle(line.getFirstPoint());
			result.add(line.getSecondPoint());
			return ;
		}
		
		result.add(line.getFirstPoint());
		result.add(line.getSecondPoint());
	}

	@Override
	public void visit(Circle circle) {
		checkBounds(
				circle.getCenter().x - circle.getRadius(),
				circle.getCenter().y - circle.getRadius(),
				circle.getCenter().x + circle.getRadius(),
				circle.getCenter().y + circle.getRadius()
				);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		visit((Circle) filledCircle);
	}

	/**
	 * Returns result
	 * @return result
	 */
	public Rectangle getBoundingBox() {
		return result;
	}
	
	/**
	 * Calculates new boundaries
	 * 
	 * @param minX Top Left X
	 * @param minY Top Left Y
	 * @param maxX Bottom Right X
	 * @param maxY Bottom Right Y
	 */
	private void checkBounds(int minX, int minY, int maxX, int maxY) {
		if(result == null) {
			result = new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
			return ;
		}
		
		result.add(minX, minY);
		result.add(maxX, maxY);

	}
}
