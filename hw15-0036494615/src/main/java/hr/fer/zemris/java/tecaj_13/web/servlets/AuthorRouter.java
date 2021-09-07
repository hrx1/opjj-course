package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet routes following patterns to appropriate servlets:
 * 
 * /servleti/author/NICK
 * /servleti/author/NICK/new
 * /servleti/author/NICK/edit
 * /servleti/author/NICK/EID
 * 
 * Redirects to index.jsp otherwise
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/author/*")
public class AuthorRouter extends HttpServlet {
	private static final long serialVersionUID = -86681665306228477L;
	
	/** Key under which nick is stored */
	public static final String nickKey = "nick";
	/** Key under which blog id is stored */
	public static final String selectedBlogIDKey = "blogID";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		routeRequest(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		routeRequest(req, resp);
	}
	
	private void routeRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		String[] infoSplitted = pathInfo.substring(1).split("/");

		if(infoSplitted.length > 2) {
			//home redirect
			//TODO Error?
			resp.sendRedirect(req.getContextPath() + "/index.jsp");
			return ;
		}
		
		req.setAttribute(nickKey, infoSplitted[0]);
		
		if(infoSplitted.length == 1) {
			//nick
			req.getRequestDispatcher("/servleti/author-home").forward(req, resp);
			return ;
		}
		
		if(infoSplitted.length == 2) {
			//new, edit, eid
			switch(infoSplitted[1]) {
				case "new":
					req.getRequestDispatcher("/servleti/author-new-blog").forward(req, resp);
					break;
				case "edit":
					req.getRequestDispatcher("/servleti/author-edit-blog").forward(req, resp);
					break;
				default:
					req.setAttribute(selectedBlogIDKey, infoSplitted[1]);
					req.getRequestDispatcher("/servleti/author-show-blog").forward(req, resp);
					break;
			}
			
		}
		
	}
}
