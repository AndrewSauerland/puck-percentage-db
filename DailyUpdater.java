package drivers;

import spider.FileDownloader;

public class DailyUpdater {
  
  //Variables
  public static String skatersSheetUrl = "https://moneypuck.com/moneypuck/playerData/seasonSummary/2023/regular/skaters.csv";
  public static String skatersSheetPath = "lib/downloads/skaters.csv";

  public static String goaliesSheetUrl = "https://moneypuck.com/moneypuck/playerData/seasonSummary/2023/regular/goalies.csv";
  public static String goaliesSheetPath = "lib/downloads/goalies.csv";

  public static String databseEndpoint = "";
  public static String databaseUsername = "";
  public static String databasePassword = "";

  public static void main(String args[]) {

    //Download skaters and goalies csvs
    //& Uncomment
    //! downloadSheets();

    //Parse csv files and upload to database



  }


  public static void downloadSheets() {


    FileDownloader skatersDownloader = new FileDownloader(skatersSheetUrl, skatersSheetPath);
    FileDownloader goaliesDownloader = new FileDownloader(goaliesSheetUrl, goaliesSheetPath);

    skatersDownloader.downloadFile();
    goaliesDownloader.downloadFile();

  }

  public static void updateDatabase() {

    //TODO Here

  }


}
