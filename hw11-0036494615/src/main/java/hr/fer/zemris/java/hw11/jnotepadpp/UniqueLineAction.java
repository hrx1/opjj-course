package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import hr.fer.zemris.java.hw11.locale.ILocalizationProvider;
import hr.fer.zemris.java.hw11.locale.LocalizableAction;

/**
 * Unique Line Action is localized Action which acts upon selected text and leaves only Unique lines.
 * Object subscribes itself to MultipleDocumentModel and upon it's current text editor's caret.
 * 
 * @author Hrvoje
 *
 */
public class UniqueLineAction extends LocalizableAction implements CaretListener, MultipleDocumentListener {
	private static final long serialVersionUID = 8929621400257952931L;
	/** Current line offset */
	private int lineOffset;
	/** Number of selected lines */
	private int numberOfLines;
	/** Current JTextArea */
	private JTextArea textArea;
	/** CurrentDocumentModel */
	private MultipleDocumentModel md;
	
	/**
	 * Constructor for Unique Line Action
	 * @param lp used for Localization
	 * @param md multiple document model to observe
	 */
	public UniqueLineAction(ILocalizationProvider lp, MultipleDocumentModel md) {
		super("unique", lp);
		
		this.md = md;
		md.addMultipleDocumentListener(this);
		
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		try {
			List<String> lines = new BufferedReader(new StringReader(textArea.getText())).lines().collect(Collectors.toList());
			Set<String> unique;
			 try { 
				 unique = new LinkedHashSet<String>(lines.subList(lineOffset, lineOffset + numberOfLines + 1));
			 }catch(IndexOutOfBoundsException e) {
				 unique = new LinkedHashSet<String>(lines.subList(lineOffset, lineOffset + numberOfLines));
			 }
			
			int startOffset = textArea.getLineStartOffset(lineOffset);
			int endOffset = textArea.getLineEndOffset(lineOffset + numberOfLines);
			
			textArea.getDocument().remove(
					startOffset,
					endOffset - startOffset
					);
			
			textArea.insert(linesToString(unique), textArea.getLineStartOffset(lineOffset));
		} catch (BadLocationException e) {
			e.printStackTrace(); //ne bi se trebalo dogoditi, profesor na predavanju rekao da ostavimo
		}
		
	}
	
	/**
	 * Converts Set of lines to one String. Between each added line new line is put.
	 * @param lines Set of lines
	 * @return lines concatenated in one String
	 */
	private String linesToString(Set<String> lines) {
		StringBuilder sb = new StringBuilder();
		for(String line : lines) {
			sb.append(line);
			sb.append('\n');
		}
		return sb.toString();
	}

	@Override
	public void caretUpdate(CaretEvent caret) {
		try {
		numberOfLines = Math.abs(textArea.getLineOfOffset(caret.getDot()) - textArea.getLineOfOffset(caret.getMark()));
		if(numberOfLines == 0) {
			this.setEnabled(false);
		} else {
			this.setEnabled(true);
			lineOffset = Math.min(textArea.getLineOfOffset(caret.getDot()), textArea.getLineOfOffset(caret.getMark()));
			}
		} catch(BadLocationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		//DO NOTHING
	}

	@Override
	public void documentAdded(SingleDocumentModel model) {
		model.getTextComponent().addCaretListener(this);
		textArea = model.getTextComponent();
	}

	@Override
	public void documentRemoved(SingleDocumentModel model) {
		model.getTextComponent().removeCaretListener(this);
		
		if(md.getNumberOfDocuments() == 0) {
			setEnabled(false);
		}
		
	}

}
