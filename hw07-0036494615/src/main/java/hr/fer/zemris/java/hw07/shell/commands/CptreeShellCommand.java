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
 * Command copies all files recursively to destination file/directory.
 * Command takes two arguments - SOURCE and DESTINATION directory.
 * If SOURCE directory exists, then SOURCE directory won't be created,
 *  only his files and directories will be created
 * SOURCE will be created otherwise.
 * 
 * @author Hrvoje
 *
 */
public class CptreeShellCommand implements ShellCommand {
	/** Command name **/
	private static final String name = "cptree";
	
	@SuppressWarnings("serial")
	/** Command description **/
	List<String> description = new ArrayList<>() {
		{
			add("Command copies all files recursively to destination file/folder.");
			add("Command takes two arguments - SOURCE and DESTINATION directory.");
			add("If SOURCE directory exists, then SOURCE directory won't be created.");
			add("\tOnly his files and directories will be created.");
			add("SOURCE will be created otherwise.");
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
		
		Path sourcePath = env.getCurrentDirectory().resolve(source).normalize();
		Path destinationPath = env.getCurrentDirectory().resolve(destination).normalize();
		
		try {
			Files.walkFileTree(sourcePath, new CopyFileTree(sourcePath, destinationPath, env));
		} catch (IOException e) {
			env.writeln("Couldn't open " + sourcePath);
			e.printStackTrace();
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
	 * File Visitor which copies files and folders to path given in a constructor.
	 * @author Hrvoje
	 *
	 */
	private static class CopyFileTree implements FileVisitor<Path> {
		/** In which files will be copied **/
		private Path destination;
		/** Source **/
		private final Path root;
		/** Shell environment **/
		private Environment env;
		
		/**
		 * Constructor for CopyFileTree
		 * @param rootSource of files
		 * @param destination for new files
		 * @param env of Shell
		 */
		private CopyFileTree(Path rootSource, Path destination, Environment env) {
			this.root = rootSource;
			this.destination = destination;
			this.env = env;
		}
		
		@Override
		public FileVisitResult postVisitDirectory(Path arg0, IOException arg1) throws IOException {
			destination = destination.getParent();
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path arg0, BasicFileAttributes arg1) throws IOException {
			
			if(arg0.equals(root) && !Files.exists(destination)) {
				if(!Files.exists(root.getParent())) {
					env.writeln("Invalid destination input. Parent of a destination doesn't exist");
					return FileVisitResult.TERMINATE;
				}

			}
			else {
				destination = destination.resolve(arg0.getFileName());
			}
			
			if(!Files.exists(destination)) {
				env.writeln("Creating directory: " + destination);
				Files.createDirectory(destination);
			}
			else {
				env.writeln("Directory " + destination + " already exists.");
			}
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path arg0, BasicFileAttributes arg1) throws IOException {
			Path newFile = destination.resolve(arg0.getFileName());
			env.writeln("Copying file " + arg0 + " to " + newFile);
			Util.copyFiles(arg0, newFile);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path arg0, IOException arg1) throws IOException {
			env.writeln("Couldn't visit: " + arg0);
			return FileVisitResult.CONTINUE;
		}
		
	}
}
