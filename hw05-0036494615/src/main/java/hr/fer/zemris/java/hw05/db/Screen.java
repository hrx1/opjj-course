package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.hw05.db.lexer.LexerException;
import hr.fer.zemris.java.hw05.db.parser.QueryParser;
import hr.fer.zemris.java.hw05.db.parser.QueryParserException;

/**
 * Implements the main task of this HW. User is required to give queries which
 * will be executed over database.txt in root folder.
 * 
 * Every query consists of one or more Conditional Expressions which can be
 * connected with AND keyword. Every Conditional Expression consists of atribute
 * name, operator and string literal.
 * 
 * Valid atribute names are: jmbag, firstName, lastName Valid operators are
 * <, <=, =, >, >=
 * 
 * Here are several legal examples of the this command:
 * query jmbag="0000000003"
 * query lastName = "Blažić" 
 * query firstName>"A" and lastName LIKE "B*ć" 
 * query firstName>"A" and firstName<"C" and lastName LIKE "B*ć" and jmbag>"0000000002"
 * 
 * Program finishes when user writes EXIT keyword
 * 
 * @author Hrvoje
 *
 */
public class Screen {
	/** Prompt printed where user input is expected **/
	private static final String PROMPT = "> ";
	/** Exit keyword **/
	private static final String EXIT = "exit";
	/** Query keyword **/
	private static final String QUERY = "query ";
	
	
	/**
	 * Main method
	 * @param args neglected
	 */
	public static void main(String[] args) {
		
		String databasePath = "database.txt";
		StudentDatabase db ;
		try {
			db = loadDatabase(databasePath);
		}catch (IOException e) {
			System.out.println("Couldn't load db from path: " + databasePath);
			return;
		}
		
		Scanner input = new Scanner(System.in);
		String line;
		while(true) {
			System.out.print(PROMPT);
		
			line = input.nextLine();
			
			if (line.equals(EXIT)) break;
			
			if (line.startsWith(QUERY)) {
				try {
				QueryParser parser = new QueryParser(line.substring(QUERY.length()));
				if(parser.isDirectQuery()) {
					System.out.println("Using index for record retrieval.");
				}
				
				printStudentRecords(processQuery(parser, db));
				}catch(LexerException le) {
					System.out.println("Cannot parse. " + le.getLocalizedMessage());
				}catch(QueryParserException qpe) {
					System.out.println("Cannot parse. " + qpe.getLocalizedMessage());
				}catch(Exception e) {
					System.out.println("Parse error. Line: " + line);
				}
			}
			else {
				System.out.println("Unknown operation: " + line);
			}
			
			System.out.println();
		}
		
		input.close();
		System.out.println("Goodbye!");
	}

	/**
	 * Loads database from <code>path</code> file
	 * @param path of the file
	 * @return database
	 * @throws IOException if file doesn't exist
	 */
	public static StudentDatabase loadDatabase(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		return new StudentDatabase(lines);
	}
	
	/**
	 * Processes query and returns list of student records.
	 * @param parser to use for getting queries
	 * @param db from which data is obtained
	 * @return list of student records from db
	 */
	private static List<StudentRecord> processQuery(QueryParser parser, StudentDatabase db) {
		List<StudentRecord> result = new LinkedList<>();
		
		if (parser.isDirectQuery()) {
			result.add(db.forJMBAG(parser.getQueriedJMBAG()));
		} else {
			for (StudentRecord r : db.filter(new QueryFilter(parser.getQuery()))) {
				result.add(r);
			}
		}
		
		return result;
	}
	
	/**
	 * Prints Student Records in format written in HW
	 * @param list of records to print
	 */
	private static void printStudentRecords(List<StudentRecord> list) {
		int maxJmbagLength = 0;
		int maxLastnameLength = 0;
		int maxFirstnameLength = 0;
		
		if (list.isEmpty()) {
			System.out.println("Records selected: 0");
			return;
		}
		
		for(StudentRecord sr : list) {
			maxJmbagLength = Math.max(maxJmbagLength, sr.getJmbag().length());
			maxLastnameLength = Math.max(maxLastnameLength, sr.getLastName().length());
			maxFirstnameLength = Math.max(maxFirstnameLength, sr.getFirstName().length());
		}
		
		printEdge(maxJmbagLength, maxLastnameLength, maxFirstnameLength);
		
		for(StudentRecord sr : list) {
			printStudentRecord(sr, maxJmbagLength, maxLastnameLength, maxFirstnameLength);
		}
		
		printEdge(maxJmbagLength, maxLastnameLength, maxFirstnameLength);
		
		System.out.println("Records selected: " + list.size());
	}
	
	/**
	 * Prints Edge
	 * @param columns size of data columns
	 */
	private static void printEdge(int ... columns) {
		StringBuilder sb = new StringBuilder();
		for(int column : columns) {
			sb.append("+=");
			appendNTimes(sb, "=", column);
			sb.append("=");
		}
		sb.append("+===+");
		System.out.println(sb.toString());
	}
	
	/**
	 * Prints one student record
	 * @param sr to print
	 * @param c1 max length of first column
	 * @param c2 max length of second column
	 * @param c3 max length of third column
	 */
	private static void printStudentRecord(StudentRecord sr, int c1, int c2, int c3) {
		StringBuilder sb = new StringBuilder();
		sb.append("| ");
		sb.append(sr.getJmbag());
		appendNTimes(sb, " ", c1 - sr.getJmbag().length() + 1);
		
		sb.append("| ");
		sb.append(sr.getLastName());
		appendNTimes(sb, " ", c2 - sr.getLastName().length() + 1);

		sb.append("| ");
		sb.append(sr.getFirstName());
		appendNTimes(sb, " ", c3 - sr.getFirstName().length() + 1);

		sb.append("| ");
		sb.append(sr.getFinalGrade());
		sb.append(" |");
		
		System.out.println(sb.toString());
	}
	
	/**
	 * Appends string s n-times to StringBuilder sb
	 * @param sb string builder in which s will be appended
	 * @param s to append
	 * @param n number of times to append
	 */
	private static void appendNTimes(StringBuilder sb, String s, int n) { 
		for(int i = 0; i < n; ++i) {
			sb.append(s);
		}
	}
}
