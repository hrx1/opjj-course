package hr.fer.zemris.java.raytracer.model;

/**
 * Class describes a Sphere and it's material properties.
 * @author Hrvoje
 *
 */
public class Sphere extends GraphicalObject {
	/** Definition of a sphere **/
	Point3D center;
	double radius;
	
	/** Material properties **/
	double kdr;
	double kdg;	
	double kdb;
	double krr;
	double krg;
	double krb;
	double krn;
	
	/**
	 * Constructor for a sphere
	 * @param center of a sphere
	 * @param radius of a sphere
	 * @param kdr of a sphere material
	 * @param kdg of a sphere material
	 * @param kdb of a sphere material
	 * @param krr of a sphere material
	 * @param krg of a sphere material
	 * @param krb of a sphere material
	 * @param krn of a sphere material
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		super();
		this.center = center;
		this.radius = radius;
		
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		
		double A = Math.pow(ray.direction.x, 2) + Math.pow(ray.direction.y, 2) + Math.pow(ray.direction.z, 2);
		Point3D difference = ray.start.sub(center);
		double B = 2 * (ray.direction.x * difference.x + ray.direction.y * difference.y + ray.direction.z * difference.z);		
		double C = Math.pow(difference.x, 2) + Math.pow(difference.y, 2) + Math.pow(difference.z, 2) - Math.pow(radius, 2);
		
		double B2 = Math.pow(B, 2);
		double discriminant = B2 - 4 * A * C;
		
		if(discriminant < 0) {
			return null;
		}
		
		double discriminantSq = Math.sqrt(discriminant);

		double lambda;
		boolean outer;

		if(-B > discriminantSq) { //-B - D > 0
			outer = false;
			lambda = (-B-discriminantSq)/(2*A);
		}
		else if(B < discriminantSq) { //-B + D > 0
			outer = true;
			lambda = (-B+discriminantSq)/(2*A);
		}
		else {
			return null;
		}
		
		Point3D point = ray.start.add(ray.direction.scalarMultiply(lambda));
		double distance = ray.start.sub(point).norm();
		
		return new SphereRayIntersection(point, distance, outer, this);
	}

}
