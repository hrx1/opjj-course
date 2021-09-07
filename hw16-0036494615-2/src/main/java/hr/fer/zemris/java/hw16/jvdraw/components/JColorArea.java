package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * Color area
 * @author Hrvoje
 *
 */
public class JColorArea extends JComponent implements IColorProvider {
	private static final long serialVersionUID = -4006026632726880833L;
	/** Selected Color */
	private Color selectedColor;
	/** Listeners */
	private List<ColorChangeListener> listeners = new LinkedList<>();
	/** Preferred Square Size*/
	private static final int squareSize = 15;
	
	/**
	 * Constructor
	 * @param selectedColor default Color
	 */
	public JColorArea(Color selectedColor) {
		super();
		this.selectedColor = selectedColor;
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Pick a color", selectedColor);
				if(newColor == null) return ;
				setSelectedColor(newColor);
			}
		});
	}
	
	/**
	 * Notifies listeners that color changed
	 * @param oldColor Old Color
	 * @param newColor New color
	 */
	private void notifyColorChanged(Color oldColor, Color newColor) {
		for(ColorChangeListener l : listeners) {
			l.newColorSelected(this, oldColor, newColor);
		}
	}



	/**
	 * @param selectedColor the selectedColor to set
	 */
	private void setSelectedColor(Color selectedColor) {
		Color oldColor = this.selectedColor;
		this.selectedColor = selectedColor;
		repaint();
		notifyColorChanged(oldColor, selectedColor);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(15, 15);
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		graphics.setColor(selectedColor);
		graphics.fillRect(
				super.getInsets().left, 
				super.getInsets().top, 
				squareSize, 
				squareSize
			);
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		listeners.add(l);
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}
}
