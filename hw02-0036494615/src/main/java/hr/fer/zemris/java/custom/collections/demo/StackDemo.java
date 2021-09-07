package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Uses stack to calculate value of postfix expression. Postfix expression
 * should be given from command line as a single argument.
 * 
 * @author Hrvoje
 *
 */
public class StackDemo {

	/**
	 * Calculates value of a postfix expression.
	 * @param args args[0] is a postfix expression
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Postfix expression must be given as single argument!");
			return;
		}

		ObjectStack stack = new ObjectStack();

		for (String element : args[0].split("\\s+")) {

			if (isNumber(element)) {
				stack.push(Integer.parseInt(element));
				
			} 
			else {
				try {
					performAction(stack, element);
					
				} catch (EmptyStackException es) {
					System.out.println("Expression is not valid postfix representation or decimal number is given.");
					return;
				} catch (IllegalArgumentException il) {
					System.out.println("Expression contains invalid operation.");
					return;
				} catch (ArithmeticException ae) {
					System.out.println("Division by zero is impossible.");
					return ;
				}
			}
		}

		if (stack.size() != 1) {
			System.out.println("Expression is not valid postfix representation!");
		} else {
			System.out.println("Expression evaluates to " + stack.pop());
		}

	}

	/**
	 * Check whether string can be interpreted as a Integer.
	 * @param string to be examined.
	 * @return true if it can.
	 */
	public static boolean isNumber(String string) {
		return string.matches("^-?\\d+$");
	}

	/**
	 * Performs action over popped top two elements of a stack and pushes result back to the stack. Action is defined by element.
	 * Supported actions and its symbols: + - * / % 
	 * Throws {@link IllegalArgumentException} if there is no action defined by element.
	 * @param stack from which elements will be popped or pushed to
	 * @param element defines action
	 */
	private static void performAction(ObjectStack stack, Object element) {
		Integer second = (Integer) stack.pop();
		Integer first = (Integer) stack.pop();

		String operation = (String) element;

		switch (operation) {
		case "+":
			stack.push(first + second);
			break;
		case "-":
			stack.push(first - second);
			break;
		case "/":
			stack.push(first / second); 
			break;
		case "*":
			stack.push(first / second);
			break;
		case "%":
			stack.push(first % second);
			break;
		default:
			throw new IllegalArgumentException("Operation " + operation + " cannot be performed");
		}
	}

}
