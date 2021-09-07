package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Line editor
 * @author Hrvoje
 *
 */
public class LineEditor extends GeometricalObjectEditor {
	private static final long serialVersionUID = -3992886194892309670L;
	/** Line Subject */
	private Line line;
	/** Constructed Text Fields */
	private JTextField x1, y1, x2, y2, color;
	/** Values */
	private int x1Value, y1Value, x2Value, y2Value;
	/** Color Value */
	private Color colorValue;
	
	/**
	 * Constructor
	 * @param line Line
	 */
	public LineEditor(Line line) {
		this.line = line;
		
		x1 = new JTextField(String.valueOf(line.getFirstPoint().x));
		y1 = new JTextField(String.valueOf(line.getFirstPoint().y));
		x2 = new JTextField(String.valueOf(line.getSecondPoint().x));
		y2 = new JTextField(String.valueOf(line.getSecondPoint().y));
		color = new JTextField(String.valueOf(
					Integer.toHexString(
							line.getColor()
								.getRGB())
								.substring(2)
						)
					);
		/**
		 * Integer.toHexString(fillColor.getRGB())
						.substring(2)
						.toUpperCase()
		 */
		setSize(new Dimension(500, 300));
		
		setLayout(new GridLayout(5, 2));
		add(new JLabel("Start point x coordinate "));
		add(x1);
		add(new JLabel("Start point y coordinate "));
		add(y1);
		add(new JLabel("End point x coordinate "));
		add(x2);
		add(new JLabel("End point y coordinate "));
		add(y2);
		
		add(new JLabel("Color "));
		add(color);
		
	}

	@Override
	public void checkEditing() throws IllegalArgumentException {
		try {
			x1Value = Integer.parseInt(x1.getText());
			y1Value = Integer.parseInt(y1.getText());
			x2Value = Integer.parseInt(x2.getText());
			y2Value = Integer.parseInt(y2.getText());
			colorValue = super.getColor(color.getText());
		}catch(Exception e) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void acceptEditing() {
		line.setFirstPoint(new Point(x1Value, y1Value));
		line.setSecondPoint(new Point(x2Value, y2Value));
		line.setColor(colorValue);
		line.notifyObjectChanged();
	}
	
	

}
