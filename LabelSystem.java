package CSE3063F20P1_GRP2;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.simple.parser.ParseException;

public class LabelSystem{

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		// TODO Auto-generated method stub

		ReadJsonFile readFile = new ReadJsonFile();

		Dataset data = readFile.readFileDataset("input.json");
	
		FileHandler fh = new FileHandler("default.log",false); 
		  Logger logger = Logger.getLogger("MyLog");  
	      logger.setUseParentHandlers(false);
	    	logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);
		ArrayList<User> user_list = readFile.readFileUsers("user.json",fh,logger);
		
		//label assignments objects
		ArrayList<LabelAssignment> assignment_list = new ArrayList<LabelAssignment>();
		
		for (int b = 0; b < data.getInstances().size(); b++) {
			for (int a = 0; a < user_list.size(); a++) {
				LabelAssignment labelAssignment = new LabelAssignment(data.getInstances().get(b), user_list.get(a));
				assignment_list.add(labelAssignment);
			}
		}
		
		//get labelled
		for (int i = 0; i < assignment_list.size(); i++) {	
			(assignment_list.get(i).getUser()).label(assignment_list.get(i), data.getLabels(), data.getMaxNoLabels(),fh,logger);
		}

		
		//output
		WriteJsonFile a = new WriteJsonFile(data.getId(), data.getName(), data.getMaxNoLabels(), data.getLabels(),
				data.getInstances(), assignment_list, user_list);
		a.printToFile("output_test.json");

	}

}