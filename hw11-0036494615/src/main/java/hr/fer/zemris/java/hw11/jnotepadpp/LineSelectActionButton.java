package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import hr.fer.zemris.java.hw11.locale.ILocalizationProvider;
import hr.fer.zemris.java.hw11.locale.LocalizableAction;

/**
 * Class describes Localized classes whose action is done upon lines selected in Current Document of MultipleDocumentModel.
 * 
 * Class listens to Caret of current Document and to Changes in MultipleDocumentModel.
 * 
 * @author Hrvoje
 *
 */
public class LineSelectActionButton extends LocalizableAction implements CaretListener, MultipleDocumentListener {
	private static final long serialVersionUID = 8929621400257952931L;

	/** Used when comparing strings */
	private Comparator<String> comparator;
	/** Line offset */
	private int lineOffset;
	/** Number of selected lines */
	private int numberOfLines;
	/** TextArea currently observed */
	private JTextArea textArea;
	/** MultipleDocumentModel currently observed */
	private MultipleDocumentModel md;
	
	/**
	 * Constructor for Localized LineSelectActionButton
	 * 
	 * @param key of name of Action
	 * @param lp used for localization
	 * @param comparator used when comparing strings
	 * @param md currently observed
	 */
	public LineSelectActionButton(String key, ILocalizationProvider lp, Comparator<String> comparator, MultipleDocumentModel md) {
		super(key, lp);
		this.comparator = comparator;
		
		this.md = md;
		md.addMultipleDocumentListener(this);
		
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		try {
			List<String> lines = new BufferedReader(new StringReader(textArea.getText())).lines().collect(Collectors.toList());
			List<String> toSort;
			 try { 
				 toSort = lines.subList(lineOffset, lineOffset + numberOfLines + 1);
			 }catch(IndexOutOfBoundsException e) {
				 toSort = lines.subList(lineOffset, lineOffset + numberOfLines);
			 }
			toSort.sort((o1, o2) -> comparator.compare(o1, o2));
			
			int startOffset = textArea.getLineStartOffset(lineOffset);
			int endOffset = textArea.getLineEndOffset(lineOffset + numberOfLines);
			
			System.out.println(lineOffset + " " + (lineOffset + numberOfLines));
			System.out.println(startOffset + " " + endOffset);
			
			textArea.getDocument().remove(
					startOffset,
					endOffset - startOffset
					);
			
			textArea.insert(linesToString(toSort), textArea.getLineStartOffset(lineOffset));
		} catch (BadLocationException e) {
			e.printStackTrace(); //ne bi se trebalo dogoditi, profesor na predavanju rekao da ostavimo
		}
		
	}
	
	/**
	 * Converts lines to one string. Lines are separated with new line.
	 * @param lines to concatenate
	 * @return lines to one string separated with new line.
	 */
	private String linesToString(List<String> lines) {
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
