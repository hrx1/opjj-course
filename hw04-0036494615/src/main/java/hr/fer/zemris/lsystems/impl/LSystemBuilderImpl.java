package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Scanner;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.Command;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;
import hr.fer.zemris.math.Vector2D;

/**
 * Class implements LSystemBuilder. Lindermayers Systems require starting axiom and productions. 
 * Class offers printing to painter which requires starting position, direction and length.
 * unitLengthDegreeScaler is used for scaling fractal so it can fit the screen.
 * 
 * @author Hrvoje
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {
	/** Direction of a turtle **/
	private double unitLength, unitLengthDegreeScaler, angle; //direction
	/** Position of a turtle **/
	private Vector2D origin; //position
	/** Axiom of fractal **/
	private String axiom;
	/** Holds data about productions and command processing **/
	private Dictionary productions, commandsToActions;
	/** Current painting color **/
	private Color color;
	/** Start state **/
	private TurtleState startState;
	
	/**
	 * Default constructor for {@link LSystemBuilderImpl}.
	 * Sets default values.
	 */
	public LSystemBuilderImpl() {
		//Default values:
		unitLength = 0.1;
		unitLengthDegreeScaler = 1;
		origin = new Vector2D(0, 0);
		angle = 0;
		axiom = "";
		
		color = new Color(0, 0, 0);
		
		productions = new Dictionary();
		commandsToActions = new Dictionary();
	}
	
	@Override
	public LSystem build() {
		//ovdje stvaram startni turtle
		Vector2D direction = new Vector2D(1, 0);
		direction.rotate(angle);
		
		startState = new TurtleState(origin, direction, color, unitLength);
		
		return new LSystemImpl(startState);
	}

	@Override
	public LSystemBuilder configureFromText(String[] directives) {
		for(String directive : directives) {
			Scanner sc = new Scanner(directive);
			
			if(!sc.hasNext()) continue; //preskace prazne linije
			
			String word = sc.next();
			
			switch (word.toLowerCase()) {
			case "origin": 
				setOrigin(sc.nextDouble(), sc.nextDouble());
				break;
			case "angle":
				setAngle(sc.nextDouble());
				break;
			case "unitlength":
				setUnitLength(sc.nextDouble());
				break;
			case "unitlengthdegreescaler":
				double d1, d2 = 0;
				d1 = sc.nextDouble();
				
				if(sc.hasNext()) {
					String s = sc.next();
					
					if(sc.hasNextDouble()) {
						d2 = sc.nextDouble();
					}
					else {
						try {
							d2 = Double.parseDouble(s.split("/")[1]);
						} catch (NumberFormatException e) {

						}
					}
				}
				
				setUnitLengthDegreeScaler(d1/d2);
				break;
				
			case "command":
				char commandChar = sc.next().charAt(0);
				String action = sc.next();
				
				if(sc.hasNext()) action += " " + sc.next();
				
				registerCommand(commandChar, action);
				break;
				
			case "axiom":
				setAxiom(sc.next());
				break;
				
			case "production":
				registerProduction(sc.next().charAt(0), sc.next());
				break;
				
			}
			
			sc.close();
		}
		return this;
	}

	@Override
	public LSystemBuilder registerCommand(char command, String action) {
		Command c;
		String[] args = action.split(" ");

		try {
			switch (args[0].toLowerCase()) {
				case "draw":
					c = new DrawCommand(Double.parseDouble(args[1]));
					break;
				case "skip":
					c = new SkipCommand(Double.parseDouble(args[1]));
					break;
				case "scale":
					c = new ScaleCommand(Double.parseDouble(args[1]));
					break;
				case "rotate":
					c = new RotateCommand(Double.parseDouble(args[1]));
					break;
				case "push":
					c = new PushCommand();
					break;
				case "pop":
					c = new PopCommand();
					break;
				case "color":
					c = new ColorCommand(Color.decode("#" + args[1]));
					break;
				default:
					throw new IllegalArgumentException(args[0] + " not default command!");
			}
		} catch (IllegalArgumentException ie) {
			throw new IllegalArgumentException(ie.getLocalizedMessage());
		} catch (Exception e) {
			throw new IllegalArgumentException("Command cannot be processed. Command: " + args[0]);
		}
		
		commandsToActions.put(command, c);
		
		return this;
	}

	@Override
	public LSystemBuilder registerProduction(char left, String right) {
		productions.put(left, right);
		return this;
	}

	@Override
	public LSystemBuilder setAngle(double angle) { //direction
		this.angle = angle;
		return this;
	}

	@Override
	public LSystemBuilder setAxiom(String axiom) {
		this.axiom = axiom;
		return this;
	}

	@Override
	public LSystemBuilder setOrigin(double x, double y) { //position
		origin = new Vector2D(x, y);
		return this;
	}

	@Override
	public LSystemBuilder setUnitLength(double length) {
		this.unitLength = length;
		return this;
	}

	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;
		return this;
	}
	
	/**
	 * LSystemImpl implements LSystem. Offers Drawing to painter and generating k-level fractal. 
	 * @author Hrvoje
	 *
	 */
	private class LSystemImpl implements LSystem {
		/** Starting position when printing **/
		TurtleState startState;
		
		/**
		 * Default constructor for {@link LSystemImpls}
		 * @param startState
		 */
		LSystemImpl(TurtleState startState) {
			this.startState = startState;
		}
		
		@Override
		public void draw(int k, Painter painter) {
			
			Context context = new Context();
			context.pushState(startState.copy());
			
			new ScaleCommand(Math.pow(unitLengthDegreeScaler, k)).execute(context, painter);
			
			String expanded = generate(k);
			Command command;
			for (char c : expanded.toCharArray()) {
				command = (Command) commandsToActions.get(c);
				if (command == null)
					continue;
				command.execute(context, painter);
			}
		}

		@Override
		public String generate(int k) {

			String toExpand = axiom;

			for (int i = 0; i < k; ++i) {
				StringBuilder result = new StringBuilder(); //rezultat prosirenja
				
				for (char c : toExpand.toCharArray()) {
					String toAppend = (String) productions.get(c);
					
					if(toAppend != null) {
						result.append(toAppend);
					} else {
						result.append(c);
					}
				}
				
				toExpand = result.toString();
			}
			
			return toExpand;
		}

	}
}
