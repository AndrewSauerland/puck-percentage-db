


import java.io.IOException;

import db_connection.GoalieUploader;
import db_connection.NameAdjuster;
import db_connection.SkaterUploader;
import file_processors.GoalieReader;
import file_processors.SkaterReader;
import spider.FileDownloader;

public class DailyUpdater {
  
  //Variables
  public static String year = "24";

  public static String skatersSheetUrl = "https://moneypuck.com/moneypuck/playerData/seasonSummary/2024/regular/skaters.csv";
  public static String skatersSheetPath = "./lib/downloads/skaters.csv";

  public static String goaliesSheetUrl = "https://moneypuck.com/moneypuck/playerData/seasonSummary/2024/regular/goalies.csv";
  public static String goaliesSheetPath = "./lib/downloads/goalies.csv";

  public static String databaseEndpoint = System.getenv("DATABASE_URL");
  public static String databaseUsername = System.getenv("DATABASE_USERNAME");
  public static String databasePassword = System.getenv("DATABASE_PASSWORD");

  public static void main(String args[]) throws IOException, ClassNotFoundException {

    System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
    System.out.println(" --- --- --- --- --- --- --- ---  Beginning puck-percentage-db Sequence  --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
    System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");

    //Download skaters and goalies csvs
    downloadSheets();

    //Parse csv files and upload to database
    updateDatabase();

    //update names
    updateNames();

    System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
    System.out.println(" --- --- --- --- --- --- --- ---   puck-percentage-db Sequence Complete  --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
    System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");


  }

  //Download updated version of stats spreadsheets
  public static void downloadSheets() {


    FileDownloader skatersDownloader = new FileDownloader(skatersSheetUrl, skatersSheetPath);
    FileDownloader goaliesDownloader = new FileDownloader(goaliesSheetUrl, goaliesSheetPath);

    skatersDownloader.downloadFile();
    goaliesDownloader.downloadFile();

  }

  public static void updateDatabase() throws IOException {

    System.out.println("Beginning upload...");

    SkaterReader sReader = new SkaterReader("lib/downloads/skaters.csv");
    SkaterUploader sUpload = new SkaterUploader(sReader, databaseEndpoint, databaseUsername, databasePassword, "skaters" + year);

    GoalieReader gReader = new GoalieReader("lib/downloads/goalies.csv");
    GoalieUploader gUpload = new GoalieUploader(gReader, databaseEndpoint, databaseUsername, databasePassword, "goalies" + year);

    System.out.println("Uploading skaters...");
    sUpload.clearTable();
    sUpload.uploadData();
    sUpload.addDate();
    System.out.println("Finished uploading skaters");

    System.out.println("Uploading goalies...");
    gUpload.clearTable();
    gUpload.uploadData();
    gUpload.addDate();
    System.out.println("Finished uploading goalies");

    System.out.println("Updated tables successfully");

  }

  public static void updateNames() throws IOException {

    NameAdjuster names = new NameAdjuster(databaseEndpoint, databaseUsername, databasePassword, year);

    System.out.println("Adjusting names...");
    names.adjust();
    System.out.println("All specified names have been adjusted");

  }


}
