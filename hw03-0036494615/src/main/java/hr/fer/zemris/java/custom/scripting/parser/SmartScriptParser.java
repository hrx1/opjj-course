package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.lexer.LexerException;
import hr.fer.zemris.java.custom.scripting.lexer.ScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.ScriptLexerState;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.hw03.prob1.LexerState;

/**
 * Class describes Smart script parser. Parser uses lexer which has two modes defined in
 * {@link LexerState}. Parser throws {@link SmartScriptParserException} if it recognizes an error.
 * 
 * Recreates syntax tree with root node named Document Node. 
 * 
 * @author Hrvoje
 *
 */
public class SmartScriptParser {
	/** Used lexer */
	private ScriptLexer lexer;
	/** Stack which helps recreating syntax tree */
	private ObjectStack stack;
	/** Root node of a syntax tree */
	private DocumentNode docNode;
	
	/**
	 * Constructor for SmartScriptParser which will parse documentBody text.
	 * @param documentBody to parse.
	 */
	public SmartScriptParser(String documentBody) {
		lexer = new ScriptLexer(documentBody);
		stack = new ObjectStack();
		
		docNode = new DocumentNode();
		stack.push(docNode);
	 	lexer.nextToken();
		startParser();
	}
	
	/**
	 * Returns root node of a Syntax tree
	 * @return root node of a Syntax tree
	 */
	public DocumentNode getDocumentNode() {
		return docNode;
	}
	
	/**
	 * Starts a parser.
	 */
	private void startParser() {
	 	try {
		while (!lexer.getToken().getType().equals(TokenType.EOF) && !stack.isEmpty()) {
			System.out.println(lexer.getToken());
			if (lexer.getToken().getType().equals(TokenType.TEXT)) {
				TextNode t = parseText(); //vrati sljedeci, zato provjeri je li eof
				
				((Node)stack.peek()).addChildNode(t);
				
				if (lexer.getToken().getType().equals(TokenType.EOF)) break;
			}
			
			else if (lexer.getToken().getType().equals(TokenType.FOR_TAG)) {
				lexer.setState(ScriptLexerState.EXPRESSION);
				ForLoopNode t = parseFor();
				
				((Node)stack.peek()).addChildNode(t);
				stack.push(t);
				lexer.setState(ScriptLexerState.TEXT);
				lexer.nextToken();
			}
			else if (lexer.getToken().getType().equals(TokenType.END_TAG)) {
				lexer.setState(ScriptLexerState.EXPRESSION);
				if(!lexer.nextToken().getType().equals(TokenType.CLOSE_TAG)) {
					throw new SmartScriptParserException("END tag is not closed");
				}
				Node n = (Node) stack.pop();
				if(!(n instanceof ForLoopNode)) {
					throw new SmartScriptParserException("END tag should close IF only");
				}
				lexer.setState(ScriptLexerState.TEXT);
				lexer.nextToken();

			}
			else if(lexer.getToken().getType().equals(TokenType.ECHO_TAG)) {
				lexer.setState(ScriptLexerState.EXPRESSION);
				EchoNode t = parseEcho();
				((Node)stack.peek()).addChildNode(t);
				lexer.setState(ScriptLexerState.TEXT);
				lexer.nextToken();

			}
			else {
				throw new SmartScriptParserException("Parse error.");
			}
		}
	 	}catch(LexerException l) {
	 		System.out.println(lexer.getToken());

	 		throw new SmartScriptParserException(l.getLocalizedMessage());
	 	}catch(Exception rl) {
	 		throw new SmartScriptParserException("Parse error");
	 	}
	}
	
	/**
	 * Parses Echo Node
	 * @return echo node
	 */
	private EchoNode parseEcho() {
		ArrayIndexedCollection elements = new ArrayIndexedCollection();
		
		lexer.nextToken(); //preskoci {$
		
		while(!lexer.getToken().getType().equals(TokenType.CLOSE_TAG)) {
			Element el;
			Object value = lexer.getToken().getValue();
			
			switch (lexer.getToken().getType()) {
				case VAR:
					el = new ElementVariable((String) value);
					break;
				case CONST_INT:
					el = new ElementConstantInteger((int) value);
					break;
				case CONST_DOUBLE:
					el = new ElementConstantDouble((double) value);
					break;
				case FUNC:
					el = new ElementFunction((String) value);
					break;
				case STRING:
					el = new ElementString((String) value);
					break;
				case OPER:
					el = new ElementOperator((String) value);
					break;
				case EOF:
					throw new SmartScriptParserException("Echo TAG not closed");
				default :
					throw new SmartScriptParserException("Unknown Token while parsing node");
			}
			
			elements.add(el);
			
			lexer.nextToken();
		}
		
		Element[] elems = new Element[elements.size()];
		
		for(int i = 0; i < elems.length; ++i) {
			elems[i] = (Element) elements.get(i);
		}
		
		return new EchoNode(elems);
	}
	
	/**
	 * Parses text node
	 * @return text node
	 */
	private TextNode parseText() {
		StringBuilder sb = new StringBuilder();
		
		while(lexer.getToken().getType().equals(TokenType.TEXT)) {
			sb.append(lexer.getToken().getValue());
			lexer.nextToken();
		}
		return new TextNode(sb.toString());
	}
	
	/** 
	 * Parses for loop node
	 * @return for loop node
	 */
	private ForLoopNode parseFor() {
	 	Token currentToken = lexer.nextToken();//preskoci for tag
		TokenType currentType = currentToken.getType();
				
		ElementVariable var;
		Element[] elems = new Element[3];
		
		if(!currentType.equals(TokenType.VAR)) {
			throw new SmartScriptParserException("Wrong format");
		}
		var = new ElementVariable((String)currentToken.getValue());
		
		for(int i = 0; i < 3; ++i) {
			currentToken = lexer.nextToken();
			currentType = currentToken.getType();
			
			if (currentType.equals(TokenType.VAR)) {
				elems[i] = new ElementVariable((String) currentToken.getValue());
			}
			else if(currentType.equals(TokenType.CONST_DOUBLE)) {
				elems[i] = new ElementConstantDouble((Double) currentToken.getValue());

			}
			else if(currentType.equals(TokenType.CONST_INT)) {
				elems[i] = new ElementConstantInteger((int) currentToken.getValue());

			}
			else if(currentType.equals(TokenType.STRING)) {
				elems[i] = new ElementString((String) currentToken.getValue());

			}
			else if(currentType.equals(TokenType.CLOSE_TAG)) {
				if(i < 2) throw new SmartScriptParserException("Invalid for loop. Too few elements");
				
				return new ForLoopNode(var, elems[0], elems[1], null);
			}
			else {
				throw new SmartScriptParserException("Invalid for loop. Invalid element in for loop");
			}
		}
		
		lexer.nextToken();
		if(!lexer.getToken().getType().equals(TokenType.CLOSE_TAG)) throw new SmartScriptParserException("Invalid for loop.");
		
		return new ForLoopNode(var, elems[0], elems[1], elems[2]);
	}
	
}
