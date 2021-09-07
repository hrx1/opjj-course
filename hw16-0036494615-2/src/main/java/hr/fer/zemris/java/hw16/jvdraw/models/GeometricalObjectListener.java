package hr.fer.zemris.java.hw16.jvdraw.models;

/**
 * 
 * Listener for changes in GeometricalObject
 * @author Hrvoje
 *
 */
public interface GeometricalObjectListener {
	/**
	 * Object Changed
	 * @param o Object
	 */
		public void geometricalObjectChanged(GeometricalObject o);
}
