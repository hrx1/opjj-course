package hr.fer.zemris.java.cmdapps.search.DocumentsModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Builds Documents class with provided informations such as stop words and
 * added Documents.
 * 
 * @author Hrvoje
 *
 */
public class DocumentsBuilder {
	/** Stop Words */
	private Set<String> stopWords;
	/** Words */
	private Set<String> words;
	/** Paths */
	private List<Path> paths;
	
	/**
	 * Constructor
	 * @param stopWords Stopwords to use
	 */
	public DocumentsBuilder(Set<String> stopWords) {
		this.stopWords = stopWords;
		
		words = new HashSet<>();
		paths = new LinkedList<>();
	}
	
	/**
	 * Add Document with path
	 * @param path of a document
	 */
	public void addDocument(Path path) {
		paths.add(path);
	}
	
	/**
	 * Build Documents
	 * @return
	 * @throws IOException
	 */
	public Documents build() throws IOException {
		words = createWords();
		
		return new Documents(words, stopWords, paths);
	}
	
	/**
	 * Create Words
	 * @return
	 * @throws IOException
	 */
	public Set<String> createWords() throws IOException {
		Set<String> result = new HashSet<>();
		
		for(Path path : paths) {
			for(String line : Files.readAllLines(path)) {
				result.addAll(Documents.parseWords(line));
			}
		}
		
		result.removeAll(stopWords);
		
		return result;
	}
}
