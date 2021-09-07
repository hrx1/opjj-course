package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.jvdraw.components.DrawingObjectListModel;
import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.components.JColorStatusLabel;
import hr.fer.zemris.java.hw16.jvdraw.components.JDrawingCanvas;
import hr.fer.zemris.java.hw16.jvdraw.models.Circle;
import hr.fer.zemris.java.hw16.jvdraw.models.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObjectFormater;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObjectPainter;
import hr.fer.zemris.java.hw16.jvdraw.models.Line;


/**
 * Simple drawing app. 
 * 
 * @author Hrvoje
 *
 */
public class JVDraw extends JFrame {
	private static final long serialVersionUID = -6850244276638398650L;
	/** State */
	private Tool state;
	/** Model */
	private DocumentModel model;
	/** App Title */
	private String appTitle = "JVDraw";
	/** Colors */
	private JColorArea fgColor, bgColor;
	/** Used canvas */
	private JDrawingCanvas canvas;
	/** Model list */
	private DrawingObjectListModel list; 
	//list ne moze biti opcenitiji tip jer mora biti DrawingModelListener
	
	private Path savePath;
	
	/**
	 * Returns current Tool State 
	 * @return current Tool State
	 */
	public Tool getTool() {
		return state;
	}
	
	/**
	 * Default Constructor
	 */
	public JVDraw() {
		
		setTitle(appTitle);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		initGUI();
	}
	
	/**
	 * Initializes GUI
	 */
	private void initGUI() {
		model = new DocumentModel();
		canvas = new JDrawingCanvas(this, model);
		
		this.getContentPane().setLayout(new BorderLayout());
		
		createMenu();
		createToolbar();
		createSideList();
		createStatusBar();
		
		this.getContentPane().add(canvas, BorderLayout.CENTER);
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(canvas.isChanged()) {
					int resultF = JOptionPane.showConfirmDialog(
							null,
							String.format("File Not Saved. Save?"),
							String.format("Save?"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE
							);
					if (resultF == JOptionPane.CANCEL_OPTION) {
						return ;
					} else if (resultF == JOptionPane.YES_OPTION) {
						FileNameExtensionFilter filter = new FileNameExtensionFilter("JVD files", "jvd");

						JFileChooser fc = new JFileChooser();
						fc.setFileFilter(filter);
						if (fc.showSaveDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
							if(Files.exists(fc.getSelectedFile().toPath())) {
								int result = JOptionPane.showConfirmDialog(
										null,
										String.format("File already exists. Override?"),
										String.format("File already exists"),
										JOptionPane.YES_NO_OPTION,
										JOptionPane.WARNING_MESSAGE
										);
								if(result == JOptionPane.NO_OPTION) return;
							}
							
							try {
								saveDrawing(fc.getSelectedFile().toPath());
								JOptionPane.showMessageDialog(null, "Saving completed");

							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null, "Cannot save file.");
								return ;
							}

					}
				}
					else {
						dispose();
					}
			}
			}});
		
	}
	
	/**
	 * Creates Side List
	 */
	private void createSideList() {
		list = new DrawingObjectListModel(model);
		model.addDrawingModelListener(list); //list opaza promjene u modelu
		JList<GeometricalObject> listComponent = new JList<GeometricalObject>(list);
		listComponent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				int selected = listComponent.getSelectedIndex();
				if(selected == -1) return ;

				if(e.getKeyCode() == KeyEvent.VK_DELETE) {
					model.remove(model.getObject(selected));
					
				} else if(e.getExtendedKeyCode() == KeyEvent.VK_ADD) {
					model.changeOrder(model.getObject(selected), -1);
					
				} else if(e.getExtendedKeyCode() == KeyEvent.VK_SUBTRACT) {
					model.changeOrder(model.getObject(selected), 1);
				}
				
			}
			
		});
		
		listComponent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if(me.getClickCount() == 2) {
					GeometricalObject clicked = (GeometricalObject) listComponent.getSelectedValue();
					GeometricalObjectEditor editor = clicked.createGeometricalObjectEditor();
					if(JOptionPane.showConfirmDialog(null, editor, "Edit Object", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
						//..., editor, …, OK_CANCEL
						 try {
							 editor.checkEditing();
							 editor.acceptEditing();
						 } catch(Exception ex) {
							 JOptionPane.showMessageDialog(null, "Object not modified. Wrong values inserted.");
						 }
						}
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(listComponent);
//		scrollPane.setPreferredSize(new Dimension(100, 100));
		
		
		this.getContentPane().add(scrollPane, BorderLayout.EAST);
	}

	/**
	 * Creates Status Bar
	 */
	private void createStatusBar() {
		this.getContentPane().add(new JColorStatusLabel(fgColor, bgColor), BorderLayout.SOUTH);
	}

	/**
	 * Creates Menu
	 */
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		
		menuBar.add(fileMenu);

		JMenuItem open = new JMenuItem("open");
		open.addActionListener(e -> {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JVD files", "jvd");

			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter);
			
			if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				try {
					loadDrawing(fc.getSelectedFile().toPath());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Unable to open JVD file.");
					return ;
				}
			}
		});
		
		JMenuItem saveAs = new JMenuItem("save as");
		saveAs.addActionListener((e) -> {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JVD files", "jvd");

			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter);
			if (fc.showSaveDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
				if(Files.exists(fc.getSelectedFile().toPath())) {
					int result = JOptionPane.showConfirmDialog(
							null,
							String.format("File already exists. Override?"),
							String.format("File already exists"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE
							);
					if(result == JOptionPane.NO_OPTION) return;
				}
				
				try {
					saveDrawing(fc.getSelectedFile().toPath());
					JOptionPane.showMessageDialog(null, "Saving completed");

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Cannot save file.");
					return ;
				}
				savePath = fc.getSelectedFile().toPath();
				canvas.setChanged(false);
			}
		});
		
		JMenuItem save = new JMenuItem("save");
		save.addActionListener((e) -> {
			if(savePath == null) {
				JOptionPane.showMessageDialog(null, "File not saved yet");
			} else {
				try {
					saveDrawing(savePath);
					JOptionPane.showMessageDialog(null, "Saving completed");

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Cannot save file.");
					return ;
				}
			}
			canvas.setChanged(false);

		});
		
		
		JMenuItem export = new JMenuItem("export");
		export.addActionListener((e) -> {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "png", "jpg", "gif");

			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter);
			if (fc.showSaveDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
				if(Files.exists(fc.getSelectedFile().toPath())) {
					int result = JOptionPane.showConfirmDialog(
							null,
							String.format("File already exists. Override?"),
							String.format("File already exists"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE
							);
					if(result == JOptionPane.NO_OPTION) return;
				}
				
				try {
					exportDrawing(fc.getSelectedFile().toPath());
					JOptionPane.showMessageDialog(null, "Saving completed");
				} catch (IllegalArgumentException e2) {
					JOptionPane.showMessageDialog(null, "File must be in jpg, png or gif format.");
					return ;

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Cannot save file.");
					return ;
				}
				savePath = fc.getSelectedFile().toPath();
				canvas.setChanged(false);

			}
		});
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(e -> {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		});


		fileMenu.add(save);
		fileMenu.add(saveAs);
		fileMenu.add(open);
		fileMenu.add(export);
		
		this.setJMenuBar(menuBar);


	}

	/**
	 * Exports drawing to path
	 * 
	 * @param path to export
	 * @throws Exception when path extension is invalid, or path is not writable
	 */
	private void exportDrawing(Path path) throws Exception {
		String extension = getExtension(path);
		
		if(!validExtension(extension)) throw new IllegalArgumentException();
		
		GeometricalObjectBBCalculator gobbc = new GeometricalObjectBBCalculator();
		for(int i = 0; i < model.getSize(); ++i) {
			model.getObject(i).accept(gobbc);
		}
		
		Rectangle box = gobbc.getBoundingBox();
		BufferedImage image = new BufferedImage(box.width, box.height, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D g = image.createGraphics();
		AffineTransform t = new AffineTransform();
		t.translate(-box.x, -box.y);
		g.transform(t);
		
		GeometricalObjectPainter gop = new GeometricalObjectPainter(g);
		for(int i = 0; i < model.getSize(); ++i) {
			model.getObject(i).accept(gop);
		}
		
		g.dispose();
		
		File file = path.toFile();
		ImageIO.write(image, extension, file);
	}

	/**
	 * Returns true if extension is valid
	 * @param extension to test
	 * @return true if extension is valid
	 */
	private boolean validExtension(String extension) {
		return extension.equals("jpg") || extension.equals("png") || extension.equals("gif");
	}

	/**
	 * Returns null if no extension is found.
	 * 
	 * @param path
	 * @return
	 */
	private String getExtension(Path path) {
		String extension = null;

		int i = path.toString().lastIndexOf('.');
		if (i > 0) {
		    extension = path.toString().substring(i+1);
		}
		
		return extension;
	}

	/**
	 * Loads drawing from path
	 * 
	 * @param path to load from
	 * @throws IOException if path is not in valid format, or is not readable
	 */
	private void loadDrawing(Path path) throws IOException {
		BufferedReader br = Files.newBufferedReader(path);
		List<GeometricalObject> result = new LinkedList<>();
		
		String line;
		for(line = br.readLine(); line != null; line = br.readLine()) {
			try {
				result.add(parseObject(line));
			} catch (Exception e) {
				throw new IOException();
			}
		}
		
		//tek ako je sve u redu i nema iznimki...
		
		for(int i = 0; i < model.getSize(); ++i) {
			model.remove(model.getObject(0));
		}
		
		for(GeometricalObject g : result) {
			model.add(g);
		}
	}

	/**
	 * Parses GeometricalObject from a line
	 * 
	 * @param line to parse
	 * @return Geometrical Object
	 * @throws Exception when line is not parseable
	 */
	private GeometricalObject parseObject(String line) throws Exception {
		String[] splitted = line.split(" ");
		switch (splitted[0]) {
			case "LINE":
				return parseLine(splitted);
			case "CIRCLE":
				return parseCircle(splitted);
			case "FCIRCLE":
				return parseFCircle(splitted);
			default:
				throw new Exception();
		}
	}

	/**
	 * Parses GeometricalObject Line from splitted string
	 * 
	 * @param splitted to parse
	 * @return GeometricalObject
	 * @throws Exception when wrong format is provided
	 */
	private GeometricalObject parseLine(String[] splitted) {
		int x1 = Integer.valueOf(splitted[1]);
		int y1 = Integer.valueOf(splitted[2]);
		int x2 = Integer.valueOf(splitted[3]);
		int y2 = Integer.valueOf(splitted[4]);
		int r = Integer.valueOf(splitted[5]);
		int g = Integer.valueOf(splitted[6]);
		int b = Integer.valueOf(splitted[7]);
		
		return new Line("LINE", new Point(x1, y1), new Point(x2, y2), new Color(r, g, b));
	}
	
	/**
	 * Parses GeometricalObject Circle from splitted string
	 * 
	 * @param splitted to parse
	 * @return GeometricalObject
	 * @throws Exception when wrong format is provided
	 */
	private GeometricalObject parseCircle(String[] splitted) {
			int x1 = Integer.valueOf(splitted[1]);
			int y1 = Integer.valueOf(splitted[2]);
			int radius = Integer.valueOf(splitted[3]);
			int r = Integer.valueOf(splitted[4]);
			int g = Integer.valueOf(splitted[5]);
			int b = Integer.valueOf(splitted[6]);
			
			return new Circle("Circle", new Point(x1, y1), radius, new Color(r, g, b));
	}
	
	/**
	 * Parses GeometricalObject FilledCirlce from splitted string
	 * 
	 * @param splitted to parse
	 * @return GeometricalObject
	 * @throws Exception when wrong format is provided
	 */
	private GeometricalObject parseFCircle(String[] splitted) {
		int x1 = Integer.valueOf(splitted[1]);
		int y1 = Integer.valueOf(splitted[2]);
		int radius = Integer.valueOf(splitted[3]);
		int r = Integer.valueOf(splitted[4]);
		int g = Integer.valueOf(splitted[5]);
		int b = Integer.valueOf(splitted[6]);
		int r2 = Integer.valueOf(splitted[7]);
		int g2 = Integer.valueOf(splitted[8]);
		int b2 = Integer.valueOf(splitted[9]);

		return new FilledCircle("FCircle", new Point(x1, y1), radius, new Color(r, g, b), new Color(r2, g2, b2));
}

	/**
	 * Saves Drawing to save path
	 * @param savePath save path
	 * @throws IOException when cannot save to save path
	 */
	private void saveDrawing(Path savePath) throws IOException {
		GeometricalObjectFormater gof = new GeometricalObjectFormater();
		
		for(int i = 0; i < model.getSize(); ++i) {
			model.getObject(i).accept(gof);
		}
		
		Files.write(savePath, gof.getResult().getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
	}

	/**
	 * Creates Toolbars
	 */
	private void createToolbar() {
		JToolBar toolBar = new JToolBar("Bar");
		toolBar.setFloatable(true);
		
		fgColor = new JColorArea(Color.BLACK);
		bgColor = new JColorArea(Color.GRAY);
		
		fgColor.setMaximumSize(fgColor.getPreferredSize());
		bgColor.setMaximumSize(bgColor.getPreferredSize());
				
		toolBar.add(fgColor);
		toolBar.add(bgColor);
		
		//add button group
		ButtonGroup group = new ButtonGroup();
		JToggleButton lineButton = new JToggleButton("Line");
		JToggleButton circleButton = new JToggleButton("Circle");
		JToggleButton filledCircleButton = new JToggleButton("Filled Cirlce");
		
		lineButton.addActionListener(e -> state = new LineTool(model, fgColor));
		circleButton.addActionListener(e -> state = new CircleTool(model, fgColor));
		filledCircleButton.addActionListener(e -> state = new FilledCircleTool(model, fgColor, bgColor));
		
		group.add(lineButton);group.add(circleButton);group.add(filledCircleButton);
		
		toolBar.add(lineButton);
		toolBar.add(circleButton);
		toolBar.add(filledCircleButton);
		
		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	
	/**
	 * Main
	 * 
	 * @param args neglected
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JVDraw c = new JVDraw();
			c.setVisible(true);
			c.setSize(800, 800);
		});

	}
	
}
