package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command creates one or more directories.
 * Will create parent directories if they don't exist.
 * 
 * @author Hrvoje
 *
 */
public class MkdirShellCommand implements ShellCommand {
	/** Command name **/
	private final String name = "mkdir";
	
	@SuppressWarnings("serial")
	/* Command description */
	List<String> description = new ArrayList<>() {
		{
			add("Command creates one or more directories.");
			add("Will create parent directories if they don't exist.");
		}
	};

	
	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		Util.requireArgumentsNotNull(env, argument);
		
		List<String> arguments;
		try {
		arguments = Util.parseArgsWithQuotedStrings(argument);
		}catch(IllegalArgumentException e) {
			env.writeln("Illegal arguments format!");
			return ShellStatus.CONTINUE;
		}
				
		if(arguments.size() != 1) {
			ShellCommand.invalidArgumentNumberMsg(env, this, arguments, 1);
			return ShellStatus.CONTINUE;
		}
		
		Path dirPath = env.getCurrentDirectory().resolve(arguments.get(0)); 
		
		try {
			Files.createDirectories(dirPath);
			env.writeln("Successfully created map: " + dirPath);
		} catch (InvalidPathException e1) {
			env.writeln("invalid path: " + dirPath);
		} catch (IOException e4) {
			env.writeln("Cannot perform mkdir. Check privilages. Directory: " + dirPath);
		}
		
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
