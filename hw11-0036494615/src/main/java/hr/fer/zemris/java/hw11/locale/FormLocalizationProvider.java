package hr.fer.zemris.java.hw11.locale;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * FormLocalizationProvider is LocalizationProviderBridge which is called upon JFrame.
 * It connects/disconnects from Localization when window is open/closed.
 * 
 * @author Hrvoje
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {
	
	/**
	 * Constructor for FormLocalizationProvider
	 * 
	 * @param parent parent Localization Provider 
	 * @param frame upon which Localization Provider is subscribed
	 */
	public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
		super(parent);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
		});
	}
	
}
