package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.Assert.*;

import org.junit.Test;

public class ScriptLexerTest {
	String[] tekstovi = {"{$ 	FOR asdf {$for",
			"{$		=k",
			"{$= dobar tekst pocinje TAGom",
			"{$END nije kraj",
			"\\{$=1$}. Now actually write one {$=1$}",
			"tag na \nkraju {$="
	};

	@Test
	public void testPrvi() {
		String [] ocekivanje = {
				"(FOR_TAG, {$ FOR )",
				"(TEXT, asdf )",
				"(FOR_TAG, {$ FOR )",
				"(EOF, null)"};
		ScriptLexer lex = new ScriptLexer(tekstovi[0]);
		
		for(String s : ocekivanje) {
			assertEquals(s, lex.nextToken().toString());
		}
	}
	
	@Test
	public void testDrugi() {
		String [] ocekivanje = {
				"(ECHO_TAG, {$ = )",
				"(TEXT, k)", 
				"(EOF, null)"
				};
		ScriptLexer lex = new ScriptLexer(tekstovi[1]);
		
		for(String s : ocekivanje) {
			assertEquals(s, lex.nextToken().toString());
		}

	}
	@Test
	public void testTreci() {
		String [] ocekivanje = {
						"(ECHO_TAG, {$ = )",
						"(TEXT, dobar tekst pocinje TAGom)",
						"(EOF, null)"};
		ScriptLexer lex = new ScriptLexer(tekstovi[2]);
		
		for(String s : ocekivanje) {
			assertEquals(s, lex.nextToken().toString());
		}

	}
	@Test
	public void testCetvrti() {
		String [] ocekivanje = {
					"(END_TAG, {$ END )",
					"(TEXT, nije kraj)",
					"(EOF, null)"
					};
		ScriptLexer lex = new ScriptLexer(tekstovi[3]);
		
		for(String s : ocekivanje) {
			assertEquals(s, lex.nextToken().toString());
		}

	}
	@Test
	public void testPeti() {
		String [] ocekivanje = {
				"(TEXT, {$=1$}. Now actually write one )" ,
				"(ECHO_TAG, {$ = )" ,
				"(TEXT, 1$})", 
				"(EOF, null)"
				};
		ScriptLexer lex = new ScriptLexer(tekstovi[4]);
		
		for(String s : ocekivanje) {
			assertEquals(s, lex.nextToken().toString());
		}

	}
	@Test
	public void testSesti() {
		String [] ocekivanje = {
				"(TEXT, tag na \nkraju )",
				"(ECHO_TAG, {$ = )",
				"(EOF, null)" 
				};
		ScriptLexer lex = new ScriptLexer(tekstovi[5]);
		
		for(String s : ocekivanje) {
			assertEquals(s, lex.nextToken().toString());
		}

	}

}
