package hr.fer.zemris.java.hw16.jvdraw.models;

/**
 * 
 * @author Hrvoje
 *
 */
public interface DrawingModelListener {
	/**
	 * 
	 * @param source
	 * @param index0
	 * @param index1
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);
	/**
	 * 
	 * @param source
	 * @param index0
	 * @param index1
	 */
	 public void objectsRemoved(DrawingModel source, int index0, int index1);
	 /**
	  * 
	  * @param source
	  * @param index0
	  * @param index1
	  */
	 public void objectsChanged(DrawingModel source, int index0, int index1);
}
