package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Home of Author Page
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/author-home")
public class AuthorHome extends HttpServlet {
	
	private static final long serialVersionUID = 2765824638187459688L;

	/** Key under which blogs are stored */
	public static final String blogsKey = "blogs";
	/** Key under which author is stored */
	public static final String authorKey = "author";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		showAuthor(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		showAuthor(req, resp);
	}
	
	private void showAuthor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = (String) req.getAttribute(AuthorRouter.nickKey);
		
		BlogUser user = DAOProvider.getDAO().getUserByNick(nick);
		
		if(user == null) {
			//TODO add error msg
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return ;
		}
		
		req.setAttribute(authorKey, user);
		req.setAttribute(blogsKey, user.getCreatedBlogs());

		req.getRequestDispatcher("/WEB-INF/pages/author-home.jsp").forward(req, resp);
	}
}
