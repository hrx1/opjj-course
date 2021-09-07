package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.components.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.models.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObject;

/**
 * Filled Circle Tool
 * @author Hrvoje
 *
 */
public class FilledCircleTool extends GeometricalShapeTool {
	
	/**
	 * Constructor
	 * @param model
	 * @param color
	 * @param bgColor
	 */
	public FilledCircleTool(DrawingModel model, IColorProvider color, IColorProvider bgColor) {
		super(model, color, bgColor);
	}

	@Override
	GeometricalObject createObject() {
		Point center = super.getStart();
		int radius = (int) Math.ceil(super.getStart().distance(super.getEnd()));
		
		return new FilledCircle(
				"Circle", 
				center, 
				radius, 
				super.getFgColorProvider().getCurrentColor(), 
				super.getBgColorProvider().getCurrentColor()
			);
	}

}
