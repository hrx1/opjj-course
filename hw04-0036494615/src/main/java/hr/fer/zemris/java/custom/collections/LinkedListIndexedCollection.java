package hr.fer.zemris.java.custom.collections;

import java.util.Objects;
/**
 * Implementation of standard Double-linked list. Doesn't permit null, but permits duplicates.
 * 
 * Average O(n) complexities: 
 * add		- O(1) 
 * get(int)	- O(n) 
 * insert 	- O(n) 
 * indexOf 	- O(n)
 * remove 	- O(n)
 * clear	- O(1)
 * 
 * @author Hrvoje
 *
 */
public class LinkedListIndexedCollection extends Collection {
	private int size;
	private ListNode first, last;

	/**
	 * Creates empty Linked List
	 */
	public LinkedListIndexedCollection() {
		size = 0;
		first = last = null;
	}
	
	/**
	 * Creates Linked List which contains collection elements.
	 * @param collection
	 */
	public LinkedListIndexedCollection(Collection collection) {
		
		addAll(collection);
	}

	/**
	 * Returns the number of elements in a List.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Adds value to the List.
	 * Throws {@link NullPointerException} if value is null.
	 * @param value to add
	 */
	public void add(Object value) {
		Objects.requireNonNull(value);

		ListNode newNode = new ListNode();

		newNode.next = null;
		newNode.previous = last;
		newNode.value = value;

		if (size == 0) {
			first = newNode;
		} else {
			last.next = newNode;
		}

		last = newNode;

		++size;
	}
	
	/**
	 * Returns value stored on position index.
	 * Throws {@link IndexOutOfBoundsException} if index is out of bounds.
	 * @param index
	 * @return
	 */
	public Object get(int index) {
		return getNodeAt(index).value;
	}

	/**
	 * Clears list.
	 */
	public void clear() {
		first = last = null;
		size = 0;
	}

	/**
	 * Inserts value on position. All elements on positions greater or equal then position are shifted to the right.
	 * Throws {@link NullPointerException} if value is null.
	 * Throws {@link IndexOutOfBoundsException} if position is out of bounds.
	 * @param value
	 * @param position
	 */
	public void insert(Object value, int position) {
		Objects.requireNonNull(value);
		
		if (position == size) {
			add(value);
			return;
		}

		ListNode node = getNodeAt(position);
		ListNode newNode = new ListNode();
		
		newNode.value = value;

		if (position == 0) {
			first = newNode;
		} else {
			node.previous.next = newNode;
		}
		
		newNode.previous = node.previous;
		newNode.next = node;
		node.previous = newNode;

		++size;
	}
	
	/**
	 * Returns index of value in a list. Returns -1 if element is not in a list.
	 * @param value to be searched for
	 * @return >= 0 if exists, -1 otherwise
	 */
	public int indexOf (Object value) {
		if(value == null) return -1;
		
		ListNode node = first;
		for(int i = 0; node != null; ++i) {
			if(value.equals(node.value)) return i;
			node = node.next;
		}
		
		return -1;
	}
	
	
	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1; 
	}
	
	/**
	 * Removes element on position index.
	 * Throws {@link IndexOutOfBoundsException} if index is out of bounds.
	 * @param index
	 */
	public void remove (int index) {
		requireNotOutOfBounds(index);
		
		if(size == 1) {
			first = last = null;
		}
		else if(index == 0) {
			first = first.next;
		}
		else if(index == size - 1) {
			last.previous.next = null;
			last = last.previous;
		} else {
			ListNode node = getNodeAt(index);
			node.previous.next = node.next;
		}
		
		--size;
	}
	
	@Override
	public boolean remove (Object value) {
		int index = indexOf(value);
		
		if (index == -1) {
			return false;
		}else {
			remove(index);
			return true;
		}
	}
	
	/**
	 * Returns new array which contains objects contained in this collection.
	 * 
	 * @return array of List elements
	 */
	@Override
	public Object[] toArray() {
		Object[] result = new Object[size];
		
		ListNode node = first;
		for (int i = 0; i < size; ++i) {
			result[i] = node.value;
			node = node.next;
		}
		
		return result;
	}
	
	/**
	 * Iterates over every element of a list, and calls Processor.procesess 
	 */
	@Override
	public void forEach(Processor processor) {
		ListNode node = first;
		for (int i = 0; i < size; ++i) {
			processor.process(node.value);
			node = node.next;
		}
	}
	
	/**
	 * Returns node at index
	 * @param index
	 * @return Node
	 */
	private ListNode getNodeAt(int index) {
		requireNotOutOfBounds(index);

		ListNode node;
		if (index < size / 2) {
			node = first;
			for (int i = 0; i != index; ++i) {
				node = node.next;
			}
		} else {
			node = last;
			for (int i = size - 1; i != index; --i) {
				node = node.previous;
			}
		}

		return node;
	}

	/**
	 * Throws {@link IndexOutOfBoundsException} if index is out of bounds
	 * 
	 * @param index
	 */
	private void requireNotOutOfBounds(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Size is " + size + ", but wanted index is " + index);
		}
	}

	/**
	 * Describes a Node of a list.
	 * @author Hrvoje
	 *
	 */
	private static class ListNode {
		Object value;
		ListNode previous, next;
	}
}
