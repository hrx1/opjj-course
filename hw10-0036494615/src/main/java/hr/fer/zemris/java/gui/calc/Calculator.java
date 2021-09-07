package hr.fer.zemris.java.gui.calc;

import java.awt.Container;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.components.BlueButton;
import hr.fer.zemris.java.gui.calc.components.BlueDigitButton;
import hr.fer.zemris.java.gui.calc.components.BlueInverseBinaryOperation;
import hr.fer.zemris.java.gui.calc.components.BlueInverseButton;
import hr.fer.zemris.java.gui.calc.components.BlueInversedFunctionButton;
import hr.fer.zemris.java.gui.calc.components.BlueOperationButton;
import hr.fer.zemris.java.gui.calc.components.BlueUnaryFunctionButton;
import hr.fer.zemris.java.gui.calc.components.Display;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Simple GUI for Classic Calculator. 
 * @author Hrvoje
 *
 */
public class Calculator extends JFrame {
	private static final long serialVersionUID = -5148492242213114522L;
	/* Window title */
	private static final String TITLE = "Calculator";
	/* Margins between elements */
	private static final int MARGIN = 10;
	
	/* Used calculator model */
	private CalcModel calculator;
	/* Internal stack */
	private Stack<Double> stack;
	
	/**
	 * Default constructor for Calculator.
	 */
	public Calculator() {
		setTitle(TITLE);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		calculator = new ClassicCalculator();
		stack = new Stack<>();
		
		initGUI();
		pack();
	}
	
	/**
	 * Initializes GUI. Sets Layout to CalcLayout.
	 * Adds display, functions, digits and operators.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(MARGIN));
		
		addDisplay(cp);
		addUnaryFunctions(cp);
		addNumpad(cp);
		
		BlueInverseButton inverseButton = new BlueInverseButton("Inv", calculator);
		cp.add(inverseButton, new RCPosition(5, 7));
		
		addInverseFunctions(cp, inverseButton);
		addOperations(cp);
		addSpecial(cp);
		addInverseBinaryOperators(cp, inverseButton);
	}
	
	/**
	 * Adds inverse binary operators to the container
	 * @param cp Container
	 * @param inverseButton inverse button
	 */
	private void addInverseBinaryOperators(Container cp, BlueInverseButton inverseButton) {
		BlueInverseBinaryOperation xn = new BlueInverseBinaryOperation("x^n", calculator, (x, n) -> Math.pow(x, n), (x, n) -> Math.pow(x, 1/n));
		
		inverseButton.addObserver(xn);
		
		cp.add(xn, new RCPosition(5, 1));
	}

	/** 
	 * Adds Display to position (1, 1) to Container
	 * @param cp Container
	 */
	private void addDisplay(Container cp) {
		Display display = new Display(calculator); 
		cp.add(display, new RCPosition(1, 1));
	}
	
	/**
	 * Adds unary function 1/x to Container
	 * @param cp Container
	 */
	private void addUnaryFunctions(Container cp) {
		cp.add(new BlueUnaryFunctionButton("1/x", calculator, x -> 1/x), new RCPosition(2, 1));
	}
	
	/**
	 * Adds functions which have inverse function, and subscribes them to inverseButton, to Container. 
	 * @param cp Container
	 * @param inverseButton which Function buttons will listen
	 */
	private void addInverseFunctions(Container cp, BlueInverseButton inverseButton) {
		BlueInversedFunctionButton sine = new BlueInversedFunctionButton("sin", calculator, x -> Math.sin(x), x -> Math.asin(x));
		BlueInversedFunctionButton cos = new BlueInversedFunctionButton("cos", calculator, x -> Math.cos(x), x -> Math.acos(x));
		BlueInversedFunctionButton tan = new BlueInversedFunctionButton("tan", calculator, x -> Math.tan(x), x -> Math.atan(x));
		BlueInversedFunctionButton ctg = new BlueInversedFunctionButton("ctg", calculator, x -> 1/Math.tan(x), x ->Math.PI /2 - Math.atan(x));
		BlueInversedFunctionButton log = new BlueInversedFunctionButton("log", calculator, x -> Math.log10(x), x -> Math.pow(10, x));
		BlueInversedFunctionButton ln = new BlueInversedFunctionButton("ln", calculator, x -> Math.log(x), x -> Math.pow(Math.E, x));

		inverseButton.addObserver(sine);
		inverseButton.addObserver(cos);
		inverseButton.addObserver(tan);
		inverseButton.addObserver(ctg);
		inverseButton.addObserver(log);
		inverseButton.addObserver(ln);
		
		cp.add(sine, new RCPosition(2, 2));
		cp.add(cos, new RCPosition(3, 2));
		cp.add(tan, new RCPosition(4, 2));
		cp.add(ctg, new RCPosition(5, 2));
		cp.add(log, new RCPosition(3, 1));
		cp.add(ln, new RCPosition(4, 1));
	}
	
	/**
	 * Adds digits, +/- and dot buttons to the Container.
	 * @param cp Container
	 */
	private void addNumpad(Container cp) {
		//add numbers
		cp.add(new BlueDigitButton(0, calculator), new RCPosition(5, 3));
		
		int number = 1;
		for(int row = 4; row > 1; --row) {
			for(int column = 3; column < 6; ++column) {
				cp.add(new BlueDigitButton(number, calculator), new RCPosition(row, column));
				++number;
			}
		}
		
		//add +/- and dot
		cp.add(new BlueButton(".", calculator) {
			private static final long serialVersionUID = 1537489037786907732L;
			@Override
			public void pressed() {
				calculator.insertDecimalPoint();
			}
		}, new RCPosition(5, 5));
		
		cp.add(new BlueButton("+/-", calculator) {
			private static final long serialVersionUID = 1537489037786907732L;
			@Override
			public void pressed() {
				calculator.swapSign();
			}
		}, new RCPosition(5, 4));

	}
	
	/**
	 * Adds division, times, minus and plus operations to the Container
	 * @param cp Container
	 */
	private void addOperations(Container cp) {
		BlueOperationButton division = new BlueOperationButton("/", calculator, (o1, o2) -> o1/o2);
		BlueOperationButton times = new BlueOperationButton("*", calculator, (o1, o2) -> o1*o2);
		BlueOperationButton minus = new BlueOperationButton("-", calculator, (o1, o2) -> o1-o2);
		BlueOperationButton plus = new BlueOperationButton("+", calculator, (o1, o2) -> o1+o2);
		
		cp.add(division, new RCPosition(2, 6));
		cp.add(times, new RCPosition(3, 6));
		cp.add(minus, new RCPosition(4, 6));
		cp.add(plus, new RCPosition(5, 6));
		
	}
	
	/**
	 * Adds buttons with special functions. Such as equals, clr, res, push, and pop.
	 * @param cp Container
	 */
	private void addSpecial(Container cp) {
		BlueButton equals = new BlueButton("=", calculator) {
			private static final long serialVersionUID = 7428408645650265598L;

			@Override
			public void pressed() {
				if(calculator.isActiveOperandSet()) {
					double result = calculator.getPendingBinaryOperation().applyAsDouble(calculator.getActiveOperand(), calculator.getValue());
					calculator.clearAll();
					calculator.setValue(result);
				}else {
					calculator.setActiveOperand(calculator.getValue());
				}
			}
		};
		
		BlueButton clr = new BlueButton("clr", calculator) {
			private static final long serialVersionUID = 189932773048500203L;

			@Override
			public void pressed() {
				calculator.clear();
			}
		};
		
		BlueButton res = new BlueButton("res", calculator) {
			private static final long serialVersionUID = 1961594502508648818L;

			@Override
			public void pressed() {
				calculator.clearAll();
			}
		};
		
		BlueButton push = new BlueButton("push", calculator) {
			private static final long serialVersionUID = 4258149483376109771L;

			@Override
			public void pressed() {
				stack.push(calculator.getValue());
			}
		};
		BlueButton pop = new BlueButton("pop", calculator) {
			private static final long serialVersionUID = 1441709769667260886L;
			@Override
			public void pressed() {
				if(!stack.isEmpty()) {
					calculator.setValue(stack.pop());
				}
			}
		};
		
		
		cp.add(equals, new RCPosition(1, 6));
		cp.add(clr, new RCPosition(1, 7));
		cp.add(res, new RCPosition(2, 7));
		cp.add(push, new RCPosition(3, 7));
		cp.add(pop, new RCPosition(4, 7));
		
	}

	/**
	 * Main method which creates Calculator window.
	 * @param args neglected
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Calculator c = new Calculator();
			c.setVisible(true);
			c.pack();
		});
	}
}
