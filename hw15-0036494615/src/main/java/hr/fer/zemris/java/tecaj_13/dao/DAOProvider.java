package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.dao.jpa.JPADAOImpl;

/**
 * Returns used DAO
 * 
 * @author Hrvoje
 *
 */
public class DAOProvider {

	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Getter for DAO
	 * @return
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}