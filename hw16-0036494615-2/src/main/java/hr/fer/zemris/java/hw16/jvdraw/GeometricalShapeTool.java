package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.components.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObjectPainter;

/**
 * Class which implements Standard Tool methods
 * 
 * @author Hrvoje
 *
 */
public abstract class GeometricalShapeTool implements Tool {
	/** Flag  */
	private boolean firstPointSet = false;
	/** Start Point */
	private Point start;
	/** End point */
	private Point end;
	/** Used model */
	private DrawingModel model;
	/** Color providers */
	private IColorProvider fgColor;
	private IColorProvider bgColor;
	
	/**
	 * 
	 * @param model in which finished objects will be painted
	 * @param colorProvider provides color
	 */
	public GeometricalShapeTool(DrawingModel model, IColorProvider fgColor, IColorProvider bgColor) {
		this.model = model;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		if(firstPointSet) {
			//postavi end
			//dodaj objekt
			end = e.getPoint();
			model.add(createObject());
			firstPointSet = false;
		}
		else {
			//postavi start
			//zamijeni flag
			start = e.getPoint();
			end = null;
			firstPointSet = true;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//ovisno o flag postavi start ili end
		if(firstPointSet) {
			end = e.getPoint();
		}
	}
	
	@Override
	public void paint(Graphics2D g2d) {
		//delegiraj svakom posebno?
		g2d.setColor(fgColor.getCurrentColor());
		g2d.setBackground(bgColor.getCurrentColor());
		
		if(firstPointSet) {
			createObject().accept(new GeometricalObjectPainter(g2d));
		}
	}
	
	abstract GeometricalObject createObject();

	@Override
	public void mouseDragged(MouseEvent e) {
		//nothing
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//nothing
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		//nothing
	}


	/**
	 * @return the firstPointSet
	 */
	public boolean isFirstPointSet() {
		return firstPointSet;
	}


	/**
	 * @return the start
	 */
	public Point getStart() {
		return start;
	}


	/**
	 * @return the end
	 */
	public Point getEnd() {
		return end;
	}


	/**
	 * @return the model
	 */
	public DrawingModel getModel() {
		return model;
	}


	/**
	 * @return the colorProvider
	 */
	public IColorProvider getFgColorProvider() {
		return fgColor;
	}
		
	/**
	 * @return the colorProvider
	 */
	public IColorProvider getBgColorProvider() {
		return bgColor;
	}
}
