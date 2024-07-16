package spider;

import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.io.FileOutputStream;

//Class is designed to download a file from a site (specified via fileURL) to a directory (specified via saveFile)
public class FileDownloader {
  
  public String fileURL;
  public String saveFile;

  public FileDownloader(String fileURL, String saveFile) {

    this.fileURL = fileURL;
    this.saveFile = saveFile;

  }


  //Downloads file in specified link to specified path
  public void downloadFile() {

    try {

      @SuppressWarnings("deprecation")
      URL website = new URL(fileURL);
      ReadableByteChannel rbc = Channels.newChannel(website.openStream());
      FileOutputStream fos = new FileOutputStream(saveFile);
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
      fos.close();

    } catch (Exception e) {
      System.out.println("Trouble saving file " + fileURL);
      e.printStackTrace();
    }


  }


  public void echos() {
    System.out.println("fileURL = " + this.fileURL);
    System.out.println("saveFile = " + this.saveFile);
  }

}
