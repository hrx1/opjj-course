package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;


/**
 * Lists all directory paths written in internal stack in which pushd and popd commands push/pop paths.
 * @author Hrvoje
 *
 */
public class ListdShellCommand implements ShellCommand {

	/* Command name */
	private final String name = "listd";
	
	/* Command description */
	@SuppressWarnings("serial")
	List<String> description = new ArrayList<>() {
		{
			add("Lists all directory paths written in internal stack in which pushd and popd commands push/pop paths.");
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
		
		pathStack.forEach(o -> env.writeln(o.toString()));
		
		return ShellStatus.CONTINUE;
	}

}
