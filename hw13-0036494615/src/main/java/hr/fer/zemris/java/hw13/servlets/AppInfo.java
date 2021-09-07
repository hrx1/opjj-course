package hr.fer.zemris.java.hw13.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

/**
 * Stores start time of a Application under timeStartKey in ServletContext attribute.
 * 
 * @author Hrvoje
 *
 */
@WebServlet("/appinfo")
@WebListener
public class AppInfo implements ServletContextListener{
	/** Key of a result */
	public static final String timeStartKey = "timeStart";
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute(timeStartKey, System.currentTimeMillis());
	}

}
