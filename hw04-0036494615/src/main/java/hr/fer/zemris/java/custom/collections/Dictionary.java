package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Class describes Dictionary data structure. Dictionary maps Keys to Values. Null value is permitted, but Null key isn't.
 * Every Value stored in Dictionary can be retrieved over it's Key.
 * @author Hrvoje
 *
 */
public class Dictionary {
	
	/** Array in which Entries are stored in **/
	private ArrayIndexedCollection buckets; //drzi niz arraylista
	
	/**
	 * Default contructor for Dictionary
	 */
	public Dictionary() {
		buckets = new ArrayIndexedCollection();
	}
	
	/**
	 * Checks whether Dictionary is empty;
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return buckets.isEmpty();
	}
	
	/**
	 * Returns size of Dictionary
	 * @return size of Dictionary
	 */
	public int size() {
		return buckets.size();
	}
	
	/**
	 * Clears Dictionary
	 */
	public void clear() {
		buckets.clear();
	}
	
	/**
	 * Stores Value under it's Key value. Overwrites existing Value if exists under Key.
	 * @param key
	 * @param value
	 */
	public void put(Object key, Object value) {
		Objects.requireNonNull(key);
		
		Entry newEntry = new Entry(key, value);
		int index = buckets.indexOf(newEntry);
		
		if(index == -1) {
			buckets.add(newEntry);
		}else {
			((Entry)buckets.get(index)).value = value;
		}
	}
	
	/**
	 * Retrieves Value stored under Key. Throws {@link NullPointerException} if key is null.
	 * Retrieves Null if there is no Key Value pair.
	 * 
	 * @param key of value to retrieve.
	 * @return Value
	 */
	public Object get(Object key) {
		Objects.requireNonNull(key);
		
		Entry newEntry = new Entry(key, null);

		int index = buckets.indexOf(newEntry);
		
		return (index > -1) ? ((Entry)buckets.get(index)).value : null;
	}
	
	/**
	 * Entry describes Key Value pair.
	 * @author Hrvoje
	 *
	 */
	private class Entry {
		
		/** Key, value pair **/
		Object key, value;
		
		/**
		 * Constructor for Entry.
		 * @param key key
		 * @param value value
		 */
		private Entry(Object key, Object value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Entry other = (Entry) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			return true;
		}
		
		/**
		 * Returns parent class
		 * @return parent class
		 */
		private Dictionary getOuterType() {
			return Dictionary.this;
		}
		
		
	}
}
