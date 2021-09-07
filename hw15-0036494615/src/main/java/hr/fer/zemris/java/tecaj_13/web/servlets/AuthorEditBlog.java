package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogForm;
import hr.fer.zemris.java.tecaj_13.web.servlets.util.SessionMethods;

/**
 * Servlet for editing blogs
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/author-edit-blog")
public class AuthorEditBlog extends HttpServlet {

	private static final long serialVersionUID = 5965674420552891716L;
	
	/** Key under which blog form is stored */
	public static final String blogFormKey = "blogForm";
	/** Key under which blog id is stored */ 
	public static final String blogIDKey = "blogID";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}
	
	/**
	 * Processes Request
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String nick = (String) req.getAttribute(AuthorRouter.nickKey);
		
		if(!SessionMethods.isLoggedInAs(req.getSession(), nick)) {
			errorRedirect(req, resp);
			return ;
		}
		
		long blogID;
		
		try {
			blogID = Long.parseLong((String) req.getParameter(blogIDKey));
		} catch(NumberFormatException e) {
			errorRedirect(req, resp);
			return ;
		}
		
		BlogEntry blog = DAOProvider.getDAO().getBlogEntry(blogID);
		
		if(blog == null || !blog.getCreator().getNick().equals(nick)) {
			errorRedirect(req, resp);
			return ;
		}
		
		//dohvati formu iz parametara, ako je validna spremi ju
		//ako nije, stavi ju u blogForm i ispisi na jsp
		
		BlogForm form = new BlogForm();
		form.fillFromRequest(req);
		form.validate();
		
		if(!form.isValid()) {
			form.fillFromBlogEntry(blog);
			req.setAttribute(blogFormKey, form);
			req.getRequestDispatcher("/WEB-INF/pages/editBlog.jsp").forward(req, resp);
			return ;
		}
		
		DAO dao = DAOProvider.getDAO();
		dao.changeBlog(blog, form);
		
		resp.sendRedirect(req.getContextPath() + "/servleti/author/" + nick + "/" + blog.getId() + "?success=true");
		
	}

	/**
	 * Error redirect
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	private void errorRedirect(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
	}

	
}
