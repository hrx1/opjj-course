package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Context;


/**
 * Describes Scale command. Scales first turtle length from context by factor.
 * @author Hrvoje
 *
 */
public class ScaleCommand implements Command {
	/** To scale with **/
	private double factor;
	
	/**
	 * Constructor for scale command
	 * @param factor
	 */
	public ScaleCommand(double factor) {
		super();
		this.factor = factor;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().scaleLength(factor);
	}

}
