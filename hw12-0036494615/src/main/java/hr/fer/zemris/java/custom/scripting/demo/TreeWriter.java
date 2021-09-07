package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Class tests TreeWriter WriterVisitor
 * @author Hrvoje
 *
 */
public class TreeWriter {
	
	/**
	 * Tests TreeWriter, takes one argument - path to file.
	 * 
	 * @param args takes one argument - path to file
	 * @throws IOException Exception
	 */
	public static void main(String[] args) throws IOException {
		String docBody = new String(
				 Files.readAllBytes(Paths.get(args[0])),
				 StandardCharsets.UTF_8
				);
		
		//printaj ucitano:
		System.out.println(docBody);
		System.out.println();
		
		//printaj parsirano ucitano:
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			throw e;
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			throw e;
		}
	
		DocumentNode document = parser.getDocumentNode();
		document.accept(new WriterVisitor());
		
	}
	
	/**
	 * Visitor will reproduce original text.
	 * 
	 * @author Hrvoje
	 *
	 */
	private static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node.nodeAndElementsAsText());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.print(node.nodeAndElementsAsText());
			
			for(int i = 0; i < node.numberOfChildren(); ++i) {
				node.getChild(i).accept(this);
			}
			
			System.out.print("{$ END $}");

		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.print(node.nodeAndElementsAsText());
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for(int i = 0; i < node.numberOfChildren(); ++i) {
				node.getChild(i).accept(this);
			}
		}
		
	}
}
