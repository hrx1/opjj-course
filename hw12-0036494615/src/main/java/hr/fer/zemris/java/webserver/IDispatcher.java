package hr.fer.zemris.java.webserver;

/**
 * Interface describes Dispatcher
 * @author Hrvoje
 *
 */
public interface IDispatcher {
	
	/**
	 * Dispatch request with urlPath
	 * @param urlPath of request
	 * @throws Exception if dispatch fails
	 */
	void dispatchRequest(String urlPath) throws Exception;
}
