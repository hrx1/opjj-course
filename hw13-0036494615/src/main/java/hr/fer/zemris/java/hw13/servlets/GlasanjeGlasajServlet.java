package hr.fer.zemris.java.hw13.servlets;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static hr.fer.zemris.java.hw13.servlets.GlasanjeServlet.VoteOption;

/**
 * Servlet updates voting results. Adds one vote to Vote with id given as a parameter.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {
	private static final long serialVersionUID = 4681116924387982988L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		try {
		Path resultsPath = Paths.get(fileName);
		if(!Files.exists(resultsPath)) {
			createResultsFile(req, resultsPath);
		}
		
		updateResult((String) req.getParameter("id"), resultsPath);
		
		}catch(Exception e) {
			resp.getWriter().write("Error while reading/writting results.");
			return ;
		}

		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}

	/**
	 * Increments value of parameter from results path.
	 * 
	 * @param parameter whose value will be incremented
	 * @param resultsPath to change
	 * @throws IOException if resultsPath is not Readable/writable
	 * 
	 */
	private void updateResult(String parameter, Path resultsPath) throws IOException {
		List<String> lines = Files.readAllLines(resultsPath);
		
		lines.replaceAll(s -> {
			String[] splitted = s.split("\t");
			String id = splitted[0];
			if(id.equals(parameter)) {
				int value = Integer.parseInt(splitted[1]);
				++value;
				return id + "\t" + value;
			} else {
				return s;
			}
		});
		
		Files.write(resultsPath, lines);
	}

	/**
	 * Writes list of Strings stored in servlet context under key GlasanjeServlet.optionsKey to resultsPath.
	 * 
	 * @param req of servlet contexts
	 * @param resultsPath to write in
	 * @throws IOException if resultsPath is not writable 
	 * 
	 */
	@SuppressWarnings("unchecked")
	static void createResultsFile(HttpServletRequest req, Path resultsPath) throws IOException {
		List<VoteOption> list = (List<VoteOption>) req.getServletContext().getAttribute(GlasanjeServlet.optionsKey);
		list.sort((o1, o2) -> o1.ID.compareTo(o2.ID));
		
		BufferedWriter bw = Files.newBufferedWriter(resultsPath);
		for(VoteOption option : list) {
			bw.write(option.ID + "\t" + "0");
			bw.write("\n");
		}
		
		bw.flush();
		bw.close();
	}

}
