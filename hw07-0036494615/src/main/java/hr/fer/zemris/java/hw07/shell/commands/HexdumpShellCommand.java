package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

import static hr.fer.zemris.java.hw07.crypto.CryptoUtil.*;

/**
 * Command prints hex values contained in a file.
 * Command accepts one argument - path to file.
 * 
 * @author Hrvoje
 *
 */
public class HexdumpShellCommand implements ShellCommand {
	/** Command name **/
	private final String name = "hexdump";
	/** Bytes in a row to be printed **/
	private static final int bytesInRow = 16;
	
	@SuppressWarnings("serial")
	/** Description of a command **/
	List<String> description = new ArrayList<>() {
		{
			add("Command prints hex values contained in a file.");
			add("Command accepts one argument - path to file.");
		}
	};

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
		
		BufferedInputStream input;
		
		Path filePath = env.getCurrentDirectory().resolve(arguments.get(0)); 
		
		try {
			 input = new BufferedInputStream(Files.newInputStream(filePath, StandardOpenOption.READ));
			 
			 byte[] tmp = new byte[bytesInRow];
			 int counter = 0;
			 int loaded;
			 while((loaded = input.read(tmp)) == bytesInRow) {
				 env.writeln(formatHexLine(tmp, counter));
				 counter += bytesInRow;
				 tmp = new byte[bytesInRow];
			 }
			 
			 if(loaded != -1) {
				 env.writeln(formatHexLine(Arrays.copyOfRange(tmp,0, loaded), counter));
			 }
			 
			 input.close();
		} catch (IOException e) {
			env.writeln("Cannot be opened: "+ filePath);
			return ShellStatus.CONTINUE;
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
	 * Formats one line in output.
	 * @param bytes to print
	 * @param lineNumber number of first address
	 * @return one line in output
	 */
	public static String formatHexLine(byte[] bytes, int lineNumber) {
		String address = addLeadingZeros(Integer.toHexString(lineNumber), 8);
		
		String bytesFirst = bytesToHex(bytes, 0, 8);
		String bytesSecond = bytesToHex(bytes, 8, 8);
		String text = bytesToText(bytes, bytesInRow);
		
		return String.format("%s: %s|%s | %s", address, bytesFirst, bytesSecond, text);
	}
	
	/**
	 * Converts bytes to text
	 * @param bytes to convert
	 * @param numberOfBytes to convert
	 * @return text
	 */
	private static String bytesToText(byte[] bytes, int numberOfBytes) {
		int  counter = 0;
		StringBuilder result = new StringBuilder(numberOfBytes);
		
		while(counter < bytes.length && counter < numberOfBytes) {
			result.append((bytes[counter] < 32 || bytes[counter] > 127)?".":(char)bytes[counter]);
			++counter;
		}
		
		return result.toString();
	}
	
	/**
	 * Converts bytes to Hex text
	 * 
	 * @param bytes to convert
	 * @param startIndex first byte to convert in bytes
	 * @param numberOfBytes to convert
	 * @return hex text
	 */
	private static String bytesToHex(byte[] bytes, int startIndex, int numberOfBytes) {
		int counter = 0;
		StringBuilder result = new StringBuilder(numberOfBytes * 2);
		
		while(counter + startIndex < bytes.length && counter < numberOfBytes) {
			result.append(byteToHex(Arrays.copyOfRange(bytes, startIndex + counter, startIndex + counter + 1)));
			result.append(" ");
			++counter;
		}
		
		while(counter < numberOfBytes) {
			result.append("   ");
			++counter;
		}
		
		return result.substring(0, result.length() -1).toUpperCase();
	}
	
	/**
	 * Add leading zeros.
	 * @param hex to add
	 * @param length of String
	 * @return string with leading zeros
	 */
	private static String addLeadingZeros(String hex, int length) {
		int diff = length - hex.length();
		StringBuilder sb = new StringBuilder(length);
		
		for(int i = 0; i < diff; ++i) {
			sb.append("0");
		}
		sb.append(hex);
		
		return sb.toString();
	}

}
