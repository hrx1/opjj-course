package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Class provides Prime number generator with GUI.
 * @author Hrvoje
 *
 */
public class PrimDemo extends JFrame {
	private static final long serialVersionUID = 2552614965736703542L;

	/**
	 * Constructor for PrimDemo
	 */
	public PrimDemo() {
		setLocation(20, 50);
		setSize(300, 200);
		setTitle("Moj prvi prozor!");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		initGUI();
	}
	
	/**
	 * Initializes GUI
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		PrimListModel model = new PrimListModel();
		
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);
		
		JButton addButton = new JButton("Dodaj");
				
		addButton.addActionListener(e -> {
			model.next();;
		});

		JPanel central = new JPanel(new GridLayout(1, 0));
		central.add(new JScrollPane(list1));
		central.add(new JScrollPane(list2));
		
		cp.add(central, BorderLayout.CENTER);
		cp.add(addButton, BorderLayout.PAGE_END);
	}
	
	/**
	 * Main method creates and shows PrimDemo
	 * @param args neglected
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new PrimDemo();
			frame.pack();
			frame.setVisible(true);
		});
		
	}

}
