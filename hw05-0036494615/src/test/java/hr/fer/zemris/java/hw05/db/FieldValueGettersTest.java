package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static hr.fer.zemris.java.hw05.db.FieldValueGetters.*;

public class FieldValueGettersTest {
	StudentRecord [] students;
	
	
	@Before
	public void setUp() {
		students = new StudentRecord[5];
		
		for(int i = 0; i < students.length; ++i) {
			students[i] = new StudentRecord("000000000" + i, "Lname" + i, "Fname" + i, String.valueOf(i));
		}
		
	}
	
	@Test
	public void firstNameTest() {
		for(int i = 0; i < students.length; ++i) {
			assertEquals("Fname" + i, FIRST_NAME.get(students[i]));
		}
	}
	
	@Test
	public void lastNameTest() {
		for(int i = 0; i < students.length; ++i) {
			assertEquals("Lname" + i, LAST_NAME.get(students[i]));
		}
	}
	
	@Test
	public void jmbagTest() {
		for(int i = 0; i < students.length; ++i) {
			assertEquals("000000000" + i, JMBAG.get(students[i]));
		}
	}

}
