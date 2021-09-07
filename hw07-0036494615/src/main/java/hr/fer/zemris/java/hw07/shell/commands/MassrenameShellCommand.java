package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.commands.namebuilder.NameBuilderInfo;
import hr.fer.zemris.java.hw07.shell.commands.namebuilder.NameBuilderParser;

/**
 * Command moves/renames multiple files in one directory (not including sub-directories).
 * Syntax:
 * 	massrename DIR1 DIR2 option ...other...
 * 
 * Command has following options:
 * 	filter, groups, show, execute
 * 
 * Filter option
 * 	massrename DIR1 'neglected' filter REGEX
 * 	prints names of files from DIR1 which satisfy given REGEX
 * 
 * Groups option
 * 	massrename DIR1 'neglected' groups REGEX
 * 	prints names of files from DIR1 which satisfy given REGEX splitted into groups.
 * 
 * Show option
 * 	massrename DIR1 'neglected' show REGEX EXPRESSION
 * 	prints names of files from DIR1 which satisfy given REGEX,
 *  and their new names defined by REGEX and EXPRESSION.
 * 
 * Execute option
 * 	massrename DIR1 DIR2 execute REGEX EXPRESSION
 * 	moves files from DIR1 which satisfy given REGEX,
 *  to DIR2 with their new names defined by REGEX and EXPRESSION.
 * 
 * @author Hrvoje
 *
 */
public class MassrenameShellCommand implements ShellCommand {
	/** Command name **/
	private static final String name = "massrename";

	@SuppressWarnings("serial")
	/** Command description **/
	List<String> description = new ArrayList<>() {
		{
			add("Command moves/renames multiple files in one directory (not including sub-directories).");
			add("Syntax:");
			add("\tmassrename DIR1 DIR2 option ...other...\n");
			add("Command has following options: filter, groups, show, execute\n");
			add("Filter option");
			add("\tmassrename DIR1 'neglected' filter REGEX");
			add("\tprints names of files from DIR1 which satisfy given REGEX");
			add("Groups option");
			add("\tmassrename DIR1 'neglected' groups REGEX");
			add("\tprints names of files from DIR1 which satisfy given REGEX splitted into groups.");
			add("Show option");
			add("\tmassrename DIR1 'neglected' show REGEX EXPRESSION");
			add("\tprints names of files from DIR1 which satisfy given REGEX, ");
			add("\tand their new names defined by REGEX and EXPRESSION.");
			add("Execute option");
			add("massrename DIR1 DIR2 execute REGEX EXPRESSION");
			add("moves files from DIR1 which satisfy given REGEX,");
			add("to DIR2 with their new names defined by REGEX and EXPRESSION.");
		}
	};

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		Util.requireArgumentsNotNull(env);

		List<String> arguments;
		try {
			arguments = Util.parseArgsWithQuotedStrings(argument);
		} catch (IllegalArgumentException e) {
			env.writeln("Illegal arguments format!");
			return ShellStatus.CONTINUE;
		}

		if (arguments.size() < 4) {
			ShellCommand.invalidArgumentNumberMsg(env, this, arguments, 4);
			return ShellStatus.CONTINUE;
		}

		String source = arguments.get(0);

		Path sourcePath = env.getCurrentDirectory().resolve(source);

		if (!Files.exists(sourcePath)) {
			env.writeln("Cannot find '" + sourcePath + "'");
			return ShellStatus.CONTINUE;
		}

		if (!Files.isDirectory(sourcePath)) {
			env.writeln("First argument has to be directory, but given: " + source);
			return ShellStatus.CONTINUE;
		}
		
		Path destinationPath = env.getCurrentDirectory().resolve(arguments.get(1));

		String command = arguments.get(2);
		Pattern mask;
		try {
			mask = Pattern.compile(arguments.get(3));
		} catch (PatternSyntaxException e) {
			env.writeln("Invalid regex syntax: " + arguments.get(3));
			return ShellStatus.CONTINUE;
		}

		switch (command) {
		case "filter":
			filterCommand(env, mask, sourcePath);
			break;
			
		case "groups":
			groupsCommand(env, mask, sourcePath);
			break;
			
		case "show":
			if(arguments.size() != 5) {
				ShellCommand.invalidArgumentNumberMsg(env, this, arguments, 5);
				return ShellStatus.CONTINUE;
			}
			showCommand(env, mask, arguments.get(4), sourcePath);
			break;
			
		case "execute":
			if(arguments.size() != 5) {
				ShellCommand.invalidArgumentNumberMsg(env, this, arguments, 5);
				return ShellStatus.CONTINUE;
			}
			executeCommand(env, mask, arguments.get(4), sourcePath, destinationPath);
			break;
			
		default:
			env.writeln("Option '" + command + "' doesn't exist");
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
	 * Filters filenames from sourcePath which match regex, and outputs their names to the Environment.
	 * @param env Environment
	 * @param regex to match
	 * @param sourcePath of files.
	 */
	private static void filterCommand(Environment env, Pattern regex, Path sourcePath) {

		try {
			Files.walk(sourcePath, 1)
				.filter(Files::isRegularFile)
				.map(o -> o.getFileName().toString())
				.filter(regex.asPredicate())
				.forEach(env::writeln);
		} catch (IOException e) {
			env.writeln("Couldn't open " + sourcePath);
		}

	}

	/**
	 * Filters filenames from sourcePath which match regex, and outputs their groups matched with regex to the Environment.
	 * @param env Environment
	 * @param regex to match
	 * @param sourcePath of files.
	 */
	private static void groupsCommand(Environment env, Pattern regex, Path sourcePath) {

		try {
			Files.walk(sourcePath, 1)
				.filter(Files::isRegularFile)
				.map(o -> o.getFileName().toString())
				//.filter(regex.asPredicate())
				.forEach(o -> groupPrint(env, o, regex));

		} catch (IOException e) {
			env.writeln("Couldn't open " + sourcePath);
		}

	}

	/**
	 * Prints one set of groups of string matched by pattern to the Environment
	 * @param env Environment
	 * @param string to group
	 * @param pattern to match
	 */
	private static void groupPrint(Environment env, String string, Pattern pattern) {
		Matcher m = pattern.matcher(string);
		while (m.find()) {
			env.write(string + " ");
			for (int i = 0; i < m.groupCount() + 1; ++i) {
				env.write(i + ": " + m.group(i) + " ");
			}
			env.writeln("");
		}

	}

	/**
	 * Prints filenames and their new names to the Environment. 
	 * Filenames are selected and grouped by mask, but new names are defined by expression.
	 * @param env Environment
	 * @param mask to match and group
	 * @param expression defines new names
	 * @param source of files
	 */
	private static void showCommand(Environment env, Pattern mask, String expression, Path source) {
		NameBuilderParser nbp = new NameBuilderParser(expression);

		try {
			Files.walk(source, 1)
				.filter(Files::isRegularFile)
				.map(o -> o.getFileName().toString())
				.filter(mask.asPredicate())
				.forEach(s -> {
					env.write(s + " => ");
					env.writeln(changeName(nbp, s, mask, expression));
				});

		} catch (IOException e) {
			env.writeln("Couldn't open " + source);
		}
	}

	/**
	 * Changes a string which matches mask with expression.
	 * @param nbp NameBilderParser
	 * @param name to change
	 * @param mask to match and group
	 * @param expression defines new name
	 * @return new name
	 */
	private static String changeName(NameBuilderParser nbp, String name, Pattern mask, String expression) {
		Matcher matcher = mask.matcher(name);

		NameBuilderInfo nbi = NameBuilderInfo.fromMatcher(matcher);
		nbp.getNameBuilder().execute(nbi);

		return nbi.getStringBuilder().toString();
	}

	/**
	 * Moves files from source which satisfy given mask,
	 * to destination with their new names defined by mask and expression.
	 * Outputs result to the environment.
	 * 
	 * @param env Environmetn
	 * @param mask to match and group
	 * @param expression defines new name
	 * @param source of files
	 * @param destination of new files
	 */
	private void executeCommand(Environment env, Pattern mask, String expression, Path source, Path destination) {
		NameBuilderParser nbp = new NameBuilderParser(expression);

		try {
			Files.walk(source, 1)
				.filter(Files::isRegularFile)
				.filter(o -> mask.matcher(o.getFileName().toString()).find())
				.forEach(o -> {
					
					String newName = changeName(nbp, o.getFileName().toString(), mask, expression);
					Path newPath =  destination.resolve(newName).normalize();
					
					try {
						Files.move(o, newPath);
						env.writeln("Moving " + o.normalize().toString() + " to " + newPath.toString());
					} catch (IOException e) {
						env.writeln("Couldn't move " + o.normalize().toString() + " to " + newPath.toString());
					}
					
				});;

		} catch (IOException e) {
			env.writeln("Couldn't open " + source);
		}
	}
}
