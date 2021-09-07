package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/*
 * MOLBA
 * 
 * Iz nekog nepoznatog razloga, sav tekst pomaknut je prema gore za visinu dodane labele.
 * 
 * Mogao sam to popraviti tako da u potrebne metode dodam jos "- visinaLabele",
 *  ali nisam htio jer tada kod u razredu Test ne bi radio.
 * Razred Test sam prilozio da prikazem kako BarChar nacelno radi (bez iznad nadodane labele)
 * 
 * Tako da molim recenzenta da nadje gresku u kodu i obavijesti me.
 * 
 */

/**
 * Class demonstrates how BarCharComponent works.
 * Main method takes 1 argument - path to file which has informations which should be plotted as BarChart.
 * 
 * @author Hrvoje
 *
 */
public class BarChartDemo extends JFrame {
	private static final long serialVersionUID = -3951326107647542465L;

	/**
	 * Constructor for BarCharDemo.
	 * 
	 * @param model to use
	 * @param pathToFile path o file
	 */
	public BarChartDemo(BarChart model, String pathToFile) {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		JLabel path = new JLabel(pathToFile);
		path.setHorizontalAlignment(SwingConstants.CENTER);
		
		BarChartComponent barChartComponent = new BarChartComponent(model);
		
		barChartComponent.setSize(900, 400);
		barChartComponent.setPreferredSize(barChartComponent.getSize());
		
		getContentPane().add(path, BorderLayout.NORTH);
		getContentPane().add(barChartComponent, BorderLayout.CENTER);
	}
	
	/**
	 * Main method takes 1 argument - path to file which has informations which should be plotted as BarChart.
	 * 
	 * @param args path to file
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Invalid number of arguments. Only path to file should be passed.");
			return ;
		}
		
		BarChart barChart;
		
		try {
			barChart = parseBarChartFromFile(args[0]);
		} catch (FileNotFoundException e) {
			System.out.println("Cannot open file: " + args[0]);
			return ;
		} catch (Exception ex) {
			System.out.println("File format not supported.");
			return ;
		}
		
		SwingUtilities.invokeLater(() ->
		{
			JFrame frame = new BarChartDemo(barChart, args[0]);
						
			frame.pack();
			frame.setVisible(true);
			System.out.println(frame.getSize());
		});

	}

	/**
	 * Parses BarChart from file.
	 * 
	 * @param string path to file
	 * @return parsed BarChart
	 * @throws FileNotFoundException if given file doesn't exist
	 */
	private static BarChart parseBarChartFromFile(String string) throws FileNotFoundException {
		Path p = Paths.get(string);
		
		Scanner sc;
		sc = new Scanner(p.toFile());
		
		String xLabel = sc.nextLine();
		String yLabel = sc.nextLine();
		List<XYValue> values = parseValues(sc.nextLine());
		int minValue = sc.nextInt();
		int maxValue = sc.nextInt();
		int difference = sc.nextInt();
		
		sc.close();
		
		return new BarChart(values, xLabel, yLabel, minValue, maxValue, difference);
	}

	/**
	 * Parses BarChart values from String.
	 * @param line to parse
	 * @return BarChart values
	 */
	private static List<XYValue> parseValues(String line) {
		List<XYValue> result = new LinkedList<>();
		
		for(String s : line.split(" ")) {
			String[] xyValues = s.split(",");
			int x = Integer.parseInt(xyValues[0]);
			int y = Integer.parseInt(xyValues[1]);
			
			result.add(new XYValue(x, y));
		}
		
		return result;
	}

}
