package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet parses Voting options and redirects them to glasanjeIndex.jsp.
 * Atributes are passed throug ServletContext with key stored in optionsKey variable. 
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = -919782656030215625L;

	/** Key of result in Servlet Context */
	public static final String optionsKey = "voteOptions";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		List<VoteOption> voteOptions = new ArrayList<>(lines.size());
		
		for(String line : lines) {
			String[] parsed = line.split("\t");
			VoteOption tmp = new VoteOption(parsed[0], parsed[1], parsed[2]);
			voteOptions.add(tmp);
		}
		
		voteOptions.sort((o1, o2) -> o1.ID.compareTo(o2.ID));
		
		req.getServletContext().setAttribute(optionsKey, voteOptions);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
	/**
	 * Class describes Vote Option.
	 * Every Vote Option has it's ID, name and song
	 * 
	 * @author Hrvoje
	 *
	 */
	public static class VoteOption {
		/* Vote option parameters */
		String ID, name, song;

		/**
		 * Constructor for Vote Option
		 * 
		 * @param ID of Vote option
		 * @param name of vote option
		 * @param song of vote option
		 */
		public VoteOption(String ID, String name, String song) {
			super();
			this.ID = ID;
			this.name = name;
			this.song = song;
		}

		/**
		 * Getter for ID
		 * 
		 * @return ID
		 */
		public String getID() {
			return ID;
		}

		/**
		 * Getter for Name
		 * 
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Getter for Song
		 * 
		 * @return song
		 */
		public String getSong() {
			return song;
		}
		
		
	}
}
