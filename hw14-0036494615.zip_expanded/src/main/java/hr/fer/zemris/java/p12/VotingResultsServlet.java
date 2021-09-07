package hr.fer.zemris.java.p12;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.VoteResult;

/**
 * Servlet collects results and stores them in Session under resultKey.
 * If no pollID is given, user is redirected to homepage.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/glasanje-rezultati")
public class VotingResultsServlet extends HttpServlet {
	
	private static final long serialVersionUID = -5867325795575845502L;

	/** Key of a result */
	public static final String resultKey = "votingResults";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

//		Integer pollID = (Integer) req.getSession().getAttribute(VotingServlet.pollIDKey); //TODO I
		Integer pollID = Integer.valueOf(req.getParameter(VotingServlet.pollIDKey)); //TODO I
		
		if (pollID == null) { //redirect to home page
			resp.sendRedirect(req.getContextPath().toString() + "/index.html");
			return ;
		}
		
		List<VoteResult> votingResults = DAOProvider.getDao()
													.getVotingResults(pollID)
													.stream()
													.sorted()
													.collect(Collectors.toList());
		
//		req.getSession().setAttribute(resultKey, votingResults);
		req.setAttribute(resultKey, votingResults);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}

}
