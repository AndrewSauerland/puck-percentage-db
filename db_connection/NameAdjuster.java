package db_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class NameAdjuster {
  
  String dbUrl;
  String dbUsername;
  String dbPassword;
  String year;
  ArrayList<String> namesBefore;
  ArrayList<String> namesAfter;
  ArrayList<String> type;

  public NameAdjuster(String dbUrl, String dbUsername, String dbPassword, String year) {
    this.dbUrl = dbUrl;
    this.dbUsername = dbUsername;
    this.dbPassword = dbPassword;
    this.year = year;
    this.namesBefore = new ArrayList<>();
    this.namesAfter = new ArrayList<>();
    this.type = new ArrayList<>();
  }

  public void adjust() {

    //Pull names
    populateNames();

    //Format names sequentially and run statement
    uploadAdjustments();


  }

  //Saves names from columns 
  public void populateNames() {

    try {
      System.out.println("Executing adjustments");
			Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			Statement statement = conn.createStatement();
			ResultSet namesTable = statement.executeQuery("SELECT * FROM names");

      while (namesTable.next()) {
        namesBefore.add(namesTable.getString("nameBefore"));
        namesAfter.add(namesTable.getString("nameAfter"));
        type.add(namesTable.getString("type"));
      }

      statement.close();
			conn.close();
		} catch (SQLException e) {
      System.out.println("Could not pull table");
			e.printStackTrace();
		}

  }

  //Cycle through lists and fix names
  public void uploadAdjustments() {

    try {
      System.out.println("Saving names");
			Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			Statement statement = conn.createStatement();
			
      for (int i = 0; i < type.size(); i++) {
        String script = "UPDATE " + type.get(i) + year + " SET name = \'" + namesAfter.get(i) + "\' WHERE name = \'" + namesBefore.get(i) + "\'";
        statement.executeUpdate(script);
      }

      statement.close();
			conn.close();
		} catch (SQLException e) {
      System.out.println("Could adjust names");
			e.printStackTrace();
		}

  }

}
