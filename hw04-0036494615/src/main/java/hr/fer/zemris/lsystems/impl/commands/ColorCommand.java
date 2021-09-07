package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.commands.Command;

/**
 * Class describes ColorCommand which operates on State from context.
 * @author Hrvoje
 *
 */
public class ColorCommand implements Command {
	/** To set **/
	private Color color;
	
	/**
	 * Constructor for ColorCommand
	 * @param color to be set
	 */
	public ColorCommand(Color color) {
		super();
		this.color = color;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setColor(color);
	}

}
