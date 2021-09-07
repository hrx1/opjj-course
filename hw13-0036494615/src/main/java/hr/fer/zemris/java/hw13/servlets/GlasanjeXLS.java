package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.hw13.servlets.GlasanjeRezultatiServlet.VoteResult;

/**
 * Servlet outputs results of voting as XML file.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/glasanje-xls")
public class GlasanjeXLS extends HttpServlet {
	private static final long serialVersionUID = 2491004537498803939L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
									throws ServletException, IOException {
		
		List<VoteResult> results = (List<VoteResult>) req.getSession().getAttribute(GlasanjeRezultatiServlet.resultKey); //TODO I
		
		resp.setHeader("Content-Disposition", "attachment; filename=\"voting_results.xls\"");
		resp.setContentType("application/vnd.ms-excel"); 
		//referenca: https://stackoverflow.com/questions/974079/setting-mime-type-for-excel-document
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet newSheet = hwb.createSheet();
		
		int i = 0; 
		for(VoteResult vr : results) {
			HSSFRow newRow = newSheet.createRow(i);
			newRow.createCell(0).setCellValue(vr.name);
			newRow.createCell(1).setCellValue(vr.numberOfVotes);
			++i;
		}

		hwb.write(resp.getOutputStream());
		hwb.close();

		
	}
}
