package hr.fer.zemris.java.p12;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.VoteOption;

/**
 * Servlet generates All possible Voting Options for given Poll ID 
 * and stores them under votingOptions Key.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/glasanje")
public class VotingServlet extends HttpServlet {
	private static final long serialVersionUID = -919782656030215625L;

	/** Key of result in Servlet Context */
	public static final String optionsKey = "votingOptions";
	/** Key of pollID in Session */
	public static final String pollIDKey = "pollID";
	/** Key of Poll Title in Session */
	public static final String pollTitleKey = "pollTitle";
	/** Key of Poll Message in Session */
	public static final String pollMsgKey = "pollMsg";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String idParam = req.getParameter("pollID");
		
		if(idParam == null) {
			resp.sendRedirect(req.getContextPath().toString() + "/index.html");
			return ;
		}
		
		int pollID = Integer.valueOf(idParam);
				
//		req.getSession().setAttribute(pollIDKey, pollID);
		req.setAttribute(pollIDKey, pollID);
		
		List<VoteOption> polls = DAOProvider.getDao().getVoteOptions(pollID);
		polls.sort((o1, o2) -> Integer.valueOf(o1.getID().compareTo(o2.getID())));
		
		
		String title, msg;
		String[] pollMeta = DAOProvider.getDao().getPollMeta(pollID);

		if(pollMeta != null) {
			title = pollMeta[0];
			msg = pollMeta[1];
		} else {
			title = msg = "untitled.";
		}
		
//		req.getSession().setAttribute(pollTitleKey, title);
//		req.getSession().setAttribute(pollMsgKey, msg);
		req.setAttribute(pollTitleKey, title);
		req.setAttribute(pollMsgKey, msg);
		
//		req.getServletContext().setAttribute(optionsKey, polls);
//		req.getServletContext().setAttribute(optionsKey, polls);
		req.setAttribute(optionsKey, polls);
		req.setAttribute(optionsKey, polls);
		req.getRequestDispatcher("/WEB-INF/pages/votingIndex.jsp").forward(req, resp);
	}
}
