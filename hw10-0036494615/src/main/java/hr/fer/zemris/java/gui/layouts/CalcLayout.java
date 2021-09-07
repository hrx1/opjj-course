package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

/**
 * Defines CalcLayout requested in homework.
 * @author Hrvoje
 *
 */
public class CalcLayout implements LayoutManager2 {
	/** Margin */
	private int margin;
	/** Components */
	private Map<RCPosition, Component> components;
	
	/** Special position */
	private static final RCPosition specialPosition = new RCPosition(1, 1); 
	/** Number of rows */
	public static final int ROWS = 5;
	/** Number of columns */
	public static final int COLUMNS = 7;
	
	/**
	 * Default constructor for CalcLayout. Sets margin to 0.
	 */
	public CalcLayout() {
		this(0);
	}
	
	/**
	 * Constructor for CalcLayout with defined margin.
	 * @param margin to set
	 */
	public CalcLayout(int margin) {
		super();
		this.margin = margin;
		components = new HashMap<>();
	}

	@Override
	public void layoutContainer(Container cont) {
		Insets insets = cont.getInsets();		
		
		Dimension cellDimension = selectFittingCellDimension(cont.getSize());
		
		for(Component component : cont.getComponents()) {
			RCPosition coordinates = getPositionByComponent(component);
			
			int x = insets.left + (coordinates.column - 1) * (margin + cellDimension.width);
		 	int y = insets.top + (coordinates.row - 1) * (margin + cellDimension.height);
		 	
			component.setBounds(x, y, cellDimension.width, cellDimension.height);
		}
		
		//popravi (1,1)
		Component tmp = components.get(specialPosition);
		if(tmp != null) {
			Rectangle r = tmp.getBounds();
			r.width = 5 * r.width + 4 * margin;
			tmp.setBounds(r);
		}
	}
	
	/**
	 * Returns RCPosition for given component.
	 * @param component whose RCPosition will be returned
	 * @return RCPosition for given component
	 */
	private RCPosition getPositionByComponent(Component component) {
	    for (Entry<RCPosition, Component> entry : components.entrySet()) {
	        if (Objects.equals(component, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    throw new CalcLayoutException("Cannot find component position. Component: " + component);
	}

	@Override
	public Dimension minimumLayoutSize(Container cont) {
		return selectFittingDimension(o -> o.getMinimumSize());
	}

	@Override
	public Dimension preferredLayoutSize(Container cont) {
		return selectFittingDimension(o -> o.getPreferredSize());
	}
	
	@Override
	public Dimension maximumLayoutSize(Container arg0) {
		return selectFittingDimension(o -> o.getMaximumSize());
	}


	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition constraint = checkConstraints(constraints);
		
		Component tmp = components.get(constraint);
		if(tmp == null) {
			components.put(constraint, comp);
		}
		else if (tmp != comp) {
			throw new CalcLayoutException("Cannot add two different components to the same field.");
		}
		
	}
	
	@Override
	public void removeLayoutComponent(Component comp) {
		components.values().remove(comp);
	}

	
	@Override
	public void addLayoutComponent(String arg0, Component arg1) {
		throw new UnsupportedOperationException();
	}


	@Override
	public float getLayoutAlignmentX(Container arg0) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container arg0) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container arg0) {
		
	}



	/**
	 * Checks whether Constraints satisfies conditions.
	 * @param constraints to check
	 * @throws CalcLayoutException if Constraints doesn't satisfy conditions
	 */
	private RCPosition checkConstraints(Object constraints) {
		
		if(constraints instanceof RCPosition) {
			RCPosition rcp = (RCPosition) constraints;
			int column = rcp.getColumn();
			int row = rcp.getRow();
			
			if(column < 1 || column > COLUMNS || row < 1 || row > ROWS) {
				throw new CalcLayoutException("Invalid row or column number given. Row : column " + row + " : " + column);
			}
			if(row == 1 && (column > 1 && column < 6)) {
				throw new CalcLayoutException("First row is special. Check documentation. Row : column " + row + " : " + column);
			}
			
			return rcp;
		} else {
			throw new CalcLayoutException("Invalid constraint object given.");
		}
	}
	
	/**
	 * Selects fitting dimension using function.
	 * @param function to use
	 * @return fitting dimension
	 */
	private Dimension selectFittingDimension(Function<Component, Dimension> function) {
		Dimension prefferedCellDimension = selectFittingCellDimension(function);
		
		int width = prefferedCellDimension.width * COLUMNS + margin * (COLUMNS - 1);
		int height = prefferedCellDimension.height * ROWS + margin * (ROWS - 1);
		
		return new Dimension(width, height);
	}
	
	/**
	 * Selects fitting cell dimension for given window dimensions.
	 * @param window whose dimensions will be used 
	 * @return fitting cell dimension
	 */
	private Dimension selectFittingCellDimension(Dimension window) {
		int width = 0;
		int height = 0;
		
		width = (window.width - (COLUMNS - 1) * margin) / COLUMNS;
		height = (window.height - (ROWS - 1) * margin) / ROWS;
				
		return new Dimension(width, height);
	}
	
	/**
	 * Selects fitting cell dimension using function.
	 * 
	 * @param function to use
	 * @return fitting cell dimension 
	 */
	private Dimension selectFittingCellDimension(Function<Component, Dimension> function) { 
		int fitH = 0;
		int fitW = 0;
		
		Component tmp = components.get(specialPosition);
		if(tmp != null) {
			Dimension dimTmp = function.apply(tmp);
			
			if(dimTmp != null) {
				fitW = (dimTmp.width - 4 * margin)/5;
				fitH = dimTmp.height;
			}
		}
		
		
		for(Entry<RCPosition, Component> entry : components.entrySet()) {
			if(entry.getKey().equals(specialPosition)) continue;
			
			Dimension dimTmp = function.apply(entry.getValue());
			
			if(dimTmp == null) {
				continue;
			}
			
			if(dimTmp.height > fitH) {
				fitH = dimTmp.height;
			}
			if(dimTmp.width > fitW) {
				fitW = dimTmp.width;
			}
		}
		
		return new Dimension(fitW, fitH);
	}

}
