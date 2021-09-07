package hr.fer.zemris.java.hw11.locale;

import java.util.Locale;

/**
 * Provides Localization
 * @author Hrvoje
 *
 */
public interface ILocalizationProvider {
	
	/**
	 * Adds listener
	 * @param l listener
	 */
	public void addLocalizationListener(ILocalizationListener l);
	
	/**
	 * Removes listener
	 * @param l listener
	 */
	public void removeLocalizationListener(ILocalizationListener l);
	
	/**
	 * Returns translation value for given Key
	 * @param key of translations
	 * @return translation
	 */
	public String getString(String key);
	
	/**
	 * Return locale used in translation
	 * @return
	 */
	public Locale getLocale();
}
