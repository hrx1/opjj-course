package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.OptionalInt;

import javax.swing.JComponent;

/**
 * Class describes BarChartComponent
 * @author Hrvoje
 *
 */
public class BarChartComponent extends JComponent {
	private static final long serialVersionUID = 2344530505005214875L;
	
	/** Bar Chart */
	private BarChart barChart;
	/** Number of columns */
	private int columnNumber;
	/** Number of rows */
	private int rowNumber;
	
	/** Margin caused by X-axis description and values */
	int marginX;
	/** Margin caused by Y-axis description and values */
	int marginY;
	/** Column length */
	double columnLength;
	/** Row length */
	double rowLength;
	/** Height */
	int height;
	/** Width */
	int width;
	
	/** Bottom left tail */
	private static final int tailStart = 10;
	/** Top right tail */
	private static final int tailEnd = 10;
	/** Fixed margin */
	private static final int fixMargin = 5;

	/**
	 * Constructor for BarCharComponent
	 * @param barChart 
	 */
	public BarChartComponent(BarChart barChart) {
		this.barChart = barChart;
		
		columnNumber = barChart.getValues().size();
		rowNumber = (barChart.getMaxY() - barChart.getMinY())/barChart.getDifferenceY() + 1; //TODO +1
	}
	
	@Override
	public void paintComponent(Graphics g) {
		//ovisi o tekstu
		marginX = getMarginX(g);
		marginY = getMarginY(g);
		
		height = getHeight();
		width = getWidth();
		
		columnLength = (width - marginY - tailStart - tailEnd) * (1.) / columnNumber;
		rowLength = (height - marginX - tailStart - tailEnd) * (1.) / (rowNumber - 1);
		
		drawGrid(g, new Color(179, 207, 214), Color.BLACK);
		g.setColor(new Color(193, 56, 86));
		for(XYValue xyValue : barChart.getValues()) {
			drawBar(g, xyValue);
		}
		g.setColor(new Color(0, 0, 0));
		drawDescription(g);
		drawXValues(g);
		drawYValues(g);
	}

	/**
	 * Draws grid on Graphics
	 * 
	 * @param g graphics to draw on
	 * @param marginX because X-axis values and description takes place
	 * @param marginY because Y-axis values and description takes place
	 * @param tailStart TODO
	 * @param columnLength column length
	 * @param tailEnd TODO
	 */
	private void drawGrid(Graphics g, Color grid, Color axis) {
		//axis
		g.setColor(axis);
		// y - axis
		double x = marginY + tailStart;
		g.drawLine((int)Math.round(x), height - marginX,(int) Math.round(x), 0);
		
		// y arrow
		Polygon triangleY = new Polygon();
		triangleY.addPoint(marginY + tailStart, 0);
		triangleY.addPoint(7*(marginY + tailStart)/8, tailEnd/2);
		triangleY.addPoint(9*(marginY + tailStart)/8, tailEnd/2);
		
		g.fillPolygon(triangleY);
		
		// x - axis
		double y = height - (marginX + tailStart);
		g.drawLine(marginY,(int) Math.round(y), width,(int) Math.round(y));
		
		// x arrow
		Polygon triangleX = new Polygon();
		triangleX.addPoint(width, height - (marginX + tailStart));
		triangleX.addPoint(width - tailEnd/2, height - (marginX + tailStart) - tailStart/2);
		triangleX.addPoint(width - tailEnd/2, height - (marginX + tailStart) + tailStart/2);
		
		g.fillPolygon(triangleX);

		
		//draw vertical lines
		g.setColor(grid);
		for(x+= columnLength; x <= width - tailEnd; x += columnLength) {
			g.drawLine((int)Math.round(x), height - marginX,(int) Math.round(x), 0);
		}
		
		//horizontal lines
		for(y -= rowLength; y >= tailEnd -1; y -= rowLength) {
			g.drawLine(marginY,(int) Math.round(y), width,(int) Math.round(y));
		}
	}
	
	/**
	 * Draws Bar with XYValue to Graphics
	 * @param g Graphics
	 * @param xyValue XYValue
	 */
	private void drawBar(Graphics g, XYValue xyValue) {
		int barHeight = (int) Math.round((xyValue.getY() - barChart.getMinY()) * ((rowNumber - 1)  * rowLength) /
				(barChart.getMaxY() - barChart.getMinY()));
		int x = marginY + tailStart + (int) Math.round((xyValue.getX() - 1) * columnLength);
		int y = height - (marginX + tailStart) - barHeight;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.fill(new Rectangle(x, y, (int) Math.round(columnLength) - 2, barHeight));
	}
	
	/**
	 * Margin on the X - axis allows axis description and values to be written without overlapping with each other or other components.
	 * @return required margin
	 */
	private int getMarginX(Graphics g) {
		return 2 * (fixMargin + g.getFontMetrics().getHeight());
	}
	
	/**
	 * Margin on the Y - axis allows axis description and values to be written without overlapping with each other or other components.
	 * @return required margin
	 */
	private int getMarginY(Graphics g) {
		OptionalInt maxNumberSize = barChart.getValues().stream()
														.mapToInt(o -> g.getFontMetrics()
																.stringWidth(String.valueOf(o.getY())))
														.max();
		return g.getFontMetrics().getHeight() + maxNumberSize.getAsInt() + 2 * fixMargin;
	}
	
	/**
	 * Draws description on Graphics
	 * @param g Graphics
	 */
	private void drawDescription(Graphics g) {
		//crtaj x
		int sWidth = g.getFontMetrics().stringWidth(barChart.getxAxisDescription());
		int x = (width - sWidth) / 2;
		int y = height - g.getFontMetrics().getDescent() + 1;
		
		g.drawString(barChart.getxAxisDescription(), x, y);
		
		//crtaj y
		sWidth = g.getFontMetrics().getHeight();
		x = g.getFontMetrics().getAscent();
		y = (height - sWidth) / 2;
		
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);
		
		g2.setTransform(at);
		g2.drawString(barChart.getyAxisDescription(), -y, x);
		
		//vrati na staro
		at.rotate(Math.PI / 2);
		g2.setTransform(at);
	}
	
	/**
	 * Draws X values to Graphics
	 * @param g Graphics
	 */
	private void drawXValues(Graphics g) {
		int y = height - (fixMargin + g.getFontMetrics().getHeight());
		double xColumn = marginY + tailStart;
		
		for(XYValue xyValue : barChart.getValues()) {
			String value = String.valueOf(xyValue.getX());
			int x = (int) (Math.round((columnLength - g.getFontMetrics().stringWidth(value))/2 + xColumn));
			g.drawString(value, x, y);
			xColumn += columnLength;
		}
		
	}

	/**
	 * Draws Y values to Graphics
	 * @param g Graphics
	 */
	private void drawYValues(Graphics g) {
		double y = height - (marginX + tailStart) + g.getFontMetrics().getDescent();
		for(int value = barChart.getMinY(); value <= barChart.getMaxY(); value += barChart.getDifferenceY()) {
			int x = marginY - g.getFontMetrics().stringWidth(String.valueOf(value)) - fixMargin;
			g.drawString(String.valueOf(value), x, (int) Math.round(y));
			y -= rowLength; 
		}
	}
}
