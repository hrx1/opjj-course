package hr.fer.zemris.java.hw11.locale;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LocalizationProvider is a singleton used for language selection.
 * 
 * @author Hrvoje
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	/** Current language */
	private static String language;
	/** Current Resource Bundle */
	private static ResourceBundle bundle;
	/** Current locale */
	Locale locale;
	/** Singleton */
	private static final LocalizationProvider instance = new LocalizationProvider();
	
	/** Bundle Package */
	private final String BUNDLE_PACKAGE = "hr.fer.zemris.java.hw11.jnotepadpp.locale.prijevodi";
	
	/**
	 * Creates LocalizationProvider with hr language.
	 */
	private LocalizationProvider() {
		language = "hr";
		locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle(BUNDLE_PACKAGE, locale);
	};
	
	/**
	 * Returns LocalizationProvider
	 * @return LocalizationProvider
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}
	
	/**
	 * Sets language
	 * @param language to set
	 */
	public void setLanguage(String language) {
		LocalizationProvider.language = language;
		Locale locale = Locale.forLanguageTag(language);
		LocalizationProvider.bundle = ResourceBundle.getBundle(BUNDLE_PACKAGE, locale);
		fire();
	}
	
	/**
	 * Returns localized String for key
	 */
	public String getString(String key) {
		return bundle.getString(key);
	}
	
	@Override
	public Locale getLocale() {
		return locale;
	}
}
