import spider.FileDownloader;

public class Sandbox {
  
  public static void main(String args[]) {

    System.out.println("Hello world");

    String url = "https://moneypuck.com/moneypuck/playerData/seasonSummary/2023/regular/skaters.csv";
    String path = "lib/downloads/file.csv";

    FileDownloader downloader = new FileDownloader(url, path);
    
    downloader.echos();
    downloader.downloadFile();

  }

}
