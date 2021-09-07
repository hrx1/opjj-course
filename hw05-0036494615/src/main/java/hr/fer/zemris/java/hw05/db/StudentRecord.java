package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Describes Student Record. 
 * Every student has unique jmbag, first and last name, and final grade
 * @author Hrvoje
 *
 */
public class StudentRecord {
	/** Fields which describe student **/
	private String  jmbag, lastName, firstName, finalGrade;

	/**
	 * Constructor for StudentRecord
	 * @param jmbag of student
	 * @param lastName of student
	 * @param firstName of student
	 * @param finalGrade of student
	 * 
	 * @throws NullPointerException if any parameter is <code>null</code>
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, String finalGrade) {
		super();
		
		Objects.requireNonNull(jmbag);
		Objects.requireNonNull(lastName);
		Objects.requireNonNull(firstName);
		Objects.requireNonNull(finalGrade);
		
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}
	
	
	/**
	 * Getter for jmbag
	 * @return jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}


	/**
	 * getter for last name
	 * @return last name
	 */
	public String getLastName() {
		return lastName;
	}


	/**
	 * getter for first name
	 * @return first name
	 */
	public String getFirstName() {
		return firstName;
	}


	/**
	 * getter for final grade
	 * @return final grade
	 */
	public String getFinalGrade() {
		return finalGrade;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentRecord other = (StudentRecord) obj;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}
	
	
	
	
}
