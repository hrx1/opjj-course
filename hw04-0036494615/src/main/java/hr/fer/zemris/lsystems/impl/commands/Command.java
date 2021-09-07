package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Interface for Commands
 * @author Hrvoje
 *
 */
public interface Command {
	
	/**
	 * Executes Command on Context and Painter
	 * @param ctx Context 
	 * @param painter Painter
	 */
	void execute(Context ctx, Painter painter);
}
