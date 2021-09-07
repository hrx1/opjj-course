package hr.fer.zemris.java.hw11.locale;

import java.util.MissingResourceException;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Action which changes it's parameters when ILocalizationListener changes
 * 
 * @author Hrvoje
 *
 */
public abstract class LocalizableAction extends AbstractAction {
	private static final long serialVersionUID = 7166976805334785458L;
	
	/**
	 * Constructor for Localizable Action.
	 * 
	 * @param key String
	 * @param lp Localization Provider
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		putValue(Action.NAME, lp.getString(key));
		try {
			putValue(Action.SHORT_DESCRIPTION, lp.getString(key + "_description"));
		}catch(MissingResourceException e) {
		}
		
		lp.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				putValue(Action.NAME, lp.getString(key));
				
				try {
					putValue(Action.SHORT_DESCRIPTION, lp.getString(key + "_description"));
				}catch(MissingResourceException e) {
				}
			}
		});
	}
	
}
