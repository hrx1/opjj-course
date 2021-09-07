package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.web.servlets.models.LoginForm;
import hr.fer.zemris.java.tecaj_13.web.servlets.util.SessionKeys;

/**
 * Login action sets Session parameters if request parameters are valid.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/login")
public class LoginAction extends HttpServlet {
	
	private static final long serialVersionUID = 5994025683303233704L;
	
	/** Key under which nick is stored */
	public static final String nickKey = "nick";
	/** Key under which password is stored */
	public static final String pwdKey = "password";
	/** Key under which form is stored */
	public static final String formAttributeKey = "loginFormKey";
	
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
		req.getRequestDispatcher("/servleti/main").forward(req, resp);
	}
}
