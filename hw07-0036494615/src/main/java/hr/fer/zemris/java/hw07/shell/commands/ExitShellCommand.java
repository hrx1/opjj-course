package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command closes console.
 * Command has no parameters.
 * 
 * It's execution sets ShellState to TERMINATE.
 * 
 * @author Hrvoje
 *
 */
public class ExitShellCommand implements ShellCommand {
	/** Command name **/
	private static final String name = "exit";
	
	@SuppressWarnings("serial")
	/** Command description **/
	List<String> description= new ArrayList<>() {{
		add("Command closes console.");
		add("Command has no parameters.");
	}};
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if(arguments != null && arguments.length() != 0) {
			ShellCommand.invalidArgumentNumberMsg(env, this, Util.parseArguments(arguments), 0);
			return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.TERMINATE;
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		return description;
	}

}
