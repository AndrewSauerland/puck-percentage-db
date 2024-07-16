package backfills;

import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import db_connection.SkaterUploader;
import file_processors.SkaterReader;

public class SkaterBackfill {
  

  public static void main(String args[]) throws IOException, ClassNotFoundException {

    int year = 2008;
    String databaseUrl = System.getenv("DATABASE_URL");
    String databaseUsername = System.getenv("DATABASE_USERNAME");
    String databasePassword = System.getenv("DATABASE_PASSWORD");

    //Loop through years 2008-2023 of hockey sheets and sequentially create/upload each collection to sql
    while (year <= 2009) {

      System.out.println("Beginning year " + year);

      String isolatedYear = Integer.toString(year).substring(2);
      String filePath = "lib/archive/skaters" + isolatedYear + ".csv";

      System.out.println(filePath);
      SkaterReader fileReader = new SkaterReader(filePath);
      SkaterUploader uploader = new SkaterUploader(fileReader, databaseUrl, databaseUsername, databasePassword, "skaters" + isolatedYear);
      uploader.createTable();
      uploader.uploadData();

      year++;

    }

    System.out.println("Complete...");

		// try {
    //   System.out.println("Testing connection");
		// 	Connection conn = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
		// 	System.out.println("Connection Passed");
		// 	conn.close();
		// } catch (SQLException e) {
    //   System.out.println("Could not connect");
		// 	e.printStackTrace();
		// }
		
  }

}
