package hr.fer.zemris.java.hw16.jvdraw.models;

/**
 * Visitor which calculates format for storing visited Objects.
 * 
 * @author Hrvoje
 *
 */
public class GeometricalObjectFormater implements GeometricalObjectVisitor {
	/** Result */
	StringBuilder result = new StringBuilder();

	@Override
	public void visit(Line line) {
		int[] rgb = GeometricalObjectEditor.getRGB(Integer.toHexString(
				line.getColor()
				.getRGB())
				.substring(2));
		
		String lineResult = String.format("LINE %d %d %d %d %d %d %d\n", 
				line.getFirstPoint().x, 
				line.getFirstPoint().y, 
				line.getSecondPoint().x,
				line.getSecondPoint().y,
				rgb[0],
				rgb[1],
				rgb[2]);
		result.append(lineResult);
	}

	@Override
	public void visit(Circle circle) {
		int [] rgb =  GeometricalObjectEditor.getRGB(Integer.toHexString(
				circle.getColor()
				.getRGB())
				.substring(2));
		
		String lineResult = String.format("CIRCLE %d %d %d %d %d %d\n",
				circle.getCenter().x,
				circle.getCenter().y,
				circle.getRadius(),
				rgb[0],
				rgb[1],
				rgb[2]
				);
		
		result.append(lineResult);

	}

	@Override
	public void visit(FilledCircle filledCircle) {
		int [] rgb =  GeometricalObjectEditor.getRGB(Integer.toHexString(
				filledCircle.getColor()
				.getRGB())
				.substring(2));
		
		int [] rgb2 =  GeometricalObjectEditor.getRGB(Integer.toHexString(
				filledCircle.getFillColor()
				.getRGB())
				.substring(2));
		
		String lineResult = String.format("FCIRCLE %d %d %d %d %d %d %d %d %d\n",
				filledCircle.getCenter().x,
				filledCircle.getCenter().y,
				filledCircle.getRadius(),
				rgb[0],
				rgb[1],
				rgb[2],
				rgb2[0],
				rgb2[1],
				rgb2[2]
				);		
		result.append(lineResult);
	}
	
	
	/**
	 * Returns results
	 * @return
	 */
	public String getResult() {
		return result.toString();
	}

}
