package hr.fer.zemris.java.custom.scripting.exec;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class defines needed operations made on Stack and Request Context.
 * 
 * @author Hrvoje
 *
 */
public class ElementStackOperations {
	/** Request Context */
	private RequestContext internal;
	/** Map of operations */
	Map<String, Consumer<Stack<String>>> stackOperations = new HashMap<>();
	
	/**
	 * Constructor for ElementStackOperations. Initializes operations.
	 */
	public ElementStackOperations() {
		super();
		
		stackOperations.put("+", (stack) -> {
			double second = Double.parseDouble(stack.pop());
			double first = Double.parseDouble(stack.pop());
			stack.push(String.valueOf(first + second));
		});
		stackOperations.put("-", (stack) -> {
			double second = Double.parseDouble(stack.pop());
			double first = Double.parseDouble(stack.pop());
			stack.push(String.valueOf(first - second));
		});
		stackOperations.put("*", (stack) -> {
			double second = Double.parseDouble(stack.pop());
			double first = Double.parseDouble(stack.pop());
			stack.push(String.valueOf(first * second));
		});
		stackOperations.put("/", (stack) -> {
			double second = Double.parseDouble(stack.pop());
			double first = Double.parseDouble(stack.pop());
			stack.push(String.valueOf(first / second));
		});
		
		stackOperations.put("@sin", (stack) -> {
			double first = Double.parseDouble(stack.pop());
			stack.push(String.valueOf(Math.sin(Math.toRadians(first))));
		});
		
		stackOperations.put("@decfmt", (stack) -> {
			String f = stack.pop();
			String x = stack.pop();
			f = f.replaceAll("\"", "").trim();
			DecimalFormat format =new DecimalFormat(f);
			stack.push(format.format(Double.valueOf(x)));
		});
		
		stackOperations.put("@dup", (stack) -> {
			String element = stack.pop();
			stack.push(element); stack.push(element);
		});
		
		stackOperations.put("@swap", (stack) -> {
			String first= stack.pop();
			String second = stack.pop();
			stack.push(first);
			stack.push(second);
		});
		
		stackOperations.put("@setMimeType", (stack) -> {
			String mime = stack.pop();
			internal.setMimeType(mime);
		});
		
		stackOperations.put("@paramGet", (stack) -> {
			String dv = stack.pop();
			String name = stack.pop();
			String value = internal.getParameter(name);
			stack.push((value == null) ? dv : value);
		});
		
		stackOperations.put("@pparamGet", (stack) -> {
			String dv = stack.pop();
			String name = stack.pop();
			String value = internal.getPersistentParameter(name);
			stack.push((value == null) ? dv : value);
		});
		
		stackOperations.put("@pparamSet", (stack) -> {
			String name = stack.pop();
			String value = stack.pop();
			internal.setPersistentParameter(name, value);
		});
		
		stackOperations.put("@pparamDel", (stack) -> {
			String name = stack.pop();
			internal.removePersistentParameter(name);
		});
		
		stackOperations.put("@tparamGet", (stack) -> {
			String dv = stack.pop();
			String name = stack.pop();
			String value = internal.getTemporaryParameter(name);
			stack.push((value == null) ? dv : value);
		});
		
		
		stackOperations.put("@tparamSet", (stack) -> {
			String name = stack.pop();
			String value = stack.pop();
			internal.setTemporaryParameter(name, value);
		});

		stackOperations.put("@tparamDel", (stack) -> {
			String name = stack.pop();
			internal.removeTemporaryParameter(name);
		});

	}
	
	/**
	 * Applies operation on stack
	 * @param stack stack
	 * @param operation operation
	 * @param internal request context
	 */
	public void applyOperation(Stack<String> stack, String operation, RequestContext internal) {
		this.internal = internal;
		stackOperations.get(operation).accept(stack);
	}
	
	
}
