package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Describes Push Command. Command pushes copy of a first state of a context to context.
 * @author Hrvoje
 *
 */
public class PushCommand implements Command {

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState ts = ctx.getCurrentState().copy();
		ctx.pushState(ts);
	}

}
