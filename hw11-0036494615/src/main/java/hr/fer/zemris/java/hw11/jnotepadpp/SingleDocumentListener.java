package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Interface defines methods of SingleDocumentListeners
 * 
 * @author Hrvoje
 *
 */
public interface SingleDocumentListener {
	/**
	 * Called when Modify flag of model is changed
	 * @param model observed
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);

	/**
	 * Called when File path of model is changed
	 * @param model observed
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}