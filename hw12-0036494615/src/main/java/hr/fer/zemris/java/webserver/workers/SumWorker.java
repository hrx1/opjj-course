package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Sum Worker sums given parameters, and stores all operands and result to the temporary 
 * values map.
 * 
 * First operand is stored under key "a", second under "b", and result under "zbroj"
 * 
 * @author Hrvoje
 *
 */
public class SumWorker implements IWebWorker {
	/** Key of first operand */
	private static String firstKey= "a";
	/** Key of second operand */
	private static String secondKey= "b";
	/** Result key */
	private static String resultKey= "zbroj";
	
	@Override
	public void processRequest(RequestContext context) throws Exception {
		String A = context.getParameter("a");
		String B = context.getParameter("b");
		
		int a = intOrDefault(A, 1);
		int b = intOrDefault(B, 2);
		
		int result = a + b;
		
		context.setTemporaryParameter(firstKey, String.valueOf(a));
		context.setTemporaryParameter(secondKey, String.valueOf(b));
		context.setTemporaryParameter(resultKey, String.valueOf(result));
		
		context.getDispatcher().dispatchRequest("/private/calc.smscr");
	}

	/**
	 * Returns integer representation of A if it is possible, otherwise returns i
	 * @param A String
	 * @param i default value
	 * @return integer representation of A if it is possible, otherwise returns i
	 */
	private int intOrDefault(String A, int i) {
		if(A == null) return i;
		try{
			return Integer.parseInt(A);
		} catch(NumberFormatException e) {
			return i;
		}
	}
	
	
	

}
