package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class ObjectMultistackTest {
	String key1, key2, nonExistingKey;
	ObjectMultistack multistack;
	ValueWrapper v1, v2, v3;
	
	@Before
	public void setUp() throws Exception {
		key1 = "key1";
		key2 = "key2";
		nonExistingKey = "nepostojim";
		v1 = new ValueWrapper("3");
		v2 = new ValueWrapper(4);
		v3 = new ValueWrapper(3.1);
		
		multistack = new ObjectMultistack();
	}

	@Test
	public void testPushAndPop() {
		multistack.push(key1, v1);
		assertEquals(v1.getValue(), multistack.pop(key1).getValue());
		
		multistack.push(key1, v1);
		multistack.push(key1, v2);
		multistack.push(key1, v3);
		
		assertEquals(v3.getValue(), multistack.pop(key1).getValue());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testPopNonExisting() {
		multistack.pop(nonExistingKey);
	}
	@Test(expected=NullPointerException.class)
	public void testPushNull() {
		multistack.push(key1, null);
	}

	@Test
	public void testPeek() {
		int oldSize = multistack.numberOfElements();
		
		multistack.push(key1, v1);
		multistack.push(key2, v2); //pusham na drugi stog
		
		assertEquals(v1.getValue(), multistack.peek(key1).getValue());
		
		assertEquals(oldSize + 2, multistack.numberOfElements());
	}

	@Test
	public void testNumberOfElements() {
		int oldSize = multistack.numberOfElements();
		
		multistack.push(key1, v1);
		multistack.push(key2, v2);
		multistack.push(key1, v3);
		multistack.push(key2, v3);
		
		multistack.pop(key1);
		multistack.pop(key2);
		
		assertEquals(oldSize + 2, multistack.numberOfElements());
	}

}
