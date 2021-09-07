package hr.fer.zemris.java.hw07.shell;

import java.util.List;

/**
 * Each ShellCommand should provide getter for name and description, and execution of a command.
 * 
 * @author Hrvoje
 *
 */
public interface ShellCommand {

	/**
	 * Executes command
	 * @param env environment in which command is ran 
	 * @param arguments passed to a command
	 * @return ShellStatus.CONTINUE
	 */
	ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Getter for command name
	 * @return name
	 */
	String getCommandName();

	/**
	 * Getter for command description
	 * @return command description
	 */
	List<String> getCommandDescription();
	
	/**
	 * Prints message which states that illegal number of arguments is passed.
	 * @param env environment in which command was run
	 * @param command which was run
	 * @param arguments passed to a command
	 * @param expectedArguments number of arguments accepted by command
	 */
	static void invalidArgumentNumberMsg(Environment env, ShellCommand command, List<String> arguments, int ... expectedArguments) {
		StringBuilder sb = new StringBuilder();
		sb.append(command.getCommandName());
		sb.append(" expects ");
		
		for(int expected : expectedArguments) {
			sb.append(expected);
			sb.append(" or ");
		}
		
		sb.setLength(sb.length() - 3); //delete last 'or '
		
		sb.append("argument(s). Given ");
		
		sb.append( + arguments.size());
		
		env.writeln(sb.toString());
	}
}
