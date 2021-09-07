package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Class describes SingleDocumentModel whose text is encapsulated in JTextArea.
 * Model is Subject in Observer design pattern.
 * Listeners can be notified when filePath of document changes or when document is modified.
 * 
 * Two DefaultSingleDocumentModels are compared using filePath they represent.
 * 
 * @author Hrvoje
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	/** Listeners */
	private List<SingleDocumentListener> listeners;
	/** File Path */
	private Path filePath;
	/** Modified flag */
	private boolean modified;
	/** Text area of SingleDocument */
	private JTextArea textArea;
	/** Listens to changes in document. 
	 * Notifies Observers and unsubscribes itself from Document listeners */
	private DocumentListener modificationListener = new DocumentListener() {
		@Override
		public void changedUpdate(DocumentEvent document) {
		}

		@Override
		public void insertUpdate(DocumentEvent document) {
			modificationMade(document);
		}

		@Override
		public void removeUpdate(DocumentEvent document) {
			modificationMade(document);
		}
		
		/**
		 * Notifies that modification is made and unsubscribes itself from Document Listeners.
		 * @param document event
		 */
		private void modificationMade(DocumentEvent document) {
			modified = true;
			notifyModifyStatusUpdated();
			document.getDocument().removeDocumentListener(modificationListener);

		}
		
	};
	
	/**
	 * Constructor for DefaultSingleDocumentModel. Sets SingleDocument Path and Text.
	 * Adds listener to current document.
	 * 
	 * @param filePath of SingleDocumentModel
	 * @param text of SingleDocumentModel
	 */
	public DefaultSingleDocumentModel(Path filePath, String text) {
		super();
		Objects.requireNonNull(text);
		
		this.filePath = filePath;
		this.textArea = new JTextArea(text);
		
		modified = false;
		listeners = new LinkedList<>();
		
		textArea.setText(text);
		
		//pretplata na promjenu teksta
		//pretplata zapravo javlja ostalima da se dogodila promjena, kad je to potrebno
		textArea.getDocument().addDocumentListener(modificationListener);
	}

	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(Path path) {
		Objects.requireNonNull(path);
		this.filePath = path;
		notifyFilePathUpdated();
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		if(modified == false) {
			textArea.getDocument().addDocumentListener(modificationListener);
		}
		this.modified = modified;
		notifyModifyStatusUpdated();
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		Objects.requireNonNull(l);
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		Objects.requireNonNull(l);
		listeners.remove(l);
	}
	
	/**
	 * Notifies listeners that Modify flag is changed
	 */
	private void notifyModifyStatusUpdated() {
		listeners.forEach(listener -> listener.documentModifyStatusUpdated(this));
	}
	
	/**
	 * Notifies listeners that file path is changed
	 */
	private void notifyFilePathUpdated() {
		listeners.forEach(listener -> listener.documentFilePathUpdated(this));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return (this == obj);
	}
	
}
