package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Describese Rotate command. Command rotates turtle by angle degrees counter-clockwise
 * @author Hrvoje
 *
 */
public class RotateCommand implements Command {
	/** Angle in degrees **/
	private double angle;
	
	/**
	 * Constructor for Rotate Command. Angle is in degrees.
	 * @param angle
	 */
	public RotateCommand(double angle) {
		super();
		this.angle = angle;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().rotate(angle);
	}

}
