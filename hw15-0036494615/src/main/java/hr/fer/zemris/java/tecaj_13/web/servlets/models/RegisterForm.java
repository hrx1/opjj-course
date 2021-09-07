package hr.fer.zemris.java.tecaj_13.web.servlets.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.web.servlets.util.Utils;

/**
 * Class abstracts register form
 * 
 * @author Hrvoje
 *
 */
public class RegisterForm extends BlogUser {
	/** First Name Key */
	public static String firstNameKey = "fn";
	/** Last Name Key*/
	public static String lastNameKey = "ln";
	/** Nick key */
	public static String nickKey = "nick";
	/** Email key */
	public static String emailKey = "email";
	/** Password key */
	public static String passwordKey = "password";
	
	/** Register form values */
	private String name, lastName, nick, email, password;
	/** Form validity */
	private boolean valid = false;
	
	/** Email check pattern */
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	
	/** Error message */
	private String errorMessage;
	
	/**
	 * Fills Form from request
	 * @param req request
	 */
	public void fillFromRequest(HttpServletRequest req) {
		name = prepareParameter(req.getParameter(firstNameKey));
		lastName = prepareParameter(req.getParameter(lastNameKey));
		nick = prepareParameter(req.getParameter(nickKey));
		email = prepareParameter(req.getParameter(emailKey));
		password = prepareParameter(req.getParameter(passwordKey));
	}
	
	/**
	 * Prepares parameter
	 * @param parameter parameter
	 * @return parameter
	 */
	private String prepareParameter(String parameter) {
		if (parameter == null) return "";
		else return parameter.trim();
	}
	
	/**
	 * Validates Form
	 * 
	 * @return true if valid
	 */
	public boolean validate() {
		if(name.isEmpty() ||
			lastName.isEmpty() ||
			nick.isEmpty() ||
			email.isEmpty() ||
			password.isEmpty()) {
				errorMessage = "Required fields are empty.";
				valid = false;
				return valid;
		}
		
		if(!isValidEMail(email)) {
			errorMessage = "Email not valid.";
			valid = false;
			return valid;
		}
		
		if(DAOProvider.getDAO().nickExists(nick)) {
			errorMessage = "Nick already exists.";
			valid = false;
			return valid;
		}
		
		valid = true;
		return true;
	}
	
	/**
	 * Checks is form valid
	 * @return true if it is valid
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Clears sensitive data
	 */
	public void clearSensitiveData() {
		password = "";
	}
	
	protected static boolean isValidEMail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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

	/**
	 * Returns Blog User from form data
	 * 
	 * @param form data
	 * @return Blog User
	 */
	public static BlogUser getBlogUser(RegisterForm form) {
		BlogUser user = new BlogUser();
		
		user.setFirstName(form.name);
		user.setLastName(form.lastName);
		user.setNick(form.nick);
		user.setEmail(form.email);
		user.setPasswordHash(Utils.hash(form.password));
		
		return user;
	}
	
	
}
