package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Method Shows image of a Scene viewed from Point Eye in direction View.
 * Uses RayCasting method, and Phong model.
 * This class is not parallelized. 
 * 
 * @author Hrvoje
 *
 */
public class RayCaster {
	/** Used when comparing points **/
	public static final double POINT_DISTANCE_THRESHOLD = 1e-4;
	/** Default ambient values **/
	public static final int AMBIANT_R = 15;
	/** Default ambient values **/
	public static final int AMBIANT_G = 15;
	/** Default ambient values **/
	public static final int AMBIANT_B = 15;
	
	/**
	 * Main method
	 * @param args neglected
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), 
				new Point3D(10, 0, 0),
				new Point3D(0, 0, 0),
				new Point3D(0, 0, 10),
				20, 20);
		
	}
	
	
	/**
	 * Returns RayTracerProducer
	 * 
	 * @return RayTracerProducer
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
		@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer) {

				System.out.println("Započinjem izračune...");

				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];
				
				Point3D zAxis = view.sub(eye).normalize(); //vektor smjera povrsine slike
				Point3D OG = viewUp.normalize();
				Point3D yAxis = OG.sub(zAxis.scalarMultiply(zAxis.scalarProduct(OG))).normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();
				
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal/2)).add(yAxis.scalarMultiply(vertical/2)) ;
				
				Scene scene = RayTracerViewer.createPredefinedScene();
				
				short[] rgb = new short[3];
				int offset = 0;
				
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(x*horizontal/(width - 1)))
															.sub(yAxis.scalarMultiply(y*vertical/(height - 1)));
						
						Ray ray = Ray.fromPoints(eye, screenPoint);
						tracer(scene, ray, rgb);
						
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						
						offset++;
					}
				}

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}
	
	/**
	 * Fills rgb values with respect to the material and light sources.
	 * 
	 * @param scene Scene
	 * @param ray ray
	 * @param rgb rgb
	 */
	protected static void tracer(Scene scene, Ray ray, short [] rgb) {
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;
		
		RayIntersection closest = findClosestIntersection(scene, ray);
		if (closest == null) {
			return;
		}
		
		rgb[0] = AMBIANT_R;
		rgb[1] = AMBIANT_G;
		rgb[2] = AMBIANT_B;
		
		for(LightSource ls : scene.getLights()) {
			Ray lightRay = Ray.fromPoints(ls.getPoint(), closest.getPoint());
			RayIntersection closestForLightRay = findClosestIntersection(scene, lightRay);
			
			if(!pointsEqual(closestForLightRay.getPoint(), closest.getPoint())) {
				continue;
			}
			
			Point3D l = ls.getPoint().sub(closest.getPoint()).normalize();
			Point3D n = closest.getNormal();
			Point3D r = l.sub(n.scalarMultiply(2 * l.scalarProduct(n)));
			Point3D v = ray.direction;
			
			rgb[0] += sourceObjectLightIntensity(closest.getKdr(), closest.getKrr(), closest.getKrn(), ls.getR(), l, n, r, v);
			rgb[1] += sourceObjectLightIntensity(closest.getKdg(), closest.getKrg(), closest.getKrn(), ls.getG(), l, n, r, v);
			rgb[2] += sourceObjectLightIntensity(closest.getKdb(), closest.getKrb(), closest.getKrn(), ls.getB(), l, n, r, v);
		}
	}
	
	/**
	 * Returns accumulated RGB values on point with given parameters.
	 * 
	 * @param kd diffusion constant
	 * @param kr reflection constant
	 * @param krn roughness of a material
	 * @param lightIntensity of source
	 * @param l direction vector from point to light source
	 * @param n normal to the object on point of intersection
	 * @param r reflective vector
	 * @param v vector to the Eye
	 * 
	 * @return accumulated RGB values on point with given parameters 
	 */
	private static short sourceObjectLightIntensity(double kd, double kr, double krn, int lightIntensity, Point3D l, Point3D n, Point3D r, Point3D v) {
		double result;
		double diffAngleFactor = l.scalarProduct(n);
		double reflAngleFactor = Math.pow(r.scalarProduct(v), krn);
		
		result = reflAngleFactor * kr;
		
		if(diffAngleFactor > 0) {
			result += diffAngleFactor * kd;
		}
		
		result *= lightIntensity;
		
		return (short) Math.round(result);
	}


	/**
	 * Method finds closest intersection point with object on Scene and wraps results as RayInteresction.
	 * 
	 * @param scene Scene
	 * @param ray which will intersect with objects
	 * @return results of closest intersection of Ray and object from the scene
	 */
	protected static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		RayIntersection result = null;
		
		for(GraphicalObject go : scene.getObjects()) {
			RayIntersection tmp = go.findClosestRayIntersection(ray);
			if(tmp == null) {
				continue;
			}
			if(result == null || tmp.getDistance() < result.getDistance()) {
				result = tmp;
				continue;
			}
		}
		
		return result;
	}
	
	/**
	 * Checks whether two points are equal.
	 * 
	 * @param p1 first point
	 * @param p2 second point
	 * @return true if they are
	 */
	private static boolean pointsEqual(Point3D p1, Point3D p2) {
		if(Math.abs(p1.x - p2.x) > POINT_DISTANCE_THRESHOLD) return false;
		if(Math.abs(p1.y - p2.y) > POINT_DISTANCE_THRESHOLD) return false;
		if(Math.abs(p1.z - p2.z) > POINT_DISTANCE_THRESHOLD) return false;
		
		return true;
	}
}
