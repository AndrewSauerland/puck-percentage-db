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
  ArrayList<String> namesBefore;
  ArrayList<String> namesAfter;
  ArrayList<String> type;

  public NameAdjuster(String dbUrl, String dbUsername, String dbPassword) {
    this.dbUrl = dbUrl;
    this.dbUsername = dbUsername;
    this.dbPassword = dbPassword;
    this.namesBefore = new ArrayList<>();
    this.namesAfter = new ArrayList<>();
    this.type = new ArrayList<>();
  }

  public void adjust() {

    //Pull names
    populateNames();

    //format names sequentially and run statement
    printVariables();
    //! Here


  }


  public void populateNames() {

    try {
      System.out.println("Saving names");
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
      System.out.println("Could pull table");
			e.printStackTrace();
		}

  }


  public void printVariables() {

    for (int i = 0; i < type.size(); i++) {
      System.out.println("i = " + i);
      System.out.println("nameBefore = " + namesBefore.get(i));
      System.out.println("nameAfter = " + namesAfter.get(i));
      System.out.println("nametype = " + type.get(i));
    }
  }

}
