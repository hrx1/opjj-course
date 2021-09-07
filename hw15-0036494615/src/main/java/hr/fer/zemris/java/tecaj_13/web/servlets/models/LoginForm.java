package hr.fer.zemris.java.tecaj_13.web.servlets.models;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.web.servlets.util.Utils;

/**
 * Class describes login form
 * @author Hrvoje
 *
 */
public class LoginForm {

	/** Key under which nick is stored */
	public static String nickKey = "nick";
	/** Key under which password is stored */
	public static String passwordKey = "password";
	/** Form properties */
	private String nick, password;
	
	private boolean valid;
	
	private String errorMessage;
	
	private BlogUser loggedUser;
	
	/**
	 * Fills form from request
	 * @param req
	 */
	public void fillFromRequest(HttpServletRequest req) {
		nick = prepareParameter(req.getParameter(nickKey));
		password = prepareParameter(req.getParameter(passwordKey));
	}
	
	/**
	 * Prepares parameter
	 * @param parameter
	 * @return parameter
	 */
	private String prepareParameter(String parameter) {
		if (parameter == null) return "";
		else return parameter.trim();
	}
	
	/**
	 * VAlidates form
	 * @return true if valid
	 */
	public boolean validate() {
		if(nick.isEmpty() || password.isEmpty()) {
				errorMessage = "Required fields are empty.";
				valid = false;
				return valid;
		}
		
		loggedUser = DAOProvider.getDAO().getUserByNick(nick);
		String givenPasswordHash = Utils.hash(password);
		
		if(loggedUser == null || !loggedUser.getPasswordHash().equals(givenPasswordHash)) {
			errorMessage = "Invalid user data.";
			valid = false;
			return valid;
		}
		
		valid = true;
		return true;
	}
	
	/**
	 * Returns form validity
	 * @return form validity
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Returns logged user
	 * 
	 * @return logged user
	 */
	public BlogUser getLoggedUser(){
		if(!valid) throw new IllegalStateException();
		
		return loggedUser;
	}
	
	/**
	 * Error Message getter
	 * @return Error Message
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	

}
