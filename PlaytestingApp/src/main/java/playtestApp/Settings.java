package playtestApp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Settings extends JFrame {

	private JPanel contentPane;

	private JFrame parent = null; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Settings frame = new Settings();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Write constructor overload which takes a parent of type JFrame
	public Settings(JFrame parent)
	{
		this(); 
		this.parent = parent; 
	}

	/**
	 * Create the frame.
	 */
	public Settings() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setResizable(false);
		setAlwaysOnTop(true);

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSettings = new JLabel("Settings");
		lblSettings.setBounds(178, 20, 61, 16);
		contentPane.add(lblSettings);
		
		JLabel lblUser = new JLabel("User");
		lblUser.setBounds(46, 61, 61, 16);
		contentPane.add(lblUser);
		
		JButton btnLogout = new JButton("Log Out");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Logout(); 
			}
		});
		btnLogout.setBounds(46, 94, 86, 29);
		contentPane.add(btnLogout);
		
		JLabel lblUsername = new JLabel("Logged in as: USERNAME");
		lblUsername.setBounds(215, 61, 182, 16);
		contentPane.add(lblUsername);

		String username = DataManager.GetUsername(); 
		Boolean isAdmin = DataManager.GetAdminStatus(); 
		
		lblUsername.setText(username + (isAdmin ? " (Admin)" : ""));
	}

	private void Logout()
	{
		DataManager.Logout(); 
		// Open menu page
		Menu menu = new Menu();
		menu.setVisible(true);

		// Close current window
		dispose();
		parent.dispose();
	}
}
