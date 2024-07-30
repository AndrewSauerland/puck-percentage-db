package db_connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;


public class ScheduleUploader {
  
  String dbUrl;
	String dbUsername;
	String dbPassword;
  String scheduleBaseUrl;
  ArrayList<ArrayList<ArrayList<Object>>> week;

  //& db items connect to DB, baseUrl endpoint called to retrieve games
  public ScheduleUploader(String dbUrl, String dbUsername, String dbPassword, String scheduleBaseUrl) {
    this.dbUrl = dbUrl;
		this.dbUsername = dbUsername;
    this.dbPassword = dbPassword;
    this.scheduleBaseUrl = scheduleBaseUrl;
    this.week = new ArrayList<>();
  }


  //gets schedule for days of the week
  public void getSchedule(String date) {

    System.out.println("Pulling data for week starting " + date);
    String formattedUrl = scheduleBaseUrl + date;

    try {

      // Pull & format data 
      @SuppressWarnings("deprecation")
      URL url = new URL(formattedUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String inputLine;
      StringBuilder response = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      
      JSONObject jsonResponse = new JSONObject(response.toString());
      JSONArray gameWeek = jsonResponse.getJSONArray("gameWeek");
      week.clear();

      // Loop through each day
      for (int i = 0; i < gameWeek.length(); i++) {
        ArrayList<ArrayList<Object>> day = new ArrayList<>();
        JSONObject dayJSON = gameWeek.getJSONObject(i);
        String currentDate = dayJSON.getString("date");
        JSONArray games = dayJSON.getJSONArray("games");

        // Loop through game objects
        int totalGames = games.length();
        for (int j = 0; j < totalGames; j++) {
          JSONObject game = games.getJSONObject(j);
          ArrayList<Object> gameInfo = new ArrayList<>();

          gameInfo.add(currentDate);
          gameInfo.add(game.getInt("id"));
          gameInfo.add(game.getInt("gameType"));
          gameInfo.add(game.getJSONObject("awayTeam").getString("abbrev"));
          gameInfo.add(game.getJSONObject("homeTeam").getString("abbrev"));
          day.add(gameInfo);

        }

        week.add(day);

      }
     
    } catch (Exception e) {
      System.out.println("Encountered error making API call");
      e.printStackTrace();
    }

  }

  // Upload to database
  public void uploadWeek() {

    System.out.println("Uploading current week");

    try {
      Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
      Statement statement = conn.createStatement();
      statement.executeUpdate("USE utilities;");

      // Loop entries and upload data
      for (ArrayList<ArrayList<Object>> day : week) {
        for (ArrayList<Object> game : day) {

          String formattedStatement = String.valueOf("\"" + game.get(0)) + "\", " + String.valueOf(game.get(1)) + ", " + String.valueOf(game.get(2)) + ", \"" + 
            String.valueOf(game.get(3)) + "\", \"" + String.valueOf(game.get(4)) + "\"";
          // System.out.println("INSERT INTO schedule (date, gameID, awayTeam, homeTeam) VALUES (" + formattedStatement + ");");
          statement.executeUpdate("INSERT INTO schedule (date, gameID, gameType, awayTeam, homeTeam) VALUES (" + formattedStatement + ");");
        
        }
      }

      statement.close();
      conn.close();
    } catch (SQLException e) {
      System.out.println("Could not upload week to sql");
      e.printStackTrace();
    }

    System.out.println("Finished current week");

  }

  // Displaying
  public void displayWeek() {
    for (ArrayList<ArrayList<Object>> day : week) {
      System.out.println("New day entered");
      for (ArrayList<Object> game : day) {
        System.out.println("New game entered");
        System.out.println("date " + game.get(0));
        System.out.println("id " + game.get(1));
        System.out.println("gameType " + game.get(2));
        System.out.println(game.get(3) + " : " + game.get(4));
      }
    }
  }


}
