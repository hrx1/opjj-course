package hr.fer.zemris.java.tecaj_13.web.servlets.util;

import javax.servlet.http.HttpSession;

/**
 * Methods which perform upon Session
 * 
 * @author Hrvoje
 *
 */
public class SessionMethods {
	
	/**
	 * Checks if any user is logged in in session
	 * 
	 * @param session session
	 * @return true if any user is logged in
	 */
	public static boolean isLoggedIn(HttpSession session) {
		return session.getAttribute(SessionKeys.userID) != null;
	}
	
	/**
	 * Cheks if user with id is logged in
	 * 
	 * @param session Session
	 * @param id ID
	 * @return true if user with id is logged in
	 */
	public static boolean isLoggedInAs(HttpSession session, long id) {
		if(!isLoggedIn(session)) return false;
		return ((long) session.getAttribute(SessionKeys.userID)) == id;
	}
	
	/**
	 * Checks if user with nick is logged in
	 * 
	 * @param session Session
	 * @param nick Nick
	 * @return true if user with nick is logged in
	 */
	public static boolean isLoggedInAs(HttpSession session, String nick) {
		if(!isLoggedIn(session)) return false;
		System.out.println(session.getAttribute(SessionKeys.userNick) + " " + nick);
		return ((String) session.getAttribute(SessionKeys.userNick)).equals(nick);
	}
}
