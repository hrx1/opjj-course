package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command outputs all files and folders recursively.
 * Each level is indented with two whitespaces more then level before.
 * 
 * @author Hrvoje
 *
 */
public class TreeShellCommand implements ShellCommand {
	/* Command name */
	private final String name = "tree";
	
	/* Command description */
	@SuppressWarnings("serial")
	List<String> description = new ArrayList<>() {
		{
			add("Command outputs all files and folders recursively.");
			add("Each level is indented with two whitespaces more then level before.");
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

		FileVisitor<Path> fv = new TreeWalker(env);
		try {
			Files.walkFileTree(root, fv);
		} catch (IOException e) {
			env.writeln("Cannot tree given path. Path must lead to directory");
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
	 * FileVisitor which ooutputs tree-like structure to Environment output.
	 * @author Hrvoje
	 *
	 */
	private static class TreeWalker implements FileVisitor<Path> {
		/* Current level */
		private int currentLevel = 0;
		/* Environment */
		private Environment env;
		/* Cached delimiter of a level */
		private StringBuilder space;
		/* Default delimiter */
		private static final Character DELIMITER = ' ';
		
		public TreeWalker(Environment env) {
			this.env = env;
			space = new StringBuilder();
		}

		@Override
		public FileVisitResult postVisitDirectory(Path arg0, IOException arg1) throws IOException {
			currentLevel -= 2;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path arg0, BasicFileAttributes arg1) throws IOException {
			env.writeln(space.substring(0, currentLevel) + arg0.getFileName().toString());
			currentLevel += 2;
			if(space.length() < currentLevel) {
				space.append(DELIMITER).append(DELIMITER);
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path arg0, BasicFileAttributes arg1) throws IOException {
			env.writeln(space.substring(0, currentLevel) + arg0.getFileName().toString());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path arg0, IOException arg1) throws IOException {
			return FileVisitResult.CONTINUE;
		}
		
	}

}
