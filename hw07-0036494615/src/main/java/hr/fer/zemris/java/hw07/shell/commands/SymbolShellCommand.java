package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command prints or changes symbol given in argument.
 * Supported symbols are: MULTILINE, PROMPT, MORELINES.
 * If second argument is given, it is interpreted as new symbol value.
 * @author Hrvoje
 *
 */
public class SymbolShellCommand implements ShellCommand {
	/* Command name */
	private final String name = "symbol";
	
	/* Command description */
	@SuppressWarnings("serial")
	List<String> description = new ArrayList<>() {
		{
			add("Command prints or changes symbol given in argument.");
			add("Supported symbols are: MULTILINE, PROMPT, MORELINES.");
			add("If second argument is given, it is interpreted as new symbol value.");
		}
	};

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		Util.requireArgumentsNotNull(env);
		
		List<String> arguments = Util.parseArguments(argument);
		
		if(arguments.size() == 1) {
			Character wantedSymbol = null;
			
			switch (arguments.get(0)) {
			case "PROMPT":
				wantedSymbol = env.getPromptSymbol();
				break;
			case "MORELINES":
				wantedSymbol = env.getMorelinesSymbol();
				break;
			case "MULTILINE":
				wantedSymbol = env.getMultilineSymbol();
				break;
			default:
				env.writeln("Wanted symbol doesn't exist.");
				return ShellStatus.CONTINUE;
			}
			
			env.writeln("Symbol for " + arguments.get(0) + " is '" + wantedSymbol + "'");
		}
		else if(arguments.size() == 2) {
			Character oldSymbol = null;
			switch (arguments.get(0)) {
			case "PROMPT":
				oldSymbol = env.getPromptSymbol();
				env.setPromptSymbol(arguments.get(1).charAt(0));
				break;
			case "MORELINES":
				oldSymbol = env.getMorelinesSymbol();
				env.setMorelinesSymbol(arguments.get(1).charAt(0));
				break;
			case "MULTILINE":
				oldSymbol = env.getMultilineSymbol();
				env.setMultilineSymbol(arguments.get(1).charAt(0));
				break;
			default:
				env.writeln("Wanted symbol doesn't exist.");
				return ShellStatus.CONTINUE;
			}
			
			env.writeln("Symbol for " + arguments.get(0) + " changed from '" + oldSymbol + "'"
					+ "to '" + arguments.get(1) + "'");

		}else {
			ShellCommand.invalidArgumentNumberMsg(env, this, arguments, 1, 2);
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
