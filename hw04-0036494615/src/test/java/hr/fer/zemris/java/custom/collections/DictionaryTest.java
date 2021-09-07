package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DictionaryTest {
	Dictionary dict;
	
	@Before
	public void setUp() {
		dict = new Dictionary();
	}
	
	@Test
	public void emptyTest() {
		assertNull(dict.get("nema nista"));
		assertEquals(0, dict.size());
	}
	
	@Test
	public void sizeTest() {
		dict.put("Pero", 2);
		dict.put("Ivo", 20);
		dict.put("Mara", 21);
		dict.put("Mara", 1);
		
		assertEquals(3, dict.size());
		
		for(int i = 0; i < 10; ++i) {
			dict.put(i + "string", i);
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

}
