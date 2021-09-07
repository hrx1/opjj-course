package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Ovaj razred sam ostavio iz razloga opisanog u BarChartsDemo
 * @author Hrvoje
 *
 */
public class Test extends JFrame {
	private static final long serialVersionUID = -4762959550268095508L;
	
	/**
	 * Constructor
	 * @param model to use
	 */
	public Test(BarChart model)  {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		BarChartComponent barChar = new BarChartComponent(model);
		
		barChar.setSize(900, 400);
		barChar.setPreferredSize(barChar.getSize());
		
		getContentPane().add(barChar, BorderLayout.CENTER);
	}

	/**
	 * Main method
	 * @param args neglected
	 */
	public static void main(String[] args) {
		BarChart model = new BarChart(
				 Arrays.asList(
				 new XYValue(1,8), new XYValue(2,20), new XYValue(3,22),
				 new XYValue(4,10), new XYValue(5,4)
				 ),
				 "Number of people in the car",
				 "Frequency",
				 0, // y-os kreće od 0
				 22, // y-os ide do 22
				 2
				);
		
		SwingUtilities.invokeLater(() ->
				{
					JFrame frame = new Test(model);
					frame.pack();
					frame.setVisible(true);
				});
	}
	
}
