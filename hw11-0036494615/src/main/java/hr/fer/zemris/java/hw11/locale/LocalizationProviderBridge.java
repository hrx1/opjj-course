package hr.fer.zemris.java.hw11.locale;

import java.util.Locale;

/**
 * LocalizationProviderBridge is a bridge between parent localization provider and its' listener.
 * It solves probable problems with memory leak.
 *  
 * @author Hrvoje
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	/** True if bridge connects parent and listener */
	private boolean connected = false;
	/** Parent */
	private ILocalizationProvider parent;
	/** Listener */
	private ILocalizationListener listener;
	
	/**
	 * Constructor for LocalizationProviderBridge
	 * @param parent Localization Provider
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
	}
	
	@Override
	public String getString(String key) {
		return parent.getString(key);
	}
	
	/**
	 * Disconnects parent and listener
	 */
	public void disconnect() {
		//odjavljuje se na promjene jezika parent providera
		if(connected) {
			parent.removeLocalizationListener(listener);
			connected = false;
		}else {
			throw new IllegalStateException("Cannot disconnect if already disconnected.");
		}
	}
	
	/**
	 * Connects parent and listener
	 */
	public void connect() {
		//prijavljuje se na promjene jezika parent providera
		if(!connected) {
			listener = () -> fire();
			parent.addLocalizationListener(listener);
			connected = true;
		} else {
			throw new IllegalStateException("Cannot connect if already connected.");
		}
	}
	
	/**
	 * Returns Locale used for localization
	 */
	public Locale getLocale() {
		return parent.getLocale();
	}
	
	
	
}
