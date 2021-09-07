package hr.fer.zemris.java.hw07.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * Defines environment in which Shell is ran. 
 * Commands communicate exclusively through environment.
 * @author Hrvoje
 *
 */
public interface Environment {
	
	/**
	 * Read line from environment input
	 * @return line
	 * @throws ShellIOException if you can't read from environment
	 */
	String readLine() throws ShellIOException;
	
	/**
	 * Writes text to environment output
	 * @param text to write
	 * @throws ShellIOException if you can't write to environment
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Writes text to environment output, with new line at the end.
	 * @param text to write
	 * @throws ShellIOException if you can't write to environment
	 */
	void writeln(String text) throws ShellIOException;

	/**
	 * Returns map of commands supported by environment
	 * @return map of commands supported by environment
	 */
	SortedMap<String, ShellCommand> commands();

	/**
	 * Getter for multiline symbol
	 * @return multiline symbol
	 */
	Character getMultilineSymbol();

	/**
	 * Setter for multiline symbol 
	 * @param symbol multiline symbol
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * Getter for prompt symbol
	 * @return prompt symbol
	 */
	Character getPromptSymbol();

	/**
	 * Setter for prompt symbol
	 * @param symbol prompt
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * Getter for morelines symbol
	 * @return morelines symbol
	 */
	Character getMorelinesSymbol();

	/**
	 * Setter for morelines symbol
	 * @param symbol morelines
	 */
	void setMorelinesSymbol(Character symbol);
	
	/**
	 * Returns absolute normalized path to current directory.
	 * @return absolute normalized path to current directory.
	 */
	Path getCurrentDirectory();
	
	/**
	 * Sets current directory to path. 
	 * Throws {@link ShellIOException} if path doesn't exist or isn't directory
	 * @param path to position on.
	 */
	void setCurrentDirectory(Path path);
	
	/**
	 * Returns shared data stored under key.
	 * @param key under which data is stored
	 * @return shared data stored under key
	 */
	Object getSharedData(String key);
	
	/**
	 * Stores shared data under key.
	 * @param key under which data is stored
	 * @param value to store.
	 */
	void setSharedData(String key, Object value);

}
