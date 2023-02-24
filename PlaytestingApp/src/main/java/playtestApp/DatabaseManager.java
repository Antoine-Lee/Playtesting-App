package playtestApp;

import java.sql.*;
import java.util.Vector;

import javax.swing.table.DefaultTableModel; 

public class DatabaseManager {
	private static Connection conn = null; 
	private static String url = "jdbc:sqlite:PlaytestData.db";
	
	public static Connection getConnection()
	{
		if (conn == null)
		{
			try 
			{
				conn = DriverManager.getConnection(url); 
			}
			catch (Exception e)
			{
				e.printStackTrace(); 
			}
		}
		
		try 
		{
			if (!conn.isValid(1000))
			{
				conn = DriverManager.getConnection(url); 
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return conn; 
	}
	
	public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException 
	{
	    ResultSetMetaData metadata = rs.getMetaData();
	    
	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metadata.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) 
	        columnNames.add(metadata.getColumnName(column));

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) 
	            vector.add(rs.getObject(columnIndex));
	        
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);
	}
}
