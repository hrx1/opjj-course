package hr.fer.zemris.java.custom.scripting.demo4;

import java.util.Objects;

/**
 * StudentRecord holds information about one Student, and his achieved results from exams and laboratory exercises.
 * Every information is required, non should be <code>null</code>.
 * @author Hrvoje
 *
 */
public class StudentRecord {
	/** Properties of StudentRecord **/
	private String jmbag, prezime, ime;
	private double bodoviMI, bodoviZI, bodoviLabos;
	private int ocjena;

	/**
	 * Constructor for StudentRecord
	 * @param jmbag of a student
	 * @param prezime of a student
	 * @param ime of a student
	 * @param bodoviMI of a student
	 * @param bodoviZI of a student
	 * @param bodoviLabos of a student
	 * @param ocjena of a student
	 * 
	 * @throws NullPointerException if any argument is <code>null</code>
	 * @throws NumberFormatException if Number results are in wrong format.
	 */
	public StudentRecord(String jmbag, String prezime, String ime, String bodoviMI, String bodoviZI, String bodoviLabos,
			String ocjena) {
		this(jmbag, prezime, ime, Double.valueOf(bodoviMI), Double.valueOf(bodoviZI), Double.valueOf(bodoviLabos), Integer.valueOf(ocjena));
	}

	/**
	 * Constructor for StudentRecord
	 * @param jmbag of a student
	 * @param prezime of a student
	 * @param ime of a student
	 * @param bodoviMI of a student
	 * @param bodoviZI of a student
	 * @param bodoviLabos of a student
	 * @param ocjena of a student
	 * 
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	public StudentRecord(String jmbag, String prezime, String ime, double bodoviMI, double bodoviZI, double bodoviLabos,
			int ocjena) {
		super();
		
		argumentsNonNull(jmbag, prezime, ime, bodoviMI, bodoviZI, bodoviLabos, ocjena);
		
		this.jmbag = jmbag;
		this.prezime = prezime;
		this.ime = ime;
		this.bodoviMI = bodoviMI;
		this.bodoviZI = bodoviZI;
		this.bodoviLabos = bodoviLabos;
		this.ocjena = ocjena;
	}
	
	/**
	 * Getter for jmbag
	 * @return jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}
	/**
	 * Setter for jmbag
	 * @param jmbag to set
	 */
	public void setJmbag(String jmbag) {
		this.jmbag = jmbag;
	}

	/**
	 * Getter for prezime
	 * @return prezime
	 */
	public String getPrezime() {
		return prezime;
	}

	/**
	 * Setter for prezime
	 * @param prezime
	 */
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	
	/**
	 * Getter for ime
	 * @return ime
	 */
	public String getIme() {
		return ime;
	}

	/**
	 * Setter for ime
	 * @param ime 
	 */
	public void setIme(String ime) {
		this.ime = ime;
	}

	/**
	 * Getter for bodoviMI
	 * @return bodoviMI
	 */
	public Double getBodoviMI() {
		return bodoviMI;
	}

	/**
	 * Setter for bodoviMI
	 * @param bodoviMI
	 */
	public void setBodoviMI(Double bodoviMI) {
		this.bodoviMI = bodoviMI;
	}

	/**
	 * Getter for bodoviZI
	 * @return bodoviZI
	 */
	public Double getBodoviZI() {
		return bodoviZI;
	}

	/**
	 * Setter for bodoviZI
	 * @param bodoviZI
	 */
	public void setBodoviZI(Double bodoviZI) {
		this.bodoviZI = bodoviZI;
	}

	/**
	 * Getter for bodoviLabos
	 * @return bodoviLabos
	 */
	public Double getBodoviLabos() {
		return bodoviLabos;
	}

	/**
	 * Setter for bodoviLabos
	 * @param bodoviLabos to set
	 */
	public void setBodoviLabos(Double bodoviLabos) {
		this.bodoviLabos = bodoviLabos;
	}

	/**
	 * Getter for ocjena
	 * @return ocjena
	 */
	public Integer getOcjena() {
		return ocjena;
	}

	/**
	 * Setter for ocjena
	 * @param ocjena to set
	 */
	public void setOcjena(Integer ocjena) {
		this.ocjena = ocjena;
	}
	
	@Override
	public String toString() {
		return jmbag + "\t" + prezime + "\t" + ime + "\t" + bodoviMI + "\t" + bodoviZI + "\t" + bodoviLabos + "\t" + ocjena;
	}
	
	/**
	 * Throws {@link NullPointerException} if any argument from arguments is <code>null</code>
	 * @param arguments to check
	 */
	private void argumentsNonNull(Object ... arguments) {
		for(Object o : arguments) {
			Objects.requireNonNull(o);
		}
	}
	
}