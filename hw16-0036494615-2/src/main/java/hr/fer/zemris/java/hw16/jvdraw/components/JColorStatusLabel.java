package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * Label which shows Status about color
 * @author Hrvoje
 *
 */
public class JColorStatusLabel extends JLabel implements ColorChangeListener {
	private static final long serialVersionUID = 7691486808510341508L;
	/** Color Providers*/
	private IColorProvider fgColorProvider, bgColorProvider;
	private Color bgColor, fgColor;
	
	/**
	 * Constructor
	 * @param fgColorProvider Foreground color provider
	 * @param bgColorProvider Background color provider
	 */
	public JColorStatusLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		fgColorProvider.addColorChangeListener(this);
		bgColorProvider.addColorChangeListener(this);
		
		this.fgColorProvider = fgColorProvider;
		this.bgColorProvider = bgColorProvider;
		
		bgColor = bgColorProvider.getCurrentColor();
		fgColor = fgColorProvider.getCurrentColor();
		
		updateStatus();
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		if(source.equals(bgColorProvider)) {
			bgColor = newColor;
		} else {
			fgColor = newColor;
		}
		
		updateStatus();
	}
	
	/**
	 * Updates status
	 */
	private void updateStatus() {
		String status = String.format(
				"Foreground color: (%d, %d, %d), "
				+ "background color (%d, %d, %d).", 
				fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue(),
				bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()
			);
		
		setText(status);
	}
}
