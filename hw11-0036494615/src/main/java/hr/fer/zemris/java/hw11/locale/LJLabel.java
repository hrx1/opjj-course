package hr.fer.zemris.java.hw11.locale;

import javax.swing.JLabel;

/**
 * LJLabel is Localized JLabel. 
 * It uses LocalizationProvider for localization, and gets its' text with key.
 * 
 * @author Hrvoje
 *
 */
public class LJLabel extends JLabel {
	private static final long serialVersionUID = 4113925064325287507L;
	
	/**
	 * LJLabel constructor
	 * 
	 * @param key of text
	 * @param lp localization pointer
	 */
	public LJLabel(String key, ILocalizationProvider lp) {
		setText( lp.getString(key));
		
		lp.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				setText(lp.getString(key));
			}
		});
	}
}
