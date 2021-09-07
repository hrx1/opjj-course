package hr.fer.zemris.java.tecaj_13.web.servlets.models;

import javax.servlet.http.HttpServletRequest;

/**
 * Class describes comment form.
 * 
 * @author Hrvoje
 *
 */
public class CommentForm {
	/** Key under which comment value is stored */
	public static final String commentValueKey = "comment-value";
	/** Key under which  Email is stored */
	public static final String commentEmailKey = "comment-email";
	/** Email */
	private String email;
	/** Value */
	private String value;
	/** Form valid */
	private boolean valid = false;
	/** Error message */
	private String errorMessage;
	
	/**
	 * Fills form from request
	 * @param req request
	 */
	public void fillFromRequest(HttpServletRequest req) {
		email = prepareParameter(req.getParameter(commentEmailKey));
		value = prepareParameter(req.getParameter(commentValueKey));

	}
	
	/**
	 * Prepares parameter
	 * 
	 * @param parameter parameter
	 * @return parameter
	 */
	private String prepareParameter(String parameter) {
		if (parameter == null) return "";
		else return parameter.trim();
	}

	/**
	 * Validates form
	 * @return form validity
	 */
	public boolean validate() {
		if(email.isEmpty() || value.isEmpty()) {
				errorMessage = "Required fields are empty.";
				valid = false;
				return valid;
		}
		
		if(!RegisterForm.isValidEMail(email)) {
			errorMessage = "Invalid email address.";
			valid = false;
			return valid;
		}
		
		valid = true;
		return true;
	}
	
	/**
	 * Returns true if form is valid
	 * @return true if form is valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	
	

}
