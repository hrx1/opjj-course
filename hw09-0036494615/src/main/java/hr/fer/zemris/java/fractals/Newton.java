package hr.fer.zemris.java.fractals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Class prints fractal printed from given polynomial roots.
 * Roots must be in a form a+bi.
 * "done" should be written when no more roots are needed.
 *  
 * @author Hrvoje
 *
 */
public class Newton {
	/** Convergence threshold **/
	private static double convergenceThreshold = 1e-3;
	/** Max iterations when calculating **/
	private static double maxIterations = 16*16*16;
	/** Acceptable deviation from root **/
	private static double rootThreshold = 1e-3;

	/**
	 * Main method
	 * @param args neglected
	 */
	public static void main(String[] args) {
		
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\n" + 
				"Please enter at least two roots, one root per line. Enter 'done' when done.");
		
		Complex[] roots;
		
		Scanner sc = new Scanner(System.in);
		try {
			roots = requestRoots(sc);
			sc.close();
		}catch(Exception e) {
			System.out.println("Invalid number format!");
			e.printStackTrace();
			return ;
		}
		
		if(roots.length < 2) {
			System.out.println("Insuficient number of roots given!");
			return;
		}
		
		System.out.println("Image of fractal will appear shortly. Thank you.");
		
		ComplexRootedPolynomial rootedPolynomial = new ComplexRootedPolynomial(roots);
		
		FractalViewer.show(new NewtonRahpsonProducer(rootedPolynomial));
	}

	/**
	 * Requests roots, and returns given
	 * @return given roots
	 * @throws ParseException when illegal argument is given
	 */
	private static Complex[] requestRoots(Scanner sc) throws ParseException {
		List<Complex> resultList = new LinkedList<>();
				
		for(int counter = 0; true; ++counter) {
			System.out.print("Root " + counter + "> ");
			String line = sc.nextLine();
			
			if(line.equals("done")) break;
			
			resultList.add(parseComplex(line));
			
			++counter;
		}
				
		Complex[] result = new Complex[resultList.size()];
		return resultList.toArray(result);
	}

	private static Complex parseComplex(String s) throws ParseException {
		s = s.replaceAll("\\s+", "");
		Pattern patternIm = Pattern.compile("[+-]?[i][0-9]*[\\.]?[0-9]*"); //kupi imaginaran dio
		Pattern patternRe = Pattern.compile("[+-]?[0-9]*[\\.]?[0-9]*"); //kupi realan dio
		
		String withoutIm = s.replaceAll("[+-]?[i][0-9]*[\\.]?[0-9]*", ""); //makne imaginaran dio
		
		Matcher matchIm = patternIm.matcher(s);
		Matcher matchRe = patternRe.matcher(withoutIm);
				
		
		double real, imaginary;
		
		//pokupi imaginaran:
		if (matchIm.find()) {
			String im = matchIm.group(0).replaceAll("i", "");
			if(im.equals("-")) {
				imaginary = -1;
			}else if(im.equals("") || im.equals(".")) {
				imaginary = 1;
			}
			else {
				imaginary = Double.parseDouble(im);
			}
			
		} else {
			imaginary = 0;
		}
		
		//pokupi realan:
		if (matchRe.find() && !matchRe.group(0).equals("")) {
			String re = matchRe.group(0);
			real = Double.parseDouble(re);
		}else {
			real = 0;
		}

		
		return new Complex(real, imaginary);
	}

	/**
	 * Producer for Newton Raphson
	 * @author Hrvoje
	 *
	 */
	public static class NewtonRahpsonProducer implements IFractalProducer {

		private ComplexRootedPolynomial rooted;
		private ComplexPolynomial polynomial; 
		private ComplexPolynomial derivation;

		private static final int NUMBER_OF_THREADS_FACTOR = 8;
		
		private static ExecutorService pool = Executors.newFixedThreadPool(NUMBER_OF_THREADS_FACTOR * Runtime.getRuntime().availableProcessors(), 
				new DaemonicThreadFactory());
		
		/**
		 * Constructor for Producer
		 * @param rooted Complex Rooted Polynomial
		 */
		public NewtonRahpsonProducer(ComplexRootedPolynomial rooted) {
			this.rooted = rooted;
			polynomial = rooted.toComplexPolynom();
			derivation = polynomial.derive();

		}
		
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer) {

			System.out.println("Zapocinjem izracun...");
			short[] data = new short[width * height];
			final int brojTraka = 16;
			int brojYPoTraci = height / brojTraka;

			List<Future<Void>> rezultati = new ArrayList<>();
			
			for(int i = 0; i < brojTraka; ++i) {
				int yMin = i*brojYPoTraci;
				int yMax = (i+1)*brojYPoTraci-1;
				
				if(i==brojTraka-1) {
					yMax = height-1;
				}
				
				NewtonRahpsonCalculator posao = new NewtonRahpsonCalculator(reMin, reMax, imMin, imMax, width, height, yMin, yMax, data, rooted, polynomial, derivation);
				rezultati.add(pool.submit(posao));
			}
			
			for(Future<Void> posao : rezultati) {
				try {
					posao.get();
				} catch (InterruptedException | ExecutionException e) {
				}
			}
			
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			
			observer.acceptResult(data, (short)(polynomial.order()+1), requestNo);
		}
		
	}
	
	/**
	 * Class offers methods which calculate NewtonRaphson iterations.
	 * Class is paralelizable
	 * 
	 * @author Hrvoje
	 *
	 */
	private static class NewtonRahpsonCalculator implements Callable<Void> {
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		short[] data;

		ComplexRootedPolynomial rooted;
		ComplexPolynomial polynomial;
		ComplexPolynomial derivation;
		
		/**
		 * Constructor for NewtonRaphsonCalcutor
		 * 
		 * @param reMin 
		 * @param reMax 
		 * @param imMin 
		 * @param imMax 
		 * @param width 
		 * @param height 
		 * @param yMin 
		 * @param yMax 
		 * @param data 
		 * @param rooted 
		 * @param polynomial
		 * @param derivation
		 */
		public NewtonRahpsonCalculator(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, short[] data, ComplexRootedPolynomial rooted, ComplexPolynomial polynomial, ComplexPolynomial derivation) {
			
			super();
			
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.data = data;
			
			this.rooted = rooted;
			this.polynomial = polynomial;
			this.derivation = derivation;
		}
		
		@Override
		public Void call() throws Exception {
			//za svaki dobiveni x, y racunaj pocetni zn i vrti iteraciju
			int offset = yMin * width;
			
			for(int y = yMin; y <= yMax; ++y) {
				for(int x = 0; x < width; ++x) {
					Complex zn = mapToComplexPlain(x, y);
					short result = iterationCalc(zn);
					data[offset] = result;
					++offset;
				}
			}
			
			return null;
		}
		
		/**
		 * Maps (x, y) to complex plain
		 * @param x
		 * @param y
		 * @return
		 */
		private Complex mapToComplexPlain(int x, int y) {
			double real = reMin + ((reMax - reMin) * x) / (double) (width - 1);
			double imaginary = imMax + ((imMin - imMax) * y) / (double) (height - 1);
			return new Complex(real, imaginary);
		}

		

		/**
		 * Calculates iteration of NewtonRahpson
		 * @param c number to calculate
		 * @return number
		 */
		private short iterationCalc(Complex c) {
			Complex current = c;
			int iter = 0;
			
			Complex division;
			Complex next;
			
			while(true) {
				division = polynomial.apply(current).divide(derivation.apply(current));
				next = current.sub(division); 
				++iter;
				if(iter >= maxIterations) break;
				if(Complex.modulSimilar(next, current, convergenceThreshold)) break;
				
				current = next;
			}
			
			return (short) (rooted.indexOfClosestRootFor(current, rootThreshold) + 1);
		}
	}

	
	
}
