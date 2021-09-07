package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class sets temporary parameters and calls /private/home.smscr script.
 * 
 * @author Hrvoje
 *
 */
public class Home implements IWebWorker {
	/** Default Background color */
	public static final String DEFAULT_COLOR = "#7F7F7F";
	/** Background key in persistent values map */
	private static final String PERSISTANT_KEY = "bgcolor";
	/** Background key in temporary values map */
	private static final String TEMP_KEY = "background";
	
	@Override
	public void processRequest(RequestContext context) throws Exception {
		String persistant = context.getPersistentParameter(PERSISTANT_KEY);
		context.setTemporaryParameter(TEMP_KEY, (persistant == null) ? DEFAULT_COLOR : persistant);
		
		context.getDispatcher().dispatchRequest("/private/home.smscr");
	}
	

}
