package hr.fer.zemris.java.p12;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.Poll;

/**
 * Servlet responsible for parsing currently available polls.
 * Forwards to "/WEB-INF/pages/pollsIndex.jsp".
 * 
 * Stores Polls in Servlet Context under optionsKey key.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/index.html")
public class PollSelectionServlet extends HttpServlet {
	private static final long serialVersionUID = -919782656030215625L;

	/** Key of result in Servlet Context */
	public static final String optionsKey = "pollsList";
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Poll> polls = DAOProvider.getDao().getPolls();
		
		polls.sort((o1, o2) -> Integer.valueOf(o1.getId()).compareTo(o2.getId()));
		
		
//		req.getServletContext().setAttribute(optionsKey, polls);
		req.setAttribute(optionsKey, polls);
		req.getRequestDispatcher("/WEB-INF/pages/pollsIndex.jsp").forward(req, resp);
	}

}
