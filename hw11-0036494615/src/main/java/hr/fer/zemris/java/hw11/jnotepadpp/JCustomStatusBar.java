package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

import hr.fer.zemris.java.hw11.locale.ILocalizationListener;
import hr.fer.zemris.java.hw11.locale.ILocalizationProvider;

/**
 * Label which shows length and caret position statistics of Current Document in MultipleDocumentModel.
 * Label value names are localized with given ILocalizationProvider.
 * Key "length" is used.
 * 
 * Object listens to MultipleDocument document change and caret position.
 * 
 * @author Hrvoje
 *
 */
public class JCustomStatusBar extends JPanel implements MultipleDocumentListener, CaretListener {
	private static final long serialVersionUID = -4832120790217152386L;
	/** JLabel for number of characters in document */
	private JLabel length;
	/** JLabel for Cursor statistics */
	private JLabel cursorStats;
	/** Used for localization */
	ILocalizationProvider lp;
	
	/**
	 * Constructor for JCustomStatusBar.
	 * Adds JCUstomStatusBar as listener for model and it's current odcument caret.
	 * 
	 * @param model to observe
	 * @param lp for localization
	 */
	public JCustomStatusBar(MultipleDocumentModel model, ILocalizationProvider lp) {
		length = new JLabel();
		cursorStats = new JLabel();
		
		model.addMultipleDocumentListener(this);
		
		this.setLayout(new GridLayout(1, 2));
		add(length);
		add(cursorStats);
		
		this.lp = lp;
		
		lp.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				
			}
		});
	}
	
	@Override
	public void caretUpdate(CaretEvent event) {
		caretUpdate(((JTextArea)event.getSource()).getCaret(), (JTextArea)event.getSource());
	}

	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		if(previousModel != null) {
			previousModel.getTextComponent().removeCaretListener(this);
		}
		if (currentModel != null) {
			currentModel.getTextComponent().addCaretListener(this);
			caretUpdate(currentModel.getTextComponent().getCaret(), currentModel.getTextComponent());
		} else {
			setStatus(0, -1, -1, 0);
		}
	}

	@Override
	public void documentAdded(SingleDocumentModel model) {
		//Do nothing
	}

	@Override
	public void documentRemoved(SingleDocumentModel model) {
		//Do nothing
	}
	
	/**
	 * Registers caret update on area.
	 * 
	 * @param caret updated
	 * @param area area
	 */
	private void caretUpdate(Caret caret, JTextArea area) {
		int position = caret.getDot();
		
		int ln;
		try {
			ln = area.getLineOfOffset(position) + 1;
		} catch (BadLocationException e) { //ne bi se trebao dogoditi
			ln = 0;
		}
		
		int col;
		try {
			col = position - area.getLineStartOffset(ln - 1);
		} catch (BadLocationException e) {
			col = -1;
		}
		
		int sel = Math.abs(caret.getDot() - caret.getMark());
		
		setStatus(area.getText().length(), ln, col, sel);
	}
	
	/**
	 * Sets status
	 * @param textLength length of text
	 * @param ln line number of caret position
	 * @param col column number of caret position
	 * @param sel selection size 
	 */
	private void setStatus(int textLength, int ln, int col, int sel) {
		length.setText(" " + lp.getString("length") + ": " + String.valueOf(textLength));
		String lnColSel = String.format(" Ln: %d   Col: %d   Sel: %d ", ln, col, sel);
		cursorStats.setText(lnColSel);
	}
}
