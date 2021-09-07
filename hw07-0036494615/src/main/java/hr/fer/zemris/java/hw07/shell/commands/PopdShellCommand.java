package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Pops directory path from internal stack.
 * Sets current directory to one popped from stack.
 * 
 * @author Hrvoje
 *
 */
public class PopdShellCommand implements ShellCommand {

	/* Command name */
	private final String name = "popd";
	
	/* Command description */
	@SuppressWarnings("serial")
	List<String> description = new ArrayList<>() {
		{
			add("Pops directory path from internal stack.");
			add("Sets current directory to one popped from stack");
		} 
	};	
	
	/* Key under which stack is stored in Shared Data */
	private static final String STACK_KEY = "cdstack";

	
	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		return description;
	}

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

		@SuppressWarnings("unchecked")
		Stack<Path> pathStack = (Stack<Path>) env.getSharedData(STACK_KEY);
		
		if(pathStack == null || pathStack.isEmpty()) {
			env.writeln("No paths previously saved. ");
			return ShellStatus.CONTINUE;
		}
		
		Path poppedPath = pathStack.pop();
		
		if(Files.isDirectory(poppedPath)) {
			env.setCurrentDirectory(poppedPath);
		}
		
		return ShellStatus.CONTINUE;
	}
	

}
