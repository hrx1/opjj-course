package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import static hr.fer.zemris.java.hw13.servlets.Utils.*;

/**
 * Servlet creates XML with respect to given parameters. Accepts a three
 * parameters a (integer from [-100,100]) b (integer from [-100,100]) and n
 * (where n>=1 and n<=5). If any parameter is invalid, it displays redirects to
 * error.jsp.
 * 
 * Servlet dynamically creates a Microsoft Excel document with n pages. On page 'i' there
 * must be a table with two columns. The first column should contain integer
 * numbers from 'a' to 'b'. The second column should contain 'i'-th powers of these
 * numbers.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/powers")
public class Powers extends HttpServlet {
	private static final long serialVersionUID = 428456595830608504L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String A = req.getParameter("a");
		String B = req.getParameter("b");
		String N = req.getParameter("n");
		
		int a = intOrDefault(A, -200);
		int b = intOrDefault(B, -200);
		int n = intOrDefault(N, 0);
		
		if (a<-100 || a > 100 || 
				b<-100 || b > 100 ||
				n < 1 || n > 5) {
			req.getSession().setAttribute("error_info", "Powers. Invalid arguments");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return ;
		}
		
		resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		for(int i = 0; i < n; ++i) {
			HSSFSheet newSheet = hwb.createSheet();
			
			for(int r = a; r <= b; ++r) {
				HSSFRow newRow = newSheet.createRow(r - a);
				newRow.createCell(0).setCellValue(r);
				newRow.createCell(1).setCellValue(Math.pow(r, i + 1));
				//Nije receno broje li se stranice od 0 ili od 1
			}
		}
		
		resp.setContentType("application/vnd.ms-excel"); //referenca: https://stackoverflow.com/questions/974079/setting-mime-type-for-excel-document
		hwb.write(resp.getOutputStream());
		hwb.close();
	}
	
	
}
