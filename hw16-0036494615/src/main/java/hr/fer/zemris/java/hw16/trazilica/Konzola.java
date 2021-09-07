package hr.fer.zemris.java.hw16.trazilica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import hr.fer.zemris.java.cmdapps.search.DocumentsModel.Documents;
import hr.fer.zemris.java.cmdapps.search.DocumentsModel.Documents.ComparisonResult;
import hr.fer.zemris.java.cmdapps.search.DocumentsModel.Documents.StringDocument;
import hr.fer.zemris.java.cmdapps.search.DocumentsModel.DocumentsBuilder;

/**
 * Console which recognizes 'query', 'type' and 'results' command.
 * Main method receives Path to documents.
 * 
 * @author Hrvoje
 *
 */
public class Konzola {
	/** Documents used */
	private Documents docs;
	
	/** DESC sorted latest results by result value */
	private List<ComparisonResult> latestResult;
	
	/** Name of a file in /src/main/resources which contain stop words */
	private static final String stopWordsFN = "stop-rijeci.txt";

	
	/**
	 * Constructor
	 * @param docs to use
	 */
	public Konzola(Documents docs) {
		this.docs = docs;
	}
	
	/**
	 * Run console
	 */
	public void run() {
		System.out.format("Velicina rjecnika je %d rijeci\n\n", docs.getWords().size());
		
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.print("Enter Command > ");
			String command = sc.nextLine();
			
			switch (command.split(" ")[0]) {
				case "query":
					doQuery(command.substring("query".length()).trim());
					break;
				case "type":
					doType(command.substring("type".length()).trim());
					break;
				case "results":
					doResults(command.substring("results".length()).trim());
					break;
				case "exit":
					sc.close();
					return ;
				default:
					System.out.println("Nepoznata naredba");
					break;
			}
			System.out.println();
		}
	}
	
	/**
	 * Do results
	 * @param empty
	 */
	private void doResults(String empty) {
		if(empty != null && !empty.isEmpty()) {
			System.out.println("Results command takes to arguments");
			return ;
		}
		if(latestResult == null || latestResult.isEmpty()) {
			System.out.println("No results to present.");
			return ;
		}
		
		int i = 0;
		for(ComparisonResult result : latestResult) {
			System.out.format("[%2d] %s\n", i, result.toString());
			++i;
		}
	}

	/**
	 * Do Type
	 * @param number
	 */
	private void doType(String number) {
		int i;
		try {
			i = Integer.parseInt(number);
		} catch(NumberFormatException e) {
			System.out.println("Type command takes only 1 number as argument. Integer - index of result");
			return ;
		}
		if(latestResult == null || i < 0 || i >= latestResult.size()) {
			System.out.println("No result with given index.");
			return ;
		}
		Path resultPath = latestResult.get(i).document.toAbsolutePath();
		System.out.println("----------------------------------------------------------------");
		System.out.println("Dokument: " + resultPath.toString());
		System.out.println("----------------------------------------------------------------");
		try {
			System.out.println(new String(Files.readAllBytes(resultPath)));
		} catch (IOException e) {
			System.out.println("Couldn't open document: " + resultPath.toString());
		}
		System.out.println("----------------------------------------------------------------");
		
	}

	/**
	 * Do Query
	 * @param query
	 */
	private void doQuery(String query) {
		if(query.isEmpty()) {
			System.out.println("Command Query takes query as argument");
			return ;
		}
		
		StringDocument sd = docs.new StringDocument(query);
		System.out.println("Query is: (" + sd.toString() + ")");
		System.out.println("Najboljih 10 rezultata: ");
		
		latestResult = docs.getNMostSimilar(sd, 10);
		
		doResults(null);
	}

	/**
	 * Main method
	 * @param args path to Documents
	 */
	public static void main(String[] args) {
		
		Path textFilesRoot;
		try {
			textFilesRoot = parsePathFromArguments(args);
		}catch(IllegalArgumentException e) {
			System.out.println(e.getLocalizedMessage());
			return ;
		}
		
		//Load stop words
		Set<String> stopWords;
		try {
			stopWords = getStopWords();
		} catch (IOException e) {
			System.out.println("No stop Words loaded.");
			stopWords = new HashSet<>();
		}

		Documents docs;
		try {
			docs = initailiseDocuments(textFilesRoot, stopWords);
		} catch (IOException e) {
			System.out.println("Error while opening text files.");
			return ;
		}
		
		new Konzola(docs).run();
	}
	
	/**
	 * Parses Path from args
	 * @param args
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static Path parsePathFromArguments(String[] args) throws IllegalArgumentException {
		if(args.length != 1) {
			throw new IllegalArgumentException("Invalid number of arguments given. Expected: 1 (Root directory of text files)");
		}
		
		Path textFilesRoot = Paths.get(args[0]);
		
		if(!Files.isDirectory(textFilesRoot)) {
			throw new IllegalArgumentException("Given argument should be a path to a directory.");
		}
		
		return textFilesRoot;
	}

	/**
	 * Initializes Document
	 * 
	 * @param textFilesRoot
	 * @param stopWords
	 * @return
	 * @throws IOException
	 */
	private static Documents initailiseDocuments(Path textFilesRoot, Set<String> stopWords) throws IOException {
		DocumentsBuilder builder = new DocumentsBuilder(stopWords);
		// Fill Builder
		Files.walk(textFilesRoot).filter(o -> Files.isReadable(o) && Files.isRegularFile(o) && !Files.isDirectory(o))
								 .forEach(p -> {
									 builder.addDocument(p);
								 });
		
		return builder.build();
	}

	/**
	 * Returns stop words
	 * @return
	 * @throws IOException
	 */
	private static Set<String> getStopWords() throws IOException {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		BufferedReader bis = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream(stopWordsFN)));
		
		Set<String> words = new HashSet<>();
		for(String word = bis.readLine(); word != null; word = bis.readLine()) {
			words.add(word);
		}
		
		try {
			bis.close();
		} catch (IOException ignorable) {
		}
		
		return words;
	}
	
	
}
