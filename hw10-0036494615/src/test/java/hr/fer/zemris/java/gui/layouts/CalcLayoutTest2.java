package hr.fer.zemris.java.gui.layouts;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;

public class CalcLayoutTest2 extends JFrame {
	JFrame frame;
	Dimension dim;
	
	public CalcLayoutTest2() {
		initGUI();
	}
	
	private void initGUI() {
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
		p.add(l1, new RCPosition(1,1));
		p.add(l2, new RCPosition(3,3));
		dim = p.getPreferredSize();		
	}

	
	@Before
	public void setUp() throws Exception {
		frame = new CalcLayoutTest2();
	}

	@Test
	public void testHeight() {
		int expected = 158;
		assertEquals(expected, Math.round(dim.getHeight()));
	}
	
	@Test
	public void testWidth() {
		int expected = 152;
		assertEquals(expected, Math.round(dim.getWidth()));
	}

}
