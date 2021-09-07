package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command which prints current directory in which shell is positioned.
 * Command takes no arguments.
 * 
 * @author Hrvoje
 *
 */
public class PwdShellCommand implements ShellCommand {
	/* Command name */
	private final String name = "pwd";
	
	/* Command description */
	@SuppressWarnings("serial")
	List<String> description = new ArrayList<>() {
		{
			add("Prints working directory.");
			add("Takes no arguments.");
		}
	};

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		Objects.requireNonNull(env);
		
		List<String> arguments;
		try {
		arguments = Util.parseArgsWithQuotedStrings(argument);
		}catch(IllegalArgumentException e) {
			env.writeln("Illegal arguments format!");
			return ShellStatus.CONTINUE;
		}
				
		if(arguments.size() != 0) {
			ShellCommand.invalidArgumentNumberMsg(env, this, arguments, 0);
			return ShellStatus.CONTINUE;
		}
		
		env.writeln(env.getCurrentDirectory().toString());
		
		return ShellStatus.CONTINUE;
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
