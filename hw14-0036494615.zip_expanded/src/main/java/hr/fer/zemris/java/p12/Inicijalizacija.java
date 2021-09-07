package hr.fer.zemris.java.p12;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.zemris.java.p12.dao.sql.SQLConnectionProvider;
import hr.fer.zemris.java.p12.model.VoteOption;

/**
 * Initializes Application.
 * 
 * @author Hrvoje
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {
	
	/** Connection Pool Key */
	public static final String poolKey = "hr.fer.zemris.dbpool";
	
	/** Config files */
	private static final String derbyDriver = "org.apache.derby.jdbc.ClientDriver";
	private static final String configFile = "/WEB-INF/dbsettings.properties";
	
	/** Name of polls Table - UPPERCASE NEEDED */
	public static final String pollsTable = "POLLS";
	/** Name of pollOptions Table - UPPERCASE NEEDED */
	public static final String pollOptions = "POLLOPTIONS";
	
	/** Default poll values */
	private static final String defaultPollSrc = "/WEB-INF/glasanje-definicija.txt";
	private static final String defaultPollTitle = "Glasanje za omiljeni bend:";
	private static final String defaultPollMessage = "Daj glas omiljenom bendu!";
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		try {
			poolSetUp(sce);
		} catch (PropertyVetoException | IOException e1) {
			e1.printStackTrace();
			System.err.println(e1.getLocalizedMessage());
			System.exit(-1);
		} 
		
		Connection con = null;
		DataSource ds = (DataSource)sce.getServletContext().getAttribute(poolKey);
		
		try {
			con = ds.getConnection();
		} catch (SQLException e) {}
		
		SQLConnectionProvider.setConnection(con);
		
		try {
			initializeTables(con);
			checkAndSetPoll(con,
							defaultPollTitle, 
							sce.getServletContext().getRealPath(defaultPollSrc),
							defaultPollMessage
						);
			
		} catch (IOException | SQLException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(-1);
		}
		
		SQLConnectionProvider.setConnection(null);
		try { con.close(); } catch(SQLException ignorable) {}
	}

	/**
	 * Creates new poll if doesn't exist. Fills it with data from pollDefinitionSrc.
	 * 
	 * @param con Connection
	 * @param pollTitle Poll title
	 * @param pollDefinitionSrc Source of Poll definition
	 * @throws SQLException can be ignored
	 */
	private static void checkAndSetPoll(Connection con, String pollTitle, String pollDefinitionSrc, String pollMsg) throws SQLException {
		int pollID = getPollID(con, pollTitle);
		
		if(pollID == -1) {
			//create poll, return its id
			//set pollID
			PreparedStatement ps = con.prepareStatement(
					"INSERT INTO " + pollsTable + "(title, message) VALUES (?, ?)"
					);
			ps.setString(1, pollTitle);
			ps.setString(2, pollMsg);
			ps.execute();
			ps.close();
			
			pollID = getPollID(con, pollTitle);
		}

		
		if(pollEmpty(con, pollID)) {
			List<VoteOption> voteOptions;
			try {
				voteOptions = VoteOption.parseVoteOptions(pollDefinitionSrc);
			} catch (IOException e) {
				e.printStackTrace();
				return ;
			}
			
			for(VoteOption option : voteOptions) { //Sto ako neki unos vec postoji? Nista! Kad brises poll, obavezno brisi i sve njegove foreign keyove
				PreparedStatement ps = con.prepareStatement("INSERT INTO " + pollOptions +
						" (optionTitle, optionLink, pollID, votesCount) VALUES (?, ?, ?, ?)");
				ps.setString(1, option.getName());
				ps.setString(2, option.getDescription());
				ps.setInt(3, pollID);
				ps.setInt(4, 0);
				ps.execute();
				ps.close();
			}
		}
		
	}

	/**
	 * Returns true if poll is empty.
	 * 
	 * @param con Connection
	 * @param pollID Poll ID
	 * @return true if poll is empty.
	 * @throws SQLException if SQL throws 
	 */
	private static boolean pollEmpty(Connection con, int pollID) throws SQLException {
		try(ResultSet rs = con.createStatement()
				.executeQuery("select count(*) from " + pollOptions + " where POLLID=" + pollID)) {
			rs.next();
			return rs.getInt(1) == 0;
		}
	}

	/**
	 * Returns Poll ID
	 * 
	 * @param con Connection
	 * @param pollTitle Poll Title
	 * @return Poll ID
	 * @throws SQLException if SQL connection cannot be made
	 */
	private static int getPollID(Connection con, String pollTitle) throws SQLException {
	    try (ResultSet rs = con.createStatement().executeQuery("select ID, title from " + pollsTable)) {
	    	while (rs.next()) {
	    		String title = rs.getString("title");
	    		if(pollTitle.equals(title)) {
	    			return rs.getInt("ID");
	    		}
	        }
	    }
	    return -1;
	}

	/**
	 * Initializes tables used for storing data about Polls and Voting Options
	 * 
	 * @param con Connection
	 * @throws IOException if table cannot be made
	 * @throws SQLException if table cannot be made
	 */
	private static void initializeTables(Connection con) throws IOException, SQLException {
		Statement createTables = con.createStatement();
		
		if(!tableExist(con, pollsTable)) {
			createTables.execute(
						" CREATE TABLE " + pollsTable + "\n" + 
						" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" + 
						" title VARCHAR(150) NOT NULL,\n" + 
						" message CLOB(2048) NOT NULL\n" + 
						")"
					);
		}
		
		if(!tableExist(con, pollOptions)) { 
			createTables.execute("CREATE TABLE " + pollOptions + "\n" + 
					" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" + 
					" optionTitle VARCHAR(100) NOT NULL,\n" + 
					" optionLink VARCHAR(150) NOT NULL,\n" + 
					" pollID BIGINT,\n" + 
					" votesCount BIGINT,\n" + 
					" FOREIGN KEY (pollID) REFERENCES Polls(id)\n" + 
					") ");
		}
		
		createTables.close();
				
	}

	/**
	 * Sets Up Pooled Data Source.
	 * 
	 * @param sce Servlet Context Event
	 * @throws PropertyVetoException if invalid property is given
	 * @throws IOException never
	 */
	private static void poolSetUp(ServletContextEvent sce) throws PropertyVetoException, IOException {
		
		Properties konfiguracija = new Properties();
		
		konfiguracija.load(Files.newInputStream(Paths.get(sce.getServletContext().getRealPath(configFile))));
		
		// Priprema pool-a
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass(derbyDriver);

		cpds.setJdbcUrl(parseURL(konfiguracija));

		cpds.setUser((String)konfiguracija.get("user"));
		cpds.setPassword((String)konfiguracija.get("password"));

		cpds.setInitialPoolSize(5);
		cpds.setMinPoolSize(5);
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);

		sce.getServletContext().setAttribute(poolKey, cpds);
		
	}

	/** 
	 * Creates URL from Properties file.
	 * 
	 * @param konfiguracija Properties file
	 * @return URL
	 */
	private static String parseURL(Properties konfiguracija) {
		return "jdbc:derby://" + 
				(String)konfiguracija.get("host") + 
				":" + 
				(String)konfiguracija.get("port") + 
				"/" + 
				(String)konfiguracija.get("name") ;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute(poolKey);
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * Returns true if table with table name exists.
	 * 
	 * @param conn Connection 
	 * @param tableName Table Name
	 * @return true if table exists
	 * @throws SQLException if connection cannot be made
	 */
	private static boolean tableExist(Connection conn, String tableName) throws SQLException { //TODO BOLJE
	    try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
	    	while (rs.next()) {
	            String tName = rs.getString("TABLE_NAME");
	            if (tName != null && tName.equals(tableName)) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
}



