package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.List;

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

import hr.fer.zemris.java.hw13.servlets.GlasanjeRezultatiServlet.VoteResult;

/**
 * Servlet outputs Results of voting represented as Chart to response output stream.
 * Results are obtained through GlasanjeRezultatiServlet.resultKey.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/glasanje-grafika")
public class GlasanjeGrafika extends HttpServlet {
	
	private static final long serialVersionUID = 5164336793091491077L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		@SuppressWarnings("unchecked")
		JFreeChart chart = createChart((List<VoteResult>) req.getServletContext().getAttribute(GlasanjeRezultatiServlet.resultKey));
		
		int width = 400, height = 400;
		resp.setContentType("image/png");
		ChartUtils.writeChartAsPNG(resp.getOutputStream(), chart, width, height);
	}

	/**
	 * Creates chart from given results.
	 * 
	 * @param results results
	 * @return chart
	 */
	private JFreeChart createChart(List<VoteResult> results) {
        PieDataset dataset = createDataset(results);
        JFreeChart chart = createChart(dataset, "OS usage");
        return chart;
	}
	
	/**
	 * Creates Data set from results.
	 * 
	 * @param results from which to parse data set
	 * @return Pie Data set
	 */
    private PieDataset createDataset(List<VoteResult> results) {
        DefaultPieDataset result = new DefaultPieDataset();
        
        for(VoteResult vr : results) {
        	result.setValue(vr.name, Integer.parseInt(vr.numberOfVotes));
        }
        
        return result;
    }
    
    /**
	 * Creates chart from given data set and title.
	 * 
	 * @param dataset of chart
	 * @param title of chart
	 * @return chart
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;

    }

}
