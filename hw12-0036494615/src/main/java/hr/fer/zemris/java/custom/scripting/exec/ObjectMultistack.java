package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * ObjectMultistack allows users to store multiple values for the same key and it provides stack-like abstraction for each key.
 * Keys are Strings, and Values are valueWrappers which contain Object Value.
 * 
 * Every method has O(1) time complexity.
 * 
 * @author Hrvoje
 *
 */
public class ObjectMultistack {
	/** Internal map of stacks **/
	private HashMap<String, MultistackEntry> stackEntries;
	/** Number of stored elements **/
	private int size;
	
	/**
	 * Default constructor for ObjectMultistack
	 */
	public ObjectMultistack() {
		stackEntries = new HashMap<>();
		size = 0;
	}
	
	/**
	 * Pushes valueWrapper on Stack under String name key
	 * @param name key
	 * @param valueWrapper to push
	 * 
	 * @throws NullPointerException if one of the arguments is <code>null</code>
	 * @throws NoSuchElementException if there is no stack under key
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(valueWrapper);

		MultistackEntry next = stackEntries.get(name);
		MultistackEntry newEntry = new MultistackEntry(valueWrapper, next);
		
		stackEntries.put(name, newEntry); //stavlja na pocetak liste da dohvacanje bude O(1)
		
		++size;		
	}
	
	/**
	 * Pops one element from Stack under key name
	 * @param name key
	 * @return popped valueWrapper
	 * 
	 * @throws NullPointerException if name is <code>null</code>
	 * @throws NoSuchElementException if there is no stack under key
	 */
	public ValueWrapper pop(String name) {
		MultistackEntry resultEntry = getStackEntry(name);
		
		stackEntries.put(name, resultEntry.next);
		
		--size;
		return resultEntry.value;
	}
	
	/**
	 * Returns valueWrapper of first element on the stack under key name.
	 * @param name of stack
	 * @return first element of the stack
	 * 
	 * @throws NullPointerException if name is <code>null</code>
	 * @throws NoSuchElementException if there is no stack under key
	 */
	public ValueWrapper peek(String name) {		
		MultistackEntry resultEntry = getStackEntry(name);
		return resultEntry.value;
	}
	
	/**
	 * Returns true if stack under key name has no elements.
	 * @param name of a stack
	 * 
	 * @return true if stack under key name has no elements
	 * @throws NullPointerException if name is <code>null</code>
	 */
	public boolean isEmpty(String name) {
		Objects.requireNonNull(name);
		return stackEntries.get(name) == null;
	}
	
	/**
	 * Returns number of elements stored in MultiStack.
	 * @return number of elements stored in MultiStack
	 */
	public int numberOfElements() {
		return size;
	}
	
	/**
	 * Returns stack under key name
	 * @param name of stack
	 * @return stack under key name
	 * @throws NullPointerException if name is <code>null</code>
	 * @throws NoSuchElementException if there is no stack under key
	 */
	private MultistackEntry getStackEntry(String name) {
		Objects.requireNonNull(name);
		
		MultistackEntry resultEntry = stackEntries.get(name);
		if(resultEntry == null) throw new NoSuchElementException();
		
		return resultEntry;
	}
	
	/**
	 * Class abstacts one entry of Multistack
	 * @author Hrvoje
	 *
	 */
	public static class MultistackEntry {
		/** ValueWrapper of MultistackEntry **/
		private ValueWrapper value;
		/** Next MultistackEntry in stack **/
		private MultistackEntry next;
		
		/**
		 * Constructor for MultistackEntry
		 * @param value which entry holds
		 * @param next entry in a stack
		 * 
		 * @throws <code>null</code> if value is null
		 */
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			super();
			Objects.requireNonNull(value);
			this.value = value;
			this.next = next;
		}
		
		/**
		 * Getter for next entry.
		 * @return next entry
		 */
		public MultistackEntry getNext() {
			return next;
		}
		
		/**
		 * Setter for next entry
		 * @param next entry
		 */
		public void setNext(MultistackEntry next) {
			this.next = next;
		}
		
		/**
		 * Getter of ValueWrapper
		 * @return valueWrapper
		 */
		public ValueWrapper getValue() {
			return value;
		}
		
		
	}
}
