package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static hr.fer.zemris.java.hw13.servlets.Utils.intOrDefault;

/**
 * Trigonometric Table is a Servlet which calculates sine and cosine values for all degree angles between given parameters a and b.
 * Saves result as a list, stores it in Servlet Context under key in resultKey variable.
 * If a > b, then a and b swap values. If b > a + 720, then b is set to a + 720.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/trigonometric")
public class TrigonometricTable extends HttpServlet {
	private static final long serialVersionUID = -2530801650516797500L;

	/** Key of a result */
	public static final String resultKey = "trigonometricTable";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String A = req.getParameter("a");
		String B = req.getParameter("b");
		
		int a = intOrDefault(A, 0);
		int b = intOrDefault(B, 360);
		
		if(a > b) {
			int r = a;
			a = b;
			b = r;
		} else if (b > a + 720) {
			b = a + 720;
		}
		
		List<CalcResult> result = new ArrayList<>(b - a + 1);
		
		for(int i = a; i <= b; ++i) {
			CalcResult r = new CalcResult();
			r.degree = i;
			double radian = Math.toRadians(i);
			r.sinValue = Math.sin(radian);
			r.cosValue = Math.cos(radian);
			
			result.add(r);
		}
		
		//parametar po zahtjevu
		req.setAttribute(resultKey, result);
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
	
	/**
	 * Describes one result of calculation.
	 * Sine and cosine of a degree.
	 * 
	 * @author Hrvoje
	 *
	 */
	public static class CalcResult {
		/** Degree */
		Integer degree;
		/** Trigonometric values of a degree */
		Double sinValue, cosValue;
		
		/**
		 * Degree getter
		 * @return degree
		 */
		public int getDegree() {
			return degree;
		}
		
		/**
		 * Sine value getter
		 * @return sine value
		 */
		public double getSinValue() {
			return sinValue;
		}
		
		/**
		 * Cosine value geter
		 * @return cosine value
		 */
		public double getCosValue() {
			return cosValue;
		}
		
		
	}
}
