package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class StudentDatabaseTest {
	StudentDatabase base;
	
	@Before
	public void setUp() throws IOException {
		base = Screen.loadDatabase("database.txt");
	}
	
	@Test
	public void testForJMBAG() {
		String[][] persons = {{
				"0000000001",
				"Akšamović",
				"Marin",
				"2" },
		{		"0000000010",
				"Dokleja",
				"Luka",
				"3" },
		{		"0000000031",
				"Krušelj Posavec",
				"Bojan",
				"4" }
				};
		
		for(int i = 0; i < 3; ++i) {
			assertEquals(persons[i][0], base.forJMBAG(persons[i][0]).getJmbag());
			assertEquals(persons[i][1], base.forJMBAG(persons[i][0]).getLastName());
			assertEquals(persons[i][2], base.forJMBAG(persons[i][0]).getFirstName());
			assertEquals(persons[i][3], base.forJMBAG(persons[i][0]).getFinalGrade());
		}
	}
	
	@Test
	public void testSize() {
		assertEquals(63, base.size());
	}
	
	@Test
	public void testFilterPassAll() {
		assertEquals(base.size(), base.filter(sr -> true).size());
	}
	
	@Test
	public void testFilterPassNone() {
		assertEquals(0, base.filter(sr -> false).size());
	}

	@Test(expected = NullPointerException.class)
	public void testFilterNullArgument() {
		base.filter(null);
	}
	
}
