package hr.fer.zemris.java.webserver;

/**
 * 
 * @author Hrvoje
 *
 */
public interface IWebWorker {
	
	/**
	 * 
	 * @param context
	 * @throws Exception
	 */
	public void processRequest(RequestContext context) throws Exception;

}
