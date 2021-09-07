package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultEditorKit;

import hr.fer.zemris.java.hw11.locale.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.locale.ILocalizationProvider;
import hr.fer.zemris.java.hw11.locale.LocalizableAction;
import hr.fer.zemris.java.hw11.locale.LocalizationProvider;

/**
 * Class realizes JNotepad++ required in homework.
 * Contains main method which sets up application, but whose arguments are neglected.
 * 
 * JNotepadPP is localized. It's configuration files are in ./locale folder.
 * 
 * JNotepadPP has these Hotkeys:
 * 	open file	-	control 0
 * 	create new	- 	control N
 * 	close tab 	-	alt C
 * 	save doc 	-	control S
 * 	save as	doc	-	control alt S
 * 	statistics	-	alt S
 * 	exit		-	alt X 
 * 
 * @author Hrvoje
 *
 */
public class JNotepadPP extends JFrame {
	private static final long serialVersionUID = -6061870829557499039L;
	/** Initial title */
	private final String INITIAL_TITLE = "JNotepad++";
	/** Separates file path and app name */
	private final String TITLE_DELIMITER = " - ";
	/** Model used */
	private DefaultMultipleDocumentModel model;
	/** Localization used */
	private FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
	
	/**
	 * Default constructor which inits GUI
	 */
	public JNotepadPP() {
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(0, 0);
		setSize(900, 600);
		setTitle(INITIAL_TITLE);

		initGUI();
		
	}
	
	/**
	 * Inits GUI
	 */
	private void initGUI() {
		this.getContentPane().setLayout(new BorderLayout());
		
		model = new DefaultMultipleDocumentModel();

		createActions();
		createMenus();
		createToolbars();
		
		initalizeWindowListener();		
		initializeDocumentsListener();
		
		notifyNoDocumentsToButtons(true);
		
		initializeStatusBar();
		
		
	}
	
	/**
	 * Initializes Status Bar
	 */
	private void initializeStatusBar() {
		getContentPane().add(model, BorderLayout.CENTER);
		
		JPanel low = new JPanel(new BorderLayout());
		JCustomStatusBar statusBar = new JCustomStatusBar(model, flp);
		JCustomTimeLabel time = new JCustomTimeLabel(this);
		
		low.add(statusBar, BorderLayout.CENTER);
		low.add(time, BorderLayout.LINE_END);
		
		getContentPane().add(low, BorderLayout.SOUTH);
	}

	/**
	 * Initializes DocumentListeners
	 */
	private void initializeDocumentsListener() {
		model.addMultipleDocumentListener(new MultipleDocumentListener() {
			
			@Override
			public void documentRemoved(SingleDocumentModel removed) {
				if(model.getNumberOfDocuments() == 0) {
					notifyNoDocumentsToButtons(true);
				}
			}
			
			@Override
			public void documentAdded(SingleDocumentModel added) {
				if(model.getNumberOfDocuments() == 1) {
					notifyNoDocumentsToButtons(false);
				}
			}
			
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if(model.getNumberOfDocuments() == 0) {
					setTitle(INITIAL_TITLE);
					return ;
				}
				
				Path currentDocumentPath = model.getCurrentDocument().getFilePath();
				String tabTitle;
				if(currentDocumentPath == null) {
					tabTitle = "untitled";
				} else {
					tabTitle = currentDocumentPath.toAbsolutePath().toString();
				}
				setTitle(tabTitle + TITLE_DELIMITER + INITIAL_TITLE);
			}
		});
	}

	/**
	 * Notifies some buttons that No documents exist
	 * @param noDocuments true or false
	 */
	private void notifyNoDocumentsToButtons(boolean noDocuments) {
		boolean enabled = !noDocuments;
		
		cutAction.setEnabled(enabled);
		copyAction.setEnabled(enabled);
		pasteAction.setEnabled(enabled);
		closeTabAction.setEnabled(enabled);
		saveDocumentAction.setEnabled(enabled);
		saveAsDocumentAction.setEnabled(enabled);
		getStatsAction.setEnabled(enabled);
	}

	/**
	 * Initializes window listener
	 */
	private void initalizeWindowListener() {
		addWindowListener(new WindowAdapter() {
			@Override
		    public void windowClosing(WindowEvent e) {
				exitAction.actionPerformed(null);
			}
		});
	}

	/**
	 * Creates Menus
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(new NameChangeAction("file_menu", flp));
		
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(createNewDocumentAction));
		fileMenu.add(new JMenuItem(closeTabAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));
		
		JMenu editMenu = new JMenu(new NameChangeAction("edit_menu", flp));
		editMenu.add(new JMenuItem(cutAction));
		editMenu.add(new JMenuItem(copyAction));
		editMenu.add(new JMenuItem(pasteAction));
		menuBar.add(editMenu);
		
		JMenu additionalMenu = new JMenu(new NameChangeAction("additional_menu", flp));
		additionalMenu.add(new JMenuItem(getStatsAction));
		menuBar.add(additionalMenu);

		JMenu languagesMenu = new JMenu(new NameChangeAction("languages", flp));
		languagesMenu.add(new JMenuItem(changeToHRAction));
		languagesMenu.add(new JMenuItem(changeToENAction));
		languagesMenu.add(new JMenuItem(changeToDEAction));
		menuBar.add(languagesMenu);

		JMenu tools = new JMenu(new NameChangeAction("tools", flp));

		JMenu changeCase = new JMenu(new NameChangeAction("changecase", flp));
		changeCase.add(new JMenuItem(new TextSelectActionButton("uppercase", flp, o -> o.toUpperCase(), model)));
		changeCase.add(new JMenuItem(new TextSelectActionButton("lowercase", flp, o -> o.toLowerCase(), model)));
		changeCase.add(new JMenuItem(new TextSelectActionButton("invertcase", flp, o -> invertCase(o), model)));
		tools.add(changeCase);
		
		JMenu sort = new JMenu(new NameChangeAction("sort", flp));
		sort.add(new JMenuItem(new LineSelectActionButton("ascending", flp, 
				(o1, o2) -> {
					Collator collator = Collator.getInstance(flp.getLocale());
					return collator.compare(o1, o2);
		}, model)));
		
		sort.add(new JMenuItem(new LineSelectActionButton("descending", flp, 
				(o1, o2) -> {
					Collator collator = Collator.getInstance(flp.getLocale());
					return -collator.compare(o1, o2);
		}, model)));
		tools.add(sort);
		
		tools.add(new UniqueLineAction(flp, model));
		
		menuBar.add(tools);
		
		
		this.setJMenuBar(menuBar);

	}
	
	/**
	 * Inverts case of letters in given text
	 * @param text to invert
	 * @return String with inverted case of letters
	 */
	private String invertCase(String text) {
		char[] chars = text.toCharArray();
		for(int i = 0; i < chars.length; ++i) {
			char c = chars[i];
			if(Character.isLowerCase(c)) {
				chars[i] = Character.toUpperCase(c);
			} else if(Character.isUpperCase(c)) {
				chars[i] = Character.toLowerCase(c);
			}
		}
		return new String(chars);	}

	/**
	 * Creates toolbars
	 */
	private void createToolbars() {
		JToolBar toolBar = new JToolBar("Bar");
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.add(new JButton(saveAsDocumentAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(createNewDocumentAction));
		toolBar.add(new JButton(closeTabAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(cutAction));
		toolBar.add(new JButton(copyAction));
		toolBar.add(new JButton(pasteAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(getStatsAction));

		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}
	
	/**
	 * Creates Action Hoteys and Mnemonics.
	 */
	private void createActions() {
		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control 0"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_0);
		
		createNewDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		createNewDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

		closeTabAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt C"));
		closeTabAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
		saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

		getStatsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt S"));
		getStatsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);

		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt X"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

	}
	
	/** Action for opening the document */
	private final Action openDocumentAction = new LocalizableAction("open", flp) {
		private static final long serialVersionUID = -7870182294812676756L;

		@Override
		public void actionPerformed(ActionEvent event) {
			JFileChooser fc = new JFileChooser();
			
			if (fc.showOpenDialog(JNotepadPP.this) == JFileChooser.APPROVE_OPTION) {
				model.loadDocument(fc.getSelectedFile().toPath());
			}
		}
	};

	/** Action for creating new document */
	private final Action createNewDocumentAction = new LocalizableAction("create", flp) {
		private static final long serialVersionUID = -8377849368655509423L;

		@Override
		public void actionPerformed(ActionEvent event) {
			model.createNewDocument();
		}
	};
	
	/** Action for saving document */
	private final Action saveDocumentAction = new LocalizableAction("save", flp) {
		private static final long serialVersionUID = 8615988030368934894L;

		@Override
		public void actionPerformed(ActionEvent event) {
			if(model.getCurrentDocument().getFilePath() == null) {
				saveAsDocumentAction.actionPerformed(event);
			} else {
				model.saveDocument(model.getCurrentDocument(), null);
			}
		}
	};
	
	/** Action for saving document as some other file */
	private final Action saveAsDocumentAction = new LocalizableAction("saveAs", flp) {
		private static final long serialVersionUID = 4493811553589717447L;

		@Override
		public void actionPerformed(ActionEvent event) {
			JFileChooser fc = new JFileChooser();
			
			if(model.getCurrentDocument().getFilePath() != null) {
				fc.setCurrentDirectory(model.getCurrentDocument().getFilePath().getParent().toFile());
			}
			
			if (fc.showSaveDialog(JNotepadPP.this) == JFileChooser.APPROVE_OPTION) {
				if(Files.exists(fc.getSelectedFile().toPath())) {
					int result = JOptionPane.showConfirmDialog(
							JNotepadPP.this,
							String.format(flp.getString("file_already_exists"), fc.getSelectedFile().toString()),
							flp.getString("file_already_exists_title"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE
							);
					if(result == JOptionPane.NO_OPTION) return;
				}
				
				model.saveDocument(model.getCurrentDocument(), fc.getSelectedFile().toPath());
			}
		}
	};

	/** Action for closing tab */
	private final Action closeTabAction = new LocalizableAction("close", flp) {
		private static final long serialVersionUID = -2280532671615517125L;

		@Override
		public void actionPerformed(ActionEvent event) {
			if(model.getCurrentDocument().isModified()) {
				String fileName = model.getTitleAt(model.getSelectedIndex());
				int response = showFileNotSavedPane(fileName);
				
				if(response == JOptionPane.CANCEL_OPTION) {
					return ;
				} else if(response == JOptionPane.YES_OPTION) {
					saveDocumentAction.actionPerformed(event);
				}
				
				model.closeDocument(model.getCurrentDocument()); 
			} else {
				model.closeDocument(model.getCurrentDocument());
			}
		}
	};
	
	/** Action for exiting the app */
	private final Action exitAction = new LocalizableAction("exit", flp) {
		private static final long serialVersionUID = 70178771341626582L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			while (model.getNumberOfDocuments() != 0) {
				SingleDocumentModel current = model.getCurrentDocument();

				if (current.isModified()) {
					String fileName = getCurrentTabTitle();
					int response = showFileNotSavedPane(fileName);

					if (response == JOptionPane.CANCEL_OPTION) {
						break;
					}

					if (response == JOptionPane.YES_OPTION) {
						saveDocumentAction.actionPerformed(e);
					}

					model.closeDocument(current);

				} else {
					model.closeDocument(current);
				}
			}
			
			if(model.getNumberOfDocuments() == 0) {
				dispose();
			}
		}

	};
	
	/** Changes Localization to Croatian language */
	private final Action changeToHRAction = new LocalizableAction("hr", flp) {
		private static final long serialVersionUID = -3674819002042796643L;

		@Override
		public void actionPerformed(ActionEvent event) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
	};
	
	/** Changes Localization to English language */
	private final Action changeToENAction = new LocalizableAction("en", flp) {
		private static final long serialVersionUID = 4777697972753306403L;

		@Override
		public void actionPerformed(ActionEvent event) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
	};
	
	/** Changes Localization to German language */
	private final Action changeToDEAction = new LocalizableAction("de", flp) {
		private static final long serialVersionUID = -3606231803311832992L;

		@Override
		public void actionPerformed(ActionEvent event) {
			LocalizationProvider.getInstance().setLanguage("de");
		}
	};
	
	/** Class provides JLabel which changes it's text according to Localization settings */
	private static class NameChangeAction extends LocalizableAction {
		private static final long serialVersionUID = -4832865879712517145L;
		
		/**
		 * Constructor
		 * @param key of JLabel
		 * @param provider Localization Provider
		 */
		public NameChangeAction(String key, ILocalizationProvider provider) {
			super(key, provider);
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
		}
	}
	
	/**
	 * Class which describes Action with Localized name
	 * @author Hrvoje
	 *
	 */
	private static class LocalizableActionWrapper extends LocalizableAction {
		private static final long serialVersionUID = 6335741335246301373L;
		/** Action */
		Action wrapped;
		
		/**
		 * Constructor
		 * @param key of Action name
		 * @param provider of Localization
		 * @param action to do
		 */
		public LocalizableActionWrapper(String key, ILocalizationProvider provider, Action action) {
			super(key, provider);
			this.wrapped = action;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			wrapped.actionPerformed(arg0);
		}
	}
	
	/**
	 * Returns Title of current Tab.
	 * @return Title of current Tab
	 */
	private String getCurrentTabTitle() {
		int index = model.getSelectedIndex();
		if (index == -1) return null;
		else return model.getTitleAt(model.getSelectedIndex());
	}
	
	/**
	 * Shows "File not Saved" message and pane
	 * @param fileName of a file
	 * @return JOptionPane constant value
	 */
	private int showFileNotSavedPane(String fileName) {
		return JOptionPane.showConfirmDialog(
						JNotepadPP.this,
						String.format(flp.getString("file_not_saved_message"), fileName),
						flp.getString("file_not_saved_title"),
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE
					);
	}

	/**
	 * When user requests statistical info on document, you it calculates:
	 * 	a number of characters found in document (everything counts), 
	 * 	a number of non-blank characters found in document (you don't count spaces, enters and tabs), 
	 * 	a number of lines that the document contains. Calculate this and
	 *  show an informational message to user having text similar to: “Your document
	 * has X characters, Y non-blank characters and Z lines.”
	 */
	private final Action getStatsAction = new LocalizableAction("statistics", flp) {
		private static final long serialVersionUID = 70178771341626582L;

		@Override
		public void actionPerformed(ActionEvent action) {
			String currentText = model.getCurrentDocument().getTextComponent().getText();
			int linesNumber = TextUtilities.numberOfLines(currentText);
			int numberOfNonWhite = TextUtilities.numberOfNonBlank(currentText);
			int count = TextUtilities.numberOfCharacters(currentText);
			
			System.out.println(currentText);
			
			String fileName = getCurrentTabTitle();
			String resultMessage = String.format(flp.getString("statistics_message"), count, numberOfNonWhite, linesNumber);
			
			JOptionPane.showMessageDialog(JNotepadPP.this, resultMessage
					, flp.getString("statistics") + " " + fileName, JOptionPane.INFORMATION_MESSAGE);
		}
		
	};
	
	/** Default CopyAction as LocalizedAction */
	private final Action copyAction = new LocalizableActionWrapper("copy", flp, new DefaultEditorKit.CopyAction());
	/** Default CutAction as LocalizedAction */
	private final Action cutAction = new LocalizableActionWrapper("cut", flp, new DefaultEditorKit.CutAction());
	/** Default PasteAction as LocalizedAction */
	private final Action pasteAction = new LocalizableActionWrapper("paste", flp, new DefaultEditorKit.PasteAction());

	/**
	 * Runs application.
	 * @param args neglected
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JNotepadPP();
			frame.setVisible(true);
		});
	}
}
