package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellIOException;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command lists all files and directories in a given directory.
 * Expects 1 argument - directory to list.
 * Lists directory-read-write-executable, file size, time created and file name attributes.
 * 
 * @author Hrvoje
 *
 */
public class LsShellCommand implements ShellCommand {
	/* Command name */
	private static String name = "ls";
	
	/* Command description */
	@SuppressWarnings("serial")
	List<String> description= new ArrayList<>() {{
		add("Command lists all files and directories in a given directory.");
		add("Expects 1 argument - directory to list.");
		add("Lists directory-read-write-executable, file size, time created and file name attributes.");
	}};
	
	
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
			Files.walk(root, 1).forEach(o -> {
				try {
					if(!o.equals(root)) 
						env.writeln(formatPathAtributes(o));
				} catch (ShellIOException | IOException e) {
					env.writeln("File "+ o.getFileName() + " couldn't be analysed");
				}
			});
		} catch (IOException e) {
			env.writeln("No permission to opetn '" + root + "'");
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

	/**
	 * Formats file attributes.
	 * @param path of a file
	 * @return String with file attributes
	 * @throws IOException if path attributes cannot be viewed
	 */
	private static String formatPathAtributes(Path path) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
		BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);
		
		BasicFileAttributes attributes = faView.readAttributes();
		
		FileTime fileTime = attributes.creationTime();
		
		String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
		
		return String.format("%s %10d %10s %s", drwxGet(path), attributes.size(), formattedDateTime, path.getFileName());

	}
	
	/**
	 * Returns direcotry-read-write-executable linux-style format of a path.
	 * @param path to get drwx
	 * @return direcotry-read-write-executable linux-style format of a path
	 */
	private static String drwxGet(Path path) {
		StringBuilder result = new StringBuilder(4);
		
		if(Files.isDirectory(path)) result.append('d');
		else result.append('-');
		
		if(Files.isReadable(path)) result.append('r');
		else result.append('-');
		
		if(Files.isWritable(path)) result.append('w');
		else result.append('-');
		
		if(Files.isExecutable(path)) result.append('x');
		else result.append('x');
		
		return result.toString();
	}

}
