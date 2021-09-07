package hr.fer.zemris.java.hw16.jvdraw;


import hr.fer.zemris.java.hw16.jvdraw.components.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.models.Line;

/**
 * State when Line Tool is used
 * @author Hrvoje
 *
 */
public class LineTool extends GeometricalShapeTool {

	/**
	 * Default Constructor
	 * @param model
	 * @param lineColor
	 */
	public LineTool(DrawingModel model, IColorProvider lineColor) {
		super(model, lineColor, lineColor);
	}

	
	@Override
	GeometricalObject createObject() {
		return new Line("linija", getStart(), getEnd(), super.getFgColorProvider().getCurrentColor());
	}

}
