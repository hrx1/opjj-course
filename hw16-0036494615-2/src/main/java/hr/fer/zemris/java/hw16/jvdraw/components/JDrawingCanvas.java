package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.models.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObjectPainter;


/**
 * Drawing Canvas stores information about drawing model on which it operates.
 * Drawing Canvas is a Subject in a Observer Design pattern.
 * 
 * Objects are added using mouse clicks. For example, if the selected tool is a
 * line, the first click defined the start point for the line and the second
 * click defines the end point for the line. Before the second click occurs, as
 * user moves the mouse, the line is drawn with end-point tracking the mouse so
 * that the user can see what will be the final result. Circle and filled circle
 * are drawn similarly: first click defines the circle center and as user moves
 * the mouse, a circle radius is defined. On second click, circle is added.
 * 
 * @author Hrvoje
 *
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {
	private static final long serialVersionUID = 5161296662488058593L;

	/** Running app */
	private JVDraw jvdraw;
	/** Drawing model used */
	private DrawingModel drawingModel;
	/** Is Canvas changed */
	private boolean changed = false;
	
	/**
	 * 
	 * 
	 * @param jvdraw
	 * @param drawingModel
	 */
	public JDrawingCanvas(JVDraw jvdraw, DrawingModel drawingModel) {
		this.jvdraw = jvdraw;
		this.drawingModel = drawingModel;
		
		registerListeners();
		registerObservers();
	}
	
	/**
	 * Register Observers
	 */
	private void registerObservers() {
		drawingModel.addDrawingModelListener(this);
	}

	/**
	 * Register Listeners
	 */
	private void registerListeners() {
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(jvdraw.getTool() != null) {
					jvdraw.getTool().mouseClicked(e);
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if(jvdraw.getTool() != null) {
					jvdraw.getTool().mouseMoved(e);;
				}
				repaint();
			}
		});
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		changed = true;
		repaint(); //todo repaint od indexa
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		changed = true;
		repaint(); //todo repaint od indexa
		//repaint preko clanske varijable koja se proteze u paintComponent,
		// ali pazi da ju postavis kako treba u mouse listneeru.
		// gldedaj gdje sve pozivas repaint pa ih tamo uredi
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		changed = true;
		repaint(); //todo repaint od indexa
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		GeometricalObjectPainter gop = new GeometricalObjectPainter(g2d);
		for(int i = 0; i < drawingModel.getSize(); ++i) {
			drawingModel.getObject(i).accept(gop);
		}
		
		if(jvdraw.getTool() != null) {
			jvdraw.getTool().paint(g2d);
		}
	}

	/**
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}

	/**
	 * @param changed the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	
}
