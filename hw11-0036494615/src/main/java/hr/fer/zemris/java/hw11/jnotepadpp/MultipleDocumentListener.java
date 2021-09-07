package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Abstracts Listener for changes in MultipleDocumentModel
 * @author Hrvoje
 *
 */
public interface MultipleDocumentListener {
	/**
	 * Action done when current document changes from previousModel to currentModel.
	 * @param previousModel previous model
	 * @param currentModel current model
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

	/**
	 * Action done when model document is added.
	 * @param model added document
	 */
	void documentAdded(SingleDocumentModel model);

	/**
	 * Action done when model document is removed.
	 * @param model removed document
	 */
	void documentRemoved(SingleDocumentModel model);
}
