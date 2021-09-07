package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstracts GeometricalObject and implements Generic methods
 * @author Hrvoje
 *
 */
public abstract class GeometricalObject {
	/** Name */
	private String name;
	/** Color */
	private Color color;
	
	/**
	 * Listeners
	 */
	private List<GeometricalObjectListener> listeners;
	
	/**
	 * Constructor
	 * @param name Name
	 * @param color Color
	 */
	public GeometricalObject(String name, Color color) {
		this.name = name;
		this.color = color;
		
		listeners = new LinkedList<>();
	}
	
	/**
	 * Get name n
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get Color
	 * @return color
	 */
	public Color getColor() {
		return this.color;
	}
	 /**
	  * Set Color
	  * @param c
	  */
	public void setColor(Color c) {
		this.color = c;
	}


	/**
	 * Add listener
	 * @param l
	 */
	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.add(l);
	}
	
	/**
	 * Remove listener
	 * @param l
	 */
	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}
	
	void notifyObjectChanged() {
		for(GeometricalObjectListener listener : listeners) {
			listener.geometricalObjectChanged(this);
		}
	}
	
	/**
	 * Accept Visitor
	 * @param v
	 */
	public abstract void accept(GeometricalObjectVisitor v);
	
	/**
	 * Create Geometrical Object Editor
	 * @return Geometrical Object Editor
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();
	
	/**
	 * Return object name
	 * @return name
	 */
	public abstract String getGeometricalObjectName();
	
	/**
	 * String of properties
	 * @return properties
	 */
	public abstract String getProperties();
	
	@Override
	public String toString() {
		return String.format("%s %s", getGeometricalObjectName(), getProperties());
	}
	
	
}
