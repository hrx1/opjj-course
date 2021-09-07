package hr.fer.zemris.java.hw05.collections;

import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.hw05.collections.SimpleHashtable;
import hr.fer.zemris.java.hw05.collections.SimpleHashtable.TableEntry;


public class SimpleHashtableTest {
	SimpleHashtable<String, String> dict;
	
	@Before
	public void setUp() {
		dict = new SimpleHashtable<>();
	}
	
	@Test
	public void emptyTest() {
		assertNull(dict.get("nema nista"));
		assertEquals(0, dict.size());
	}
	
	@Test
	public void sizeTest() {
		dict.put("Pero", "2");
		dict.put("Ivo", "20");
		dict.put("Mara", "21");
		dict.put("Mara", "1");
		
		assertEquals(3, dict.size());
		
		for(int i = 0; i < 10; ++i) {
			dict.put(i + "string", String.valueOf(i));
		}
		
		assertEquals(13, dict.size());
	}
	
	@Test
	public void putGetTest() {
		String key = "Kljuc";
		String value = "Vrijednost";
		
		dict.put(key, value);
		
		assertEquals(value, dict.get(key));
	}
	
	@Test
	public void collisionTest() {
		String key = "Kljuc2";
		String oldValue = "2";
		String newValue = "4";
		
		dict.put(key, oldValue);
		int oldSize = dict.size();

		//value prije i poslije
		assertEquals(oldValue, dict.get(key));
		dict.put(key, newValue);
		assertEquals(newValue, dict.get(key));
		//size poslije
		assertEquals(oldSize, dict.size());
	}
	
	@Test
	public void putGetNullTest() {
		String key = "NullTest";
		dict.put(key, null);
		
		assertNull(dict.get(key));
		
		assertNull(dict.get("Nepostojeci kljuc"));
	}
	
	@Test
	public void containsKeyTest() {
		String key = "KeyContains";
		String value = "Value";
		
		dict.put(key, value);
		assertTrue(dict.containsKey(key));
		assertFalse(dict.containsKey("asdf;lklkasdf"));
		assertFalse(dict.containsKey(null));
		assertFalse(dict.containsKey(1));
	}
	
	@Test
	public void containsValueTest() {
		String key = "KeyContains123";
		String value = "Value123";
		
		dict.put(key, value);
		assertTrue(dict.containsValue(value));
		assertFalse(dict.containsValue("asdf;lklkasdf"));
		assertFalse(dict.containsValue(null));
		assertFalse(dict.containsValue(1));
		
		dict.put(key, null);
		assertTrue(dict.containsValue(null));

	}
	
	@Test
	public void removeTest() {
		String key = "KeyContains123";
		String value = "Value123";
		
		dict.put(key, value);

		assertTrue(dict.containsKey(key));
		assertTrue(dict.containsValue(value));
		
		dict.remove(key);
		
		assertFalse(dict.containsKey(key));

	}
	
	@Test(expected = ConcurrentModificationException.class)
	public void outerModificationTest() {
		dict.put("Pero", "2");
		dict.put("Ivo", "20");
		dict.put("Mara", "21");
		dict.put("Mara", "1");

		for(TableEntry<String, String> te : dict) {
			dict.remove(te.getKey());
		}
	}

}
