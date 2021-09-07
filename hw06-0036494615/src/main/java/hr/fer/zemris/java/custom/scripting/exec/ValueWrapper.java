package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Class wraps Object Values and provides simple arithmetic with them. 
 * Arithmetic supports these operations: addition, subtraction, multiplication and division.
 * Class also provides numerical comparasion.
 * 
 * All operations follow the same directive:
 * If values are strings, they are converted to Integer or Double.
 *  If that's not possible, {@link UnsupportedOperationException} is thrown.
 * If binary operator is called and both members are integers, then Class of the result will be Integer.
 * If binary operator is called and one or both members are Double, then Class of the result will be Double.
 * 
 * @author Hrvoje
 *
 */
public class ValueWrapper {
	/** Object which wrapper holds **/
	Object value;
	
	/**
	 * Constructor for valueWrapper
	 * @param value to set
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Getter for value
	 * @return value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Setter for value
	 * @param value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Adds incValue to current Wrapper
	 * @param incValue to add
	 * @throws UnsupportedOperationException if operation is not supported
	 */
	public void add(Object incValue) {
		Object member1 = parseMember(this.value);
		Object member2 = parseMember(incValue);
		
		if(member1.getClass().equals(Double.class) || member2.getClass().equals(Double.class)) { //TODO obavezno bolje!!! PITAJ ANTU
			this.value = Double.valueOf(member1.toString()) + Double.valueOf(member2.toString());
		}
		else {
			this.value = Integer.valueOf(member1.toString()) + Integer.valueOf(member2.toString());

		}
		
	}
	
	/**
	 * Subtracts incValue to current Wrapper
	 * @param incValue to subtract
	 */
	public void subtract(Object decValue) {
		Object member1 = parseMember(this.value);
		Object member2 = parseMember(decValue);
		
		if(member1.getClass().equals(Double.class) || member2.getClass().equals(Double.class)) { //TODO obavezno bolje!!! PITAJ ANTU
			this.value = Double.valueOf(member1.toString()) - Double.valueOf(member2.toString());
		}
		else {
			this.value = Integer.valueOf(member1.toString()) - Integer.valueOf(member2.toString());

		}
	}
	
	/**
	 * Multiplies current value by mulValue
	 * @param mulValue to multiply with
	 * @throws UnsupportedOperationException if operation is not supported
	 */
	public void multiply(Object mulValue) {
		Object member1 = parseMember(this.value);
		Object member2 = parseMember(mulValue);
		
		if(member1.getClass().equals(Double.class) || member2.getClass().equals(Double.class)) { //TODO obavezno bolje!!! PITAJ ANTU
			this.value = Double.valueOf(member1.toString()) * Double.valueOf(member2.toString());
		}
		else {
			this.value = Integer.valueOf(member1.toString()) * Integer.valueOf(member2.toString());

		}
	}
	
	/**
	 * Divides current value by divValue
	 * @param divValue to divide with
	 * @throws ArithmeticException if divValue is 0
	 * @throws UnsupportedOperationException if operation is not supported
	 * 
	 */
	public void divide(Object divValue) {
		Object member1 = parseMember(this.value);
		Object member2 = parseMember(divValue);
				
		if(member1.getClass().equals(Double.class) || member2.getClass().equals(Double.class)) { //TODO obavezno bolje!!! PITAJ ANTU
			this.value = Double.valueOf(member1.toString()) / Double.valueOf(member2.toString());
		}
		else {
			this.value = (Integer) member1 / (Integer)member2;

		}
	}
	
	/**
	 * Compares with another value withValue
	 * @param withValue to compare with
	 * @return -1 if this value is less than withValue, 0 if they are equal, 1 otherwise
	 * @throws UnsupportedOperationException if operation is not supported
	 */
	public int numCompare(Object withValue) {
		Object member1 = parseMember(this.value);
		Object member2 = parseMember(withValue);
		
		if(member1.getClass().equals(Double.class) || member2.getClass().equals(Double.class)) { //TODO obavezno bolje!!! PITAJ ANTU
			return Double.valueOf(member1.toString()).compareTo(Double.valueOf(member2.toString()));
		}
		else {
			return Integer.valueOf(member1.toString()).compareTo(Integer.valueOf(member2.toString()));

		}

	}
	
		
	/**
	 * Converts member to number object
	 * @param member to convert
	 * @return Object which is castable to Integer or Double
	 * @throws NumberFormatException  if String cannot be converted to integer or double
	 * @Throws UnsupportedOperationException if operation is not supported over class
	 */
	public Object parseMember(Object member) {
		if(member == null) return Integer.valueOf(0);
				
		if(member instanceof String) {
			try {
				return Integer.parseInt((String) member);
			}catch(NumberFormatException e) {
				try {
					return Double.parseDouble((String) member);
				}catch(NumberFormatException ep) {}
			}
		}
		
		if(member instanceof Integer || member instanceof Double) {
			return member;
		}
		
		throw new UnsupportedOperationException("Cannot perform arithmetic operation over class: " + member.getClass());
			
	}
	
}
