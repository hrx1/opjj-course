package hr.fer.zemris.java.hw07.shell;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw07.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CptreeShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.DropdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.ListdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.MassrenameShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.PopdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.PushdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.PwdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.RmtreeShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.TreeShellCommand;

/**
 * Shell which has default environment and default commands.
 * Default environment outputs to Standard Output, and takes input from Standard Input.
 * Supported commands are: 
 *	cat
 *	charsets
 *	copy
 *	exit
 *	help
 *	hexdump
 *	ls
 *	mkdir
 *	symbol
 *	tree
 *	
 * @author Hrvoje
 *
 */
public class MyShell {
	
	/** Version of a Shell **/
	private static final String VERSION = "1.0";
	/** Greeting message **/
	private static final String GREETING_MESSAGE = "Welcome to my Shell v " + VERSION;
	/** Goodbye message **/
	private static final String GOODBYE_MESSAGE = "Goodbye!";
	
	/**
	 * Main method
	 * 
	 * @param args neglected
	 */
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		SortedMap<String, ShellCommand> commands = loadCommands();
		
		Environment environment = new StandardEnvironment(commands, input);
		ShellStatus status = ShellStatus.CONTINUE;
		
		System.out.println(GREETING_MESSAGE);
		
		do {			
			String[] splittedCommandLine = requestLineOrLines(environment).split(" ", 2); //0 - command name; 1 - command arguments
			
			ShellCommand command = commands.get(splittedCommandLine[0]);
			
			//command doesn't exist
			if(command == null) {
				System.out.println("Command doesn't exist: " + splittedCommandLine[0]);
				continue;
			}
			
			//If arguments exist, parse them. Store null otherwise
			String arguments;
			if (splittedCommandLine.length > 1) {
				arguments = splittedCommandLine[1];
			}else {
				arguments = null;
			}
			
			try {
				status = command.executeCommand(environment, arguments);
			}catch (ShellIOException e) {
				System.out.println("Command " + command.getCommandName() + " interupted. Terminating...");
				status = ShellStatus.TERMINATE;
			}
			
		}while(status != ShellStatus.TERMINATE);
		
		System.out.println(GOODBYE_MESSAGE);
	}


	/**
	 * Method requests lines from input. It doesn't close scanner. 
	 * Multiple lines can be processed at once if Morelines symbol is at the end.
	 * 
	 * In case of multiline input, the shell will
	 * concatenate all lines into a single line and removed MORELINES symbol from
	 * line endings (before concatenation). This way, the command will always get a
	 * single line with arguments.
	 * 
	 * @param env environment in which command is run
	 * @return lines
	 */
	private static String requestLineOrLines(Environment env) {
		
		//Parse first line:
		System.out.print(env.getPromptSymbol() + " ");
		String line = env.readLine();
		if (!line.endsWith(env.getMorelinesSymbol().toString())) {
			return line;
		}
		
		//Request other lines if necessary
		StringBuilder processedInput = new StringBuilder();
		processedInput.append(line);
		
		do {
			processedInput.deleteCharAt(processedInput.length() - 1); //delete multiline symbol
			System.out.print(env.getMultilineSymbol() + " ");
			line = env.readLine();
			processedInput.append(line);
		}while(line.endsWith(env.getMorelinesSymbol().toString()));
		
		return processedInput.toString();
	}

	@SuppressWarnings("serial")
	/**
	 * Loads commands and returns them as a Map
	 * @return map of commands
	 */
	private static SortedMap<String, ShellCommand> loadCommands() {
		List<ShellCommand> list = new LinkedList<>() {{
			add(new CatShellCommand());
			add(new ExitShellCommand());
			add(new CharsetsShellCommand());
			add(new MkdirShellCommand());
			add(new CopyShellCommand());
			add(new TreeShellCommand());
			add(new LsShellCommand());
			add(new HelpShellCommand());
			add(new SymbolShellCommand());
			add(new HexdumpShellCommand());
			add(new PwdShellCommand());
			add(new CdShellCommand());
			add(new PushdShellCommand());
			add(new PopdShellCommand());
			add(new ListdShellCommand());
			add(new DropdShellCommand());
			add(new RmtreeShellCommand());
			add(new CptreeShellCommand());
			add(new MassrenameShellCommand());
		}};
		
		SortedMap<String, ShellCommand> tmp = new TreeMap<>();
		list.forEach(o -> tmp.put(o.getCommandName(), o));
		
		return tmp;
	}
	
}
