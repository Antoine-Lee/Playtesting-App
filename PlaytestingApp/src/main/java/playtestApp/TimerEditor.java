package playtestApp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TimerEditor extends JFrame {

	private JPanel contentPane;
	private JTextField txtTimer;

	private PlaytestScreen parent;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TimerEditor frame = new TimerEditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Create a constructor overload which takes in elapsedTime
	// and sets the text of txtTimer to the elapsedTime
	// This will be used when the user clicks on the timer
	// in the main window
	public TimerEditor(long elapsedTime, PlaytestScreen parent) 
	{
		this();

		this.parent = parent; 

		// Convert elapsedTime to a 00:00:00 format with minutes, seconds and milliseconds
		int minutes = (int) (elapsedTime / 60000);
		int seconds = (int) ((elapsedTime % 60000) / 1000);
		int milliseconds = (int) (elapsedTime % 1000);
		String time = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds / 10);

		txtTimer.setText(time);
	}

	/**
	 * Create the frame.
	 */
	public TimerEditor() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setResizable(false);
		setAlwaysOnTop(true);

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtTimer = new JTextField();
		txtTimer.setBounds(40, 74, 258, 93);
		contentPane.add(txtTimer);
		txtTimer.setColumns(10);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetTimer();
			}
		});
		btnReset.setBounds(310, 117, 117, 29);
		contentPane.add(btnReset);
		
		JLabel lblTitle = new JLabel("Edit Timer");
		lblTitle.setBounds(176, 32, 117, 16);
		contentPane.add(lblTitle);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SaveTimer(); 
			}
		});
		btnSave.setBounds(251, 197, 117, 29);
		contentPane.add(btnSave);
	}

	private void ResetTimer()
	{
		txtTimer.setText("00:00:00");
	}

	private void SaveTimer ()
	{
		// Convert the time in the text box to a long elapsedTime. The text is in the format minutes, seconds, milliseconds (00:00:00)
		String[] time = txtTimer.getText().split(":");
		long elapsedTime = Long.parseLong(time[0]) * 60000 + Long.parseLong(time[1]) * 1000 + Long.parseLong(time[2]) * 10;
		
		parent.SetTimer(elapsedTime);

		// Re-enable the main window
		parent.setEnabled(true);

		// Close the window
		this.dispose();
	}

	// When window is closed or red x button is clicked, re-enable the main window
	protected void processWindowEvent(java.awt.event.WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == java.awt.event.WindowEvent.WINDOW_CLOSING) {
			parent.setEnabled(true);
		}
	}
}
