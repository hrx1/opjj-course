package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.math.Vector2D;

/**
 * Describes Turtle state. Turtle has position and direction described as two 2D
 * Vectors, Color, and length.
 * 
 * @author Hrvoje
 *
 */
public class TurtleState {

	/** Position of a turtle **/
	private Vector2D position;
	/** Direction of a turtle **/
	private Vector2D direction;
	/** Color of a turtle */
	private Color color;
	/** Length of a turtle step */
	private double length; // EFEKTIVNA DULJINA POMAKA!!!

	/**
	 * Constructor for TurtleState.
	 * 
	 * @param position
	 *            of a turtle
	 * @param direction
	 *            of a turtle
	 * @param color
	 *            of a turtle
	 * @param length
	 *            of a turtle
	 */
	public TurtleState(Vector2D position, Vector2D direction, Color color, double length) {
		super();
		this.position = position;
		this.direction = direction;
		this.color = color;
		this.length = length;
	}

	/**
	 * Returns position of a turtle.
	 * 
	 * @return position of a turtle.
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Sets Turtle position
	 * 
	 * @param position
	 *            of a turtle
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
	}

	/**
	 * Returns direction of a turtle.
	 * 
	 * @return direction of a turtle
	 */
	public Vector2D getDirection() {
		return direction;
	}

	/**
	 * Sets direction of a turtle
	 * 
	 * @param direction
	 *            of a turtle
	 */
	public void setDirection(Vector2D direction) {
		this.direction = direction;
	}

	/**
	 * Returns of a turtle
	 * 
	 * @return of a turtle
	 */
	public double getLength() {
		return length;
	}

	/**
	 * Sets length of a turtle
	 * 
	 * @param length
	 *            of a turtle
	 */
	public void setLength(double length) {
		this.length = length;
	}

	/**
	 * Returns color of a turtle
	 * 
	 * @return color of a turtle
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets color of a turtle
	 * 
	 * @param color
	 *            of a turtle
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Returns copy of this turtle
	 * 
	 * @return copy of this turtle
	 */
	public TurtleState copy() {
		return new TurtleState(position.copy(), direction.copy(), color, length);
	}

	/**
	 * Rotates turtle for angle DEGREES counter-clockwise
	 * 
	 * @param angle
	 *            of counter-clockwise rotation
	 */
	public void rotate(double angle) {
		direction.rotate(angle);
	}

	/**
	 * Moves turtle forward for step*length length
	 */
	public void move(double step) {
		position.translate(direction.scaled(step * length));
	}

	public void scaleLength(double factor) {
		length = length * factor;
	}

	@Override
	public String toString() {
		return position + " " + direction + " " + length;
	}
}
