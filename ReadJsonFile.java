package CSE3063F20P1_GRP2;

/*
 * Reads given input json file
 * 	 
 * 
 */



import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ReadJsonFile {
	
	public static void main(String[] args){
		
		JSONParser parser = new JSONParser();
		
		try {
			Object obj = parser.parse(new FileReader("CES3063F20_LabelingProject_Input-1.json"));
			JSONObject jsonObject = (JSONObject) obj;
			
			long id = (long)jsonObject .get("dataset id");
			System.out.println("dataset id: "+ id);
			
			String name = (String)jsonObject.get("dataset name");
			System.out.println("dataset name: "+ name);
			
			long maxNumber = (long)jsonObject.get("maximum number of labels per instance");
			System.out.println("maximum number of labels per instance: "+ maxNumber);
			System.out.println();
			
			
			JSONArray labels = (JSONArray) jsonObject.get("class labels");
			for(int i=0; i<labels.size(); ++i) {
				
				JSONObject labelObject = (JSONObject) labels.get(i);
				long labelId = (long)labelObject.get("label id");
				String labelText = (String)labelObject.get("label text");
				
				System.out.println("label id: "+ labelId);
				System.out.println("label text: "+ labelText);
			}
			System.out.println();
			JSONArray instances = (JSONArray) jsonObject.get("instances");
			for(int i=0; i<instances.size(); ++i) {
				
				JSONObject instanceObject = (JSONObject) instances.get(i);
				
				long instanceId = (long)instanceObject.get("id");
				String instanceText = (String)instanceObject.get("instance");
				
				System.out.println("instance id:  "+ instanceId);
				System.out.println("instance text: "+ instanceText);
			}
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
