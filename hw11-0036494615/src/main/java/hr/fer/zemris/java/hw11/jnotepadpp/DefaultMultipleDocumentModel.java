package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Class creates JTabbedPane whose Tabs are SingleDocuments
 * Class listens to changes in SingleDocuments and notifies it's listeners about changes.
 * 
 * Listeners are notified when current document is changed, or when document is added, or when document is removed.
 * 
 * @author Hrvoje
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	private static final long serialVersionUID = 3354615413876524989L;
	/** List of contained documents */
	private List<SingleDocumentModel> documents;
	/** Current document */
	private SingleDocumentModel current;
	/** Listeners */
	private List<MultipleDocumentListener> listeners;
	/** Action done when one contained document notifies listeners that change happened */
	private DocumentUpdate documentUpdate;

	/**
	 * Default Constructor for DefaultMultipleDocumentModel.
	 */
	public DefaultMultipleDocumentModel() {
		documents = new LinkedList<>();
		listeners = new LinkedList<>();
		documentUpdate = new DocumentUpdate();
		
		addChangeListener(o -> {
			
			int tabIndex = getSelectedIndex();
			SingleDocumentModel previous = current;
			
			if(tabIndex >= 0) {
				current = documents.get(tabIndex);
			} else {
				current = null;
			}
			
			notifyCurrentDocumentChanged(previous, current);
			
		});
	}
	
	@Override
	public SingleDocumentModel getCurrentDocument() {
		return current;
	}
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel newDoc = new DefaultSingleDocumentModel(null, "");
		
		appendDocument(newDoc);
		
		return newDoc;
	}
	

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		Objects.requireNonNull(path);
		
		SingleDocumentModel created = getDocumentFromPath(path);
		
		if(created == null) {
			created = new DefaultSingleDocumentModel(path, loadText(path));
			appendDocument(created);
		} else {
			setSelectedIndex(documents.indexOf(created));
		}
		
		return created;
	}
	
	/**
	 * Appends document to list of contained documents, adds it into new tab and notifies listeners about a change.
	 * 
	 * @param newDoc new Document
	 */
	private void appendDocument(SingleDocumentModel newDoc) {
		documents.add(newDoc);
		newDoc.addSingleDocumentListener(documentUpdate);
		addDocumentTab(newDoc);
		
		notifyDocumentAdded(newDoc);
		
		if(documents.size() > 1) {
			setSelectedIndex(documents.size() - 1);
		}
		
	}
	
	/**
	 * Loads text from path
	 * @param path to load text from
	 * @return text from path
	 */
	private String loadText(Path path) {
		try {
			return new String(Files.readAllBytes(path));
		} catch (IOException ignored) { //Ne ocekujemo
			return "Error while reading file.";
		}
	}

	@Override
	/**
	 * newPath can be null; if null, document should be saved using path associated
	 * from document, otherwise, new path should be used and after saving is
	 * completed, document’s path should be updated to newPath
	 */
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		Path savePath;
		
		if(newPath == null) {
			if(getDocumentFromPath(newPath) != null) { //ne bi se trebalo dogoditi
				
			}
			savePath = model.getFilePath();
		} else {
			savePath = newPath;
		}
		
		try {
			Files.write(savePath, model.getTextComponent().getText().getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
		} catch (IOException e) { //nece se dogoditi
			e.printStackTrace();
		}
		model.setModified(false);
		model.setFilePath(savePath);
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		int tabIndex = documents.indexOf(model);
		
		SingleDocumentModel removed = documents.remove(tabIndex);
		model.removeSingleDocumentListener(documentUpdate);
		removeTabAt(tabIndex);
		
		notifyDocumentRemoved(removed);
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);		
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return documents.get(index);
	}
	
	/**
	 * Notifies listeners that current document changed from previous to current.
	 * @param previous document
	 * @param current document
	 */
	private void notifyCurrentDocumentChanged(SingleDocumentModel previous, SingleDocumentModel current) {
		listeners.forEach(listener -> listener.currentDocumentChanged(previous, current));
	}
	
	/**
	 * Notifies listeners that new document is added.
	 * @param added added document
	 */
	private void notifyDocumentAdded(SingleDocumentModel added) {
		listeners.forEach(listener -> listener.documentAdded(added));

	}
	
	/**
	 * Notifies listeners that document is removed.
	 * @param removed removed document
	 */
	private void notifyDocumentRemoved(SingleDocumentModel removed) {
		listeners.forEach(listener -> listener.documentRemoved(removed));

	}
	
	/**
	 * Returns already loaded SingleDocumentModel with path. 
	 * Null path is not equal to itself, meaning two Documents with path null will be treated as different.
	 * If there is no already loaded SingleDocumentModel, null is returned.
	 * 
	 * @param path of SingleDocumentModel
	 * @return model with path, or null if it doesn't exist
	 */
	private SingleDocumentModel getDocumentFromPath(Path path) {
		
		for(SingleDocumentModel model : documents) {
			if(model.getFilePath() != null && model.getFilePath().equals(path)) {
				return model;
			}
		}
		
		return null;
	}

	/**
	 * Adds tab whose tab name is model file name, and which contains it's text.
	 * 
	 * @param model document to add
	 */
	private void addDocumentTab(SingleDocumentModel model) {
		String tabName, tip;
		if(model.getFilePath() == null) {
			tabName = "untitled";
			tip = "Not saved";
		} else {
			tabName = model.getFilePath().getFileName().toString();
			tip = model.getFilePath().toString();
		}
		

		JScrollPane scroll = new JScrollPane(model.getTextComponent());
		JPanel panel = new JPanel(new BorderLayout());
		scroll.setVisible(true);
		panel.add(scroll, BorderLayout.CENTER);
		
		addTab(tabName, getModifiedIcon(model.isModified()), panel, tip);
	}
	
	/**
	 * Returns icon corresponding to whether Document is modified or not. 
	 * 
	 * @param modified of a document 
	 * @return icon corresponding to whether Document is modified or not
	 */
	private ImageIcon getModifiedIcon(boolean modified) {
		if(modified) { 
			return getIcon("redDisk.png");
		} else {
			return getIcon("greenDisk.png");
		}
	}
	
	/**
	 * Returnes icon with icon name. Icon is stored in icons folder.
	 * 
	 * @param iconName name of icon
	 * @return icon with icon name
	 */
	private ImageIcon getIcon(String iconName) {
		InputStream is = this.getClass().getResourceAsStream("icons/" + iconName);
		byte[] bytes = null;
		
		try {
			bytes = is.readAllBytes();
			is.close();

		} catch (IOException | NullPointerException e) {
			e.printStackTrace(); //OVO NE OCEKUJEMO
		}
		
		return new ImageIcon(bytes);
	}
	
	/**
	 * SingleDocumentListener which updates tab icons, title and notifies listeners that a change has been made.
	 * @author Hrvoje
	 *
	 */
	private class DocumentUpdate implements SingleDocumentListener {
		
		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			int tabIndex = documents.indexOf(model);
			ImageIcon icon = getModifiedIcon(model.isModified());
			
			setIconAt(tabIndex, icon);
		}

		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			int tabIndex = documents.indexOf(model);
			setTitleAt(tabIndex, model.getFilePath().getFileName().toString());
			setToolTipTextAt(tabIndex, model.getFilePath().toString());
			
			notifyCurrentDocumentChanged(model, model);
		}
		
	}
}
