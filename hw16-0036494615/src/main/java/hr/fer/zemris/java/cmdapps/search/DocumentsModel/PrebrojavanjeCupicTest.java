package hr.fer.zemris.java.cmdapps.search.DocumentsModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

public class PrebrojavanjeCupicTest {
	public static void main(String[] args) throws IOException {
		Path clanci = Paths.get("/home/john/Desktop/zadaca_testovi/clanci/");
		Path stopWords = Paths.get("/home/john/Desktop/zadaca_testovi/stop-rijeci.txt");
		
		DocumentsBuilder db = new DocumentsBuilder(new HashSet<String>(Files.readAllLines(stopWords)));
		
		Files.walk(clanci).filter(o -> Files.isReadable(o) && Files.isRegularFile(o))
						  			.forEach(p -> { db.addDocument(p);});
		
		Documents docs = db.build();
				
		docs.getWords().stream().forEach(o -> {
			System.out.println(o);
		});
		
		System.out.println(docs.getWords().size());
		
	}
}
