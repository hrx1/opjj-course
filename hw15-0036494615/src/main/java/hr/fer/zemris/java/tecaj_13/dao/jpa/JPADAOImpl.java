package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogForm;
//import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.web.servlets.models.CommentForm;
import hr.fer.zemris.java.tecaj_13.web.servlets.models.RegisterForm;

/**
 * DAO implementation which uses JPA
 * 
 * @author Hrvoje
 *
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}
	
	@Override
	public boolean nickExists(String nick) throws DAOException {
		return getUserByNick(nick) != null;
	}
	
	@Override
	public BlogUser getUserByNick(String nick) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		List<BlogUser> tmp = em.createNamedQuery("BlogUser.userByNick", BlogUser.class)
				.setParameter("nick", nick)
				.getResultList();
		
		JPAEMProvider.close();
		return (tmp.isEmpty()) ? null: tmp.get(0);
	}

	@Override
	public void registerUser(RegisterForm form) throws DAOException {		
		BlogUser newUser = RegisterForm.getBlogUser(form);
		registerUser(newUser);
	}

	@Override
	public void registerUser(BlogUser user) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(user);
		JPAEMProvider.close();
	}
	
	@Override
	public List<BlogUser> getAllUsers() {
		EntityManager em = JPAEMProvider.getEntityManager();
		List<BlogUser> result = em.createNamedQuery("BlogUser.getAll", BlogUser.class).getResultList();
		JPAEMProvider.close();
		return result;
	}

	@Override
	public void addBlog(BlogEntry blog) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(blog);
		blog.getCreator().addCreatedBlog(blog);
		JPAEMProvider.close();
	}

	@Override
	public String getUserEmail(String nick) throws DAOException {
		BlogUser b = getUserByNick(nick);
		if(b == null) return null;
		return b.getEmail();
	}

	@Override
	public void addComment(BlogEntry blog, CommentForm comment) {
		BlogComment bc = new BlogComment();
		bc.setBlogEntry(blog);
		bc.setMessage(comment.getValue());
		bc.setUsersEMail(comment.getEmail());
		bc.setPostedOn(new Date());
		
		blog.getComments().add(bc);
		
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(bc);
		JPAEMProvider.close();
	}

	@Override
	public void changeBlog(BlogEntry blog, BlogForm form) {
		JPAEMProvider.getEntityManager();
		blog.setText(form.getText());
		blog.setTitle(form.getTitle());
		blog.setLastModifiedAt(new Date());
		JPAEMProvider.close();
	}
	
}