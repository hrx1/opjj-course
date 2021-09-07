package hr.fer.zemris.java.hw16.jvdraw.components;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw16.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.models.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObject;

/**
 * List Model 
 * @author Hrvoje
 *
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {
	private static final long serialVersionUID = -6784047481235811374L;
	/** Used model */
	private DrawingModel model;
	
	/**
	 * Constructor 
	 * @param model Model
	 */
	public DrawingObjectListModel(DrawingModel model) {
		this.model = model;
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return model.getObject(index);
	}

	@Override
	public int getSize() {
		return model.getSize();
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		fireIntervalAdded(source, index0, index1);
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		fireIntervalRemoved(source, index0, index1);
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		fireContentsChanged(source, 0, getSize());
	}

}
