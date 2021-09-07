package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Utility functions needed for Commands.
 * 
 * @author Hrvoje
 *
 */
public class Util {
	
	/**
	 * Tokenizes arguments with space delimiter, except when they are under quotation marks.
	 * String under quotation marks are one token.
	 * 
	 * @param arguments to tokenize
	 * @return tokens as list of strings
	 */
	public static List<String> parseArgsWithQuotedStrings(String arguments) {
		List<String> result = new LinkedList<>();
		
		if(arguments == null) return result;
		
		Scanner sc = new Scanner(arguments);
		
		while(sc.hasNext()) {
			
			String nextLine = sc.next();
			
			if(nextLine.startsWith("\"")) {
				boolean qoute = true;
				
				StringBuilder sb = new StringBuilder();
				String tmp = nextLine;
				
				sb.append(tmp + " ");
				
				if(!tmp.endsWith("\"") || tmp.length() == 1) {
					while(sc.hasNext()) {
						tmp = sc.next();
						sb.append(tmp + " ");
						if(tmp.endsWith("\"")) {
							qoute = false;
							break;
						}
					}
				}
				else {
					qoute = false;
				}
				
				if(qoute) {
					sc.close();
					throw new IllegalArgumentException();
				}
				
				if(sb.chars().filter(o -> o == '"').count() != 2) {
					sc.close();
					throw new IllegalArgumentException();
				}
				nextLine = sb.toString().replace("\"", "").trim();
			}
			
			result.add(nextLine);
		}
		
		sc.close();
		
		return result; //TODO
	}
	
	/**
	 * Parses arguments with space delimiter.
	 * @param argument to parse
	 * @return list of parsed arguments
	 */
	public static List<String> parseArguments(String argument) {
		List<String> result = new LinkedList<>();
		
		if (argument == null || argument.length() == 0) return result;
		
		for(String s : argument.split(" ")) {
			result.add(s);
		}
		
		return result;
	}
	
	/**
	 * Throws {@link NullPointerException} if at least one argument is <code>null</code>
	 * @param args to chec
	 */
	public static void requireArgumentsNotNull(Object ... args) {
		Objects.requireNonNull(args);
		
		for(Object arg : args) {
			Objects.requireNonNull(arg);
		}
		
	}

	/**
	 * Copies files from sourcePath to destinationPath
	 * @param sourcePath source
	 * @param destinationPath destination
	 * @throws IOException if source/destination is not readable/writeable
	 */
	public static void copyFiles(Path sourcePath, Path destinationPath) throws IOException {
		
		BufferedInputStream source = new BufferedInputStream(Files.newInputStream(sourcePath, StandardOpenOption.READ));
		BufferedOutputStream destination = new BufferedOutputStream(Files.newOutputStream(destinationPath, StandardOpenOption.CREATE));
		
		source.transferTo(destination);
		
		source.close();
		destination.close();
		
	}
	
}
