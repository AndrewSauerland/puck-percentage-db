package db_connection;

import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import file_processors.SkaterReader;

public class SkaterUploader {
  
	SkaterReader sR;
	String dbUrl;
  String dbUsername;
  String dbPassword;
  String tableName;
  	
	public SkaterUploader(SkaterReader sR, String dbUrl, String dbUsername, String dbPassword, String tableName) {
		this.sR = sR;
    this.dbUrl = dbUrl;
    this.dbUsername = dbUsername;
    this.dbPassword = dbPassword;
    this.tableName = tableName;
	}
	
	//Creates table
	public void createTable() throws IOException {
		
		String script = "CREATE TABLE " + tableName + " ( " + skaterTableColumns + " ); ";
		
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
	
  //Uses the skaterReader object passed in (containing csv of parsed data) to insert all data into sql line-by-line
	public void uploadData() throws IOException {
		
    System.out.println("Uploading " + tableName);

		String skaterUploadColumns = sR.readFirstLine(sR.path);
		ArrayList<ArrayList<String>> sRdata = sR.readSkaterData(sR.path);
		String valuesEntry = "";

    try {
      Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
      Statement statement = conn.createStatement();

      //Loop through every line of data, formatting each value as a comma-separated string
      for (ArrayList<String> entry : sRdata) {
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
      statement.executeUpdate("INSERT INTO " + tableName + " (" + skaterUploadColumns + ") VALUES (" + valuesEntry + ");");
      }

      conn.close();
      statement.close();
      
    } catch (SQLException e) {
      System.out.println("Could not enter " + valuesEntry);
      e.printStackTrace();
    }

		System.out.println("Finished table " + tableName);

	}

  //^ All columns defined with their datatype to be used in table creation. These fields will remain constant in every iteration of skaters (No elegant way to do this)
	String skaterTableColumns = "playerId int, season int, name text, team text, position text, situation text, games_played int, icetime int, shifts int, gameScore double, "
  + "onIce_xGoalsPercentage double, offIce_xGoalsPercentage double, onIce_corsiPercentage double, offIce_corsiPercentage double, onIce_fenwickPercentage double, "
  + "offIce_fenwickPercentage double, iceTimeRank double, I_F_xOnGoal double, I_F_xGoals double, I_F_xRebounds double, I_F_xFreeze double, I_F_xPlayStopped double, "
  + "I_F_xPlayContinuedInZone double, I_F_xPlayContinuedOutsideZone double, I_F_flurryAdjustedxGoals double, I_F_scoreVenueAdjustedxGoals double, I_F_flurryScoreVenueAdjustedxGoals double, "
  + "I_F_primaryAssists double, I_F_secondaryAssists double, I_F_shotsOnGoal double, I_F_missedShots double, I_F_blockedShotAttempts double, I_F_shotAttempts double, "
  + "I_F_points double, I_F_goals double, I_F_rebounds double, I_F_reboundGoals double, I_F_freeze double, I_F_playStopped double, I_F_playContinuedInZone double, "
  + "I_F_playContinuedOutsideZone double, I_F_savedShotsOnGoal double, I_F_savedUnblockedShotAttempts double, penalties double, I_F_penalityMinutes double, I_F_faceOffsWon double, "
  + "I_F_hits double, I_F_takeaways double, I_F_giveaways double, I_F_lowDangerShots double, I_F_mediumDangerShots double, I_F_highDangerShots double, I_F_lowDangerxGoals double, "
  + "I_F_mediumDangerxGoals double, I_F_highDangerxGoals double, I_F_lowDangerGoals double, I_F_mediumDangerGoals double, I_F_highDangerGoals double, I_F_scoreAdjustedShotsAttempts double, "
  + "I_F_unblockedShotAttempts double, I_F_scoreAdjustedUnblockedShotAttempts double, I_F_dZoneGiveaways double, I_F_xGoalsFromxReboundsOfShots double, I_F_xGoalsFromActualReboundsOfShots double, "
  + "I_F_reboundxGoals double, I_F_xGoals_with_earned_rebounds double, I_F_xGoals_with_earned_rebounds_scoreAdjusted double, I_F_xGoals_with_earned_rebounds_scoreFlurryAdjusted double, "
  + "I_F_shifts double, I_F_oZoneShiftStarts double, I_F_dZoneShiftStarts double, I_F_neutralZoneShiftStarts double, I_F_flyShiftStarts double, I_F_oZoneShiftEnds double, "
  + "I_F_dZoneShiftEnds double, I_F_neutralZoneShiftEnds double, I_F_flyShiftEnds double, faceoffsWon double, faceoffsLost double, timeOnBench double, penalityMinutes double, "
  + "penalityMinutesDrawn double, penaltiesDrawn double, shotsBlockedByPlayer double, OnIce_F_xOnGoal double, OnIce_F_xGoals double, OnIce_F_flurryAdjustedxGoals double, "
  + "OnIce_F_scoreVenueAdjustedxGoals double, OnIce_F_flurryScoreVenueAdjustedxGoals double, OnIce_F_shotsOnGoal double, OnIce_F_missedShots double, OnIce_F_blockedShotAttempts double, "
  + "OnIce_F_shotAttempts double, OnIce_F_goals double, OnIce_F_rebounds double, OnIce_F_reboundGoals double, OnIce_F_lowDangerShots double, OnIce_F_mediumDangerShots double, "
  + "OnIce_F_highDangerShots double, OnIce_F_lowDangerxGoals double, OnIce_F_mediumDangerxGoals double, OnIce_F_highDangerxGoals double, OnIce_F_lowDangerGoals double, "
  + "OnIce_F_mediumDangerGoals double, OnIce_F_highDangerGoals double, OnIce_F_scoreAdjustedShotsAttempts double, OnIce_F_unblockedShotAttempts double, "
  + "OnIce_F_scoreAdjustedUnblockedShotAttempts double, OnIce_F_xGoalsFromxReboundsOfShots double, OnIce_F_xGoalsFromActualReboundsOfShots double, OnIce_F_reboundxGoals double, "
  + "OnIce_F_xGoals_with_earned_rebounds double, OnIce_F_xGoals_with_earned_rebounds_scoreAdjusted double, OnIce_F_xGoals_with_earned_rebounds_scoreFlurryAdjusted double, "
  + "OnIce_A_xOnGoal double, OnIce_A_xGoals double, OnIce_A_flurryAdjustedxGoals double, OnIce_A_scoreVenueAdjustedxGoals double, OnIce_A_flurryScoreVenueAdjustedxGoals double, "
  + "OnIce_A_shotsOnGoal double, OnIce_A_missedShots double, OnIce_A_blockedShotAttempts double, OnIce_A_shotAttempts double, OnIce_A_goals double, OnIce_A_rebounds double, "
  + "OnIce_A_reboundGoals double, OnIce_A_lowDangerShots double, OnIce_A_mediumDangerShots double, OnIce_A_highDangerShots double, OnIce_A_lowDangerxGoals double, "
  + "OnIce_A_mediumDangerxGoals double, OnIce_A_highDangerxGoals double, OnIce_A_lowDangerGoals double, OnIce_A_mediumDangerGoals double, OnIce_A_highDangerGoals double, "
  + "OnIce_A_scoreAdjustedShotsAttempts double, OnIce_A_unblockedShotAttempts double, OnIce_A_scoreAdjustedUnblockedShotAttempts double, OnIce_A_xGoalsFromxReboundsOfShots double, "
  + "OnIce_A_xGoalsFromActualReboundsOfShots double, OnIce_A_reboundxGoals double, OnIce_A_xGoals_with_earned_rebounds double, OnIce_A_xGoals_with_earned_rebounds_scoreAdjusted double, "
  + "OnIce_A_xGoals_with_earned_rebounds_scoreFlurryAdjusted double, OffIce_F_xGoals double, OffIce_A_xGoals double, OffIce_F_shotAttempts double, OffIce_A_shotAttempts double, "
  + "xGoalsForAfterShifts double, xGoalsAgainstAfterShifts double, corsiForAfterShifts double, corsiAgainstAfterShifts double, fenwickForAfterShifts double, fenwickAgainstAfterShifts double"; 

}
