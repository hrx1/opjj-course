package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw05.collections.SimpleHashtable;


/**
 * Class describes database of student records.
 * @author Hrvoje
 *
 */
public class StudentDatabase {
	/** Internal indexing student records by jmbag **/
	SimpleHashtable<String, StudentRecord> jmbagIndexing;
	/** List of student records **/
	List<StudentRecord> records;
	
	/**
	 * Parses student records from list of strings.
	 * @param data list to parse
	 */
	public StudentDatabase(List<String> data) {
		jmbagIndexing = new SimpleHashtable<>(data.size());
		records = new ArrayList<>(data.size());
		
		for(String studentData : data) {
			addStudent(studentData);
		}
	}
	
	/**
	 * Returns Student Record for student with JMBAG.
	 * Returns null if record doesn't exist.
	 * Throws {@link NullPointerException} if jmbag is null.
	 * @param jmbag of a student
	 * @return student record
	 * @throws NullPointerException if jmbag is null
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return jmbagIndexing.get(jmbag);
	}
	
	/**
	 * Returns list of all saved Student Records which pass <code>filter</code>.
	 * @param filter which decides who passes
	 * @return list of all saved Student Records which pass <code>filter</code>
	 */
	public List<StudentRecord> filter(IFilter filter) {
		Objects.requireNonNull(filter);
		List<StudentRecord> list = new LinkedList<>();
		
		for(StudentRecord record : records) {
			if(filter.accepts(record)) {
				list.add(record);
			}
		}
		
		return list;
	}
	
	/**
	 * Returns number of student records in database.
	 * @return number of student records in database
	 */
	public int size() {
		return jmbagIndexing.size();
	}
	
	/**
	 * Indexes one student record from <code>data</code>.
	 * @param data to be parsed
	 */
	private void addStudent(String data) {
		String[] split = data.split("\t");
		String jmbag = split[0];
		String lastName = split[1];
		String firstName = split[2];
		String finalGrade = split[3];
		
		StudentRecord tmp =  new StudentRecord(jmbag, lastName, firstName, finalGrade);
		
		jmbagIndexing.put(jmbag, tmp);
		records.add(tmp);
	}

}
