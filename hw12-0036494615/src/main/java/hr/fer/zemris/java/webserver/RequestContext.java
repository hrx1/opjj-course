package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** 
 * RequestContext generates message sent to outputStream. 
 * Message contains HTTP Header (defined by HTTP Standard and list of cookies), and message written with write methods.
 * 
 * 	@author Hrvoje
 */
public class RequestContext {
	/** Output Stream */
	private OutputStream outputStream;
	/** Charset */
	private Charset charset;
	/** Encoding */
	private String encoding = "UTF-8"; //W only
	/** Status code */
	private int statusCode = 200;
	/** Status text */
	private String statusText = "OK";
	/** Mime type */
	private String mimeType = "text/html";
	
	/** Variable maps */
	private Map<String, String> parameters; //R only G
	private Map<String, String> temporaryParameters; //RW G 
	private Map<String, String> persistentParameters; //RW G
	/** Output Cookies */
	private List<RCCookie> outputCookies;
	/** Header Generated Flag */
	private boolean headerGenerated = false;
	/** Request dispatcher */
	private IDispatcher dispatcher; //R only
	
	/** Defined by the HTTP Standard */
	@SuppressWarnings("unused")
	private static final Charset headerEncoding = StandardCharsets.ISO_8859_1;
	
	/**
	 * Constructor for Request Context.
	 * OutputStream cannot be <code>null</code>, other parameters can and they will be treated as empty sets.
	 * 
	 * @param outputStream Output Stream
	 * @param parameters Parameters
	 * @param persistentParameters Persistent Parameters
	 * @param outputCookies Output Cookies
	 * 
	 * @throws NullPointerException if outputStream is <code>null</code>
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {

		Objects.requireNonNull(outputStream);
		
		this.outputStream = outputStream;
		this.parameters = (parameters == null)? new HashMap<>() : parameters;
		this.persistentParameters = (persistentParameters == null)? new HashMap<>() : persistentParameters;
		this.outputCookies = (outputCookies == null) ? new LinkedList<>() : outputCookies;
		
		this.temporaryParameters = new HashMap<>();
		
	}
	
	/**
	 * Map<String,String> temporaryParameters and IDispatcher dispatcher. This
	 * constructor should not make a copy of given temporary parameters map but
	 * instead use the given map. Refactor both constructors so that code
	 * duplication is avoided.
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies, 
			Map<String,String> temporaryParameters, IDispatcher dispatcher) {
		this(outputStream, parameters, persistentParameters, outputCookies);
		
		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;
	}
	
	/**
	 * Returns parameter stored under key 'name'. Returns <code>null</code> if no value exists.
	 * @param name key of a parameter
	 * @return parameter stored under key 'name'. Returns <code>null</code> if no value exists.
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * Returns read-only set of names of all parameters in parameters map.
	 * @return read-only set of names of all parameters in parameters map
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}
	
	/**
	 * Returns value from persistentParameters map (or null if no association exists).
	 * 
	 * @param name Key of value in map
	 * @return value from persistentParameters map (or null if no association exists)
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	
	/**
	 * Returns read-only set of names of all parameters in persistent parameters map.
	 * @return read-only set of names of all parameters in persistent parameters map
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}
	
	/**
	 * Stores a Key-Value pair to persistentParameters map
	 * @param name Key
	 * @param value Value
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	/**
	 * Removes Key from persistent parameter map.
	 * @param name Key
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	/**
	 * Returns value from temporary parameters map (or null if no association exists).
	 * 
	 * @param name Key of value in map
	 * @return value from temporary parameters map (or null if no association exists)
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	/**
	 * Returns read-only set of names of all parameters in temporary parameters map.
	 * @return read-only set of names of all parameters in temporary parameters map
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	/**
	 * Stores a value to temporaryParameters map.
	 * 
	 * @param name Key
	 * @param value Value
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}
	
	/**
	 * Removes Key from temporary parameter map.
	 * @param name Key
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	/**
	 * Setter for Encoding
	 * @param encoding encoding
	 * @throws RuntimeException if Header is generated
	 */
	public void setEncoding(String encoding) {
		checkHeaderGenerated("Encoding");
		this.encoding = encoding;
	}

	/**
	 * Setter for status code
	 * @param statusCode satus code
	 * @throws RuntimeException if Header is generated
	 */
	public void setStatusCode(int statusCode) {
		checkHeaderGenerated("Status Code");
		this.statusCode = statusCode;
	}

	/**
	 * Setter for status text
	 * @param statusText status text
	 * @throws RuntimeException if Header is generated
	 */
	public void setStatusText(String statusText) {
		checkHeaderGenerated("Status Text");
		this.statusText = statusText;
	}

	/**
	 * Setter for mime type
	 * @param mimeType
	 * @throws RuntimeException if Header is generated
	 */
	public void setMimeType(String mimeType) {
		checkHeaderGenerated("Mime Type");
		this.mimeType = mimeType;
	}
	
	/**
	 * Adds cookie to list of cookes
	 * @param cookie to add
	 * @throws RuntimeException if Header is generated
	 */
	public void addRCCookie(RCCookie cookie) {
		checkHeaderGenerated("RCCookie list");
		outputCookies.add(cookie);
	}
	
	/**
	 * Getter for dispatcher
	 * @return dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}
	
	/**
	 * Throws {@link RuntimeException} if header is generated.
	 * 
	 * @param member for which method is called
	 * @throws RuntimeException if Header is generated
	 */
	private void checkHeaderGenerated(String member) {
		if (headerGenerated) throw new RuntimeException(
				"Cannot modify " + member + 
				" if header is already generated");
	}

	/**
	 * Writes data to output stream
	 * @param data to write
	 * @return this
	 * @throws IOException if cannot write
	 */
	public RequestContext write(byte[] data) throws IOException {
		if(!headerGenerated) {
			writeBytes(generateHeader().getBytes(charset));
		}
		writeBytes(data);
		return this; //TODO
	}
	
	/**
	 * Writes text to output stream
	 * @param text to write
	 * @return this
	 * @throws IOException if cannot write
	 */
	public RequestContext write(String text) throws IOException {
		if(!headerGenerated) {
			writeBytes(generateHeader().getBytes(charset));
		}
		return write(text.getBytes(charset));
	}
	
	/**
	 * Writes bytes to output stream without checking is header generated.
	 * @param data to write
	 * @throws IOException if cannot perform write
	 */
	private void writeBytes(byte[] data) throws IOException {
		outputStream.write(data);
		outputStream.flush();
	}
	
	/**
	 * Returns string representation of header. Properties used for header construction are
	 * encoding, statusCode, statusText, mimeType, outputCookies.
	 * 
	 * Forbids encoding, statusCode, statusText and mimeType to be changed.
	 * Sets charset to Charset appropriate for it's encoding.
	 * 
	 * @return String representation of header
	 */
	private String generateHeader() {
		charset = Charset.forName(encoding);
		
		StringBuilder header = new StringBuilder();
		header.append("HTTP/1.1 " + statusCode + " " + statusText);
		header.append("\r\n");
		
		header.append("Content-Type: " + mimeType);
		if(mimeType.startsWith("text/")) {
			header.append("; charset=" + encoding);
		}
		header.append("\r\n");

		for(RCCookie cookie : outputCookies) {
			header.append("Set-Cookie: " + cookie.toString());
			header.append("\r\n");
		}
		
		headerGenerated = true;
		
		header.append("\r\n");
		return header.toString();
	}

	
	/**
	 * RCCookie has read-only String properties name, value, domain and path and
	 * read-only Integer property maxAge.
	 * 
	 * @author Hrvoje
	 *
	 */
	public static class RCCookie {
		/** Cookie properties */
		private String name, value, domain, path;
		private Integer maxAge;
		
		/**
		 * Constructor for RCCookie
		 * @param name Name 
		 * @param value Value
		 * @param maxAge Age
		 * @param domain Domain
		 * @param path Path
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}
		
		/**
		 * Name getter
		 * @return name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Value getter
		 * @return value
		 */
		public String getValue() {
			return value;
		}
		
		/**
		 * Domain getter
		 * @return domain
		 */
		public String getDomain() {
			return domain;
		}
		
		/**
		 * Path getter
		 * @return path
		 */
		public String getPath() {
			return path;
		}
		
		/**
		 * Max Age getter
		 * @return MaxAge
		 */
		public int getMaxAge() {
			return maxAge;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			
			sb.append(name + "=\"" + value + "\"" );
			
			if(domain != null) {
				sb.append("; Domain=" + domain);
			}
			if(path != null) {
				sb.append("; Path=" + path);
			}
			if(maxAge != null) {
				sb.append("; Max-Age=" + maxAge);
			}
			
			return sb.toString();
		}
	}
}
