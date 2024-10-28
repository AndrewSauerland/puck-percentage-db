package backfills;

import java.io.IOException;

import db_connection.GoalieUploader;
import file_processors.GoalieReader;

public class GoalieBackfill {
  

  public static void main(String args[]) throws IOException, ClassNotFoundException {

    int year = 2008;
    String databaseUrl = System.getenv("DATABASE_URL");
    String databaseUsername = System.getenv("DATABASE_USERNAME");
    String databasePassword = System.getenv("DATABASE_PASSWORD");

    //Loop through years 2015-2023 of hockey sheets and sequentially create/upload each collection to sql
    while (year <= 2014) {

      System.out.println("Beginning year " + year);

      String isolatedYear = Integer.toString(year).substring(2);
      String filePath = "lib/archive/goalies" + isolatedYear + ".csv";

      GoalieReader fileReader = new GoalieReader(filePath);
      GoalieUploader uploader = new GoalieUploader(fileReader, databaseUrl, databaseUsername, databasePassword, "goalies" + isolatedYear);
      uploader.createTable();
      uploader.uploadData();

      year++;

    }

    System.out.println("Complete...");
		
  }

}
