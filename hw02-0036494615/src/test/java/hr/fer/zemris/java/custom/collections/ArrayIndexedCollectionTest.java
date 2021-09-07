package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

public class ArrayIndexedCollectionTest {

	ArrayIndexedCollection array1 = new ArrayIndexedCollection();
	ArrayIndexedCollection array2 = new ArrayIndexedCollection(4);
	ArrayIndexedCollection array3;
	ArrayIndexedCollection array4;
	ArrayIndexedCollection array5;
	
	String obj1 = "bla";
	String obj2 = "blabla";
	String obj3 = "string3";
	String obj4 = "string4";
	
	@Before
	public void setUp() {
		array1.add(obj1);
		array1.add(obj2);
		array1.add(obj3);
		array1.add(obj4);
		
		array3 = new ArrayIndexedCollection(array1, 3);
		array4 = new ArrayIndexedCollection(array1, 50);
		array5 = new ArrayIndexedCollection(array1);
		
	}
	
	@Test
	public void testConstructors() {
		int emptySize = 0;
		int expectedSize1 = 4;
		
		assertEquals(expectedSize1, array1.size());
		assertEquals(emptySize, array2.size());
		assertEquals(expectedSize1, array3.size());
		assertEquals(expectedSize1, array4.size());
		assertEquals(expectedSize1, array5.size());
		
		//provjerava je li "blabla" dodan u svaki:
		assertTrue(array1.contains("blabla"));
		assertFalse(array2.contains("blabla"));
		assertTrue(array3.contains("blabla"));
		assertTrue(array4.contains("blabla"));
		assertTrue(array5.contains("blabla"));
	}
	
	@Test
	public void testAdd() {
		int expectedSize = 5;
		String obj = "novo";
		array1.add(obj);
		
		assertEquals(expectedSize, array1.size());
	}
	
	@Test
	public void testAddNull() {
		try {
			array1.add(null);
			Assert.fail();
		}catch (NullPointerException e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testGet() {
		assertEquals(obj1, array1.get(0));
		assertEquals(obj2, array1.get(1));
		assertEquals(obj3, array1.get(2));
	}
	
	@Test
	public void testGetOutOfBounds() {
		int index = array1.size();
		try {
			array1.get(index);
			Assert.fail();
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
	}
	
	@Test 
	public void testClear() {
		array1.clear();
		assertEquals(0, array1.size());
		try {
			array1.get(0);
			Assert.fail();
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testInsert() {
		String obj1 = "Inserted1";
		String obj2 = "Inserted2";
		String obj3 = "Inserted3";
		
		array3.insert(obj1, 0);
		array3.insert(obj2, 4);
		array3.insert(obj3,  array3.size() - 1);
				
		assertEquals(obj1, array3.get(0));
		assertEquals(obj2, array3.get(4));
		assertEquals(obj3, array3.get(array3.size() - 2));	
	}
	
	@Test
	public void testIndexOf() {
		int expected = 3;
		String obj = "insertedIndexOf";
		
		assertEquals(-1, array4.indexOf(obj));
		
		array4.insert(obj, expected);
		assertEquals(expected, array4.indexOf(obj));
		
		//Test indexOf null
		assertEquals(-1, array4.indexOf(null));
	}
	
	@Test
	public void testRemove() {
		int size = array4.size();
		int index = 1;

		Object obj = array4.get(index);
		array4.remove(index);
		assertFalse(array4.contains(obj));
		assertNotEquals(size, array4.size());
	}
}
