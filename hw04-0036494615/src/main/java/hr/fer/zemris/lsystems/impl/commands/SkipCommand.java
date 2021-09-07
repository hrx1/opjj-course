package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Describes Skip command. Skip translates Turtle without leaving a footprint.
 * @author Hrvoje
 *
 */
public class SkipCommand implements Command {
	/** Turtle moves step * length length */
	private double step;
		
	/**
	 * Constructor for skip command
	 * @param step of a turtle
	 */
	public SkipCommand(double step) {
		super();
		this.step = step;
	}



	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().move(step);
	}

}
