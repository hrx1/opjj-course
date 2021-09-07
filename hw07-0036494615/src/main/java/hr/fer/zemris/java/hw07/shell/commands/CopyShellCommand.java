package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command copies source file to destination file/folder.
 * Cannot copy to a folder which doesn't exist.
 * 
 * @author Hrvoje
 *
 */
public class CopyShellCommand implements ShellCommand {
	/** Command name **/
	private static final String name = "copy";
	
	@SuppressWarnings("serial")
	/** Command description **/
	List<String> description = new ArrayList<>() {
		{
			add("Command copies source file to destination file/folder.");
			add("Cannot copy to a folder which doesn't exist.");
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
				
		if(arguments.size() != 2) {
			ShellCommand.invalidArgumentNumberMsg(env, this, arguments, 2);
			return ShellStatus.CONTINUE;
		}

		String source = arguments.get(0);
		String destination = arguments.get(1);
		
		Path sourcePath = env.getCurrentDirectory().resolve(source);
		Path destinationPath = env.getCurrentDirectory().resolve(destination);
		
		if(!Files.exists(sourcePath)) {
			env.writeln("Cannot find '" + sourcePath + "'");
			return ShellStatus.CONTINUE;
		}
		
		if(Files.isDirectory(sourcePath)) {
			env.writeln("First argument has to be file, but given: " + source);
			return ShellStatus.CONTINUE;
		}
				
		if(Files.isDirectory(destinationPath)) {
			destination += "//" + sourcePath.getFileName();
			destinationPath = Paths.get(destination);
		}
		
		try {
			Util.copyFiles(sourcePath, destinationPath);
		} catch(IOException io) {
			env.writeln("Error while reading/writing files. \n Check whether destination directory exists or is it writable.");
			return ShellStatus.CONTINUE;
		}
		
		env.writeln(source + " is copied to " + destination);
		
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