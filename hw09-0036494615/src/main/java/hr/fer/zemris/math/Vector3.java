package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Class describes 3D vectors and simple operations with them.
 * @author Hrvoje
 *
 */
public class Vector3 {
	/** Definition of a vector **/
	private double x, y, z;
	private double norm;
	private Vector3 normalized; 
	
	private static final double THRESHOLD = 1e-5;

	
	/**
	 * Constructor for vector with x, y, z values 
	 * @param x value
	 * @param y value
	 * @param z value
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		norm = Math.sqrt(x*x + y*y + z*z);
		
		normalized = new Vector3();
		normalized.x = x/norm;
		normalized.y = y/norm;
		normalized.z = z/norm;
	}
	
	/**
	 * Default constructor used for creating norms
	 */
	private Vector3() {
		norm = 1;
	}
		
	/**
	 * Returns length of a vector
	 * @return length of a vector
	 */
	public double norm() {
		return norm;
	}

	/**
	 * Returns normalized vector. Vector whos length is 1
	 * @return normalized vector
	 */
	public Vector3 normalized() {
		return normalized;
	} 

	/**
	 * Returns result of addition of this and other vector
	 * @param other to add 
	 * @return result of addition of this and other vector
	 */
	public Vector3 add(Vector3 other) {
		Objects.requireNonNull(other);
		
		double newX = this.x + other.x;
		double newY = this.y + other.y;
		double newZ = this.z + other.z;
		
		return new Vector3(newX, newY, newZ);
	} 

	/**
	 * Returns result of subtraction of this and other vector
	 * @param other to subtract 
	 * @return result of subtraction of this and other vector
	 */
	public Vector3 sub(Vector3 other) {
		Objects.requireNonNull(other);

		double newX = this.x - other.x;
		double newY = this.y - other.y;
		double newZ = this.z - other.z;

		return new Vector3(newX, newY, newZ);		
	} 
	
	/**
	 * Returns result of dot product of this and other vector
	 * @param other to product with 
	 * @return result of dot product of this and other vector
	 */
	public double dot(Vector3 other) {
		Objects.requireNonNull(other);

		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	/**
	 * Returns result of cross product of this and other vector
	 * @param other to cross with 
	 * @return result of cross product of this and other vector
	 */
	public Vector3 cross(Vector3 other) {
		Objects.requireNonNull(other);

		return new Vector3(
				this.y * other.z - this.z * other.y,
				this.z * other.x - this.x * other.z,
				this.x * other.y - this.y * other.x);
	} 

	/**
	 * Returns new Vector which is in the same direction as this vector, 
	 * but his norm is scaled by s, and direction determined with sign of s
	 * 
	 * @param s scale
	 * @return new vector scaled by s
	 */
	public Vector3 scale(double s) {
		return new Vector3(x*s, y*s, z*s);
	} 

	/**
	 * Returns cos angle between this an other
	 * @param other
	 * @return cos angle between this an other
	 */
	public double cosAngle(Vector3 other) {
		Objects.requireNonNull(other);

		return this.dot(other)/(this.norm * other.norm);
	} 

	/**
	 * X component of a vector
	 * @return X component of a vector
	 */
	public double getX() {
		return x;
	} // prva komponenta vektora

	/**
	 * Y component of a vector
	 * @return Y component of a vector
	 */
	public double getY() {
		return y;
	} // druga komponenta vektora

	/**
	 * Z component of a vector
	 * @return Z component of a vector
	 */
	public double getZ() {
		return z;
	} 

	/**
	 * x, y, z components of a vector as array
	 * @return x, y, z components of a vector as array
	 */
	public double[] toArray() {
		return new double[] {x, y, z};
	} // pretvorba u polje s 3 elementa

	@Override
	public String toString() {
		return String.format("[%.6f, %.6f, %.6f]" , x, y, z);
	} // pretvorba u string

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3 other = (Vector3) obj;
		
		if(Math.abs(this.x - other.x) > THRESHOLD) return false;
		if(Math.abs(this.y - other.y) > THRESHOLD) return false;
		if(Math.abs(this.z - other.z) > THRESHOLD) return false;
		
		return true;
	}

	
	
}
