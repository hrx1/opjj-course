package hr.fer.zemris.java.hw07.shell;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;

/**
 * Defines environment in which Shell is ran. 
 * Commands communicate exclusively through environment.
 * Environment input is Standard Input, and output is Standard Output
 * 
 * Default variable values are:
 * 	PROMPTSYMBOL = '>'
 * 	MORELINESSYMBOL = '\\'
 * 	MULTILINESSYMBOL = '|'
 * 
 * @author Hrvoje
 *
 */
public class StandardEnvironment implements Environment {
	/** Standard symbols **/
	private Character PROMPTSYMBOL = '>';
	private Character MORELINESSYMBOL = '\\';
	private Character MULTILINESSYMBOL = '|';
	
	/** Current path **/
	private Path currentDirectory;
	
	/** Map of supported commands **/
	SortedMap<String, ShellCommand> commands;
	/** Environment input **/
	Scanner input;
	
	/** Shared data **/
	Map<String, Object> sharedData;
	
	/**
	 * Constructor for StandardEnvironmetn
	 * @param commands supported
	 * @param input Standard input scanner
	 */
	public StandardEnvironment(SortedMap<String, ShellCommand> commands, Scanner input) {
		this.commands = Collections.unmodifiableSortedMap(commands);
		this.input = input;
		
		currentDirectory = Paths.get(".").toAbsolutePath().normalize();
		sharedData = new HashMap<String, Object>();
	}
	
	@Override
	public String readLine() throws ShellIOException {
		try {
			return input.nextLine();
		}catch(NoSuchElementException | IllegalStateException e) {
			throw new ShellIOException();
		}
	}

	@Override
	public void write(String text) throws ShellIOException {
		Objects.requireNonNull(text);
		System.out.print(text);
	}

	@Override
	public void writeln(String text) throws ShellIOException {
		System.out.println(text);
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return commands;
	}

	@Override
	public Character getMultilineSymbol() {
		return MULTILINESSYMBOL;
	}

	@Override
	public void setMultilineSymbol(Character symbol) {
		MULTILINESSYMBOL = symbol;
	}

	@Override
	public Character getPromptSymbol() {
		return PROMPTSYMBOL;
	}

	@Override
	public void setPromptSymbol(Character symbol) {
		PROMPTSYMBOL = symbol;
	}

	@Override
	public Character getMorelinesSymbol() {
		return MORELINESSYMBOL;
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {
		MORELINESSYMBOL = symbol;
	}

	@Override
	public Path getCurrentDirectory() {
		return currentDirectory;
	}

	@Override
	public void setCurrentDirectory(Path path) {
		if(!Files.isDirectory(path)) {
			throw new ShellIOException("Cannot open " + path.toAbsolutePath().normalize());
		}
		
		currentDirectory = path.toAbsolutePath().normalize();
	}

	@Override
	public Object getSharedData(String key) {
		return sharedData.get(key);
	}

	@Override
	public void setSharedData(String key, Object value) {
		sharedData.put(key, value);
	}

}
