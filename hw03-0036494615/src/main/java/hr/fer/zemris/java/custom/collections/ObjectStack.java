package hr.fer.zemris.java.custom.collections;

/**
 *  Implementation of standard stack. Doesn't permit null, but permits duplicates.
 * 
 * @author Hrvoje
 *
 */
public class ObjectStack {
	private ArrayIndexedCollection elements;
	
	/**
	 * Creates empty stack
	 */
	public ObjectStack() {
		elements = new ArrayIndexedCollection();
	}
	
	/**
	 * Checks whether stack is empty. 
	 * @return True if stack is empty
	 */
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	/**
	 * Returns number of elements in a stack.
	 * @return number of elements in a stack.
	 */
	public int size() {
		return elements.size();
	}
	
	/**
	 * Pushes value on stack.
	 * @param value to be pushed.
	 */
	public void push(Object value) {
		elements.add(value);
	}
	
	/**
	 * Pop from top of the stack.
	 * @return popped element.
	 */
	public Object pop() {
		if(isEmpty()) throw new EmptyStackException();
		
		Object toReturn = elements.get(elements.size() -1);
		elements.remove(elements.size() -1);
		
		return toReturn;
	}
	
	/**
	 * Returns reference to the first element on the stack.
	 * @return reference to the first element on the stack.
	 */
	public Object peek() {
		if (isEmpty()) throw new EmptyStackException();
		
		return elements.get(elements.size() -1);
	}
	
	/**
	 * Clears stack.
	 */
	public void clear() {
		elements.clear();
	}
}
