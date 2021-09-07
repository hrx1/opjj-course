package hr.fer.zemris.java.cmdapps.search.DocumentsModel;

/**
 * Abstracts Vectorized Document
 * @author Hrvoje
 *
 */
public interface DocumentVector {
	/**
	 * Return Vector
	 * @return Vector
	 */
	public double[] getVector();
	
	/**
	 * Get vector length
	 * @return vector length
	 */
	default double getAbsolute() {
		double[] vector = getVector();
		double result = 0;
		for(int i = 0; i < vector.length; ++i) {
			result += vector[i] * vector[i];
		}
		return Math.sqrt(result);
	}
}
