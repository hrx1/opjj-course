package hr.fer.zemris.java.cmdapps.search.DocumentsModel;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;

public class DocumentsBuilderTest {
	public static void main(String[] args) throws IOException {
		
		DocumentsBuilder db = new DocumentsBuilder(new HashSet());
		
		db.addDocument(Paths.get("/home/john/Desktop/zadaca_testovi/Ovo_je_primjer_clanka"));
		db.addDocument(Paths.get("/home/john/Desktop/zadaca_testovi/prebrojavanje"));

		
		db.createWords().stream().sorted().forEach(System.out::println);
	}
}
