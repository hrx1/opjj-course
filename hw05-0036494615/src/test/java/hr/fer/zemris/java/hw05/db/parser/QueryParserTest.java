package hr.fer.zemris.java.hw05.db.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class QueryParserTest {
	QueryParser qp1, qp2;
	
	@Before
	public void setUp() throws Exception {
		qp1 = new QueryParser(" jmbag =       \"0123456789\" ");
		qp2 = new QueryParser("jmbag <= \"0000000003\" AND jmbag = \"0000000003\" AND lastName LIKE \"L*\"");
	}

	@Test
	public void testIsDirectQuery() {
		assertTrue(qp1.isDirectQuery());
		assertFalse(qp2.isDirectQuery());
	}

	@Test
	public void testGetQueriedJMBAG() {
		assertEquals("0123456789", qp1.getQueriedJMBAG());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetQueriedJMBAG_Illegal() {
		qp2.getQueriedJMBAG();
	}
	
	@Test
	public void testSize() {
		assertEquals(1, qp1.getQuery().size());
		assertEquals(3, qp2.getQuery().size());
	}

}
