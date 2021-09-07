package hr.fer.zemris.java.raytracer.model;

/**
 * Class describes intersection of a sphere and a ray
 * @author Hrvoje
 *
 */
public class SphereRayIntersection extends RayIntersection {
	/** Sphere **/
	Sphere sphere;
	/** Normal of the sphere on the intersection **/
	Point3D normal;
	
	/**
	 * Constructor for a SpehereRayIntersection
	 * @param point of intersection
	 * @param distance of intersection
	 * @param outer is out of sphere
	 * @param sphere 
	 */
	protected SphereRayIntersection(Point3D point, double distance, boolean outer, Sphere sphere) {
		super(point, distance, outer);
		this.sphere = sphere;
		normal = point.sub(sphere.center).normalize();
	}

	@Override
	public Point3D getNormal() {
		return normal;
	}

	@Override
	public double getKdr() {
		return sphere.kdr;
	}

	@Override
	public double getKdg() {
		return sphere.kdg;
	}

	@Override
	public double getKdb() {
		return sphere.kdb;
	}

	@Override
	public double getKrr() {
		return sphere.krr;
	}

	@Override
	public double getKrg() {
		return sphere.krg;
	}

	@Override
	public double getKrb() {
		return sphere.krb;
	}

	@Override
	public double getKrn() {
		return sphere.krn;
	}

}
