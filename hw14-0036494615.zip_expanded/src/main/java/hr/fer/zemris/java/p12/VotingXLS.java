package hr.fer.zemris.java.p12;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.VoteResult;

/**
 * Servlet generates XLS file from data in VotingResultsServlet.resultKey.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/servleti/glasanje-xls")
public class VotingXLS extends HttpServlet {
	private static final long serialVersionUID = -3841525828101391256L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
									throws ServletException, IOException {
		
		Integer pollID = Integer.valueOf(req.getParameter(VotingServlet.pollIDKey)); //TODO I
		
		List<VoteResult> results = DAOProvider.getDao()
				.getVotingResults(pollID)
				.stream()
				.sorted()
				.collect(Collectors.toList());

		
		if(results == null) {
			resp.sendRedirect(req.getContextPath().toString() + "/index.html");
			return ;
		}
		
		resp.setHeader("Content-Disposition", "attachment; filename=\"voting_results.xls\"");
		resp.setContentType("application/vnd.ms-excel"); 
		//referenca: https://stackoverflow.com/questions/974079/setting-mime-type-for-excel-document
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet newSheet = hwb.createSheet();
		
		int i = 0; 
		for(VoteResult vr : results) {
			HSSFRow newRow = newSheet.createRow(i);
			newRow.createCell(0).setCellValue(vr.getName());
			newRow.createCell(1).setCellValue(vr.getVoteNumber());
			++i;
		}

		hwb.write(resp.getOutputStream());
		hwb.close();

		
	}
}
