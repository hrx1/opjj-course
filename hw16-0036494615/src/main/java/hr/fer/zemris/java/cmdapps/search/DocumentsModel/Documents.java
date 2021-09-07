package hr.fer.zemris.java.cmdapps.search.DocumentsModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Class defined with words, documents and StopWords
 * @author Hrvoje
 *
 */
public class Documents {
	/** Used Documents*/
	private Set<PathDocument> documents;
	/** Used Stop Words */
	private Set<String> stopWords;
	/** Indexed Words */
	private HashMap<String, Integer> wordIndexing; //omoguci nam spremanje vektora u double[]
	/** Words */
	private Set<String> words;
	/** IDF */
	private double[] IDF;
	
//	private static final double ZERO_THRESHOLD = 1e-6;

	/**
	 * Treba mu words i stopWords za usporedjivanje
	 * paths - lista datoteka koje zeli analizirati (indexirati)
	 * Ako je nesto i u stopWords i u words, onda kao da nije u words
	 * 
	 * @param words
	 * @param stopWords
	 * @param paths 
	 * @throws IOException 
	 */
	Documents(Set<String> words, Set<String> stopWords, List<Path> paths) throws IOException {
		Objects.requireNonNull(words);
		Objects.requireNonNull(stopWords);
		Objects.requireNonNull(paths);
		
		this.stopWords = stopWords;
		this.words = words;
		
		indexWords(words); //String -> Index mapiranje
		indexDocuments(paths); //racuna njihov tf
		calculateIDF(); //racuna IDF
		calculateTF_IDF();
	}

	/**
	 * Calculates IDF
	 */
	private void calculateTF_IDF() {
		for(PathDocument doc : documents) {
			for(int i = 0; i < doc.vector.length; ++i) {
				doc.vector[i] *= IDF[i];
			}
		}
	}

	/**
	 * Indexes Words
	 * @param words
	 */
	private void indexWords(Set<String> words) {
		wordIndexing = new HashMap<>(words.size());
		int i = 0;

		for(String word : words) {
			wordIndexing.put(word, i);
			++i;
		}
		
	}
	
	/**
	 * Calculates IDF
	 */
	private void calculateIDF() {
		int numberOfWords = wordIndexing.size();
		IDF = new double[numberOfWords];
		
		for(int i = 0; i < numberOfWords; ++i) {
			int counter = 0;
			
			for(PathDocument doc : documents) {
				if(doc.vector[i] > 0) {
					++counter;
				}
			}
			
			if(counter != 0) {
				IDF[i] = Math.log(documents.size() * (1.)/counter);
			}
			
		}
	}

	/**
	 * Term frequency of documents
	 * 
	 * @param paths
	 * @throws IOException 
	 */
	private void indexDocuments(List<Path> paths) throws IOException {
		documents =  new LinkedHashSet<>();
		
		for(Path p : paths) {
			documents.add(new PathDocument(p));
		}
	}
	
	/**
	 * First element has highest similarity factor.
	 * 
	 * @param s
	 * @param n
	 * @return
	 */
	public List<ComparisonResult> getNMostSimilar(String s, int n) {
		return getNMostSimilar(new StringDocument(s), n);
	}	
	
	/**
	 * Returns N most similar documents
	 * @param doc
	 * @param n
	 * @return
	 */
	public List<ComparisonResult> getNMostSimilar(DocumentVector doc, int n) {
		if(n < 1) throw new IllegalArgumentException("n should be greater then 0.");
		
		TreeSet<ComparisonResult> results = new TreeSet<>((o1, o2) -> {
			int cr = Double.valueOf(o2.result).compareTo(o1.result);
			if(cr == 0) {
				cr = o1.document.compareTo(o2.document);
			}
			return cr;
		});
		
		for(PathDocument pd : documents) {
			double currResult = calcSimilarity(doc, pd);
			if(currResult == 0) continue; //TODO THRESHOLD
			results.add(new ComparisonResult(pd.path, currResult));
			if(results.size() > n) {
				results.pollLast();
			}
		}
		
		return new ArrayList<ComparisonResult>(results);
	}
	
	/**
	 * Returns N most similar documents
	 * @param doc
	 * @param n
	 * @return
	 */
	public List<ComparisonResult> getNMostSimilar(StringDocument doc, int n) {
		if(doc.relevantWords.isEmpty()) return new LinkedList<ComparisonResult>();
		return getNMostSimilar((DocumentVector) doc, n);
	}
	
	
	/**
	 * Calculates similarity using defined word mapping
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public double calcSimilarity(DocumentVector v1, PathDocument v2) {
		int numberOfWords = wordIndexing.size();
		
		if(v1.getVector().length != numberOfWords && v2.getVector().length != numberOfWords) {
			throw new IllegalArgumentException(
					"Vectors must dimensions equal to number of words in dictionary." +
					" Given: " + v1.getVector().length + " and " + v2.getVector().length +
					", but needed: " + wordIndexing.size()
					);
		}
		
		double result = 0;
		double[] vec1 = v1.getVector();
		double[] vec2 = v2.getVector();
		
		double vec1Abs = v1.getAbsolute();
		double vec2Abs = v2.getAbsolute();
		double abs = vec1Abs * vec2Abs;
		
		for(int i = 0; i < numberOfWords; ++i) {
			result += vec1[i]*vec2[i];
		}
		
		return result/abs;
	}

	/**
	 * Document represented with path to text file
	 * @author Hrvoje
	 *
	 */
 	class PathDocument implements DocumentVector {
 		
 		private Path path;
 		private double[] vector;
 		
 		public PathDocument(Path path) throws IOException {
 			this.path = path;
 			this.vector = new double[wordIndexing.size()];
 			
 			BufferedReader br = Files.newBufferedReader(path);

 			for (String line = br.readLine(); line != null; line = br.readLine()) {
 				for (String word : parseWords(line)) {
 					Integer index = wordIndexing.get(word);

 					if (index == null) continue;
 					if (stopWords.contains(word)) continue;

 					++vector[index];
 				}
 			}

 			br.close();
 			
		}
 		
		@Override
		public double[] getVector() {
			return vector;
		}
		
		public Path getPath() {
			return path;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((path == null) ? 0 : path.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PathDocument other = (PathDocument) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (path == null) {
				if (other.path != null)
					return false;
			} else if (!path.equals(other.path))
				return false;
			return true;
		}

		private Documents getOuterType() {
			return Documents.this;
		}
		
		
 		
 	}
 	
 	/**
 	 * Document represented with a string
 	 * @author Hrvoje
 	 *
 	 */
 	public class StringDocument implements DocumentVector {
 		private double[] vector;
 		private Set<String> relevantWords;
 		
 		/**
 		 * Constructor
 		 * @param string representative
 		 */
 		public StringDocument(String string) {
 				List<String> parsedWords = parseWords(string);
 				relevantWords = new HashSet<>(parsedWords.size());
 				
 				vector = new double[wordIndexing.size()];
 				
 				for (String word : parsedWords) {
 					Integer index = wordIndexing.get(word);

 					if (index == null) continue;
 					if (stopWords.contains(word)) continue;
 					
 					relevantWords.add(word);
 					++vector[index];
 			}
 			
 				//IDF korekcija
 				for(int j = 0; j < vector.length; ++j) {
 					vector[j] = IDF[j] * vector[j];
 				}
 		}

		@Override
		public double[] getVector() {
			return vector;
		}
 		
		/**
		 * Returns relevant words
		 * @return
		 */
		public Set<String> getRelevantWords() {
			return relevantWords;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(relevantWords.size() * 10); //procjena
			
			if (relevantWords.isEmpty()) return "";
			
			for(String word : relevantWords) {
				sb.append(word);
				sb.append(", ");
			}
			
			sb.delete(sb.length() - 2, sb.length());
			return sb.toString();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((relevantWords == null) ? 0 : relevantWords.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			StringDocument other = (StringDocument) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (relevantWords == null) {
				if (other.relevantWords != null)
					return false;
			} else if (!relevantWords.equals(other.relevantWords))
				return false;
			return true;
		}

		private Documents getOuterType() {
			return Documents.this;
		}
		
		
 	}

 	public static class ComparisonResult {
 		
		public Path document;
 		public double result;
 		
		public ComparisonResult(Path document, double result) {
			super();
			this.document = document;
			this.result = result;
		}

		
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((document == null) ? 0 : document.hashCode());
			return result;
		}



		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ComparisonResult other = (ComparisonResult) obj;
			if (document == null) {
				if (other.document != null)
					return false;
			} else if (!document.equals(other.document))
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return String.format("(%2.4f) %s", result, document.toAbsolutePath().toString());
		}
	}

	static List<String> parseWords(String line) {
		return Arrays.stream(line.split("\\P{IsAlphabetic}+"))
					 .map(o -> o.trim().toLowerCase())
					 .filter(o -> !o.isEmpty())
					 .collect(Collectors.toList());
	}


	/**
	 * @return the documents
	 */
	public Set<PathDocument> getDocuments() {
		return documents;
	}


	/**
	 * @return the stopWords
	 */
	public Set<String> getStopWords() {
		return stopWords;
	}



	/**
	 * @return the words
	 */
	public Set<String> getWords() {
		return words;
	}

	
	/**
	 * @return the wordIndexing
	 */
	public HashMap<String, Integer> getWordIndexing() {
		return wordIndexing;
	}


	/**
	 * @return the iDF
	 */
	public double[] getIDF() {
		return IDF;
	}

	
	
}
