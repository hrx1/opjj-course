package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command removes recursively all files and folders from path given in arguments.
 * @author Hrvoje
 *
 */
public class RmtreeShellCommand implements ShellCommand {
	/* Command name */
	private final String name = "rmtree";
	
	/* Command description */
	@SuppressWarnings("serial")
	List<String> description = new ArrayList<>() {
		{
			add("Command removes all files and directories recursively.");
			add("Command takes 1 argument - path to directory");
			add("Command also removes given directory.");
		}
	};


	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		Util.requireArgumentsNotNull(env);
		
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
		
		Path root = env.getCurrentDirectory().resolve(arguments.get(0));
		
		if(!Files.exists(root)) {
			env.writeln("Cannot find '" + root + "'");
			return ShellStatus.CONTINUE;
		}
		
		if(!Files.isDirectory(root)) {
			env.writeln("Is not directory: '" + root + "'");
			return ShellStatus.CONTINUE;
		}

		try {
			Files.walk(root)
				.sorted((o1, o2) -> o2.compareTo(o1))
				.forEach(o -> {
					try {
						Files.delete(o);
					} catch (IOException e) {
						env.writeln("Cannot delete: " + o);
					}
				});
		} catch (IOException e) {
			env.writeln("Directory cannot be opened: '" + root + "'");
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
