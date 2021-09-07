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
 * Servlet responsible for creating new blog
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/author-new-blog")
public class AuthorNewBlog extends HttpServlet {

	private static final long serialVersionUID = 5252662413152192103L;
	
	/** Key under which blog form is stored */
	public static final String blogFormKey = "blogForm";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = (String) req.getAttribute(AuthorRouter.nickKey);
		
		if(!SessionMethods.isLoggedInAs(req.getSession(), nick)) {
			errorRedirect(req, resp);
			return ;
		}
		
		req.getRequestDispatcher("/WEB-INF/pages/newBlog.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException {
		String nick = (String) req.getAttribute(AuthorRouter.nickKey);
		
		if(!SessionMethods.isLoggedInAs(req.getSession(), nick)) {
			errorRedirect(req, resp);
			return ;
		}
		
		BlogForm form = new BlogForm();
		form.fillFromRequest(req);
		form.validate();
		
		if(!form.isValid()) {
			req.setAttribute(blogFormKey, form);
			req.getRequestDispatcher("/WEB-INF/pages/newBlog.jsp").forward(req, resp);
			return ;
		}
		
		DAO dao = DAOProvider.getDAO();
		BlogEntry newBlog = BlogForm.createBlog(form, new Date(), dao.getUserByNick(nick));
		dao.addBlog(newBlog);
		
		//redirect na edit blog i postavi poruku
		resp.sendRedirect(req.getContextPath() + "/servleti/author/" + nick + "/" + newBlog.getId() + "?success=true");
		return ;
	}
	
	/**
	 * Redirects to error.jsp
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
