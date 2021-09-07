package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

/**
 * Class abstracts work with Multiple SingleDocumentModel's.
 * Class listens to changes in SingleDocuments and notifies it's listeners about changes.
 * 
 * Listeners are notified when current document is changed, or when document is added, or when document is removed.
 * 
 * @author Hrvoje
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	/**
	 * Creates new blank Document
	 * @return new blank Document
	 */
	SingleDocumentModel createNewDocument();

	/**
	 * Returns current document.
	 * @return current document
	 */
	SingleDocumentModel getCurrentDocument();

	/**
	 * Loads document from path.
	 * @param path to load document from 
	 * @return loaded document
	 */
	SingleDocumentModel loadDocument(Path path);

	/**
	 * Saves model to newPath
	 * @param model to save
	 * @param newPath save destination
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);

	/**
	 * Closes model document
	 * @param model to close
	 */
	void closeDocument(SingleDocumentModel model);

	/**
	 * Adds listener.
	 * @param l listener
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Removes listener.
	 * @param l listener
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Returns number of contained documents.
	 * @return number of contained documents
	 */
	int getNumberOfDocuments();

	/**
	 * Returns document stored under tab index index.
	 * @param index of a tab
	 * @return document under index tab
	 */
	SingleDocumentModel getDocument(int index);
}