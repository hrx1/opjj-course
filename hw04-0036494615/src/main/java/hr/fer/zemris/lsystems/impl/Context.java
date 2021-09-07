package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Class describes current stack-like Context of Turtle States.
 * @author Hrvoje
 *
 */
public class Context {
	/** internal stack **/
	private ObjectStack internalStack;
	
	/**
	 * Default constructor for Context.
	 */
	public Context() {
		internalStack = new ObjectStack();
	}
	
	/**
	 * Returns current state. Throws {@link EmptyStackException} if there are no states in a context.
	 * @return current state
	 */
 	public TurtleState getCurrentState() {
		return (TurtleState) internalStack.peek();
	}
 	
 	/**
 	 * Pushes state to context. Throws {@link NullPointerException} if state is null.
 	 * @param state to push, non null
 	 */
	public void pushState(TurtleState state) {
		// na vrh gura predano stanje
		internalStack.push(state);
	}
	
	/**
	 * Removes first element on top of a Context. Throws {@link EmptyStackException} if there are no states in a context.
	 */
	public void popState() {
		// briše jedno stanje s vrha
		internalStack.pop();
	}
}
