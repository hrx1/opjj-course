package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command returns informations about commands. 
 * If no arguments are given, all commands will be listed.
 * If one argument is given, it will print description of command whose name is argument.
 * 
 * @author Hrvoje
 *
 */
public class HelpShellCommand implements ShellCommand {
	/** Command Name **/
	private static final String name = "help";
	
	@SuppressWarnings("serial")
	/** Description of command **/
	List<String> description = new ArrayList<>() {
		{
			add("Command returns informations about commands.");
			add("If no arguments are given, all commands will be listed.");
			add("If one argument is given, it will print description of command whose name is argument.");
		}
	};

	
	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		Objects.requireNonNull(env);
		
		List<String> arguments = Util.parseArguments(argument);
		
		if(arguments.size() == 0) {
			env.writeln("Supported commands are: ");
			env.commands().keySet().forEach(o -> env.writeln("\t" + o));
		}
		else if(arguments.size() == 1) {
			ShellCommand tmp = env.commands().get(arguments.get(0));
			
			if(tmp == null) {
				env.writeln("Command not supported: " + arguments.get(0));
			}else {
				tmp.getCommandDescription().forEach(o -> env.writeln("\t" + o));
			}
			
		}
		else {
			ShellCommand.invalidArgumentNumberMsg(env, this, arguments, 0, 1);
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
