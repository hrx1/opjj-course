package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static hr.fer.zemris.java.hw05.db.ComparisonOperators.*;

public class ComparisonOperatorsTest {
	StudentDatabase database;
	String firstS, secondS, thirdS;
	
	@Before
	public void setUp() {
		firstS = "AAA";
		secondS = "BBB";
		thirdS = "BBB";
	}
	
	@Test
	public void lessTest() {
		assertTrue(LESS.satisfied(firstS, secondS));
		assertFalse(LESS.satisfied(secondS, thirdS));
		assertFalse(LESS.satisfied(thirdS, firstS));
	}
	
	@Test
	public void lessOrEqualTest() {
		assertTrue(LESS_OR_EQUALS.satisfied(firstS, secondS));
		assertTrue(LESS_OR_EQUALS.satisfied(secondS, thirdS));
		assertFalse(LESS_OR_EQUALS.satisfied(thirdS, firstS));
	}
	
	@Test
	public void greater() {
		assertFalse(GREATER.satisfied(firstS, secondS));
		assertFalse(GREATER.satisfied(secondS, thirdS));
		assertTrue(GREATER.satisfied(thirdS, firstS));
	}
	
	@Test
	public void greaterOrEqualTest() {
		assertFalse(GREATER_OR_EQUALS.satisfied(firstS, secondS));
		assertTrue(GREATER_OR_EQUALS.satisfied(secondS, thirdS));
		assertTrue(GREATER_OR_EQUALS.satisfied(thirdS, firstS));

	}
	
	@Test
	public void equalsTest() {
		assertFalse(EQUALS.satisfied(firstS, secondS));
		assertTrue(EQUALS.satisfied(secondS, thirdS));
		assertFalse(EQUALS.satisfied(thirdS, firstS));
	}
	
	@Test
	public void notEqualsTest() {
		assertTrue(NOT_EQUALS.satisfied(firstS, secondS));
		assertFalse(NOT_EQUALS.satisfied(secondS, thirdS));
		assertTrue(NOT_EQUALS.satisfied(thirdS, firstS));
	}
	
	@Test
	public void likeTest() {
		String pattern1 = "ac*bc";
		String s11 = "acbc";
		assertTrue(LIKE.satisfied(s11, pattern1)); //T
		
		String s12 = "acaclkajdfbcbc";
		assertTrue(LIKE.satisfied(s12, pattern1)); //T
		
		String pattern2 = "acsadf";
		String s21 = "asd";
		String s22 = "acs";
		String s23 = "acsadf";

		assertFalse(LIKE.satisfied(s21,pattern2)); //F
		assertFalse(LIKE.satisfied(s22,pattern2)); //F
		assertTrue(LIKE.satisfied(s23,pattern2)); //T
		assertFalse(LIKE.satisfied(s23 + "a",pattern2)); //F
		
		String pattern3 = "*asdf";
		assertTrue(LIKE.satisfied("asdf", pattern3)); //T
		assertTrue(LIKE.satisfied("basdf", pattern3)); //T
		assertFalse(LIKE.satisfied("asdfb", pattern3)); //F
		
		String pattern4 = "asdf*";
		assertTrue(LIKE.satisfied("asdf", pattern4)); //T
		assertFalse(LIKE.satisfied("basdf", pattern4)); //F
		assertTrue(LIKE.satisfied("asdfb", pattern4)); //T
		
		String pattern5 = "*";
		assertTrue(LIKE.satisfied("asdf", pattern5)); //T
		assertTrue(LIKE.satisfied("basdf", pattern5)); //T
		assertTrue(LIKE.satisfied("asdfb", pattern5)); //T
	}
}
