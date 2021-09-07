package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class QueryFilterTest {
	IFilter f1, f2, f3, f4;
	StudentRecord s1, s2;
	
	@Before
	public void setUp() throws Exception {
		f1 = r -> Integer.parseInt(r.getFinalGrade()) < 3;
		f2 = r -> Integer.parseInt(r.getFinalGrade()) >= 3;
		f3 = r -> r.getFirstName().compareTo("Cz") < 0; 
		f4 = r -> r.getFirstName().compareTo("Cz") >= 0;
		
		s1 = new StudentRecord("0000000", "prezime1", "Cacaca", "4");
		s2 = new StudentRecord("1595735", "prezime2", "zadnji", "1");
	}

	@Test
	public void testAccepts() {
		assertFalse(f1.accepts(s1));
		assertTrue(f1.accepts(s2));
		
		assertTrue(f2.accepts(s1));
		assertFalse(f2.accepts(s2));
		
		assertTrue(f3.accepts(s1));
		assertFalse(f3.accepts(s2));
		
		assertFalse(f4.accepts(s1));
		assertTrue(f4.accepts(s2));
	}

}
