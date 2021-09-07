package hr.fer.zemris.java.gui.calc.components;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Describes buttons which orders functions or inverse functions should be used.
 * @author Hrvoje
 *
 */
public class BlueInverseButton extends BlueButton {
	private static final long serialVersionUID = -7495354466456387454L;
	/* List of observers */
	private List<InverseButtonListener> observers;
	/* Flag which states should we calculate function or inverse function */
	private boolean inversed;
	
	/**
	 * Constructor for BlueInverseButton
	 * @param text of button
	 * @param calculator CalcModel to use
	 */
	public BlueInverseButton(String text, CalcModel calculator) {
		super(text, calculator);
		observers = new LinkedList<>();
	}

	@Override
	public void pressed() {
		inversed = !inversed;
		if(inversed) {
			setBackground(Color.GRAY);
		}
		else {
			setBackground(Color.BLUE);
		}
		
		notifyObservers();
	}

	/**
	 * Adds observer for changed value
	 * @param observer to add
	 */
	public void addObserver(InverseButtonListener observer) {
		observers.add(observer);
	}
	
	/**
	 * Notifies observers
	 */
	private void notifyObservers() {
		for(InverseButtonListener observer : observers) {
			observer.inverseChanged(this);
		}
	}
	
}
