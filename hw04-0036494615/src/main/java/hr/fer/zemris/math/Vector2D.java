package hr.fer.zemris.math;

/**
 * Class describes 2D vector and simple operations on them.
 * Vector has it's x and y component. Supported operations are Rotation, Translation and Scale.
 * @author Hrvoje
 *
 */
public class Vector2D {
	private double x, y;
	
	/**
	 * Constructor for vector with x and y component.
	 * @param x component of a vector
	 * @param y component of a vector
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns X component of a vector.
	 * @return X component of a vector
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Returns X component of a vector.
	 * @return X compoment of a vector
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Translates vector by offset vector.
	 * @param offset
	 */
	public void translate(Vector2D offset) {
		x += offset.x;
		y += offset.y;
	}
	
	/**
	 * Returns new vector which is the result of translating this vector by offset.
	 * @param offset offset
	 * @return result vector 
	 */
	public Vector2D translated(Vector2D offset) {
		return new Vector2D(x + offset.x, y + offset.y);
	}
	
	/**
	 * Rotates vector by angle in degrees.
	 * @param angle in degrees.
	 */
	public void rotate(double angle) {		
		Vector2D help = rotated(angle);
		x = help.x;
		y = help.y;
	}
	
	/**
	 * Returns result vector which is this vector rotated by angle in degrees (counter-clockwise)
	 * @param angle in degrees.
	 * @return result vector
	 */
	public Vector2D rotated(double angle) {
		angle = convertToRadians(angle);
		
		return new Vector2D(
				x * Math.cos(angle) - y * Math.sin(angle),
				x * Math.sin(angle) + y * Math.cos(angle)
				);
	}
	
	/**
	 * Scales vector by scaler.
	 * @param scaler to scale.
	 */
	public void scale (double scaler) {
		x = x * scaler;
		y = y * scaler;
	}
	
	/**
	 * Returns result vector which is this vector scaled by scaler.
	 * @param scaler to scale with.
	 * @return scaled vector
	 */
	public Vector2D scaled(double scaler) {
		return new Vector2D(x * scaler, y * scaler);
	}
	
	/**
	 * Returns deep copy of this vector.
	 * @return deep copy of this vector
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
	
	/**
	 * Converts degrees to radians.
	 * @param angle to convert
	 * @return radians
	 */
	private double convertToRadians(double angle) {
		return angle * Math.PI / 180;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
