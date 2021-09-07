package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.Objects;

import static java.lang.Math.max;

/**
 * Array with dynamically allocated capacity. Capacity doubles every time when
 * you try to add/insert element into a full Array. Doesn't permit null value,
 * but permits duplicates.
 * 
 * Average O(n) complexities: 
 * add		- O(1) 
 * get 		- O(1) 
 * insert 	- O(n) 
 * indexOf 	- O(n)
 * remove 	- O(n)
 * 
 * @author Hrvoje
 *
 */
public class ArrayIndexedCollection extends Collection {

	private static final int DEFAULT_CAPACITY = 16;
	private int size = 0;
	private int capacity;
	private Object[] elements;

	/**
	 * Creates Array with DEFAULT_CAPACITY
	 */
	public ArrayIndexedCollection() {
		callocArray(DEFAULT_CAPACITY);
	}

	/**
	 * Creates Array which contains all collection elements. Capacity of the Array
	 * is max(collection.size(), initialCapacity).
	 * 
	 * Throws {@link NullPointerException} if collection is null.
	 * 
	 * @param collection
	 * @param initialCapacity
	 */
	public ArrayIndexedCollection(Collection collection, int initialCapacity) {
		Objects.requireNonNull(collection);
		if (initialCapacity < 1)
			throw new IllegalArgumentException("Capacity must be positive. Wanted: " + initialCapacity);

		callocArray(max(collection.size(), initialCapacity));

		this.addAll(collection);
	}

	/**
	 * Creates Array with initialCapacity.
	 * 
	 * @param initialCapacity
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		callocArray(initialCapacity);
	}

	/**
	 * Creates Array which contains all collection elements.
	 * 
	 * Throws {@link NullPointerException} if collection is null.
	 * 
	 * @param collection
	 */
	public ArrayIndexedCollection(Collection collection) {
		this(collection, 1);
		/*
		 * 1 je predan jer je moguce da je nad Kolekcijom collection prethodno pozvana
		 * clear() metoda koja postavlja size na 0
		 */
	}

	/**
	 * Returns the number of elements in this collection.
	 * 
	 * @return number of elements.
	 */
	public int size() {
		return size;
	}

	/**
	 * Adds value to the collection. Throws {@link NullPointerException} if value is null, it won't add null.
	 * 
	 * @param value to add
	 */
	public void add(Object value) {
		Objects.requireNonNull(value);

		if (size == capacity) {
			elements = Arrays.copyOf(elements, capacity * 2);
			capacity *= 2;
		}

		elements[size] = value;
		++size;
	}

	/**
	 * Removes first instance with value value from collection, returns true if
	 * successful. Value is determined with equals method.
	 * 
	 * @param value to be removed.
	 * @return true if element is removed, false otherwise.
	 */
	public boolean remove(Object value) {
		int valueIndex = indexOf(value); // vrati -1 ako nije u nizu

		if (valueIndex == -1)
			return false;

		remove(valueIndex);

		return true;
	}

	/**
	 * Removes element stored on position index. Throws
	 * {@link IndexOutOfBoundsException} if index is out of bounds.
	 * 
	 * @param index
	 *            position of element to remove.
	 */
	public void remove(int index) {
		requireNotOutOfBounds(index);

		--size;

		for (int i = index; i < size; ++i) { // shiftaj svaki daljnji element za jedan u lijevo
			elements[i] = elements[i + 1];
		}

		elements[size] = null; // ovaj element je pomaknut u lijevo pa nam vise ne treba na ovom indexu.
	}

	/*
	 * Clears Array, but Array keeps the same capacity as before.
	 */
	public void clear() {

		for (int i = 0; i < size; ++i) {
			elements[i] = null;
		}

		size = 0;
	}

	/**
	 * Returns true only if this collection contains given value, as determined by
	 * equals method.
	 * 
	 * @param value to search for.
	 * @return true if contains, false otherwise.
	 */
	public boolean contains(Object value) {
		if (value == null)
			return false;

		for (int i = 0; i < size; ++i) {
			if (elements[i].equals(value))
				return true;
		}

		return false;
	}

	/**
	 * Returns index of value in Array. Returns -1 if Array doesn't contain value.
	 * 
	 * @param value
	 * @return
	 */
	public int indexOf(Object value) {
		if (value == null)
			return -1;

		for (int i = 0; i < size; ++i) {
			if (elements[i].equals(value))
				return i;
		}

		return -1;
	}

	/**
	 * Returns element on position index in Array. Throws
	 * {@link IndexOutOfBoundsException} if index is out of bounds.
	 * 
	 * @param index
	 * @return element
	 */
	public Object get(int index) {
		requireNotOutOfBounds(index);

		return elements[index];
	}

	/**
	 * Inserts value on position. Throws {@link IndexOutOfBoundsException} if
	 * position is out of bounds.
	 * 
	 * @param value
	 * @param position
	 */
	public void insert(Object value, int position) {

		if (position == size) { // ako zelimo ubaciti na mjesto na kojem bi dosao sljedeci element, zapravo
								// radimo add();
			add(value);
			return;
		}

		requireNotOutOfBounds(position);

		if (size == capacity) {
			elements = Arrays.copyOf(elements, capacity * 2);
			capacity *= 2;
		}

		for (int i = position; i < size; ++i) { // pomicem svaki element od (ukljucujuci) pozicije position u desno
			elements[i + 1] = elements[i];
		}

		elements[position] = value;
		++size;
	}

	/**
	 * Returns new array which contains objects contained in this collection.
	 * 
	 * @return
	 */
	@Override
	public Object[] toArray() {
		Object[] result = new Object[size];

		for (int i = 0; i < size; ++i) {
			result[i] = elements[i];
		}

		return result;
	}

	/**
	 * Method calls processor.process() for each element of this collection. The
	 * order in which elements will be sent is undefined in this class.
	 * 
	 * @param processor
	 */
	public void forEach(Processor processor) {
		for (int i = 0; i < size; ++i) {
			processor.process(elements[i]);
		}
	}

	@Override
	public String toString() {
		String s = "[";
		for (int i = 0; i < size - 1; ++i) {
			s += elements[i].toString() + ", ";
		}
		s += elements[size - 1] + "]";
		return s;
	}

	/**
	 * Allocates array with size capacity for internal elements Array. All elements
	 * are referencing to null value. Sets size to 0.
	 * 
	 * @param capacity
	 */
	private void callocArray(int capacity) {
		elements = new Object[capacity];
		this.capacity = capacity;
		size = 0;
	}

	/**
	 * Throws {@link IndexOutOfBoundsException} if index is less then 0 or greater
	 * then size.
	 * 
	 * @param index to test
	 */
	private void requireNotOutOfBounds(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Size is " + size + ", but wanted index is " + index);
		}
	}
}
