package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * JCustomTimeLabel is a JLabel which shows time in format: yyyy/MM/dd HH:mm:ss
 * JCustomTimeLabel requires a reference to JFrame which called him so it can stop when frame is closed.
 * 
 * Object creates Timer which is ran in a new Thread.
 * Timer can be stopped externaly with stop() method.
 * 
 * @author Hrvoje
 *
 */
public class JCustomTimeLabel extends JLabel implements ActionListener {
	private static final long serialVersionUID = -1531620787210199656L;
	/** Current date */
	private Date date;
	/** Time format */
    private final DateFormat timeFormat;
    /** Timer which updates date value */
    Timer timer;
    
    /**
     * Constructor for JCustomTimeLabel
     * 
     * @param frame which closing will stop timer
     */
	public JCustomTimeLabel(JFrame frame) {
		timeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
        timer = new Timer(1000, this);
        timer.setInitialDelay(0);
        timer.start();
        
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				stop();
			}
		});

	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
        date = new Date();
        String time = timeFormat.format(date);
        setText(time);
	}
	
	/**
	 * Stops timer
	 */
	public void stop() {
		timer.stop();
	}
}
