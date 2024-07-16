import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SkaterReader {

	public String path;
	public String tableName;
	
	public SkaterReader(String path, String tableName) {
		this.path = path;
		this.tableName = tableName;
	}
	
	//Gets headers - Returns ArrayList<String> of headers from "path"
	public ArrayList<String> getHeaders(String path) throws IOException {
		String line = readFirstLine(path);
		ArrayList<String> headers = new ArrayList<String>();
		String [] split = line.split(",");
		for (String word : split) {
			headers.add(word);
		}
		return headers;
		
	}
	
	//Reads the first line - Returns first line of "path", unedited
	public String readFirstLine(String path) throws IOException {
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();
			return formatFirstLine(line);
		}
	}
	
	
	public String formatFirstLine(String s) {
		String formatted = "";
		String[] split = s.split(",");
		for (String word : split) {
			formatted += word + ", ";
		}
		int lastComma = formatted.lastIndexOf(",");
		formatted = formatted.substring(0, lastComma);
		return formatted;
	}
	
	//Reads the skaters sheet - Returns ArrayList<ArrayList<String>> from "path"
	public ArrayList<ArrayList<String>> readSkaterData(String path) throws IOException {
		ArrayList<ArrayList<String>> fullData = new ArrayList<ArrayList<String>>();
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
			br.readLine();
			String line = null;
			while((line = br.readLine()) != null){
				ArrayList<String> modifiedLine = new ArrayList<String>();
				String [] split = line.split(",");
				for (String word : split) {
					modifiedLine.add(word);
				}
				fullData.add(modifiedLine);
			}
		}
		return fullData;
	}

}