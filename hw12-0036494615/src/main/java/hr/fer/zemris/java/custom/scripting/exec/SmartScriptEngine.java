package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * SmartScriptEngine is engine for running Script given in homework.
 * 
 * @author Hrvoje
 *
 */
public class SmartScriptEngine {
	/** Document Node */
	private DocumentNode documentNode;
	/** Request Context */
	private RequestContext requestContext;
	/** Object Multi Stack */
	private ObjectMultistack multistack = new ObjectMultistack();
	/** Visitor */
	private INodeVisitor visitor = new INodeVisitor() {
		private ElementStackOperations operations = new ElementStackOperations();
		
		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException ignorable) {}

		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			ElementVariable variable = node.getVariable();
			Element startElement = node.getStartExpression();
			Element endElement = node.getEndExpression();
			Element stepElement = node.getStepExpression();
			
			int current = Integer.parseInt(startElement.asText());
			int end = Integer.parseInt(endElement.asText());
			int step = Integer.parseInt(stepElement.asText());
			
			multistack.push(variable.asText(), new ValueWrapper(current));
			
			while(true) {
				current = (Integer) multistack.peek(variable.asText()).getValue();
				if(current > end) break;
				
				for(int i = 0; i < node.numberOfChildren(); ++i) {
					node.getChild(i).accept(visitor);
				}
				
				current = (Integer) multistack.pop(variable.asText()).getValue();
				current += step;
				multistack.push(variable.asText(), new ValueWrapper(current));
			}
			
			multistack.pop(variable.asText());
			
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<String> elements = new Stack<>();
			
			
			for(Element child : node.getElements()) {
				if(child instanceof ElementConstantDouble || 
						child instanceof ElementConstantInteger ||
						child instanceof ElementString) {
					String toPush = child.asText().trim();
					if(toPush.startsWith("\"")) {
						toPush = toPush.substring(1);
					}
					if(toPush.endsWith("\"")) {
						toPush = toPush.substring(0, toPush.length() - 1);
					}
					elements.push(toPush);
					
				}
				else if(child instanceof ElementOperator ||
						child instanceof ElementFunction) {
					operations.applyOperation(elements, child.asText(), requestContext);
				}
				else if(child instanceof ElementVariable) {
					double value = Double.valueOf(multistack.peek(child.asText()).getValue().toString());
					elements.push(String.valueOf(value));
				}
				
			}
			
			//outputs remaining:
			printStackReverse(elements);
		}
		
		/**
		 * Prints stack elements in reversed order
		 * @param elements
		 */
		private void printStackReverse(Stack<String> elements) {
			if(elements.isEmpty()) return ;
			String s = elements.pop();
			printStackReverse(elements);
			try {
				requestContext.write(s);
			} catch (IOException ignorable) {}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for(int i = 0; i < node.numberOfChildren(); ++i) {
				node.getChild(i).accept(visitor);
			}
		}

	};

	/**
	 * Constructor for SmartScriptEngine
	 * 
	 * @param documentNode Document Node
	 * @param requestContext Request Context
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Executes Engine
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
}
