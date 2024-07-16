package db_connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import file_processors.GoalieReader;

public class GoalieUploader {
  
  String url = "jdbc:mysql://127.0.0.1:3306/fantasyhockey";
	GoalieReader gR;
	
	//Converts ArrayList<String> of headers into clean list to be input to VALUES
	public static String getValues(ArrayList<String> headers) {
		String VALUES = "";
		int counter = 1;
		for (String item : headers) {
			if (counter == headers.size()) {
				VALUES += item;
			} else {
				VALUES += item + ", ";
			}
			counter++;
		}
		return VALUES;
	}
	
	//Creates a custom string to use where VALUES are defined
	public static String formatEntry(ArrayList<String> entry) {
		String formattedEntry = "";
		int L = entry.size();
		int indexCounter = 0;
		for (String item : entry) {
			if (indexCounter >= 2 && indexCounter <= 5) {
				formattedEntry += "\"" + item + "\", ";
			} else if (indexCounter == L-1) {
				formattedEntry += item;
			} else {
				formattedEntry += item + ", ";
			}
			indexCounter++;
		}
		return formattedEntry;
	}
	
	public GoalieUploader(GoalieReader gR) {
		this.gR = gR;
	}
	
	public void clearTable() throws IOException {
		
		try {
			
			Connection conn = DriverManager.getConnection(url, "root", "cleo2011");
			Statement statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM " + gR.tableName);
			
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("Could not clear table");
			e.printStackTrace();
		}
		
	}
	
	public void uploadData() throws IOException {
		
		String goalieUploadColumns = gR.readFirstLine(gR.path);
		ArrayList<ArrayList<String>> gRdata = gR.readGoalieData(gR.path);
		
		//This for loop creates a custom string to use where VALUES are defined before importing to SQL
		for (ArrayList<String> skater : gRdata) {
			String valuesEntry = "";
			int counter = 0;
			int datapoints = skater.size();
			for (String s : skater) {
				if (counter >=2 && counter <= 5) {
					valuesEntry += "\"" + s + "\", ";
				} else if (datapoints - 1 == counter) {
					valuesEntry += s;
				} else {
					valuesEntry += s + ", ";
				}
				counter++;
			}
			//System.out.println(valuesEntry);
			
			//This try/catch attempts to send SQL an INSERT statement containing all fields in database, executing for each instance
			try {
				
				Connection conn = DriverManager.getConnection(url, "root", "cleo2011");
				Statement statement = conn.createStatement();
				statement.executeUpdate("INSERT INTO " + gR.tableName + " (" + goalieUploadColumns + ") VALUES (" + valuesEntry + ");");
				 
				conn.close();
				
			} catch (SQLException e) {
				System.out.println("Could not enter " + valuesEntry);
				e.printStackTrace();
			}
		}
		System.out.println("Finished table " + gR.tableName);
		
	}

}
