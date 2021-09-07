package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Filled Circle Editor
 * @author Hrvoje
 *
 */
public class FilledCircleEditor extends CircleEditor {
	private JTextField fillColor;
	private Color fillColorValue;

	private FilledCircle circle;
	
	/**
	 * Constructor
	 * @param circle used circle
	 */
	public FilledCircleEditor(FilledCircle circle) {
		super(circle);
		this.circle = circle;
		fillColor = new  JTextField(String.valueOf(
				Integer.toHexString(
						circle.getFillColor()
							.getRGB())
							.substring(2)
					)
				);
		add(new JLabel("Fill Color: "));
		add(fillColor);
	}
	
	
	@Override
	public void checkEditing() throws IllegalArgumentException {
		super.checkEditing();
		try {
			fillColorValue = super.getColor(fillColor.getText());
			
		}catch(Exception e) {
			throw new IllegalArgumentException();
		}
		
	}

	@Override
	public void acceptEditing() {
		circle.setFillColor(fillColorValue);
		super.acceptEditing();
	}

}

