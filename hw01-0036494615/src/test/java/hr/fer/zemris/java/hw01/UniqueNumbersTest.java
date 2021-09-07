package hr.fer.zemris.java.hw01;

import static org.junit.Assert.*;

import org.junit.Test;

public class UniqueNumbersTest {

	@Test
	public void poredakUStablu() {		
		UniqueNumbers.TreeNode glava = null;
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 21);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 35);
		
		assertEquals(42, glava.value);
		assertEquals(21, glava.left.value);
		assertEquals(35, glava.left.right.value);
		assertEquals(76, glava.right.value);		
	}
	
	@Test
	public void velicinaPraznogStabla() {
		UniqueNumbers.TreeNode glava = null;
		assertEquals(0, UniqueNumbers.treeSize(glava));
	}
	
	@Test
	public void velicinaPopunjenogStablaSDodanimDuplikatima() {
		UniqueNumbers.TreeNode glava = null;
		
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 21);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 35);
		
		assertEquals(4, UniqueNumbers.treeSize(glava));
	}

	@Test
	public void sadrziVrijednostUPraznomStablu() {
		UniqueNumbers.TreeNode glava = null;

		assertFalse(UniqueNumbers.containsValue(glava, 12));
	}
	
	@Test
	public void sadrziVrijednost() {
		UniqueNumbers.TreeNode glava = null;
		
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 21);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 35);

		assertFalse(UniqueNumbers.containsValue(glava, 36));
		assertTrue(UniqueNumbers.containsValue(glava, 42));
	}
}
