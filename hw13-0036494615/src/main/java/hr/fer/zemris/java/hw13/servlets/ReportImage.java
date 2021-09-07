package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;


/**
 * Servlet responsible for generating report about OS usage.
 * Outputs result chart as PNG in HttpServlet Response output stream.
 * PNG is has width and height of 200px.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/reportImage")
public class ReportImage extends HttpServlet {
	private static final long serialVersionUID = 4189345324687275293L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JFreeChart chart = getPredefinedChart();
		int width = 200, height = 200;
		
		resp.setContentType("image/png");
		ChartUtils.writeChartAsPNG(resp.getOutputStream(), chart, width, height);
		
	}

	/**
	 * Returns predefined chart.
	 * 
	 * @return predefined chart
	 */
	private JFreeChart getPredefinedChart() {
        PieDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset, "OS usage");
        return chart;
	}
	
	/**
	 * Creates database.
	 * 
	 * @return database
	 */
    private  PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Linux", 100);
        result.setValue("Mac", 10);
        result.setValue("Windows", 5);
        return result;
    }
    
    /**
     * Creates Chart with defined data set and title
     * 
     * @param dataset of values in a chart
     * @param title of a chart
     * @return chart
     */
    private JFreeChart createChart(PieDataset dataset, String title) {

        JFreeChart chart = ChartFactory.createPieChart3D(
            title,                  // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;

    }


	
}
