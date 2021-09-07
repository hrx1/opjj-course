package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.event.ActionEvent;
import java.util.function.Function;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import hr.fer.zemris.java.hw11.locale.ILocalizationProvider;
import hr.fer.zemris.java.hw11.locale.LocalizableAction;

/**
 * TextSelectActionButton is LocalizableAction which action acts upon selected Text in current Text of MultipleDocumentListener.
 * 
 * Class listens to Caret of current Document and to Changes in MultipleDocumentModel.
 * 
 * @author Hrvoje
 *
 */
public class TextSelectActionButton extends LocalizableAction implements CaretListener, MultipleDocumentListener {
	private static final long serialVersionUID = 8929621400257952931L;
	/** Function applied */
	private Function<String, String> function;
	/** Current caret offset */
	private int caretOffset;
	/** Difference between caret dot and mark */
	private int difference;
	/** Current text area */
	private JTextArea textArea;
	/** Observed MultipleDocumentModel */
	private MultipleDocumentModel md;
	
	/**
	 * Constructor for TextSelectActionButton.
	 * @param key of name in Localization
	 * @param lp used for localization
	 * @param function on selected text
	 * @param md observed MultipleDocumentModel
	 */
	public TextSelectActionButton(String key, ILocalizationProvider lp, Function<String, String> function, MultipleDocumentModel md) {
		super(key, lp);
		this.function = function;
		
		this.md = md;
		md.addMultipleDocumentListener(this);
		
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String newText = function.apply(textArea.getSelectedText());
		try {
			textArea.getDocument().remove(caretOffset, difference);
			textArea.getDocument().insertString(caretOffset, newText, null);
		} catch (BadLocationException e) {
			e.printStackTrace(); //ne bi se trebalo dogoditi, profesor na predavanju rekao da ostavimo
		}
		
	}

	@Override
	public void caretUpdate(CaretEvent caret) {
		difference = Math.abs(caret.getDot() - caret.getMark());
		if(difference == 0) {
			this.setEnabled(false);
		} else {
			this.setEnabled(true);
			caretOffset = Math.min(caret.getDot(), caret.getMark());
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
