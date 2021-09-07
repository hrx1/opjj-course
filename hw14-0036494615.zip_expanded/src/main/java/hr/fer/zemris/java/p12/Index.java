package hr.fer.zemris.java.p12;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WebServlet for /index.html location. Current implementation forwards user to /servleti/index.html
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/index.html")
public class Index extends HttpServlet {
	private static final long serialVersionUID = -7383641861207504793L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect(req.getContextPath().toString() + "/servleti/index.html");
	}
}
