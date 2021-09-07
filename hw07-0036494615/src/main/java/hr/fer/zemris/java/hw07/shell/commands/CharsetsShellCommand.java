package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command returns supported Charsets.
 * @author Hrvoje
 *
 */
public class CharsetsShellCommand implements ShellCommand {
	
	/** Command name **/
	String name = "charsets";
	
	@SuppressWarnings("serial")
	/** Command description **/
	List<String> description = new ArrayList<>() {
		{
			add("Command returns supported Charsets.");
		}
	};

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if(arguments != null && arguments.length() != 0) {
			ShellCommand.invalidArgumentNumberMsg(env, this, Util.parseArguments(arguments), 0);
			return ShellStatus.CONTINUE;
		}
		
		env.write("Available charsets are: ");
		env.write(
				Charset.availableCharsets()
				.values()
				.stream()
				.map(o -> o.toString())
				.collect(Collectors.joining("\n"))
				);
		
		env.write("\n");
		
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
