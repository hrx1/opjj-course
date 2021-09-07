package hr.fer.zemris.java.p12.model;

/**
 * Class Abstracts Poll
 * 
 * @author Hrvoje
 *
 */
public class Poll {
	/** ID */
	public int id;
	/**	Title */
	public String title;
	/** Message */
	public String message;

	/**
	 * Poll Constructor
	 * 
	 * @param id of poll
	 * @param title of poll
	 * @param message of poll
	 */
	public Poll(int id, String title, String message) {
		super();
		this.id = id;
		this.title = title;
		this.message = message;
	}

	/**
	 * Getter for ID
	 * @return ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Title Getter
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Message Getter
	 * @return message
	 */
	public String getMessage() {
		return message;
	}
	
	
}
