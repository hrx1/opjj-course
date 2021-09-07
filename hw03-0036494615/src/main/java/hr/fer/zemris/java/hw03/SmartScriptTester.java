package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
/**
 * Implements simple test for Smart Script Parser
 * @author Hrvoje
 *
 */
public class SmartScriptTester {

	/**
	 * Main method
	 * @param args nothing
	 * @throws IOException nothing
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
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody);
		
		//Printaj rezultat parsiranog proslog:
		SmartScriptParser parser2 = null;
		try {
			parser2 = new SmartScriptParser(originalDocumentBody);
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			throw e;
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			throw e;
		}
		DocumentNode document2 = parser2.getDocumentNode();
		String originalDocumentBody2 = createOriginalDocumentBody(document2);
		System.out.println(originalDocumentBody2);
		
		
		//KRAJNJE:
		System.out.println("Isti su?" + originalDocumentBody.equals(originalDocumentBody2));

	}

	/**
	 * Recreates original body text from syntax tree. Tree has root node DocumentNode
	 * @param documentNode root 
	 * @return String of original body
	 */
	public static String createOriginalDocumentBody(DocumentNode documentNode) {
		Objects.requireNonNull(documentNode);
		String s = "";
		
		if (documentNode.numberOfChildren() == 0) {
			return s;
		}
		
		for (int i = 0; i < documentNode.numberOfChildren(); ++i) {
			s += expandNodeString(documentNode.getChild(i));
		}

		return s;
	}
	
	/**
	 * Returns string representation of node and it's children
	 * @param node
	 * @return
	 */
	private static String expandNodeString(Node node) {
		String result = node.nodeAndElementsAsText();

		if (node.numberOfChildren() == 0) {
			return result;
		}
			
		for (int i = 0; i < node.numberOfChildren(); ++i) {
			result = result + expandNodeString(node.getChild(i));
		}
		
		if(node instanceof ForLoopNode) {
			result += "{$ END $}";
		}
		return result;
	}
}
