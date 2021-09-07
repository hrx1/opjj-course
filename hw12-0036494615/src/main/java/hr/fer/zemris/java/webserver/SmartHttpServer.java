package hr.fer.zemris.java.webserver;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Implementation of a simple HTTP Server, with support for Cookies and simple script language (smscr).
 * All configuration data is parsed from CONFIG_ROOT_DIR. Parsing is done via Properties class.
 * 
 * SmartHTTPServer processes each request in a separate thread (with maximal number of threads given in config file).
 * It also starts one thread for accepting request, and one thread for cleaning old sessions.
 * 
 * @author Hrvoje
 *
 */
public class SmartHttpServer {
	/** IP address of web server */
	private String address;
	/** Domain name of web server */
	private String domainName;
	/** Port of web server */
	private int port;
	/** Maximal number of threads in which request are processed */
	private int workerThreads;
	/** Session timeout in seconds */
	private int sessionTimeout;
	/** Mime types */
	private Map<String, String> mimeTypes;
	/** Thread used for accepting requests */
	private ServerThread serverThread;
	/** Thread pool for processing requests */
	private ExecutorService threadPool;
	/** Thread which cleans old sessions */
	private OldSessionsCleaner sessionsCleaner;
	/** Root of web files */
	private Path documentRoot;
	
	/** Map of workers */
	private Map<String,IWebWorker> workersMap;

	/** Sessions */
	private volatile Map<String, SessionMapEntry> sessions = new HashMap<String, SmartHttpServer.SessionMapEntry>();
	/** Used for generating SID */
	private Random sessionRandom = new Random();
	
	/** Path to config files */
	private static final String CONFIG_ROOT_DIR = "./config";
	
	/** Period of cleaning old sessions in milliseconds */
	private static final long OLD_SESSIONS_CLEANING_PERIOD = 1 * 30 * 1000; //0.5 Minute(s)

	/**
	 * Starts HTTP Server
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SmartHttpServer server;
		try {
			server = new SmartHttpServer("server.properties");
		} catch (ClassNotFoundException | IOException | InstantiationException | IllegalAccessException e) {
			
			System.out.println("Error while loading server configuration.");
			e.printStackTrace();
			return ;
		}
		
		server.start();
		
		System.out.println("Server started.");
		System.out.println("Address: " + server.address);
		System.out.println("Port: " + server.port);
		System.out.println("Domain: " + server.domainName);
	}
	
	/**
	 * Constructor for SmartHttpServer. Sets up Server using configuration files.
	 * 
	 * @param configFileName main configuration file
	 * @throws IOException if one of the configuration files is not found.
	 * @throws ClassNotFoundException if Worker is not IWebWorker
	 * @throws IllegalAccessException if file cannot be opened
	 * @throws InstantiationException if Worker is not IWebWorker
	 */
	public SmartHttpServer(String configFileName) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException { 
		Properties serverProperties = new Properties();
		
		initServerProperties(serverProperties, configFileName);
		
		Path mimeConfig = Paths.get(serverProperties.getProperty("server.mimeConfig"));
		Path workersConfig = Paths.get(serverProperties.getProperty("server.workers"));
		
		loadMimes(mimeConfig);
		
		loadWorkers(workersConfig);
	}
	
	/**
	 * Fills Server Properties using data in configFileName.
	 * 
	 * @param serverProperties to fill with properties
	 * @param configFileName configuration files
	 * @throws IOException if configFileName doesn't exist or cannot be read
	 */
	private void initServerProperties(Properties serverProperties, String configFileName) throws IOException {
		serverProperties.load(Files.newBufferedReader(Paths.get(CONFIG_ROOT_DIR + "/" + configFileName)));

		address = serverProperties.getProperty("server.address");
		domainName = serverProperties.getProperty("server.domainName");
		port = Integer.parseInt(serverProperties.getProperty("server.port"));
		workerThreads = Integer.parseInt(serverProperties.getProperty("server.workerThreads"));
		documentRoot = Paths.get(serverProperties.getProperty("server.documentRoot"));
		sessionTimeout = Integer.parseInt(serverProperties.getProperty("session.timeout"));
	}
	
	/**
	 * Parses mimes from configuration file
	 * 
	 * @param mimeConfig mime configuration file
	 * @throws IOException if if mimeConfig doesn't exist or cannot be read
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadMimes(Path mimeConfig) throws IOException {
		Properties mimes = new Properties();
		mimes.load(Files.newBufferedReader(mimeConfig));
		this.mimeTypes = (Map) mimes;
	}
	
	/**
	 * Loads workers from workersConfig.
	 * 
	 * @param workersConfig configuration file for workers
	 * @throws ClassNotFoundException if worker is not found
	 * @throws IOException if configuration file doesn't exist, or worker cannot be opened
	 * @throws IllegalAccessException if cannot access to Worker
	 * @throws InstantiationException if worker is not IWebWorker
	 */
	@SuppressWarnings("deprecation")
	private void loadWorkers(Path workersConfig) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
		Properties workersTmp = new Properties();
		workersTmp.load(Files.newBufferedReader(workersConfig));
		
		workersMap = new HashMap<>();
		
		for(Entry<Object, Object> entry : workersTmp.entrySet()) {
			String path = (String) entry.getKey();
			String fqcn = (String) entry.getValue();
			
			Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
			IWebWorker iww = (IWebWorker)referenceToClass.newInstance();
			workersMap.put(path, iww);
		}

	}
	
//	private void printDebug(Object ... objects) {
//		for (Object o : objects) {
//			System.out.println(o);
//		}
//	}

	/**
	 * Starts Threads for web server, requests and cleaner.
	 */
	protected synchronized void start() {
		// … start server thread if not already running …
		// … init threadpool by Executors.newFixedThreadPool(...); …
		if(serverThread != null && serverThread.isAlive()) return ;
		
		serverThread = new ServerThread();
		serverThread.start();
		
		threadPool = Executors.newFixedThreadPool(workerThreads);
		
		sessionsCleaner = new OldSessionsCleaner();
		sessionsCleaner.start();
	}

	/**
	 * Signals server to stop running
	 */
	protected synchronized void stop() {
		// … signal server thread to stop running …
		// … shutdown threadpool …
		serverThread.interrupt();
		threadPool.shutdown();
	}

	/**
	 * ServerThread accepts new requests, and demands their processing by putting them in thread pool.
	 * 
	 * @author Hrvoje
	 *
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			
			ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(
					new InetSocketAddress((InetAddress)null, port)
				);
			} catch (IOException e) {
				System.out.println("Port unavailable? Port: " + port );
				e.printStackTrace();
				return ; //TODO stop vrsni objekt
			}
			
			while(true) {
				Socket client;
				try {
					client = serverSocket.accept();
				} catch (IOException ignorable) {
					System.out.println("Client communication not possible.");
					return ;
				}
				ClientWorker cw = new ClientWorker(client);
				threadPool.submit(cw);
			}
		}
	}

	/**
	 * Class defines how Clinet Request is processed.
	 * 
	 * @author Hrvoje
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		/** Client socket */
		private Socket csocket;
		/** Stream coming from client */
		private PushbackInputStream istream;
		/** Stream heading to client */
		private OutputStream ostream;
		/** HTML Version */
		private String version;
		/** Requested method */
		private String method;
		/** Requested host */
		private String host;
		/** Parameters */
		private Map<String, String> params = new HashMap<String, String>();
		/** Temporary parameters */
		private Map<String, String> tempParams = new HashMap<String, String>();
		/** Permanent parameters */
		private Map<String, String> permParams;
		/** Cookies included in output header */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		
		/** Client SID */
		private String SID;
		/** Generates output content */
		private RequestContext context = null;

		/**
		 * Constructor for ClientWorker
		 * @param csocket client socket
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		/**
		 * Processes ClientWorker request
		 */
		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = new BufferedOutputStream(csocket.getOutputStream());
			} catch (IOException ignorable) {
			}

			List<String> headers = extractHeaders(istream); // TODO uhvati iznmku

			String[] firstLine = headers.isEmpty() ? null : headers.get(0).split(" ");
			
			if (firstLine == null || firstLine.length != 3) {
				sendError(ostream, 400, "Bad request");
				return;
			}
			
			method = firstLine[0].toUpperCase();
			String requestedPath = firstLine[1]; //npr., /index.html
			version = firstLine[2].toUpperCase();
			
			if(!method.equals("GET") || 
					!version.equals("HTTP/1.1") && !version.equals("HTTP/1.0")) {
				sendError(ostream, 400, "Bad request");
				return ;
			}
			
			host = parseHost(headers);
			
			String[] splitTmp = requestedPath.split("\\?");
			String path = splitTmp[0];
			String paramString = (splitTmp.length > 1) ? splitTmp[1] : null;
			
			checkSession(headers);
			outputCookies.add(new RCCookie("sid", SID, null, host, "/"));
			
			parseParameters(paramString);			
			
			try {
				internalDispatchRequest(path, true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				istream.close();
				ostream.close();
			} catch (IOException ignorable) {
			}
			
		}

		/**
		 * Sets current Session depending on whether current client has valid old session.
		 * If old session doesn't exist or it isn't valid it creates new one.
		 * 
		 * @param headers header sent from client
		 */
		private synchronized void checkSession(List<String> headers) {
			String tmpSID = null;
			tmpSID = getSID(getCookieHeader(headers));
			
			SessionMapEntry session = sessions.get(tmpSID);
			
			if (tmpSID == null || session == null || !session.host.equals(host)) {
				tmpSID = generateSID();
				session = SessionMapEntry.createNewCurrentSession(tmpSID, host, sessionTimeout);
				
			} else if(session.isExpired()) {
				sessions.remove(tmpSID);
				tmpSID = generateSID();
				session = SessionMapEntry.createNewCurrentSession(tmpSID, host, sessionTimeout);
			
			} else {
				session.validUntil = System.currentTimeMillis() + sessionTimeout * 1000;
			}
			
			SID = tmpSID;
			sessions.put(tmpSID, session);
			permParams = session.map;
			return ;
		}
		
		/**
		 * Returns Header line where Cookies are defined. 
		 * Null if it doesn't exist.
		 * 
		 * @param headers list of header lines
		 * @return header line where cookies are defined, null if it doesn't exist.
		 */
		private String getCookieHeader(List<String> headers) {
			for(String header : headers) {
				if(!header.startsWith("Cookie: ")) continue;
				
				return header;
			}
			return null;
		}
		
		/**
		 * Parses SID from header. Returns null if no valid SID can be parsed.
		 * Valid SID has 20 characters.
		 * 
		 * @param header String to parse SID from
		 * @return parses SID from header, or null if no valid SID can be parsed
		 */
		private String getSID(String header) {
			if(header == null) return null;
			
			header = header.substring("Cookie: ".length());
			String[] cookies = header.split(";");
			
			for(String cookie : cookies) {
				if(cookie.startsWith("sid=")) {
					String splitted = cookie.split("=")[1];
					if(splitted.length() != 22) continue;
					return splitted.substring(1, 21);
				}
			}
			return null;
		}
 		
		/**
		 * Generates SID, 20 upper-case letters
		 * @return SID
		 */
		private String generateSID() {
			StringBuilder sb = new StringBuilder(21);
			int range = (int) 'Z' - (int) 'A' + 1;
			for(int i = 0; i < 20; ++i) {
				char c = (char) (sessionRandom.nextInt(range) + (int) 'A');
				sb.append((char)c);
			}
			return sb.toString();
		}
		
		/**
		 * Returns extension of a given string.
		 * 
		 * @param string to parse
		 * @return extension of a given string
		 */
		private String parseExtension(String string) {
			String[] tmpSplit = string.split("\\.");
			return tmpSplit[tmpSplit.length - 1];
		}
		
		/**
		 * Stores parameters to parameters map
		 * 
		 * @param paramString to parse
		 */
		private void parseParameters(String paramString) {
			if(paramString == null) return;
			for(String param : paramString.split("&")) {
				String[] p = param.split("=");
				params.put(p[0], p[1]);
			}
		}

		/**
		 * Returns host given in headers, domainName if it doesn't exist.
		 * @param headers
		 * @return host given in headers, domainName if it doesn't exist.
		 */
		private String parseHost(List<String> headers) {
			for(String line : headers) {
				if(line.startsWith("Host: ")) {
					return line.split(":")[1].trim();
				}
			}
			return domainName;
		}

		/**
		 * Returns list of headers from inputStream.
		 * 
		 * @param inputStream input stream
		 * @return list of headers from inputStream
		 * @throws HeaderParseException if header cannot be read
		 */
		private List<String> extractHeaders(InputStream inputStream) throws HeaderParseException {
			byte[] request;
			try {
				request = readRequest(istream);
			} catch (IOException e) {
				throw new HeaderParseException();
			}
			if (request == null) {
				throw new HeaderParseException();
			}
			return extractHeaders(request);
		}

		/**
		/**
		 * Returns list of headers from request.
		 * 
		 * @param request request
		 * @return list of headers from inputStream
		 * @throws HeaderParseException if request cannot be read
		 */
		private List<String> extractHeaders(byte[] request) throws HeaderParseException {
			String requestHeader = new String(request, StandardCharsets.US_ASCII);

			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for (String s : requestHeader.split("\n")) {
				if (s.isEmpty())
					break;
				char c = s.charAt(0);
				if (c == 9 || c == 32) {
					currentLine += s;
				} else {
					if (currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if (!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}

		/**
		 * Reads request from input stream
		 * 
		 * @param inputStream input stream
		 * @return request
		 * @throws IOException if input stream cannot be read. 
		 */
		private byte[] readRequest(InputStream inputStream) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			l: while (true) {
				int b = inputStream.read();
				if (b == -1)
					return null;
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0:
					if (b == 13) {
						state = 1;
					} else if (b == 10)
						state = 4;
					break;
				case 1:
					if (b == 10) {
						state = 2;
					} else
						state = 0;
					break;
				case 2:
					if (b == 13) {
						state = 3;
					} else
						state = 0;
					break;
				case 3:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				case 4:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				}
			}
			return bos.toByteArray();
		}

		/**
		 * Dispatches request. Decides whether script or regular call is made.
		 * 
		 * @param path given in link
		 * @param directCall is direct call
		 * @throws Exception if error occurs
		 */
		public void internalDispatchRequest(String path, boolean directCall) throws Exception {
			Path rootPath = documentRoot.toAbsolutePath().normalize();
			Path requestedFile = rootPath.resolve(path.toString().substring(1));
			
			if (path.startsWith("/private/")) {
				if(directCall) {
					sendError(ostream, 404, "File not found");
					return ;
				}
				
				if(context == null) {
					context = new RequestContext(ostream, params, permParams, outputCookies, tempParams, this);
				}
				
				scriptRequest(requestedFile);
				return ;
			}
			
			
			if(context == null) {
				context = new RequestContext(ostream, params, permParams, outputCookies, tempParams, this);
			}
			
			//Vidi je li vanjski webworker
			if(path.startsWith("/ext/")) {
				externalWorkerRequest(requestedFile, path);
				return ;
			}
			
			//Vidi je li predefinirani webworker
			IWebWorker worker = workersMap.get(path);
			
			if(worker != null) {
				worker.processRequest(context);
				return ;
			}

			//vidi postoji li
			if(!requestedFile.startsWith(rootPath) 
					|| !Files.isReadable(requestedFile) 
					|| !Files.isRegularFile(requestedFile)) {
				sendError(ostream, 404, "File not found");
				return ;
			}
			
			
			String extension = parseExtension(requestedFile.getFileName().toString());
			
			if(extension.equals("smscr")) {
				scriptRequest(requestedFile);
				return ;
			}
			else {
				regularRequest(requestedFile, extension);
				return ;
			}
			
		}
		
		/**
		 * Processes external worker request.
		 * 
		 * @param requestedFile request
		 * @param path
		 */
		@SuppressWarnings("deprecation")
		private void externalWorkerRequest(Path requestedFile, String path) {
			Class<?> referenceToClass;
			try {
				referenceToClass = this.getClass().getClassLoader().loadClass("hr.fer.zemris.java.webserver.workers." + path.substring("/ext/".length()));
			} catch (ClassNotFoundException e) {
				sendError(ostream, 404, "No such file " + path);
				return ;
			}
			
			Object newObject;
			try {
				newObject = referenceToClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				sendError(ostream, 404, "No such file");
				return ;
			}
			IWebWorker iww = (IWebWorker)newObject;
			try {
				iww.processRequest(context);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Processes script request
		 * 
		 * @param requestedScriptPath
		 */
		private void scriptRequest(Path requestedScriptPath) {
			String script;
			try {
				script = new String(Files.readAllBytes(requestedScriptPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ;
			}
			
			new SmartScriptEngine(
					new SmartScriptParser(script).getDocumentNode(),
					context
				).execute();
		}
		
		/**
		 * Processes regular request
		 * 
		 * @param requestedFile
		 * @param extension
		 */
		private void regularRequest(Path requestedFile, String extension) {
			String mimeType = mimeTypes.get(extension);
			
			if(mimeType == null) mimeType = "application/octet-stream";
			context.setMimeType(mimeType);
			context.setStatusCode(200);
			
			try {
				context.write(Files.readAllBytes(requestedFile));
			} catch (IOException e) {
			}			
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}
	}

	/**
	 * Sends error message with status code and text to Output stream, 
	 * 
	 * @param cos Output stream
	 * @param statusCode status code
	 * @param statusText text
	 */
	private static void sendError(OutputStream cos, int statusCode, String statusText) {
		try {
		cos.write(
				("HTTP/1.1 "+statusCode+" "+statusText+"\r\n"+
						"Server: simple java server\r\n"+
						"Content-Type: text/plain;charset=UTF-8\r\n"+
						"Content-Length: 0\r\n"+
						"Connection: close\r\n"+
						"\r\n").getBytes(StandardCharsets.US_ASCII)
					);
		cos.flush();
		}catch (IOException e) {
			System.out.println("Cannot send error: " + statusCode + " " + statusText);
		}
	}
	
	/**
	 * SessionMapEntry defines values of one User Session.
	 * Entry is defined with it's SID, host, validity duration, and permanent parameters of a session.
	 * 
	 * @author Hrvoje
	 *
	 */
	private static class SessionMapEntry {
		/** SID */
		String sid;
		/** Host */
		String host;
		/** In milliseconds */
		long validUntil;
		/** Session permanent parameters */
		Map<String, String> map;
		
		/**
		 * Constructor for SessionMapEntry
		 * 
		 * @param sid SID
		 * @param host host
		 * @param validUntil valid until (in milliseconds)
		 * @param map of permanent parameters
		 */
		public SessionMapEntry(String sid, String host, long validUntil, Map<String, String> map) {
			super();
			this.sid = sid;
			this.host = host;
			this.validUntil = validUntil;
			this.map = map;
		}
		
		/**
		 * Returns true if session expired.
		 * @return true if session expired
		 */
		public boolean isExpired() {
			return validUntil <= System.currentTimeMillis();
		}
		
		/**
		 * Creates new current session valid for sessionTimeout seconds.
		 * 
		 * @param tmpSID SID
		 * @param host Host
		 * @param sessionTimeout Session Timeout (in seconds)
		 * @return new current session valid for sessionTimeout seconds
		 */
		public static SessionMapEntry createNewCurrentSession(String tmpSID, String host, long sessionTimeout) {
			return new SessionMapEntry(tmpSID, host, System.currentTimeMillis() + sessionTimeout * 1000, new ConcurrentHashMap<>());
		}

	}
	
	/**
	 * Daemon Thread which cleans Old sessions each OLD_SESSIONS_CLEANING_PERIOD milliseconds.
	 * 
	 * @author Hrvoje
	 *
	 */
	private class OldSessionsCleaner extends Thread {
		
		/**
		 * Constructor for OldSessionsCleaner
		 */
		public OldSessionsCleaner() {
			setDaemon(true);
		}
		
		@Override
		/**
		 * Cleans Old sessions each OLD_SESSIONS_CLEANING_PERIOD milliseconds
		 */
		public synchronized void run() {
			while(true) {
				try {
					sleep(OLD_SESSIONS_CLEANING_PERIOD);
				} catch (InterruptedException ignorable) {
				}
				for (SessionMapEntry session : sessions.values()) {
					if (session.isExpired()) {
						sessions.remove(session.sid);
					}
				}
			}
		}
		
	}
}