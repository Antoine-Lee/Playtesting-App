package playtestApp;

import java.awt.EventQueue;
import java.sql.*;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class AllReviews extends JFrame {

	private JPanel contentPane;

	private JTable tblReviews = null; 
	private JComboBox<String> cmbField;
	private JTextField txtValue;
	private JComboBox<String> cmbCondition; 

	private Vector<Vector<Object>> data = new Vector<Vector<Object>>(); 
	private Vector<Vector<Object>> filteredData = new Vector<Vector<Object>>();

	private Boolean advancedFilter = false; 
	private JLabel lblField;
	private JLabel lblCondition;
	private JLabel lblValue;
	private JButton btnAdvanced;
	private JButton btnFilter;
	private JTextArea txtAdvancedFilter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AllReviews frame = new AllReviews();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AllReviews() 
	{
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("Reviews");
		lblTitle.setBounds(388, 20, 61, 16);
		contentPane.add(lblTitle);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(43, 69, 714, 224);
		contentPane.add(scrollPane);
		
		tblReviews = new JTable();
		scrollPane.setViewportView(tblReviews);
		
		JLabel lblFilter = new JLabel("Filter");
		lblFilter.setBounds(50, 317, 61, 16);
		contentPane.add(lblFilter);
		
		lblField = new JLabel("Field");
		lblField.setBounds(43, 350, 61, 16);
		contentPane.add(lblField);
		
		cmbField = new JComboBox<String>();
		cmbField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { UpdateConditions(); }
		});
		cmbField.setBounds(53, 378, 117, 27);
		contentPane.add(cmbField);
		// Set options for each of the following: "User", "Game", "Level", "Difficulty", "TimeTaken"
		cmbField.addItem("No Filter");
		cmbField.addItem("User");
		cmbField.addItem("Game");
		cmbField.addItem("Level");
		cmbField.addItem("Difficulty");
		cmbField.addItem("Time Taken");
		
		lblCondition = new JLabel("Condition");
		lblCondition.setBounds(190, 350, 94, 16);
		contentPane.add(lblCondition);
		
		cmbCondition = new JComboBox<String>();
		cmbCondition.setBounds(182, 378, 117, 27);
		contentPane.add(cmbCondition);
		UpdateConditions();
		
		lblValue = new JLabel("Value");
		lblValue.setBounds(333, 350, 94, 16);
		contentPane.add(lblValue);
		
		txtValue = new JTextField();
		txtValue.setBounds(319, 377, 130, 26);
		contentPane.add(txtValue);
		txtValue.setColumns(10);

		btnFilter = new JButton("Filter");
		btnFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateTable();
			}
		});
		btnFilter.setBounds(474, 377, 117, 29);
		contentPane.add(btnFilter);
		
		btnAdvanced = new JButton("Advanced");
		btnAdvanced.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ToggleAdvanced(); 
			}
		});
		btnAdvanced.setBounds(629, 377, 117, 29);
		contentPane.add(btnAdvanced);
		
		txtAdvancedFilter = new JTextArea();
		txtAdvancedFilter.setBounds(37, 336, 580, 111);
		contentPane.add(txtAdvancedFilter);
		
		ToggleAdvanced();
		GetReviews(); 
	}

	private void UpdateConditions()
	{
		if (cmbCondition == null) return; 

		// If the user selects "User", "Game" or "Level" then the options are "Equals" and "Not Equals"
		// If the user selects "Difficulty" then the options are "Equals", "Not Equals", "Greater Than" and "Less Than"
		// If the user selects "TimeTaken" then the options are "Equals", "Not Equals", "Greater Than" and "Less Than"
		
		// First, reset the cmbCondition options
		cmbCondition.removeAllItems();

		if (cmbField.getSelectedItem().equals("No Filter"))
		{
			cmbCondition.addItem("-");
			return;
		}

		if (cmbField.getSelectedItem().equals("User") || cmbField.getSelectedItem().equals("Game") || cmbField.getSelectedItem().equals("Level"))
		{
			cmbCondition.addItem("Equals");
			cmbCondition.addItem("Not Equals");
		}
		else if (cmbField.getSelectedItem().equals("Difficulty") || cmbField.getSelectedItem().equals("Time Taken"))
		{
			cmbCondition.addItem("Equals");
			cmbCondition.addItem("Not Equals");
			cmbCondition.addItem("Greater Than");
			cmbCondition.addItem("Less Than");
		}
	}

	private void GetReviews() // Get all reviews, only called once
	{
		Connection conn; 

		try
		{
			conn = DatabaseManager.getConnection(); 

			// Query the Reviews table in the database and output all the reviews to the tblReviews table
			// The columns are LevelID, UserID, Difficulty, TimeTaken, Suggestions and Bugs
			// Join the Reviews table with the Levels table to get the LevelName
			// Join the Reviews table with the Users table to get the Username

			String sql = "SELECT LevelName, GameID, Username, Difficulty, TimeTaken, Suggestions, Bugs FROM Reviews JOIN Levels ON Reviews.LevelID = Levels.LevelID JOIN Users ON Reviews.UserID = Users.UserID";
			// String sql = "SELECT * FROM Reviews";

			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			data = new Vector<Vector<Object>>();
			while (rs.next())
			{
				Vector<Object> row = new Vector<Object>();

				String user = rs.getString("Username");

				String levelName = rs.getString("LevelName"); 
				int gameId = rs.getInt("GameID");
				String gameName = GetGame(gameId);

				String difficulty = rs.getString("Difficulty");
				String timeTaken = rs.getString("TimeTaken");
				String suggestions = rs.getString("Suggestions");
				String bugs = rs.getString("Bugs");

				if (suggestions.equals(""))
					suggestions = "-";

				if (bugs.equals(""))
					bugs = "-";

				row.add(user); 
				row.add(gameName);
				row.add(levelName);
				row.add(difficulty);
				row.add(timeTaken);
				row.add(suggestions);
				row.add(bugs);

				data.add(row);
			}

			conn.close();

			UpdateTable(); 
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	private String GetGame (int gameId)
	{
		Connection conn; 

		try 
		{
			conn = DatabaseManager.getConnection();
			
			String sql = "SELECT Name FROM Games WHERE GameID = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, gameId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
			{
				String gameName = rs.getString("Name");
				return gameName;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return "NOT FOUND";
	}

	private void UpdateTable()
	{
		FilterData();

		DefaultTableModel model = new DefaultTableModel(new String[]{"User", "Game", "Level", "Difficulty", "TimeTaken", "Suggestions", "Bugs"}, 0);
		// Iterate through data
		for (Vector<Object> row : filteredData)
		{
			String user = (String)row.get(0);
			String gameName = (String)row.get(1);
			String levelName = (String)row.get(2);
			String difficulty = (String)row.get(3);
			String timeTaken = (String)row.get(4);
			String suggestions = (String)row.get(5);
			String bugs = (String)row.get(6);

			model.addRow(new Object[]{user, gameName, levelName, difficulty, timeTaken, suggestions, bugs});
		}

		tblReviews.setModel(model);
	}

	private void FilterData()
	{
		filteredData = new Vector<Vector<Object>>();
		
		String field = (String)cmbField.getSelectedItem();
		String condition = (String)cmbCondition.getSelectedItem();
		String value = txtValue.getText();

		if (field.equals("No Filter"))
		{
			filteredData = data;
			return;
		}

		for (Vector<Object> row : data)
		{
			String user = (String)row.get(0);
			String gameName = (String)row.get(1);
			String levelName = (String)row.get(2);
			String difficulty = (String)row.get(3);
			String timeTaken = (String)row.get(4);

			// Convert timeTaken to a rounded integer
			int timeTakenInt = 0;
			timeTakenInt = (int)Math.round(Double.parseDouble(timeTaken));
			System.out.println("Time Taken: " + timeTakenInt);
			
			// Convert value to a rounded integer
			int valueInt = 0;
			valueInt = (int)Math.round(Double.parseDouble(value));
			System.out.println("Value: " + valueInt);

			Boolean shouldAddRow = false; 

			if (field.equals("User"))
			{
				if (condition.equals("Equals"))
				{
					if (user.equals(value))
						shouldAddRow = true; 
				}
				else if (condition.equals("Not Equals"))
				{
					if (!user.equals(value))
						shouldAddRow = true; 
				}
			}
			else if (field.equals("Game"))
			{
				if (condition.equals("Equals"))
				{
					if (gameName.equals(value))
						shouldAddRow = true; 
				}
				else if (condition.equals("Not Equals"))
				{
					if (!gameName.equals(value))
						shouldAddRow = true; 
				}
			}
			else if (field.equals("Level"))
			{
				if (condition.equals("Equals"))
				{
					if (levelName.equals(value))
						shouldAddRow = true; 
				}
				else if (condition.equals("Not Equals"))
				{
					if (!levelName.equals(value))
						shouldAddRow = true; 
				}
			}
			else if (field.equals("Difficulty"))
			{
				if (condition.equals("Equals"))
				{
					if (difficulty.equals(value))
						shouldAddRow = true; 
				}
				else if (condition.equals("Not Equals"))
				{
					if (!difficulty.equals(value))
						shouldAddRow = true; 
				}
				else if (condition.equals("Greater Than"))
				{
					if (Integer.parseInt(difficulty) > Integer.parseInt(value))
						shouldAddRow = true; 
				}
				else if (condition.equals("Less Than"))
				{
					if (Integer.parseInt(difficulty) < Integer.parseInt(value))
						shouldAddRow = true; 
				}
			}
			else if (field.equals("Time Taken"))
			{
				if (condition.equals("Equals"))
				{
					if (timeTaken.equals(value))
						shouldAddRow = true; 
				}
				else if (condition.equals("Not Equals"))
				{
					if (!timeTaken.equals(value))
						shouldAddRow = true; 
				}
				else if (condition.equals("Greater Than"))
				{
					// if (Integer.parseInt(timeTaken) > Integer.parseInt(value))
					if (timeTakenInt > valueInt)
						shouldAddRow = true; 
				}
				else if (condition.equals("Less Than"))
				{
					// if (Integer.parseInt(timeTaken) < Integer.parseInt(value))
					if (timeTakenInt < valueInt)
						shouldAddRow = true; 
				}
			}

			if (shouldAddRow)
				filteredData.add(row);
		}
	}

	private void ToggleAdvanced()
	{
		if (advancedFilter)
		{
			btnAdvanced.setText("Advanced");
			SetBasicFilterEnabled(false);
			txtAdvancedFilter.setVisible(true);
			advancedFilter = false;
		}
		else
		{
			btnAdvanced.setText("Basic");
			SetBasicFilterEnabled(true);
			txtAdvancedFilter.setVisible(false);
			advancedFilter = true;
		}
	}

	private void SetBasicFilterEnabled (Boolean isEnabled)
	{
		cmbField.setVisible(isEnabled);
		cmbCondition.setVisible(isEnabled);
		txtValue.setVisible(isEnabled);
		lblField.setVisible(isEnabled);
		lblValue.setVisible(isEnabled);
		lblCondition.setVisible(isEnabled);
		btnFilter.setVisible(isEnabled);
	}
}
