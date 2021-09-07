package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.web.servlets.models.CommentForm;

/**
 * Servlet which is responsible for showing a blog.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/author-show-blog")
public class AuthorShowBlog extends HttpServlet {
	
	private static final long serialVersionUID = 5866175296119676069L;

	/** Key under which blog is stored */
	public static final String blogKey = "blog_show";
	/** Key under which Comment Form is stored */
	public static final String commentFormKey = "commentFormKey";
	/** Key under which commentSent flag is stored */
	public static final String commentSent = "comment-sent";

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogEntry requestedBlog = processRequest(req, resp);
		processComment(req, resp, requestedBlog);
		success(req, resp);
	}

	private void processComment(HttpServletRequest req, HttpServletResponse resp, BlogEntry blog) {
		if(!"true".equals(req.getParameter(commentSent))) return ;
		
		CommentForm form = new CommentForm();
		form.fillFromRequest(req);
		form.validate();
		
		req.setAttribute(commentFormKey, form);
		if(form.isValid()) {
			DAOProvider.getDAO().addComment(blog, form);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(processRequest(req, resp) == null) return ;
		success(req, resp);
	}
	
	
	private BlogEntry processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = (String) req.getAttribute(AuthorRouter.nickKey);
		long blogID;
		
		try{
			 blogID = Long.parseLong((String) req.getAttribute(AuthorRouter.selectedBlogIDKey));
		}catch(NumberFormatException e) {
			 //redirect na error
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return null;
		}
		
		DAO dao = DAOProvider.getDAO();
		//provjeri odgovara li blog, nicku
		BlogEntry blog = dao.getBlogEntry(blogID);
		
		if(blog == null || !blog.getCreator().getNick().equals(nick)) {
			//redirect na error
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return null;
		}
		
		req.setAttribute(blogKey, blog);
		
		return blog;
	}
	
	private void success(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/show-blog.jsp").forward(req, resp);

	}
	
}
