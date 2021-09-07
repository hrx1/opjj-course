package hr.fer.zemris.java.hw05.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
/**
 * Class describes Hashtable/Dictionary data structure. Maps every Key to it's Value.
 * This implementation provides constant-time performance for the put, get, remove, contains and all similar methods, but the constant time is not
 * guaranteed if hash function of Values is "bad" (maps multiple values to the same hash values - keys).
 * 
 * Keys cannot be <code>null</code>, but values can.
 * 
 * Class can be iterated over its Table Entries. It doesn't allow for hashtable to be modified externaly while iterating.
 * 
 * To avoid frequent collisions, this implementation changes it's inner structure if MAX_LOAD_FACTOR is reached.
 * Load factor is defined as ration of number of elements and number of empty slots in inner structure.
 * Changing inner structure is time expensive, and doubles inner table.
 * @author Hrvoje
 *
 * @param <K> Key class
 * @param <V> Value class
 */
public class SimpleHashtable <K, V> implements Iterable<SimpleHashtable.TableEntry<K,V>> {
	/** Table in which heads of lists are stored **/
	private TableEntry<K,V>[] table;
	/** Number of elements stored in Hashtable */
	private int size;
	/** Number of modifications made on hashtable **/
	private int modificationCount;
	/** Default capacity of hashtable table **/
	private static final int DEFAULT_CAPACITY = 16;
	/** Resize factor of table. Used when MAX_LOAD_FACTOR is reached **/
	private static final int RESIZE_FACTOR = 2;
	/** Maximum size/table.length ratio **/
	private static final double MAX_LOAD_FACTOR = 0.75;
	
	/**
	 * Default constructor for SimpleHashtable. Sets capacity to DEFAULT_CAPACITY
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructor for SimpleHashtable with <code>capacity</code>.
	 * Capacity will be set to first greater or equal 2^n number.
	 * @param size number of slots.
	 * @throws IllegalArgumentException if <code>capacity</code> is 0 or less or is out of range of int.
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if(capacity <= 0) throw new IllegalArgumentException("Capacity of hashtable can't be zero or negative. Given: " + capacity);
		
		table = (TableEntry<K,V>[]) new TableEntry[nextPowerOfTwo(capacity)];		
		this.size = 0;
		this.modificationCount = 0;
	}
	
	/**
	 * Puts Value under Key in a map. If Key already exists, then value will be overwritten.
	 * @param key of a value
	 * @param value to store
	 * @throws NullPointerException if <code>Key</code> is <code>null</code>.
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key, "Key cannot be null.");
		
		int slotIndex = calculateSlotIndex(key);		
		
		if(table[slotIndex] == null) {
			table[slotIndex] = new TableEntry<>(key, value, null);
			
			++size;
			++modificationCount;
			if(size >= MAX_LOAD_FACTOR * table.length) {
				resizeHashtable();
			}

			return ;
		}
		
		TableEntry<K, V> currentEntry = table[slotIndex];
		while(true) {
			if(currentEntry.key.equals(key)) {
				currentEntry.value = value;
				return ;
			}
			if(currentEntry.next == null) {
				currentEntry.next = new TableEntry<>(key, value, null);
				
				++size;
				++modificationCount;
				if(size >= MAX_LOAD_FACTOR * table.length) {
					resizeHashtable();
				}
				
				return ;
			}
			currentEntry = currentEntry.next;
		}
	}


	/**
	 * Returns Value stored in Hashtable under Key. 
	 * If key doesn't exist (null included) then <code>null</code> is returned.
	 * @param key under which value is stored
	 * @return value if exists, <code>null</code> otherwise
	 */
	public V get(Object key) {
		if(!isValidKey(key)) return null;
		
		@SuppressWarnings("unchecked")
		TableEntry<K, V> entry = getEntry((K)key);
		
		return (entry == null) ? null : entry.value;
	}
	
	/**
	 * Removes Key-Value pair in hashtable if pair exists.
	 * @param key to be deleted
	 */
	public void remove(Object key) {
		if(!isValidKey(key)) return;
		
		int slotIndex = calculateSlotIndex(key);
		
		if(table[slotIndex] == null) {
			return;
		}
		
		if(table[slotIndex].key.equals(key)) {
			table[slotIndex] = table[slotIndex].next;
			--size;
			++modificationCount;
			return;
		}
		
		TableEntry<K, V> currentEntry = table[slotIndex];
		while(true) {
			if(currentEntry.next == null) return;
			
			if(currentEntry.next.key.equals(key)) {
				currentEntry.next = currentEntry.next.next;
				--size;
				++modificationCount;
				return ;
			}
		}
		
	}
	
	/**
	 * Returns <code>true</code> if hashtable contains key.
	 * @param key to be searched for
	 * @return true if table contains key
	 */
	@SuppressWarnings("unchecked")
	public boolean containsKey(Object key) {
		if(!isValidKey(key)) return false;

		return getEntry((K) key) != null;
	}
	
	/**
	 * Returns <code>true</code> if hashtable contains value.
	 * @param value to be searched for
	 * @return true if table contains value
	 */
	public boolean containsValue(Object value) {
		
		for(TableEntry<K, V> entry : this) {
			if(Objects.deepEquals(entry.getValue(), value)) {
				return true;
			}
		}
			
		return false;
	}
	
	/**
	 * Returns <code>true</code> if hashtable is empty.
	 * @return <code>true</code> if hashtable is empty.
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Clears all entries in hashtable.
	 */
	public void clear() {
		for(int i = 0; i < table.length; ++i) {
			table[i] = null;
		}
		size = 0;
		++modificationCount;
	}

	/**
	 * Returns number of elements in hashtable.
	 * @return number of elements in hashtable.
	 */
	public int size() {
		return size;
	}
	
	@Override
	/**
	 * Returns iterator.
	 * @return iterator
	 */
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * Returns String representation of SimpleHashtable. Contains all elements.
	 * @return String representation of SimpleHashtable
	 */
	@Override
	public String toString() {
		//Magic numbers, ali bezopasno i nepotrebno nigdje dalje. 
		StringBuilder result = new StringBuilder(15 * size + 3); 
		result.append("[");
		
		for(TableEntry<K, V> entry : this) {
			result.append(entry.asText());
			result.append(", ");
		}
		
		if(size > 0) {
			result.replace(result.length() - 2, result.length(), "]"); //umjesto zadnjeg zareza i razmaka
		}
		else {
			result.append("]");
		}
		
		return result.toString();
	}
	
	/**
	 * Resizes inner hashtable with factor RESIZE_FACTOR
	 */
	@SuppressWarnings("unchecked")
	private void resizeHashtable() {
		TableEntry<K, V> [] oldTable = table;
		table = (TableEntry<K, V> []) new TableEntry[oldTable.length * RESIZE_FACTOR];
		
		size = 0; //uzrok nimalo zanimljivog buga
		
		for(TableEntry<K, V> entry : oldTable) {
			while(entry != null) {
				put(entry.key, entry.value);
				entry = entry.next;
			}
		}
	}
	
	/**
	 * Returns <code>true</code> if Key is from the same class as Keys in hashtable and isn't <code>null</code>.
	 * @param key to be tested.
	 * @return <code>true</code> if Key is from the same class as Keys in hashtable and isn't <code>null</code>.
	 */
	private boolean isValidKey(Object key) {
		if (key == null) return false;
		
		try {
			@SuppressWarnings({ "unchecked", "unused" })
			K test = (K) key;
		}catch(ClassCastException c) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns TableEntry stored under key.
	 * @param key of TableEntry
	 * @return TableEntry stored under key.
	 */
	private TableEntry<K, V> getEntry(K key) {
		return getEntry(key,  calculateSlotIndex((K)key));
	}
	
	/**
	 * Returns TableEntry stored under key in slot stored on slotIndex.
	 * @param key of TableEntry
	 * @param slotIndex of TableEntry
	 * @return TableEntry stored under key in slot stored on slotIndex..
	 */
	private TableEntry<K, V> getEntry(K key, int slotIndex) {
		TableEntry<K, V> currentEntry = table[slotIndex];
		
		while(currentEntry != null) {
			if(currentEntry.key.equals(key)) return currentEntry;
			currentEntry = currentEntry.next;
		}
		
		return null;
	}
	
	/**
	 * Returns next positive power of two bigger or equal to number. Returns 1 if <code>number</code> is negative or zero.
	 * Throws {@link IllegalArgumentException} if result is greater then range of int.
	 * @param number 
	 * @return next positive power of two bigger or equal to number. Returns 1 if <code>number</code> is negative or zero
	 * @throws IllegalArgumentException if result is greater then range of int
	 */
	private int nextPowerOfTwo (int number) {		
		int power = 1;
		
		while(power < number) {
			power = power << 1;
			if(power < 0) 
				throw new IllegalArgumentException("Size out of permitted range. Size: " + number);
		}
		
		return power;
	}
	
	/**
	 * Calculates slot index of a key. Uses Object.hashCode() method.
	 * 
	 * @param key which index will be calculated
	 * @return slot index of a key.
	 */
	@SuppressWarnings("unchecked")
	private int calculateSlotIndex(Object key) {
		return Math.floorMod(((K)key).hashCode(), table.length);
	}
	
	
	/**
	 * Class describes one entry in a Hashtable. Entry is a Key-Value pair.
	 * Key cannot be <code>null</code>, but Value can.
	 * @author Hrvoje
	 *
	 * @param <K> key of a pair
	 * @param <V> value of a pair
	 */
	public static class TableEntry<K,V> {
		/** Key of a pair **/
		private K key;
		/** Value of a pair **/
		private V value;
		/** Next table entry. Used in table of Hashtable **/
		private TableEntry<K, V> next;
		
		/**
		 * Constructor for Table Entry.
		 * @param key of a entry
		 * @param value of a entry
		 * @param next entry
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
		
		/**
		 * Getter for value
		 * @return value
		 */
		public V getValue() {
			return value;
		}
		
		/**
		 * Setter for value
		 * @param value to be set
		 */
		public void setValue(V value) {
			this.value = value;
		}
		
		/**
		 * Getter of a key
		 * @return key
		 */
		public K getKey() {
			return key;
		}
		
		/**
		 * Returns string representation of a key entry.
		 * @return string representation of a key entry
		 */
		private String asText() {
			String valueS = (value == null)?"null":value.toString();
			return key.toString() + "=" + valueS;
		}
	}
	
	/**
	 * Iterator implemenation for Hashtable. It provides iteration over TableEntries.
	 * @author Hrvoje
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
		/** Index of current slot **/
		private int index;
		/** Pointers to current and next Table Entry **/
		TableEntry<K, V> current, next;
		/** Used for checking if any external modifications on Hashtable were made **/
		private int iteratorModificationCount;
		
		/**
		 * Default constructor for IteratorImpl.
		 */
		public IteratorImpl() {
			index = 0;
			skipEmptySlots();
			iteratorModificationCount = modificationCount;
		}
		
		/*
		 * Na realizaciju sljedecih metoda utjecala je meni nejasna recenica:
		 * 
		 *  Posljedica: nije zadaća metode hasNext() da ona računa
		 * koji je sljedeći element – pripazite na to pri pisanju koda. 
		 * 
		 * Tako da u hasNext ne pravim nikakve slozenije operacije (tipa pozicioniranje na sljedeci element) 
		 * iako mislim da bi ta implementacija bila bolja/ljepsa
		 */
		
		/* 
		 * Smatram da sljedece Overrideane metode imaju zadovoljavajucu dokumentaciju
		 * 
		 */
		
		@Override
		public boolean hasNext() {
			checkConcurrentModification();
			return index < table.length && next != null;
		}

		@Override
		public TableEntry<K, V> next() {
			checkConcurrentModification();
			current = next;
			
			if(current == null || index >= table.length) {
				throw new NoSuchElementException("Iterator has no more elements.");
			}
			
			next = next.next;
			if(next == null) {
				++index;
				skipEmptySlots();
			}
			
			return current;
		}

		@Override
		public void remove() {
			checkConcurrentModification();

			if(!SimpleHashtable.this.containsKey(current.getKey()))
				throw new IllegalStateException(".remove() can only be called once in single iteration");
			
			SimpleHashtable.this.remove(current.key);
			++iteratorModificationCount;
		}
		
		/**
		 * Sets <code>index</code> to first index of non null slot in table.
		 * If there is no more non null slots, then index = table.length.
		 */
		private void skipEmptySlots() {
			while(index < table.length && table[index] == null) {
				++index;
			}
			
		 	next = (index < table.length) ? table[index] : null;
		}
		
		/**
		 * Throws {@link ConcurrentModificationException} if two or more operations are performed in the same time.
		 */
	 	private void checkConcurrentModification() {
	 		if (iteratorModificationCount != modificationCount) 
	 			throw new ConcurrentModificationException("Cannot change hashtable while iterator is running");
	 	}

	}

}
