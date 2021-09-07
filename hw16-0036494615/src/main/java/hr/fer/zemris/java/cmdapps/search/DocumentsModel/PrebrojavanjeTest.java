package hr.fer.zemris.java.cmdapps.search.DocumentsModel;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class PrebrojavanjeTest {
	
	public static void main(String[] args) throws IOException {
		Set<String> zaustavni = new HashSet<>();
		zaustavni.add("nece");
		zaustavni.add("Prvi");
		
		DocumentsBuilder db = new DocumentsBuilder(zaustavni);

		db.addDocument(Paths.get("/home/john/Desktop/zadaca_testovi/prebrojavanje"));
		db.addDocument(Paths.get("/home/john/Desktop/zadaca_testovi/prebrojavanje2"));
		Documents doc = db.build();
		
		//vidi indeksiranje
		doc.getDocuments().stream().forEach(o -> {
			for(String word : doc.getWords()) {
				System.out.println(word + "\t\t" + o.getVector()[doc.getWordIndexing().get(word)]);
			}
		});
	}
}
