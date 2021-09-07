package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Interface describes basic operations upon SingleDocuments
 * 
 * Model is Subject in Observer design pattern.
 * Listeners can be notified when filePath of document changes or when document is modified.
 * 
 * @author Hrvoje
 *
 */
public interface SingleDocumentModel {
	/**
	 * Returns text component
	 * @return text component
	 */
	JTextArea getTextComponent();

	/**
	 * Returns file path of SingleDocument
	 * @return file path of SingleDocument
	 */
	Path getFilePath();

	/**
	 * Sets file path to path
	 * @param path to set
	 */
	void setFilePath(Path path);

	/**
	 * Returns true if document is modified
	 * @return true if document is modified
	 */
	boolean isModified();

	/**
	 * Sets modified flag
	 * @param modified to set
	 */
	void setModified(boolean modified);

	/**
	 * Adds listener l
	 * @param l listener to add
	 */
	void addSingleDocumentListener(SingleDocumentListener l);

	/**
	 * Removes listener l
	 * @param l listener to remove
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}