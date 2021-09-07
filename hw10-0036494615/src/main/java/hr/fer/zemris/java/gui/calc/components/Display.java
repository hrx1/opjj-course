package hr.fer.zemris.java.gui.calc.components;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Display with background color (241, 244, 65), and text color (0, 0, 0).
 * Display can be subscribed to value stored in CalcModel
 * 
 * @author Hrvoje
 *
 */
public class Display extends JLabel implements CalcValueListener {
	private static final long serialVersionUID = 2819890907857170993L;
	
	/**
	 * Constructor for display.
	 * @param calculator subscribe to
	 */
	public Display(CalcModel calculator) {
		super();
		setOpaque(true);
		setBackground(new Color(241, 244, 65));
		setForeground(Color.BLACK);
		setText("0");
		setBorder(new EmptyBorder(0, 0, 0, 10));
		setHorizontalAlignment(SwingConstants.RIGHT);
		
		calculator.addCalcValueListener(this);
	}

	@Override
	public void valueChanged(CalcModel model) {
		setText(model.toString());
	}

}
