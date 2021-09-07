package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw13.servlets.GlasanjeServlet.VoteOption;

/**
 * Servlet which reads results from /WEB-INF/glasanje-rezultati.txt,
 * and stores them in Servlet Context under resultKey key.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {
	
	private static final long serialVersionUID = -5867325795575845502L;

	/** Key of a result */
	public static final String resultKey = "votingResults";
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path resultPath = Paths.get(fileName);
		
		if(!Files.exists(resultPath)) {
			GlasanjeGlasajServlet.createResultsFile(req, resultPath);
		}
		
		List<String> resultsString = Files.readAllLines(resultPath);
		List<VoteResult> votingResults = new LinkedList<>();
		
		Iterator<VoteOption> voteIter = (Iterator<VoteOption>) ((List<VoteOption>) req.getServletContext()
										.getAttribute(GlasanjeServlet.optionsKey)).iterator();
		
		for(String result : resultsString) {
			VoteOption vo = voteIter.next();
			votingResults.add(new VoteResult(vo.name, result.split("\t")[1], vo.song));
		}
		
		votingResults.sort((o1, o2) -> -o1.numberOfVotes.compareTo(o2.numberOfVotes));
		
		req.getServletContext().setAttribute(resultKey, votingResults);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
	
	/**
	 * Class which describes parameters of processed Vote.
	 * Each vote is defined with current number of votes, band name, and band song.
	 * 
	 * @author Hrvoje
	 *
	 */
	public static class VoteResult {
		/** Parameters of vote */
		String name, numberOfVotes, song;

		/**
		 * Constructor for vote result.
		 * 
		 * @param name of band
		 * @param numberOfVotes number of votes
		 * @param song representative song
		 */
		public VoteResult(String name, String numberOfVotes, String song) {
			super();
			this.name = name;
			this.numberOfVotes = numberOfVotes;
			this.song = song;
		}

		/**
		 * Getter for name
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Getter for number of votes
		 * @return number of votes
		 */
		public String getNumberOfVotes() {
			return numberOfVotes;
		}
		
		/**
		 * Getter for a song
		 * @return song
		 */
		public String getSong() {
			return song;
		}
		
		
	}
}
