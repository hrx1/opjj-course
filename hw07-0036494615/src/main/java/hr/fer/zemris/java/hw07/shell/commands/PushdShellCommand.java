package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command pushes current directory to internal stack,
 * and sets new current directory as one given in arguments.
 * @author Hrvoje
 *
 */
public class PushdShellCommand implements ShellCommand {
	/* Command name */
	private final String name = "pushd";
	/* Command description */
	@SuppressWarnings("serial")
	List<String> description = new ArrayList<>() {
		{
			add("Command pushes current directory to internal stack");
			add("Sets new current directory as one given in arguments.");
		} 
	};	
	
	/* Key under which stack is stored in Shared Data */
	private static final String STACK_KEY = "cdstack";

	
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
		
		Path newDir = env.getCurrentDirectory().resolve(arguments.get(0));

		if(!Files.isDirectory(newDir)) {
			env.writeln("Cannot change directory to: " + newDir); 
			return ShellStatus.CONTINUE;
		}
		
		@SuppressWarnings("unchecked")
		Stack<Path> pathStack = (Stack<Path>) env.getSharedData(STACK_KEY);
		
		if(pathStack == null) {
			pathStack = new Stack<Path>();
			env.setSharedData(STACK_KEY, pathStack);
		}
		
		pathStack.add(env.getCurrentDirectory());
		env.setCurrentDirectory(newDir);
		
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
