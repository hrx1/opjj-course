package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.web.servlets.models.RegisterForm;

/**
 * Servlet Registers user if it is possible. Error is returned otherwise
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/register")
public class RegisterAction  extends HttpServlet{

	private static final long serialVersionUID = -5595479677216811244L;
	/** Key under which form is stored */
	public static final String formAttributeKey = "formKey";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		checkRegistration(req, resp);
	}
	
	private void checkRegistration(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RegisterForm form = new RegisterForm();
		
		form.fillFromRequest(req);
		form.validate();
		
		if(!form.isValid()) {
			form.clearSensitiveData();
			req.setAttribute(formAttributeKey, form);
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
			return ;
		}
		
		DAOProvider.getDAO().registerUser(form);
		resp.sendRedirect(req.getContextPath() + "/servleti/main");

	}
}
