package hr.fer.zemris.java.hw16.jvdraw;

import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.models.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.models.GeometricalObjectListener;

/**
 * Class describes Drawing Model.
 * @author Hrvoje
 *
 */
public class DocumentModel implements DrawingModel, GeometricalObjectListener {
	/** List of objects it stores */
	private List<GeometricalObject> geomObjects;
	/** Listeners */
	private List<DrawingModelListener> listeners;
	
	/**
	 * Default Constructor
	 */
	public DocumentModel() {
		geomObjects = new LinkedList<>();
		listeners = new LinkedList<>();
	}

	@Override
	public int getSize() {
		return geomObjects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return geomObjects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		geomObjects.add(object);
		object.addGeometricalObjectListener(this);
		notifyObjectAdded(geomObjects.size() - 1);
	}

	@Override
	public void remove(GeometricalObject object) {
		int index = geomObjects.indexOf(object);
		
		geomObjects.remove(index);
		object.removeGeometricalObjectListener(this);
		notifyObjectRemoved(index);
	}

	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		int objIndex = geomObjects.indexOf(object);
		int newPosition = objIndex + offset;
		if(newPosition >= geomObjects.size() || newPosition < 0) return ;
		geomObjects.remove(objIndex);
		geomObjects.add(newPosition, object);
		
		notifyObjectChanged(objIndex);
	}


	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}


	
	@Override
	public void geometricalObjectChanged(GeometricalObject o) {
		notifyObjectChanged(geomObjects.indexOf(o));
	}

	/**
	 * Notify Listeners Object has changed
	 * @param index of object
	 */
	private void notifyObjectChanged(int index) {
		for(DrawingModelListener l : listeners) {
			l.objectsChanged(this, index, index);
		}		
	}
	
	/**
	 * Notify Listeners Object is added
	 * @param index of object
	 */
	private void notifyObjectAdded(int index) {
		for(DrawingModelListener l : listeners) {
			l.objectsAdded(this, index, index);
		}
	}
	/**
	 * Notify Listeners Object is removed
	 * @param index of object
	 */
	private void notifyObjectRemoved(int index) {
		for(DrawingModelListener l : listeners) {
			l.objectsRemoved(this, index, index);
		}
	}

	

}
