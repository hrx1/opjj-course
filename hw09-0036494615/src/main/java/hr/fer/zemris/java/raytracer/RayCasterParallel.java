package hr.fer.zemris.java.raytracer;

import static hr.fer.zemris.java.raytracer.RayCaster.*;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Method Shows image of a Scene viewed from Point Eye in direction View.
 * Uses RayCasting method, and Phong model.
 * This class is parallelized. 
 * 
 * Minimum chunk which is filled by thread is defined by HEIGHT_TO_CALCULATE constant.
 * 
 * @author Hrvoje
 *
 */
public class RayCasterParallel {
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
	
	/** RGB values **/
	volatile static short[] red;
	volatile static short[] green;
	volatile static short[] blue;
	
	/**
	 * Producer for IRayTracer
	 * @return RayTracerProducer
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
		@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer) {

				System.out.println("Započinjem izračune...");

				red = new short[width * height];
				green = new short[width * height];
				blue = new short[width * height];

				
				Point3D zAxis = view.sub(eye).normalize(); //vektor smjera povrsine slike
				Point3D OG = viewUp.normalize();
				Point3D yAxis = OG.sub(zAxis.scalarMultiply(zAxis.scalarProduct(OG))).normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();
				
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal/2)).add(yAxis.scalarMultiply(vertical/2)) ;
				
				Scene scene = RayTracerViewer.createPredefinedScene();
				
				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new CalculateTracer(0, height -1, height, width, horizontal, vertical, xAxis, yAxis, screenCorner, eye, scene, red, green, blue));
				pool.shutdown();

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}
	
	/**
	 * Calculates tracer
	 * @author Hrvoje
	 *
	 */
	private static class CalculateTracer extends RecursiveAction {
		/** SVUID */
		private static final long serialVersionUID = 8572508453652694435L;
		/** Used in recursion **/
		private static final int HEIGHT_TO_CALCULATE = 8;
		/** Used in calculating **/
		private int minHeight, maxHeight, height, width;
		private double horizontal, vertical;
		private Point3D xAxis, yAxis, screenCorner, eye;
		private Scene scene;
		private short[] red, green, blue;
		
		/**
		 * Constructor for calculate tracer
		 * @param minHeight to use in calculations
		 * @param maxHeight to use in calculations
		 * @param height of a picture
		 * @param width of a picture
		 * @param horizontal of a picture
		 * @param vertical of a picture
		 * @param xAxis of a picture
		 * @param yAxis of a picture
		 * @param screenCorner of a picture
		 * @param eye Eye
		 * @param scene scene
		 * @param red values
		 * @param green values
		 * @param blue values
		 */
		public CalculateTracer(int minHeight, int maxHeight, int height, int width, double horizontal, double vertical,
				Point3D xAxis, Point3D yAxis, Point3D screenCorner, Point3D eye, Scene scene, short[] red,
				short[] green, short[] blue) {
			super();
			this.minHeight = minHeight;
			this.maxHeight = maxHeight;
			this.height = height;
			this.width = width;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.screenCorner = screenCorner;
			this.eye = eye;
			this.scene = scene;
			this.red = red;
			this.green = green;
			this.blue = blue;
			
		}

		@Override
		protected void compute() {
			if (maxHeight - minHeight +1 <= HEIGHT_TO_CALCULATE) {
				computeDirect();
				return ;
			}

			invokeAll(
					new CalculateTracer(minHeight, minHeight + (maxHeight - minHeight)/2, height, width, horizontal, vertical, xAxis, yAxis, screenCorner, eye, scene, red, green, blue),
					new CalculateTracer(minHeight + (maxHeight - minHeight)/2 + 1, maxHeight, height, width, horizontal, vertical, xAxis, yAxis, screenCorner, eye, scene, red, green, blue));
		}
			
		/**
		 * Method fills rgb values
		 */
		private void computeDirect() {
			short[] rgb = new short[3];
			int offset = minHeight * width;
			
			for (int y = minHeight; y <= maxHeight; y++) {
				for (int x = 0; x < width; x++) {
					Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(x*horizontal/(width - 1)))
														.sub(yAxis.scalarMultiply(y*vertical/(height - 1)));
					
					Ray ray = Ray.fromPoints(eye, screenPoint);
					tracer(scene, ray, rgb);
					
					if(red[offset] != 0) System.out.println(offset);
					
					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					
					offset++;
					
				}
			}
		}
	}
	
	

}
