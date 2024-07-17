package file_processors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GoalieReader {
  
  public String path;
  
	//& Path represents filepath to csv
	public GoalieReader(String path) {
		this.path = path;
	}
	
	//Establish document headers - returns ArrayList<String> of headers from "path"
	public ArrayList<String> getHeaders(String path) throws IOException {
		String line = readFirstLine(path);
		ArrayList<String> headers = new ArrayList<>();
		String [] split = line.split(",");
		for (String item : split) {
			headers.add(item);
		}
		return headers;
	}
	
	//Reads first line, returns unedited string 
	public String readFirstLine(String path) throws IOException{
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
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
	
	//Reads the goalies sheet - returns ArrayList<ArrayList<String>> from "path"
	public ArrayList<ArrayList<String>> readGoalieData(String path) throws IOException {
		ArrayList<ArrayList<String>> data = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			br.readLine();
			String line = null;
			while ((line = br.readLine()) != null) {
				ArrayList<String> lineData = new ArrayList<>();
				String [] broken = line.split(",");
				for (String item : broken) {
					lineData.add(item);
				}
				data.add(lineData);
			}
		}
		return data;
	}

}
