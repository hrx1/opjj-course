package hr.fer.zemris.java.tecaj_13.model;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * Class describes blog form.
 * 
 * @author Hrvoje
 *
 */
public class BlogForm {
	
	/** Key under which title is stored */
	private static final String titleKey = "title";
	/** Key under which text is stored */
	private static final String textKey = "text";
	
	/** Title */
	private String title;
	/** Text */
	private String text;
	/** Validity of form */
	private boolean valid;
	/** Error Message */
	private String errorMessage;
	
	/**
	 * Fills Form from request
	 * @param req request
	 */
	public void fillFromRequest(HttpServletRequest req) {
		title = prepareParameter(req.getParameter(titleKey));
		text = prepareParameter(req.getParameter(textKey));
	}
	
	/**
	 * Validates form
	 * @return validity
	 */
	public boolean validate() {
		valid = (!title.isEmpty() && !text.isEmpty());

		if(!valid) {
			errorMessage = "Required fields are empty.";
		}
		
		return valid;
	}
	
	/**
	 * True if form is valid
	 * @return true if form is valid
	 */
	public boolean isValid() {
		return valid;
	}
	
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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

	private String prepareParameter(String parameter) {
		if (parameter == null) return "";
		else return parameter.trim();
	}

	/**
	 * Creates blog from form, date, and creator info
	 * @param form Form
	 * @param date Date
	 * @param creator Creator
	 * @return created blog
	 */
	public static BlogEntry createBlog(BlogForm form, Date date, BlogUser creator) {
		if(!form.valid) throw new IllegalStateException();
		
		BlogEntry blog = new BlogEntry();
		blog.setCreatedAt(date);
		blog.setCreator(creator);
		blog.setText(form.text);
		blog.setTitle(form.title);
		blog.setLastModifiedAt(date);
		
		return blog;
	}

	/**
	 * Fills Form from blog entry
	 * @param blog Blog Entry
	 */
	public void fillFromBlogEntry(BlogEntry blog) {
		title = blog.getTitle();
		text = blog.getText();
	}


	
	
}
