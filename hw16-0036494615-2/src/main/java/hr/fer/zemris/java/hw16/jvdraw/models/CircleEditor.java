package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class CircleEditor extends GeometricalObjectEditor {
	private Circle circle;
	private JTextField x1, y1, radius, color;
	
	private int x1Value, y1Value, radiusValue;
	
	private Color colorValue;
	
	public CircleEditor(Circle circle) {
		this.circle = circle;
		
		x1 = new JTextField(String.valueOf(circle.getCenter().x));
		y1 = new JTextField(String.valueOf(circle.getCenter().y));
		radius = new JTextField(String.valueOf(circle.getRadius()));
		
		color = new JTextField(String.valueOf(
					Integer.toHexString(
							circle.getColor()
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
		
		setLayout(new GridLayout(0, 2));
		add(new JLabel("Center point x coordinate "));
		add(x1);
		add(new JLabel("Center point y coordinate "));
		add(y1);
		add(new JLabel("Radius "));
		add(radius);
		
		add(new JLabel("Color "));
		add(color);
		
	}

	@Override
	public void checkEditing() throws IllegalArgumentException {
		try {
			x1Value = Integer.parseInt(x1.getText());
			y1Value = Integer.parseInt(y1.getText());
			radiusValue = Integer.parseInt(radius.getText());
			colorValue = super.getColor(color.getText());
			
		}catch(Exception e) {
			throw new IllegalArgumentException();
		}
		
		checkNegative(x1Value, y1Value, radiusValue);
	}

	@Override
	public void acceptEditing() {
		circle.setCenter(new Point(x1Value, y1Value));
		circle.setRadius(radiusValue);
		circle.setColor(colorValue);
		circle.notifyObjectChanged();
	}
	
	

}
