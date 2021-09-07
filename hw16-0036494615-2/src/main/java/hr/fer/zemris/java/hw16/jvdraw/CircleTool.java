package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.components.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.models.Circle;
import hr.fer.zemris.java.hw16.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObject;

public class CircleTool extends GeometricalShapeTool {
	
	public CircleTool(DrawingModel model, IColorProvider color) {
		super(model, color, color);
	}

	@Override
	GeometricalObject createObject() {
		Point center = super.getStart();
		int radius = (int) Math.ceil(super.getStart().distance(super.getEnd()));
		return new Circle("Circle", center, radius, super.getFgColorProvider().getCurrentColor());
	}

}
