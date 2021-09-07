package hr.fer.zemris.java.custom.collections;

/**
 * Abstract collection of elements and defines basic operations with collection.
 * @author Hrvoje
 *
 */
public class Collection {
	
	/**
	 * Constructor for collection
	 */
	protected Collection() {
		
	}
	
	/**
	 * Return true if this collection is empty.
	 * @return
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
	 * Returns the number of elements in this collection.
	 * @return
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Adds value to the collection.
	 * @param value
	 */
	public void add(Object value) {
		
	}
	
	/**
	 * Returns true only if this collection contains given value, as determined by equals method.
	 * @param value
	 * @return
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	
	/**
	 * Removes first instance with value value from collection, returns true if successful. 
	 * Value is determined with equals method.
	 * @param value
	 * @return
	 */
	public boolean remove (Object value) {
		return false;
	}
	
	/**
	 * Returns new array which contains objects contained in this collection.
	 * @return
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method calls processor.process() for each element of this collection.
	 * The order in which elements will be sent is undefined in this class.
	 * @param processor
	 */
	public void forEach(Processor processor) {
		
	}
	
	/**
	 * Adds all elements from other to this
	 * @param other
	 */
	public void addAll(Collection other) {
		class Adder extends Processor {
			@Override
			public void process(Object value) {
				add(value);
			}
		}
		
		other.forEach(new Adder()); 
	}
	
	/**
	 * Clears collection.
	 */
	public void clear() {
		
	}
}
