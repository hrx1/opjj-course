package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.web.servlets.models.LoginForm;
import hr.fer.zemris.java.tecaj_13.web.servlets.util.SessionKeys;

/**
 * Main servlet
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 3146597545808468379L;
	/** Key under which login form is stored */
	public static final String formAttributeKey = "loginFormKey";
	/** Key under which  authors is stored */
	public static final String authorsAttributeKey = "authorsKey";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setAuthors(req);
		forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginForm loginForm = new LoginForm();
		loginForm.fillFromRequest(req);
		loginForm.validate();
		
		if(loginForm.isValid()) {
			BlogUser user = loginForm.getLoggedUser();

			req.getSession().setAttribute(SessionKeys.userFirstName, user.getFirstName());
			req.getSession().setAttribute(SessionKeys.userLastName, user.getLastName());
			req.getSession().setAttribute(SessionKeys.userID, user.getId());
			req.getSession().setAttribute(SessionKeys.userNick, user.getNick());
		}
		req.setAttribute(formAttributeKey, loginForm);
		
		setAuthors(req);
		
		if(loginForm.isValid()) {
			redirect(req, resp);
		}else {
			forward(req, resp);
		}
	}
	
	/**
	 * Redirects to index
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendRedirect(req.getContextPath() + "/index.jsp");
	}

	/**
	 * Sets authors as attribute
	 * @param req
	 */
	private void setAuthors(HttpServletRequest req) {
		List<BlogUser> authors = DAOProvider.getDAO().getAllUsers();
		req.setAttribute(authorsAttributeKey, authors);
	}
	
	/**
	 * Forwards to pages/index.jsp
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void forward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
	
	
}
