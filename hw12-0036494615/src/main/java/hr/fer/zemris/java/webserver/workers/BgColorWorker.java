package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker changes Background Color value stored in persistent variable map.
 * 
 * @author Hrvoje
 *
 */
public class BgColorWorker implements IWebWorker {
	
	/** Background Color key */
	public static final String BG_COLOR_PARAM= "bgcolor";
	
	@Override
	public void processRequest(RequestContext context) throws Exception {
		
		String parameter = context.getParameter(BG_COLOR_PARAM);
		
		if(parameter != null && !parameter.startsWith("#")) { 
			parameter = "#" + parameter;
			context.setPersistentParameter(BG_COLOR_PARAM, parameter);
		}
		
		context.write("<html><body>");
		String result = String.format("Value %s stored. \n Index: <a href=\"/index2.html\">link</a>",(parameter == null)?"is not":"is");
		context.write(result);
		context.write("</body></html>");
	}

}
