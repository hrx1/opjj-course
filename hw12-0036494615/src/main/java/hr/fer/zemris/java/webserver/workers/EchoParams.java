package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker Echoes given parameters to the Request
 * 
 * @author Hrvoje
 *
 */
public class EchoParams implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		context.write("<html><body><table style=\"border: 1px solid black\">");
		for(String paramName : context.getParameterNames()) {
			String row = String.format(
					"<tr>"
					+ "<th>%s</th>"
					+ "<th>%s</th>"
					+ "</tr>", paramName, context.getParameter(paramName));
			context.write(row);
		}
		context.write("</table></body></html>");
	}

}
