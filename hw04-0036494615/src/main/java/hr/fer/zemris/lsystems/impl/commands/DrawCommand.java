package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Describes Skip command. Skip translates Turtle with leaving a footprint of color of a turtle.
 * @author Hrvoje
 *
 */
public class DrawCommand implements Command{
	/** Turtle moves step * length length **/
	double step;
	
	/**
	 * Constructor for draw command
	 * @param step step
	 */
	public DrawCommand(double step) {
		super();
		this.step = step;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState turtle = ctx.getCurrentState();
		
		double startX = turtle.getPosition().getX();
		double startY = turtle.getPosition().getY();
				
		turtle.move(step);
		
		double endX = turtle.getPosition().getX();
		double endY = turtle.getPosition().getY();

		painter.drawLine(startX, startY, endX, endY, turtle.getColor(), 1);
	}
	
	
	
}
