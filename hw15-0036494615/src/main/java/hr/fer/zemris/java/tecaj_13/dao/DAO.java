package hr.fer.zemris.java.tecaj_13.dao;

import java.util.List;

import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogForm;
//import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.web.servlets.models.CommentForm;
import hr.fer.zemris.java.tecaj_13.web.servlets.models.RegisterForm;

/**
 * DAO interface which abstracts database communication.
 * 
 * @author Hrvoje
 *
 */
public interface DAO {

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em. Ako takav entry ne postoji,
	 * vraća <code>null</code>.
	 * 
	 * @param id ključ zapisa
	 * @return entry ili <code>null</code> ako entry ne postoji
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;

	/**
	 * Returns true if nick exists.
	 * 
	 * @param nick to check
	 * @return true if nick exists
	 * @throws DAOException
	 */
	public boolean nickExists(String nick) throws DAOException;

	/**
	 * Returns BlogUser with given nick. Null if such user doesn't exist
	 * 
	 * @param nick Nick
	 * @return BlogUser with given nick or Null if such user doesn't exist
	 * @throws DAOException
	 */
	public BlogUser getUserByNick(String nick) throws DAOException;
	
//	public List<BlogUser> getUsers();
	
	/**
	 * Registers user with information given in form
	 * 
	 * @param form Informations about user
	 * @throws DAOException
	 */
	public void registerUser(RegisterForm form) throws DAOException;
	
	/**
	 * Registers user
	 * 
	 * @param user to register
	 * @throws DAOException
	 */
	public void registerUser(BlogUser user) throws DAOException;

	/**
	 * Returns list of all users
	 * 
	 * @return list of all users
	 * @throws DAOException
	 */
	public List<BlogUser> getAllUsers() throws DAOException;
	
	/**
	 * Adds blog
	 * 
	 * @param blog to add
 	 * @throws DAOException
	 */
	public void addBlog(BlogEntry blog) throws DAOException;
	
	
	/**
	 * Returns email of user with nick, null if no such user exists
	 * 
	 * @param nick of user
	 * @return email of user with nick, null if no such user exists
	 * @throws DAOException
	 */
	public String getUserEmail(String nick) throws DAOException;

	/**
	 * Adds comment with informations from CommentForm to BlogEntry blog
	 * 
	 * @param blog in which comment is added
	 * @param comment to add
	 */
	public void addComment(BlogEntry blog, CommentForm comment);

	/**
	 * Updates blog values from values in form
	 * @param blog to change
	 * @param form with new values
	 */
	public void changeBlog(BlogEntry blog, BlogForm form);
}