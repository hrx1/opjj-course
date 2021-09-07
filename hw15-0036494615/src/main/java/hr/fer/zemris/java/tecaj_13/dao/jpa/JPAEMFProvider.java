package hr.fer.zemris.java.tecaj_13.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Factory for JPAEProvider
 * @author Hrvoje
 *
 */
public class JPAEMFProvider {

	/** EMF */
	public static EntityManagerFactory emf;
	
	/** Getter for EMF */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * Sets factory
	 * @param emf factory
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}