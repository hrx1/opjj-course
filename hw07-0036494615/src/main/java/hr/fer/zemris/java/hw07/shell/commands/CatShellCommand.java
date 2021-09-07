package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command opens given file and writes its content to console.
 * The first argument is path to some file and is mandatory.
 * Second argument is charset name that should be used to interpret chars from bytes.
 * 
 * @author Hrvoje
 *
 */
public class CatShellCommand implements ShellCommand {
	/** Command name **/
	static final String commandName = "cat";
	
	@SuppressWarnings("serial")
	/** Command description **/
	List<String> description= new ArrayList<>() {{
		add("Command opens given file and writes its content to console");
		add("The first argument is path to some file and is mandatory.");
		add("Second argument is charset name that should be used to interpret chars from bytes.");
	}};
	
	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		
		List<String> arguments;
		try {
			arguments = Util.parseArgsWithQuotedStrings(argument);
		}catch(IllegalArgumentException e) {
			env.writeln("Illegal arguments format!");
			return ShellStatus.CONTINUE;
		}
		
		//check arguments
		if(arguments.size() != 1 && arguments.size() != 2) {
			ShellCommand.invalidArgumentNumberMsg(env, this, arguments, 1, 2);
			return ShellStatus.CONTINUE;
		}
		
		//set charset
		Charset charset;
		if(arguments.size() == 2) {
			try {
			charset = Charset.forName(arguments.get(1));
			}catch(UnsupportedCharsetException e) {
				env.writeln("Unsupported Charset '" + arguments.get(1) + "'");
				return ShellStatus.CONTINUE;
			}
		} else {
			charset = Charset.defaultCharset();
		}
		
		//can be "catted"?
		String inputFile = arguments.get(0);
		Path inputFilePath = env.getCurrentDirectory().resolve(inputFile);
		
		if(Files.isDirectory(inputFilePath)) {
			env.writeln("Given path is a directory. Path: " + inputFile);
			return ShellStatus.CONTINUE;
		}
		
		//write file to stdout
		BufferedReader bf;
		try {
			bf = Files.newBufferedReader(inputFilePath, charset);
		} catch (MalformedInputException mie) {
			env.writeln("File " + inputFile + " cannot be opened with charset " + charset.toString());
			return ShellStatus.CONTINUE;
		} catch (IOException e) {
			env.writeln("File " + inputFile+ " doesn't exist");
			return ShellStatus.CONTINUE;
		}
		
		bf.lines().forEach(o1 -> {
			env.writeln(new String(o1.getBytes(), charset));
		});
				
		env.write("\n"); //flusha
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public List<String> getCommandDescription() {
		return description;
	}

}
