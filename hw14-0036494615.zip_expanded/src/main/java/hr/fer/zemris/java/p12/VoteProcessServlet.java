package hr.fer.zemris.java.p12;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * Servlet processes Vote given as request parameter under voteIdKey.
 * 
 * Redirects user to "/servleti/glasanje-rezultati".
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/glasanje-glasaj")
public class VoteProcessServlet extends HttpServlet {
	private static final long serialVersionUID = -976340403063692564L;

	/** Key for Vote ID in Session */
	public static final String voteIdKey = "id";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int voteID = Integer.valueOf(req.getParameter(voteIdKey));
		
		DAOProvider.getDao().addVote(voteID);
		
		//REDIRECT
		resp.sendRedirect(
					req.getContextPath() + 
					"/servleti/glasanje-rezultati?" + 
					VotingServlet.pollIDKey + 
					"=" + 
					req.getParameter(VotingServlet.pollIDKey)
				);
}
}
