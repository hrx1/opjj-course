package hr.fer.zemris.java.hw11.locale;

import java.util.LinkedList;
import java.util.List;

/**
 * ILocalization provider with implemented General methods called upon listeners.
 * 
 * @author Hrvoje
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	/** List of listeners */
	private List<ILocalizationListener> listeners = new LinkedList<>();
	
	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		listeners.add(l);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}

	/** Notifies all listeners */
	public void fire() {
		for(ILocalizationListener l : listeners) {
			l.localizationChanged();
		}
	}

}
