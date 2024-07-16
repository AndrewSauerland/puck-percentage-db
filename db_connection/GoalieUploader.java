package db_connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import file_processors.GoalieReader;

public class GoalieUploader {
  
	GoalieReader gR;
	String dbUrl;
	String dbUsername;
	String dbPassword;
	String tableName;

	//& gR object parses csv file, db info used to connect, tableName identifies table to create/access
	public GoalieUploader(GoalieReader gR, String dbUrl, String dbUsername, String dbPassword, String tableName) {
		this.gR = gR;
		this.dbUrl = dbUrl;
		this.dbUsername = dbUsername;
    this.dbPassword = dbPassword;
    this.tableName = tableName;
	}

	//Creates table
	public void createTable() throws IOException {
		
		String script = "CREATE TABLE " + tableName + " ( " + goalieTableColumns + " ); ";
		
		try {
      System.out.println("Creating table " + tableName + " in " + dbUrl);
			Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			Statement statement = conn.createStatement();
			statement.executeUpdate(script);
			conn.close();
		} catch (SQLException e) {
      System.out.println("Could not create table");
			e.printStackTrace();
		}
		
	}

	//Clears table but does not delete it
	public void clearTable() throws IOException {
		
		try {
			System.out.println("Clearing table " + tableName + " in " + dbUrl);
			Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			Statement statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM " + tableName);
			conn.close();
		} catch (SQLException e) {
			System.out.println("Could not clear table");
			e.printStackTrace();
		}
		
	}
	
	//Uses the goalieReader object passed in (containing csv of parsed data) to insert all data into sql line-by-line
	public void uploadData() throws IOException {
		
		System.out.println("Uploading " + tableName);

		//Obtain data, instantiate variable to persist loop
		String goalieUploadColumns = gR.readFirstLine(gR.path);
		ArrayList<ArrayList<String>> gRdata = gR.readGoalieData(gR.path);
		String valuesEntry = "";

		try {
			//Establish connection
      Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
      Statement statement = conn.createStatement();

      //Loop through every line of data, formatting each value as a comma-separated string
      for (ArrayList<String> entry : gRdata) {
        valuesEntry = "";
        int counter = 0;
        int datapoints = entry.size();
        for (String value : entry) {
          if (counter >=2 && counter <= 5) {
            valuesEntry += "\"" + value + "\", ";
          } else if (datapoints - 1 == counter) {
            valuesEntry += value;
          } else {
            valuesEntry += value + ", ";
          }
          counter++;
        }
      //Execute an insert statement containing all persisting fields and values in this line
      statement.executeUpdate("INSERT INTO " + tableName + " (" + goalieUploadColumns + ") VALUES (" + valuesEntry + ");");
      }

			//Close connections
      conn.close();
      statement.close();
      
    } catch (SQLException e) {
      System.out.println("Could not enter " + valuesEntry);
      e.printStackTrace();
    }

		System.out.println("Finished table " + tableName);

	}

	//^ All columns defined with their datatype to be used in table creation. These fields will remain constant in every iteration of goalies (No elegant way to do this)
	String goalieTableColumns = "playerId int, season int, name text, team text, position text, situation text, games_played double, icetime double, xGoals double, goals double, unblocked_shot_attempts double, "
	+ "xRebounds double, rebounds double, xFreeze double, freeze double, xOnGoal double, ongoal double, xPlayStopped double, playStopped double, xPlayContinuedInZone double, playContinuedInZone double, "
	+ "xPlayContinuedOutsideZone double, playContinuedOutsideZone double, flurryAdjustedxGoals double, lowDangerShots double, mediumDangerShots double, highDangerShots double, lowDangerxGoals double, "
	+ "mediumDangerxGoals double, highDangerxGoals double, lowDangerGoals double, mediumDangerGoals double, highDangerGoals double, blocked_shot_attempts double, penalityMinutes double, penalties double";

}
