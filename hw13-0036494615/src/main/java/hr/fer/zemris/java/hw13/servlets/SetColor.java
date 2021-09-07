package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet responsible for inserting color given in parameters to Session attribute.
 * Stores Color under pickedBgCol key.
 * @author Hrvoje
 *
 */
@WebServlet("/setcolor")
public class SetColor extends HttpServlet{

	private static final long serialVersionUID = -4907845054610730497L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String picked = (String) req.getParameter("pickedBgCol");
		resp.getWriter().write("<html>\n" + 
				"<body bgcolor=\""+ picked +  "\">\n" + 
				"");
		resp.getWriter().write("Color picked: " + picked);
		resp.getWriter().write("\n</body></html>");
		req.getSession().setAttribute("pickedBgCol", picked);
	}
}
