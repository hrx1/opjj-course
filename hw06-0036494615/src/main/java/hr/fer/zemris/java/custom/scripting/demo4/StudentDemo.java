
package hr.fer.zemris.java.custom.scripting.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Class provides tests examples given in the assignment.
 * Students can be filtered through these 8 methods:
 * 
 * 1. vratiBodovaViseOd25
 * 2. vratiBrojOdlikasa
 * 3. vratiListuOdlikasa
 * 4. vratiSortiranuListuOdlikasa
 * 5. vratiPopisNepolozenih
 * 6. razvrstajStudentePoOcjenama
 * 7. vratiBrojStudenataPoOcjenama
 * 8. razvrstajProlazPad
 * 
 * @author Hrvoje
 *
 */
public class StudentDemo {
	
	/**
	 * Main method provides tests examples given in the assignment
	 * @param args neglected
	 * @throws IOException if studenti2.txt doesn't exist
	 * @throws NoSuchElementException if data is not formatted properly
	 */
	public static void main(String[] args) throws IOException {
		
		List<String> lines = Files.readAllLines(Paths.get("studenti2.txt"));
		List<StudentRecord> records = convert(lines);
		System.out.println("Broj studenata: " + records.size());
		
		//1
		long broj = vratiBodovaViseOd25(records);
		System.out.println("Broj studenata cija je suma bodova > 25 je " + broj);
		
		//2
		long broj5 = vratiBrojOdlikasa(records);
		System.out.println("Broj studenata cija je ocjena " + 5 + " je " + broj5);
		
		//3
		int odlican = 5;
		List<StudentRecord> odlikasi = vratiListuOdlikasa(records);
		System.out.println("Broj odlikasa je: " + odlikasi.size());
		//4
		List<StudentRecord> odlikasiSortirano = vratiSortiranuListuOdlikasa(records);
		
		odlikasiSortirano.forEach(o -> System.out.println(o));
		
		//5
		List<String> nepolozeniJMBAGovi = vratiPopisNepolozenih(records);
		
		nepolozeniJMBAGovi.forEach(o -> System.out.println(o));
		
		//6
		Map<Integer, List<StudentRecord>> mapaPoOcjenama = razvrstajStudentePoOcjenama(records);
		
		System.out.println(mapaPoOcjenama.get(odlican).size());
	
		//7
		Map<Integer, Integer> mapaPoOcjenama2 = records.stream()
				.collect(Collectors
						.toMap(StudentRecord::getOcjena, o -> 1, (o1, o2) -> o1 + 1));
		
		mapaPoOcjenama2.entrySet().stream()
				.sorted((o1, o2) -> o1.getKey()
						.compareTo(o1.getKey()))
				.forEach(System.out::println);;
		
		
		//8
		Map<Boolean, List<StudentRecord>> prolazNeprolaz = razvrstajProlazPad(records);
		
		System.out.println("Broj padova je " + prolazNeprolaz.get(false).size());
		}
	
	
	/**
	 * Partitions students according to whether they passed or failed.
	 * @param records of records
	 * @return map whose keys are boolean, and values are lists of StudentRecords.
	 */
	private static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.partitioningBy(o -> o.getOcjena() > 1));
	}


	/**
	 * Returns map whose keys are grades, and values are list of student records who have that grade.
	 * @param records of students
	 * @return map whose keys are grades, and values are list of student records who have that grade.

	 */
	private static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.groupingBy(o -> o.getOcjena()));
	}


	/**
	 * Return list of jmbags of students who failed the class.
	 * @param records of students
	 * @return list of jmbags of students who failed the class.
	 */
	private static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
		return records.stream()
				.filter(o -> o.getOcjena() == 1)
				.map(o -> o.getJmbag())
				.sorted((o1, o2) -> o1.compareTo(o2))
				.collect(Collectors.toList());
	}



	/**
	 * Return sorted list of student records whose grade is 5.
	 * @param records of students
	 * @return sorted list of student records whose grade is 5.
	 */
	private static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		return records.stream()
				.filter(o -> o.getOcjena() == 5)
				.sorted((o1, o2) -> o1.getOcjena().compareTo(o2.getOcjena()))
				.collect(Collectors.toList());
	}


	/**
	 * Return list of student records whose grade is 5.
	 * @param records of students
	 * @return list of student records whose grade is 5.
	 */
	private static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
		return records.stream()
				.filter(o -> o.getOcjena() == 5)
				.collect(Collectors.toList());
	}
	
	/**
	 * Return number of students from records whose grade is 5.
	 * @param records of students
	 * @return number of students from records whose grade is 5.
	 */
	private static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(o -> o.getOcjena() == 5).count();
	}

	/**
	 * Returns number of students from records who achieved more than 25 points.
	 * @param records of students
	 * @return number of students from records who achieved more than 25 points.
	 */
	private static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream().filter(o -> o.getBodoviLabos() + o.getBodoviMI() + o.getBodoviZI() > 25).count();
	}
	
	
	
	
	/**
	 * Converts data from lines to ArrayList of StudentRecord objects.
	 * @param lines data
	 * @return list of StudentRecord objects
	 * @throws NoSuchElementException if data is not formatted correctly
	 * @throws NullPointerException if lines is <code>null</code>
	 */
	private static List<StudentRecord> convert(List<String> lines) {
		Objects.requireNonNull(lines);
		
		ArrayList<StudentRecord> result = new ArrayList<>(lines.size());
		
		for(String data : lines) {
			if(data.length() == 0) continue; //preskoci prazne linije
			Scanner sc = new Scanner(data);
			sc.useDelimiter("\t");
			
			try {
				result.add(new StudentRecord(sc.next(), sc.next(), sc.next(), sc.next(), sc.next(), sc.next(), sc.next()));
			}catch(NoSuchElementException e) {
				sc.close();
				throw new NoSuchElementException("Data not formatted correctly.");
			}
			sc.close();
		}
		
		return result;
	}
}
